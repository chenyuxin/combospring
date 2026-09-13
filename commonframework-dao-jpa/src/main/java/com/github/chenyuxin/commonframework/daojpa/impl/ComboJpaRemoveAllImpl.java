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
import com.github.chenyuxin.commonframework.daojpa.common.sql.CommonSql;
import com.github.chenyuxin.commonframework.daojpa.config.DaoResource;
import com.github.chenyuxin.commonframework.daojpa.intf.ComboJpa;
import com.github.chenyuxin.commonframework.daojpa.jparun.JpaRunner;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

/**
 * 批量 remove 操作 — 使用 NamedParameterJdbcTemplate.batchUpdate() 替代 EntityManager 循环
 * 
 * 实现策略：
 * 1. 从实体中提取 @Id 主键值
 * 2. 利用 CommonSql.deleteSql() 生成 DELETE SQL（带 WHERE 主键条件）
 * 3. 调用 batchUpdate() 批量执行删除
 */
@Service("removeAll" + DaoConst.JPA_RUNNER_SUFFIX)
public class ComboJpaRemoveAllImpl implements ComboJpa {

	private final DaoResource daoResource;

	public ComboJpaRemoveAllImpl(DaoResource daoResource) {
		this.daoResource = daoResource;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public String run(JpaRunner jpaRunner) {
		Collection<?> entityList = (Collection<?>) jpaRunner.getEntity();
		if (entityList == null || entityList.isEmpty()) {
			return DaoConst.delObj_SUCCESS_MESSAGE;
		}

		NamedParameterJdbcTemplate jdbcTemplate = daoResource.moreJdbcTemplate(jpaRunner.getDataSourceName());
		Class<?> entityClass = jpaRunner.getEntityClass();
		var tableType = jpaRunner.getTableType();
		if (tableType == null) {
			tableType = com.github.chenyuxin.commonframework.daojpa.common.TableType.of(
				DaoUtil.camelToUnderline(entityClass.getSimpleName())
			);
		}

		String tableName = tableType.getTableName();
		String[] idColumns = getIdColumns(entityClass);

		if (idColumns.length == 0) {
			throw new RuntimeException("Entity class must have @Id annotation for batch remove: " + entityClass.getName());
		}

		// 第一步：从每个实体中提取主键 Map（用于 DELETE WHERE id = :xxx）
		List<Map<String, Object>> keyMaps = new ArrayList<>();
		for (Object entity : entityList) {
			Map<String, Object> keyMap = extractIdMap(entity);
			if (keyMap != null && !keyMap.isEmpty()) {
				keyMaps.add(keyMap);
			}
		}

		if (keyMaps.isEmpty()) {
			throw new RuntimeException("No valid entity with @Id found for batch remove");
		}

		// 第二步：生成 DELETE SQL（使用 CommonSql.deleteSql）
		String sql = CommonSql.deleteSql(tableName, idColumns);
		if (sql == null || sql.isEmpty()) {
			throw new RuntimeException("Failed to generate DELETE SQL for table: " + tableName);
		}

		// 第三步：批量执行
		SqlParameterSource[] batchParams = new SqlParameterSource[keyMaps.size()];
		for (int i = 0; i < keyMaps.size(); i++) {
			batchParams[i] = new MapSqlParameterSource(keyMaps.get(i));
		}

		int[] rowsAffected = jdbcTemplate.batchUpdate(sql, batchParams);

		return DaoConst.delObj_SUCCESS_MESSAGE;
	}

	/**
	 * 从实体中提取 @Id 主键字段值
	 */
	private Map<String, Object> extractIdMap(Object entity) {
		Map<String, Object> map = new LinkedHashMap<>();
		Class<?> clazz = entity.getClass();
		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				field.setAccessible(true);
				String fieldName = field.getName();
				String dbColumnName = fieldName;

				// 检查 @Column 注解映射
				Column column = field.getAnnotation(Column.class);
				if (column != null && !column.name().isEmpty()) {
					dbColumnName = column.name().toLowerCase();
				}

				try {
					Object value = field.get(entity);
					if (value != null) {
						map.put(fieldName, value);
					} else {
						// null 主键也传入（数据库会处理）
						map.put(fieldName, null);
					}
				} catch (IllegalAccessException e) {
					// 忽略
				}
			}
		}
		return map;
	}

	/**
	 * 获取实体所有 @Id 主键字段名（下划线格式）
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
}
