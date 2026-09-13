package com.github.chenyuxin.commonframework.daojpa.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.chenyuxin.commonframework.daojpa.common.DaoConst;
import com.github.chenyuxin.commonframework.daojpa.common.DaoUtil;
import com.github.chenyuxin.commonframework.daojpa.common.TableType;
import com.github.chenyuxin.commonframework.daojpa.common.sql.CommonSql;
import com.github.chenyuxin.commonframework.daojpa.config.DaoResource;
import com.github.chenyuxin.commonframework.daojpa.intf.ComboJpa;
import com.github.chenyuxin.commonframework.daojpa.jparun.JpaRunner;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

/**
 * 批量 save/merge 操作 — 使用 NamedParameterJdbcTemplate.batchUpdate() 替代 EntityManager 循环
 * 
 * 实现策略：
 * 1. 将每个实体转换为 Map<String, Object>（字段名→值，支持 @Column 注解）
 * 2. 利用 CommonSql.insertSql() 生成 INSERT SQL（使用 :param 命名参数）
 * 3. 调用 batchUpdate() 批量执行，数据库层自动处理参数绑定
 * 4. 对于有主键的实体，使用 saveOrUpdateSql() 生成 UPSERT 语句（支持 MySQL/Oracle/PostgreSQL）
 */
@Service("mergeAll" + DaoConst.JPA_RUNNER_SUFFIX)
public class ComboJpaMergeAllImpl implements ComboJpa {

	private final DaoResource daoResource;

	public ComboJpaMergeAllImpl(DaoResource daoResource) {
		this.daoResource = daoResource;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public Object run(JpaRunner jpaRunner) {
		Collection<?> entityList = (Collection<?>) jpaRunner.getEntity();
		if (entityList == null || entityList.isEmpty()) {
			return new ArrayList<Object>();
		}

		NamedParameterJdbcTemplate jdbcTemplate = daoResource.moreJdbcTemplate(jpaRunner.getDataSourceName());
		List<Map<String, Object>> paramLists = new ArrayList<>();
		boolean allHasId = true;

		// 第一步：将每个实体转换为参数 Map
		for (Object entity : entityList) {
			Map<String, Object> paramMap = entityToMap(entity);
			if (paramMap == null) {
				continue;
			}
			paramLists.add(paramMap);
			if (!hasId(entity)) {
				allHasId = false;
			}
		}

		if (paramLists.isEmpty()) {
			return new ArrayList<Object>();
		}

		Class<?> entityClass = jpaRunner.getEntityClass();
		var tableType = jpaRunner.getTableType();
		if (tableType == null) {
			tableType = TableType.of(DaoUtil.camelToUnderline(entityClass.getSimpleName()));
		}

		String tableName = tableType.getTableName();

		String sql;

		// 第二步：根据是否有主键选择 SQL 策略
		if (allHasId) {
			// 有主键 → 使用 saveOrUpdateSql（UPSERT），支持 MySQL/Oracle/PostgreSQL
			com.alibaba.druid.DbType dbType = getDatabaseType();
			sql = CommonSql.saveOrUpdateSql(
				paramLists.get(0),
				tableName,
				dbType,
				getIdColumns(entityClass)
			);
		} else {
			// 无主键 → 使用 insertSql（纯插入）
			sql = CommonSql.insertSql(entityClass, tableType);
		}

		if (sql == null || sql.isEmpty()) {
			throw new RuntimeException("Failed to generate SQL for table: " + tableName);
		}

		// 将 List<Map> 转换为 SqlParameterSource[]
		SqlParameterSource[] batchParams = new SqlParameterSource[paramLists.size()];
		for (int i = 0; i < paramLists.size(); i++) {
			batchParams[i] = new MapSqlParameterSource(paramLists.get(i));
		}

		int[] rowsAffected = jdbcTemplate.batchUpdate(sql, batchParams);

		// 第三步：返回影响行数
		List<Object> result = new ArrayList<>();
		for (int rows : rowsAffected) {
			result.add(rows);
		}
		return result;
	}

	/**
	 * 将实体对象转换为 Map（字段名→值）
	 * 支持 @Column 注解映射列名，忽略 column.name="" 的字段
	 */
	private Map<String, Object> entityToMap(Object entity) {
		Map<String, Object> map = new LinkedHashMap<>();
		Class<?> clazz = entity.getClass();
		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			field.setAccessible(true);
			String fieldName = field.getName();
			String dbColumnName = fieldName;

			// 检查 @Column 注解
			Column column = field.getAnnotation(Column.class);
			if (column != null && !column.name().isEmpty()) {
				dbColumnName = column.name().toLowerCase();
			} else if (column != null && column.name().isEmpty()) {
				// @Column(name = "") 表示不在数据库中映射该字段
				continue;
			}

			try {
				Object value = field.get(entity);
				if (value != null) {
					map.put(fieldName, value);
				} else {
					// 空值也需要插入（使用 null）
					map.put(fieldName, null);
				}
			} catch (IllegalAccessException e) {
				// 忽略不可访问的字段
			}
		}
		return map;
	}

	/**
	 * 获取实体主键字段名列表（@Id 注解标识的字段）
	 */
	private String[] getIdColumns(Class<?> entityClass) {
		List<String> ids = new ArrayList<>();
		Field[] fields = entityClass.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				String colName = field.getName();
				Column column = field.getAnnotation(Column.class);
				if (column != null && !column.name().isEmpty()) {
					colName = column.name().toLowerCase();
				}
				ids.add(colName);
			}
		}
		return ids.toArray(new String[0]);
	}

	/**
	 * 判断实体是否有 @Id 主键
	 */
	private boolean hasId(Object entity) {
		Field[] fields = entity.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取数据库类型（从 DruidDataSource 解析）
	 */
	private com.alibaba.druid.DbType getDatabaseType() {
		try {
			com.alibaba.druid.DbType dbType = daoResource.getDataBaseType(DaoConst.defaultDataSourceName);
			return dbType;
		} catch (Exception e) {
			// 降级：默认 MySQL 策略
			return com.alibaba.druid.DbType.mysql;
		}
	}
}
