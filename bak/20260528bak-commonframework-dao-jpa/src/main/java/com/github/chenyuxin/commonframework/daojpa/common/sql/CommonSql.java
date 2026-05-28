package com.github.chenyuxin.commonframework.daojpa.common.sql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.DbType;
import com.github.chenyuxin.commonframework.base.constant.StringPool;
import com.github.chenyuxin.commonframework.daojpa.common.DaoUtil;
import com.github.chenyuxin.commonframework.daojpa.common.TableType;
import com.github.chenyuxin.commonframework.daojpa.option.QueryCondition;
import com.github.chenyuxin.commonframework.daojpa.option.custom.OrderCondition;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

/**
 * 通用sql
 */
public class CommonSql {

	/**
	 * 判断类属性名如何转换成数据库字段名
	 * 
	 * @param clazz
	 * @param dbType 数据库操作方式 insert 、 select 和 totalselect
	 * @return List<String>
	 */
	public static <T> List<String> getConvertColNames(Class<T> clazz, String dbType) {
		List<String> objFieldList = new ArrayList<String>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String colName = DaoUtil.camelToUnderline(field.getName());
			Column column = field.getAnnotation(Column.class);
			if (null != column) {
				if (StringPool.BLANK.equals(column.name())) {// 若字段名为""，则在数据库中不使用该字段
					continue;
				} else {
					if ("select".equals(dbType)) {
						colName = column.name().toLowerCase().concat(" as \"").concat(colName).concat("\" ");
					} else if ("insert".equals(dbType)) {
						colName = column.name().toLowerCase();
					} else if ("totalselect".equals(dbType)) {// 用于关联查询返回的字段名
						// 有嵌套时外层查询直接使用as后的字段field.getName()
						colName = column.name().toLowerCase().concat(" as \"").concat(field.getName()).concat("\" ");
					}
				}
			} else {
				if ("totalselect".equals(dbType)) {// 用于关联查询返回的字段名
					colName = colName.concat(" as \"").concat(field.getName()).concat("\" ");
				}
			}

			objFieldList.add(new String(colName.getBytes()));
		}

		return objFieldList;
	}

	/**
	 * 类转插入语句
	 * 
	 * @param <T>       保存的对象
	 * @param tableName 表名 ,可选自己定义,可选填null,默认使用javaBean定义的表名
	 * @return
	 */
	public static <T> String insertSql(Class<T> clazz, TableType tableType) {
		StringBuilder sBuilder = new StringBuilder();
		List<String> objFieldList = getConvertColNames(clazz, "insert");
		List<String> objFieldListO = new ArrayList<String>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String name = new String(field.getName());
			Column column = field.getAnnotation(Column.class);
			if (null != column) {
				if (StringPool.BLANK.equals(column.name())) {// 若字段名为""，则在数据库中不使用该字段
					continue;
				} else {
					name = column.name().toLowerCase();
				}
			}
			objFieldListO.add(name);
		}

		sBuilder.append("insert into ");
		String tableName = tableType.getTableName();
		sBuilder.append(tableName);
		sBuilder.append(" (");

		for (int i = 0; i < objFieldList.size(); i++) {
			String fieldName = objFieldList.get(i);
			sBuilder.append(fieldName);
			if (i != objFieldList.size() - 1) {
				sBuilder.append(StringPool.COMMA);
			}
		}
		sBuilder.append(") values (:");
		for (int i = 0; i < objFieldListO.size(); i++) {
			sBuilder.append(objFieldListO.get(i));
			if (i != objFieldListO.size() - 1) {
				sBuilder.append(",:");
			}
		}
		sBuilder.append(")");

		return sBuilder.toString();
	}

	/**
	 * map转插入语句(注意不转驼峰)
	 * 
	 * @param clazz
	 * @param tableType
	 * @return
	 */
	public static <T> String insertSql4Map(Map<String, Object> map, TableType tableType) {
		StringBuilder sBuilder = new StringBuilder();
		List<String> objFieldList = new ArrayList<String>();

		sBuilder.append("insert into ");
		sBuilder.append(tableType.getTableName());
		sBuilder.append(" (");

		for (Map.Entry<String, Object> e : map.entrySet()) {
			objFieldList.add(e.getKey());
		}

		/*
		 * 可能的遍历方法
		 * Iterator<Map.Entry<String, Object>> e = map.entrySet().iterator();
		 * while (e.hasNext()) {
		 * Map.Entry<String, Object> entry = e.next();
		 * String fieldName = entry.getKey();
		 * sBuilder.append(fieldName);
		 * if (e.hasNext()) {
		 * sBuilder.append(StringPool.COMMA);
		 * }
		 * }
		 */

		for (int i = 0; i < objFieldList.size(); i++) {
			String fieldName = objFieldList.get(i);
			// sBuilder.append(fieldName);
			sBuilder.append(DaoUtil.camelToUnderline(fieldName));

			if (i != objFieldList.size() - 1) {
				sBuilder.append(StringPool.COMMA);
			}
		}
		sBuilder.append(") values (:");
		for (int i = 0; i < objFieldList.size(); i++) {
			sBuilder.append(objFieldList.get(i));
			if (i != objFieldList.size() - 1) {
				sBuilder.append(",:");
			}
		}
		sBuilder.append(")");

		return sBuilder.toString();
	}

	/**
	 * 获取更新语句(注意驼峰)
	 * 
	 * @param paramMap       参数Map
	 * @param tableName      数据库表名
	 * @param fieldNameByIds 根据某字段名作为筛选条件
	 * @return
	 */
	public static String updateSql(Map<String, Object> paramMap, String tableName, String... fieldNameByIds) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("update ").append(tableName).append(" set ");
		Iterator<Map.Entry<String, Object>> e = paramMap.entrySet().iterator();
		while (e.hasNext()) {
			Map.Entry<String, Object> param = e.next();
			sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
			sBuilder.append(" = :").append(param.getKey());
			if (e.hasNext()) {
				sBuilder.append(StringPool.COMMA);
			}
		}

		boolean whereFlag = true;
		for (int i = 0; i < fieldNameByIds.length; i++) {
			String fieldNameById = fieldNameByIds[i];
			if (null != fieldNameById && !StringPool.BLANK.equals(fieldNameById)) {
				if (whereFlag) {
					sBuilder.append(" where ").append(DaoUtil.camelToUnderline(fieldNameById)).append(" = :")
							.append(fieldNameById);
					whereFlag = false;
				} else {
					sBuilder.append(" and ").append(DaoUtil.camelToUnderline(fieldNameById)).append(" = :")
							.append(fieldNameById);
				}
			}

		}

		String sql = sBuilder.toString();
		if (!sql.contains("where")) {
			return StringPool.BLANK;// 没有筛选条件，fieldNameById的值不合适
		}

		return sql;

	}

	/**
	 * 获取新增或更新语句(注意驼峰),mysql数据库必须数据库表建立主键才能正确使用此方法
	 * 
	 * @param paramMap       参数Map
	 * @param tableName      数据库表名
	 * @param dataBaseType   数据库类型
	 * @param fieldNameByIds 根据某字段名作为筛选条件
	 * @return
	 * 
	 */
	public static String saveOrUpdateSql(Map<String, Object> paramMap, String tableName, DbType dataBaseType,
			String... fieldNameByIds) {
		StringBuilder sBuilder = new StringBuilder();
		if (DbType.mysql.equals(dataBaseType)) {// 必须mysql数据库建立主键
			// insert into test (TEST_ID, TEST_NAME) values ('223','insert') ON DUPLICATE
			// KEY UPDATE TEST_NAME = 'update';
			sBuilder.append(" insert into ").append(tableName).append(" ( ");
			Iterator<Map.Entry<String, Object>> eIn = paramMap.entrySet().iterator();
			while (eIn.hasNext()) {
				Map.Entry<String, Object> param = eIn.next();
				sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
				if (eIn.hasNext()) {
					sBuilder.append(StringPool.COMMA);
				}
			}
			sBuilder.append(" ) values ( :");
			Iterator<Map.Entry<String, Object>> eValues = paramMap.entrySet().iterator();
			while (eValues.hasNext()) {
				Map.Entry<String, Object> param = eValues.next();
				sBuilder.append(param.getKey());
				if (eValues.hasNext()) {
					sBuilder.append(", :");
				}
			}
			sBuilder.append(" ) ON DUPLICATE KEY UPDATE ");
			Iterator<Map.Entry<String, Object>> eSet = paramMap.entrySet().iterator();
			a: while (eSet.hasNext()) {
				Map.Entry<String, Object> param = eSet.next();
				for (int i = 0; i < fieldNameByIds.length; i++) {
					if (param.getKey().equals(fieldNameByIds[i])) {
						if (!eSet.hasNext()) {
							// 删除 之前添加StringPool.COMMA
							sBuilder.deleteCharAt(sBuilder.lastIndexOf(StringPool.COMMA));
						}
						continue a;
					}
				}

				sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
				sBuilder.append(" = :").append(param.getKey());
				if (eSet.hasNext()) {
					sBuilder.append(StringPool.COMMA);
				}
			}

		} else if (DbType.oracle.equals(dataBaseType)) {
			sBuilder.append(" merge into ").append(tableName).append(" t1 using ( select ");
			Iterator<Map.Entry<String, Object>> e = paramMap.entrySet().iterator();
			while (e.hasNext()) {
				Map.Entry<String, Object> param = e.next();
				sBuilder.append(" :").append(param.getKey());
				sBuilder.append(" AS \"");
				sBuilder.append(DaoUtil.camelToUnderline(param.getKey())).append(StringPool.QUOTE);
				if (e.hasNext()) {
					sBuilder.append(StringPool.COMMA);
				}
			}
			sBuilder.append(" from dual ) t2 on ( ");
			for (int i = 0; i < fieldNameByIds.length; i++) {
				String fieldNameById = fieldNameByIds[i];
				if (null != fieldNameById && !StringPool.BLANK.equals(fieldNameById)) {
					sBuilder.append(" t1.").append(DaoUtil.camelToUnderline(fieldNameById)).append(" = t2.")
							.append(DaoUtil.camelToUnderline(fieldNameById));
					if (i != fieldNameByIds.length - 1 && null != fieldNameByIds[i + 1]
							&& !StringPool.BLANK.equals(fieldNameByIds[i + 1])) {
						sBuilder.append(" and ");
					}

				}
			}
			sBuilder.append(" ) ");
			if (paramMap.size() == fieldNameByIds.length) {
				// 主键字段就是全部字段的情况，就什么都不做
			} else {
				sBuilder.append(" when matched then update set ");
				Iterator<Map.Entry<String, Object>> eSet = paramMap.entrySet().iterator();
				a: while (eSet.hasNext()) {
					Map.Entry<String, Object> param = eSet.next();
					for (int i = 0; i < fieldNameByIds.length; i++) {
						if (param.getKey().equals(fieldNameByIds[i])) {
							if (!eSet.hasNext()) {
								// 删除 之前添加StringPool.COMMA
								sBuilder.deleteCharAt(sBuilder.lastIndexOf(StringPool.COMMA));
							}
							continue a;
						}
					}

					sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
					sBuilder.append(" = :").append(param.getKey());
					if (eSet.hasNext()) {
						sBuilder.append(StringPool.COMMA);
					}
				}
			}
			sBuilder.append(" when not matched then insert ( ");
			Iterator<Map.Entry<String, Object>> eIn = paramMap.entrySet().iterator();
			while (eIn.hasNext()) {
				Map.Entry<String, Object> param = eIn.next();
				sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
				if (eIn.hasNext()) {
					sBuilder.append(StringPool.COMMA);
				}
			}
			sBuilder.append(" ) values ( :");
			Iterator<Map.Entry<String, Object>> eValues = paramMap.entrySet().iterator();
			while (eValues.hasNext()) {
				Map.Entry<String, Object> param = eValues.next();
				sBuilder.append(param.getKey());
				if (eValues.hasNext()) {
					sBuilder.append(", :");
				}
			}
			sBuilder.append(" ) ");
		} else if (DbType.postgresql.equals(dataBaseType)) {// 必须POSTGREPSQL数据库建立主键或唯一索引
			// insert into test1 (name,id) values ('测试name','XN_JZtcKHmJWHSxZVeQrsg') on
			// conflict (id) do update set name='测试name',id='XN-JZtcKHmJWHSxZVeQrsg';
			sBuilder.append(" insert into ").append(tableName).append(" ( ");
			Iterator<Map.Entry<String, Object>> eIn = paramMap.entrySet().iterator();
			while (eIn.hasNext()) {
				Map.Entry<String, Object> param = eIn.next();
				sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
				if (eIn.hasNext()) {
					sBuilder.append(StringPool.COMMA);
				}
			}
			sBuilder.append(" ) values ( :");
			Iterator<Map.Entry<String, Object>> eValues = paramMap.entrySet().iterator();
			while (eValues.hasNext()) {
				Map.Entry<String, Object> param = eValues.next();
				sBuilder.append(param.getKey());
				if (eValues.hasNext()) {
					sBuilder.append(", :");
				}
			}
			sBuilder.append(" ) on conflict ( ");
			for (int i = 0; i < fieldNameByIds.length; i++) {
				sBuilder.append(fieldNameByIds[i]);
				if (i < fieldNameByIds.length - 1) {
					sBuilder.append(StringPool.COMMA);
				}
			}
			sBuilder.append(")");
			if (paramMap.size() == fieldNameByIds.length) {
				sBuilder.append(" do nothing ");// 主键字段就是全部字段的情况，就什么都不做
			} else {
				sBuilder.append(" do update set ");
				Iterator<Map.Entry<String, Object>> eSet = paramMap.entrySet().iterator();
				a: while (eSet.hasNext()) {
					Map.Entry<String, Object> param = eSet.next();
					for (int i = 0; i < fieldNameByIds.length; i++) {
						if (param.getKey().equals(fieldNameByIds[i])) {
							if (!eSet.hasNext()) {
								// 删除 在while循环的最后添加的StringPool.COMMA
								sBuilder.deleteCharAt(sBuilder.lastIndexOf(StringPool.COMMA));
							}
							continue a;
						}
					}

					sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
					sBuilder.append(" = :").append(param.getKey());
					if (eSet.hasNext()) {
						sBuilder.append(StringPool.COMMA);
					}
				}

			}

		}
		return sBuilder.toString();
	}

	/**
	 * 获取删除语句(注意驼峰)
	 * 
	 * @param tableName      数据库表名
	 * @param fieldNameByIds 根据某字段名作为筛选条件
	 * @return
	 */
	public static String deleteSql(String tableName, String[] fieldNameByIds) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("delete from ").append(tableName);
		boolean whereFlag = true;
		for (int i = 0; i < fieldNameByIds.length; i++) {
			String fieldNameById = fieldNameByIds[i];
			if (null != fieldNameById && !StringPool.BLANK.equals(fieldNameById)) {
				if (whereFlag) {
					sBuilder.append(" where ").append(DaoUtil.camelToUnderline(fieldNameById)).append(" = :")
							.append(fieldNameById);
					whereFlag = false;
				} else {
					sBuilder.append(" and ").append(DaoUtil.camelToUnderline(fieldNameById)).append(" = :")
							.append(fieldNameById);
				}
			}

		}

		String sql = sBuilder.toString();
		if (!sql.contains("where")) {
			return StringPool.BLANK;// 没有筛选条件，fieldNameById的值不合适
		}

		return sql;
	}

	/**
	 * 查询sql获取实体模型Map 带条件查询(注意paramMap会驼峰转下划线)
	 * 
	 * @param attributeNames  属性list，数据库字段名
	 * @param tableName       数据库表名
	 * @param paramMap        查询条件参数Map(传入的对象会被修改！)
	 * @param queryConditions 自定义通用查询条件 (并且将查询条件参数并入paramMap)
	 */
	public static String selectSql4Map(List<String> attributeNames, String tableName, Map<String, Object> paramMap,
			QueryCondition... queryConditions) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("select ");
		for (int i = 0; i < attributeNames.size(); i++) {
			sBuilder.append(attributeNames.get(i)).append(" as \"").append(attributeNames.get(i)).append("\" ");// 注意oacle此处as保证attributeName的在map里面的key的大小写准确;
			if (i != attributeNames.size() - 1) {
				sBuilder.append(StringPool.COMMA);
			}
		}
		sBuilder.append(" from ").append(tableName);
		Iterator<Map.Entry<String, Object>> e = paramMap.entrySet().iterator();
		if (e.hasNext()) {
			Map.Entry<String, Object> param = e.next();
			sBuilder.append(" where ");
			sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
			sBuilder.append(" = :");
			sBuilder.append(param.getKey());
		}
		while (e.hasNext()) {
			Map.Entry<String, Object> param = e.next();
			sBuilder.append(" and ");
			sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
			sBuilder.append(" = :");
			sBuilder.append(param.getKey());
		}

		if (null != queryConditions) {
			boolean whereFlag = false;
			if (null == paramMap || paramMap.isEmpty()) {
				whereFlag = true;
			}
			for (int i = 0; i < queryConditions.length; i++) {
				QueryCondition queryCondition = queryConditions[i];
				if (null != queryCondition) {
					if (queryCondition instanceof OrderCondition) {
						continue;
					}
					String where;
					if (whereFlag) {
						where = "where";
						whereFlag = false;
					} else {
						where = "and";
					}
					String conditionSql = queryCondition.getSqlString(where);
					sBuilder.append(conditionSql);
					queryCondition.setParamMap(paramMap);// 将自定义条件的参数map加入总的参数map
				}
			}

			StringBuilder orderBuilder = new StringBuilder();
			for (int i = 0; i < queryConditions.length; i++) {
				QueryCondition queryCondition = queryConditions[i];
				if (null != queryCondition && queryCondition instanceof OrderCondition) {
					orderBuilder.append(queryCondition.getSqlString(StringPool.BLANK));
				}
			}
			if (orderBuilder.length() > 0) {
				sBuilder.append(" order by ");
				sBuilder.append(orderBuilder);
				sBuilder.deleteCharAt(sBuilder.lastIndexOf(StringPool.COMMA));
			}
		}
		return sBuilder.toString();

	}

	/**
	 * 查询sql获取实体模型Map封装的List,带参数条件
	 * 
	 * @param attributeNames  属性list
	 * @param tableName       数据库表名
	 * @param pageNo          第几页
	 * @param pageSize        分页数，页面大小
	 * @param dataBaseType    数据库类型
	 * @param paramMap        条件参数查询
	 * @param queryConditions 自定义通用查询条件 (并且将查询条件参数并入paramMap)
	 * @return
	 */
	public static String selectSql4Map(List<String> attributeNames, String tableName, int pageNo, int pageSize,
			DbType dataBaseType, Map<String, Object> paramMap, QueryCondition... queryConditions) {
		StringBuilder sBuilder = new StringBuilder();
		if (DbType.mysql.equals(dataBaseType)) {
			sBuilder.append("select ");
			for (int i = 0; i < attributeNames.size(); i++) {
				sBuilder.append(attributeNames.get(i));
				if (i != attributeNames.size() - 1) {
					sBuilder.append(StringPool.COMMA);
				}
			}
			sBuilder.append(" from ").append(tableName);

			Iterator<Map.Entry<String, Object>> e = paramMap.entrySet().iterator();
			if (e.hasNext()) {
				Map.Entry<String, Object> param = e.next();
				sBuilder.append(" where ");
				sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
				sBuilder.append(" = :");
				sBuilder.append(param.getKey());
			}
			while (e.hasNext()) {
				Map.Entry<String, Object> param = e.next();
				sBuilder.append(" and ");
				sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
				sBuilder.append(" = :");
				sBuilder.append(param.getKey());
			}

			if (null != queryConditions) {
				boolean whereFlag = false;
				if (null == paramMap || paramMap.isEmpty()) {
					whereFlag = true;
				}
				for (int i = 0; i < queryConditions.length; i++) {
					QueryCondition queryCondition = queryConditions[i];
					if (null != queryCondition) {
						if (queryCondition instanceof OrderCondition) {
							continue;
						}
						String where;
						if (whereFlag) {
							where = "where";
							whereFlag = false;
						} else {
							where = "and";
						}
						String conditionSql = queryCondition.getSqlString(where);
						sBuilder.append(conditionSql);
						queryCondition.setParamMap(paramMap);// 将自定义条件的参数map加入总的参数map
					}
				}

				StringBuilder orderBuilder = new StringBuilder();
				for (int i = 0; i < queryConditions.length; i++) {
					QueryCondition queryCondition = queryConditions[i];
					if (null != queryCondition && queryCondition instanceof OrderCondition) {
						orderBuilder.append(queryCondition.getSqlString(StringPool.BLANK));
					}
				}
				if (orderBuilder.length() > 0) {
					sBuilder.append(" order by ");
					sBuilder.append(orderBuilder);
					sBuilder.deleteCharAt(sBuilder.lastIndexOf(StringPool.COMMA));
				}
			}

			sBuilder.append(" limit ").append((pageNo - 1) * pageSize).append(StringPool.COMMA).append(pageSize);

		} else if (DbType.oracle.equals(dataBaseType)) {
			sBuilder.append("select ");
			for (int i = 0; i < attributeNames.size(); i++) {
				sBuilder.append(attributeNames.get(i)).append(" as \"").append(attributeNames.get(i)).append("\" ");// 注意oacle此处as保证attributeName的在map里面的key的大小写准确
				if (i != attributeNames.size() - 1) {
					sBuilder.append(StringPool.COMMA);
				}
			}
			sBuilder.append(" from (");
			sBuilder.append("select rownum as rowno, ");
			for (int i = 0; i < attributeNames.size(); i++) {
				sBuilder.append(attributeNames.get(i));
				if (i != attributeNames.size() - 1) {
					sBuilder.append(StringPool.COMMA);
				}
			}
			sBuilder.append(" from ( select ");
			for (int i = 0; i < attributeNames.size(); i++) {
				sBuilder.append(attributeNames.get(i));
				if (i != attributeNames.size() - 1) {
					sBuilder.append(StringPool.COMMA);
				}
			}
			sBuilder.append(" from ").append(tableName);
			Iterator<Map.Entry<String, Object>> e = paramMap.entrySet().iterator();
			if (e.hasNext()) {
				Map.Entry<String, Object> param = e.next();
				sBuilder.append(" where ");
				sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
				sBuilder.append(" = :");
				sBuilder.append(param.getKey());
			}
			while (e.hasNext()) {
				Map.Entry<String, Object> param = e.next();
				sBuilder.append(" and ");
				sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
				sBuilder.append(" = :");
				sBuilder.append(param.getKey());
			}
			if (null != queryConditions) {
				boolean whereFlag = false;
				if (null == paramMap || paramMap.isEmpty()) {
					whereFlag = true;
				}
				for (int i = 0; i < queryConditions.length; i++) {
					QueryCondition queryCondition = queryConditions[i];
					if (null != queryCondition) {
						if (queryCondition instanceof OrderCondition) {
							continue;
						}
						String where;
						if (whereFlag) {
							where = "where";
							whereFlag = false;
						} else {
							where = "and";
						}
						String conditionSql = queryCondition.getSqlString(where);
						sBuilder.append(conditionSql);
						queryCondition.setParamMap(paramMap);// 将自定义条件的参数map加入总的参数map
					}
				}

				StringBuilder orderBuilder = new StringBuilder();
				for (int i = 0; i < queryConditions.length; i++) {
					QueryCondition queryCondition = queryConditions[i];
					if (null != queryCondition && queryCondition instanceof OrderCondition) {
						orderBuilder.append(queryCondition.getSqlString(StringPool.BLANK));
					}
				}
				if (orderBuilder.length() > 0) {
					sBuilder.append(" order by ");
					sBuilder.append(orderBuilder);
					sBuilder.deleteCharAt(sBuilder.lastIndexOf(StringPool.COMMA));
				}
			}
			sBuilder.append(") where rownum <= ").append(pageNo * pageSize);
			sBuilder.append(" ) where rowno > ").append((pageNo - 1) * pageSize);

		} else if (DbType.postgresql.equals(dataBaseType)) {
			sBuilder.append("select ");
			for (int i = 0; i < attributeNames.size(); i++) {
				sBuilder.append(attributeNames.get(i));
				if (i != attributeNames.size() - 1) {
					sBuilder.append(StringPool.COMMA);
				}
			}
			sBuilder.append(" from ").append(tableName);

			Iterator<Map.Entry<String, Object>> e = paramMap.entrySet().iterator();
			if (e.hasNext()) {
				Map.Entry<String, Object> param = e.next();
				sBuilder.append(" where ");
				sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
				sBuilder.append(" = :");
				sBuilder.append(param.getKey());
			}
			while (e.hasNext()) {
				Map.Entry<String, Object> param = e.next();
				sBuilder.append(" and ");
				sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
				sBuilder.append(" = :");
				sBuilder.append(param.getKey());
			}

			if (null != queryConditions) {
				boolean whereFlag = false;
				if (null == paramMap || paramMap.isEmpty()) {
					whereFlag = true;
				}
				for (int i = 0; i < queryConditions.length; i++) {
					QueryCondition queryCondition = queryConditions[i];
					if (null != queryCondition) {
						if (queryCondition instanceof OrderCondition) {
							continue;
						}
						String where;
						if (whereFlag) {
							where = "where";
							whereFlag = false;
						} else {
							where = "and";
						}
						String conditionSql = queryCondition.getSqlString(where);
						sBuilder.append(conditionSql);
						queryCondition.setParamMap(paramMap);// 将自定义条件的参数map加入总的参数map
					}
				}

				StringBuilder orderBuilder = new StringBuilder();
				for (int i = 0; i < queryConditions.length; i++) {
					QueryCondition queryCondition = queryConditions[i];
					if (null != queryCondition && queryCondition instanceof OrderCondition) {
						orderBuilder.append(queryCondition.getSqlString(StringPool.BLANK));
					}
				}
				if (orderBuilder.length() > 0) {
					sBuilder.append(" order by ");
					sBuilder.append(orderBuilder);
					sBuilder.deleteCharAt(sBuilder.lastIndexOf(StringPool.COMMA));
				}
			}

			sBuilder.append(" limit ").append(pageSize).append(" offset ").append((pageNo - 1) * pageSize);
		}

		return sBuilder.toString();
	}

	/**
	 * 查询sql获取实体对象list
	 * 
	 * @param clazz           对象
	 * @param pageNo          第几页
	 * @param pageSize        每页大小,分页大小
	 * @param dataBaseType    数据库类型
	 * @param queryConditions 自定义通用查询条件
	 * @return
	 */
	public static <T> String selectSql4Obj(Class<T> clazz, int pageNo, int pageSize, DbType dataBaseType,
			TableType tableType,
			Map<String, Object> paramMap, QueryCondition... queryConditions) {
		StringBuilder sBuilder = new StringBuilder();
		List<String> objFieldList = CommonSql.getConvertColNames(clazz, "select");
		if (null == tableType) {
			Table table = clazz.getAnnotation(Table.class);
			tableType = TableType.of(table.name());
		}

		if (DbType.mysql.equals(dataBaseType)) {
			sBuilder.append("select ");
			for (int i = 0; i < objFieldList.size(); i++) {
				String fieldName = objFieldList.get(i);
				sBuilder.append(fieldName);
				if (i != objFieldList.size() - 1) {
					sBuilder.append(StringPool.COMMA);
				}
			}
			sBuilder.append(" from ").append(tableType.getTableName());

			Iterator<Map.Entry<String, Object>> e = paramMap.entrySet().iterator();
			if (e.hasNext()) {
				Map.Entry<String, Object> param = e.next();
				sBuilder.append(" where ");
				sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
				sBuilder.append(" = :");
				sBuilder.append(param.getKey());
			}
			while (e.hasNext()) {
				Map.Entry<String, Object> param = e.next();
				sBuilder.append(" and ");
				sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
				sBuilder.append(" = :");
				sBuilder.append(param.getKey());
			}

			if (null != queryConditions) {
				boolean whereFlag = false;
				if (null == paramMap || paramMap.isEmpty()) {
					whereFlag = true;
				}
				for (int i = 0; i < queryConditions.length; i++) {
					QueryCondition queryCondition = queryConditions[i];
					if (null != queryCondition) {
						if (queryCondition instanceof OrderCondition) {
							continue;
						}
						String where;
						if (whereFlag) {
							where = "where";
							whereFlag = false;
						} else {
							where = "and";
						}
						String conditionSql = queryCondition.getSqlString(where);
						sBuilder.append(conditionSql);
						queryCondition.setParamMap(paramMap);// 将自定义条件的参数map加入总的参数map
					}
				}

				StringBuilder orderBuilder = new StringBuilder();
				for (int i = 0; i < queryConditions.length; i++) {
					QueryCondition queryCondition = queryConditions[i];
					if (null != queryCondition && queryCondition instanceof OrderCondition) {
						orderBuilder.append(queryCondition.getSqlString(StringPool.BLANK));
					}
				}
				if (orderBuilder.length() > 0) {
					sBuilder.append(" order by ");
					sBuilder.append(orderBuilder);
					sBuilder.deleteCharAt(sBuilder.lastIndexOf(StringPool.COMMA));
				}
			}

			sBuilder.append(" limit ").append((pageNo - 1) * pageSize).append(StringPool.COMMA).append(pageSize);

		} else if (DbType.oracle.equals(dataBaseType)) {
			sBuilder.append("select ");
			for (int i = 0; i < objFieldList.size(); i++) {
				String fieldName = objFieldList.get(i);
				if (fieldName.contains(" as ")) {
					int beginIndex = fieldName.lastIndexOf(" as ") + 4;
					fieldName = fieldName.substring(beginIndex);
					if (fieldName.contains(StringPool.QUOTE)) {
						fieldName.replaceAll(StringPool.QUOTE, StringPool.BLANK);
					}
				}
				sBuilder.append(fieldName);
				if (i != objFieldList.size() - 1) {
					sBuilder.append(StringPool.COMMA);
				}
			}

			sBuilder.append(" from ( select rownum as rowno, ");
			for (int i = 0; i < objFieldList.size(); i++) {
				String fieldName = objFieldList.get(i);
				sBuilder.append(fieldName);
				if (i != objFieldList.size() - 1) {
					sBuilder.append(StringPool.COMMA);
				}
			}

			sBuilder.append(" from (");
			sBuilder.append("select ");
			for (int i = 0; i < objFieldList.size(); i++) {
				String fieldName = objFieldList.get(i);
				sBuilder.append(fieldName);
				if (i != objFieldList.size() - 1) {
					sBuilder.append(StringPool.COMMA);
				}
			}
			sBuilder.append(" from ").append(tableType.getTableName());
			// sBuilder.append(" where rownum<= ").append(pageNo*pageSize);
			Iterator<Map.Entry<String, Object>> e = paramMap.entrySet().iterator();
			if (e.hasNext()) {
				Map.Entry<String, Object> param = e.next();
				sBuilder.append(" where ");
				sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
				sBuilder.append(" = :");
				sBuilder.append(param.getKey());
			}
			while (e.hasNext()) {
				Map.Entry<String, Object> param = e.next();
				sBuilder.append(" and ");
				sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
				sBuilder.append(" = :");
				sBuilder.append(param.getKey());
			}
			if (null != queryConditions) {
				boolean whereFlag = false;
				if (null == paramMap || paramMap.isEmpty()) {
					whereFlag = true;
				}
				for (int i = 0; i < queryConditions.length; i++) {
					QueryCondition queryCondition = queryConditions[i];
					if (null != queryCondition) {
						if (queryCondition instanceof OrderCondition) {
							continue;
						}
						String where;
						if (whereFlag) {
							where = "where";
							whereFlag = false;
						} else {
							where = "and";
						}
						String conditionSql = queryCondition.getSqlString(where);
						sBuilder.append(conditionSql);
						queryCondition.setParamMap(paramMap);// 将自定义条件的参数map加入总的参数map
					}
				}

				StringBuilder orderBuilder = new StringBuilder();
				for (int i = 0; i < queryConditions.length; i++) {
					QueryCondition queryCondition = queryConditions[i];
					if (null != queryCondition && queryCondition instanceof OrderCondition) {
						orderBuilder.append(queryCondition.getSqlString(StringPool.BLANK));
					}
				}
				if (orderBuilder.length() > 0) {
					sBuilder.append(" order by ");
					sBuilder.append(orderBuilder);
					sBuilder.deleteCharAt(sBuilder.lastIndexOf(StringPool.COMMA));
				}
			}
			sBuilder.append(") where rownum <= ").append(pageNo * pageSize);
			sBuilder.append(") where rowno >").append((pageNo - 1) * pageSize);
		} else if (DbType.postgresql.equals(dataBaseType)) {
			sBuilder.append("select ");
			for (int i = 0; i < objFieldList.size(); i++) {
				String fieldName = objFieldList.get(i);
				sBuilder.append(fieldName);
				if (i != objFieldList.size() - 1) {
					sBuilder.append(StringPool.COMMA);
				}
			}
			sBuilder.append(" from ").append(tableType.getTableName());

			Iterator<Map.Entry<String, Object>> e = paramMap.entrySet().iterator();
			if (e.hasNext()) {
				Map.Entry<String, Object> param = e.next();
				sBuilder.append(" where ");
				sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
				sBuilder.append(" = :");
				sBuilder.append(param.getKey());
			}
			while (e.hasNext()) {
				Map.Entry<String, Object> param = e.next();
				sBuilder.append(" and ");
				sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
				sBuilder.append(" = :");
				sBuilder.append(param.getKey());
			}

			if (null != queryConditions) {
				boolean whereFlag = false;
				if (null == paramMap || paramMap.isEmpty()) {
					whereFlag = true;
				}
				for (int i = 0; i < queryConditions.length; i++) {
					QueryCondition queryCondition = queryConditions[i];
					if (null != queryCondition) {
						if (queryCondition instanceof OrderCondition) {
							continue;
						}
						String where;
						if (whereFlag) {
							where = "where";
							whereFlag = false;
						} else {
							where = "and";
						}
						String conditionSql = queryCondition.getSqlString(where);
						sBuilder.append(conditionSql);
						queryCondition.setParamMap(paramMap);// 将自定义条件的参数map加入总的参数map
					}
				}

				StringBuilder orderBuilder = new StringBuilder();
				for (int i = 0; i < queryConditions.length; i++) {
					QueryCondition queryCondition = queryConditions[i];
					if (null != queryCondition && queryCondition instanceof OrderCondition) {
						orderBuilder.append(queryCondition.getSqlString(StringPool.BLANK));
					}
				}
				if (orderBuilder.length() > 0) {
					sBuilder.append(" order by ");
					sBuilder.append(orderBuilder);
					sBuilder.deleteCharAt(sBuilder.lastIndexOf(StringPool.COMMA));
				}
			}

			sBuilder.append(" limit ").append(pageSize).append(" offset ").append((pageNo - 1) * pageSize);
		}

		return sBuilder.toString();
	}

	/**
	 * 将自定义查询sql分页后返回
	 * 
	 * @param sql          分页前自定义的查询sql
	 * @param pageNo       第几页
	 * @param pageSize     每页大小,分页大小
	 * @param dataBaseType 数据库类型
	 * @return
	 */
	public static String pageing(String sql, int pageNo, int pageSize, DbType dataBaseType) {
		if (!sql.contains("select")) {// 只能操作查询语句
			if (!sql.contains("SELECT")) {
				return "传入的sql有误,只能操作查询语句";
			}
		}

		StringBuffer sBuffer = new StringBuffer();
		List<String> objFieldList = DaoSqlReader.getSelectColumns(sql, dataBaseType.name().toLowerCase());
		sBuffer.append("select ");
		for (int i = 0; i < objFieldList.size(); i++) {
			String fieldName = objFieldList.get(i);
			sBuffer.append(fieldName);
			if (i != objFieldList.size() - 1) {
				sBuffer.append(StringPool.COMMA);
			}
		}

		if (DbType.mysql.equals(dataBaseType)) {
			sBuffer.append(" from ( ");
			sBuffer.append(sql);
			sBuffer.append(" ) ");
			sBuffer.append(" limit ").append((pageNo - 1) * pageSize).append(StringPool.COMMA).append(pageSize);
		} else if (DbType.oracle.equals(dataBaseType)) {
			sBuffer.append(
					" from (select rownum as rowno5404aae525df42c9f5d38, pagingsql5404aae525df42c9f5d38.* from ( ");
			sBuffer.append(sql);
			sBuffer.append(" ) pagingsql5404aae525df42c9f5d38 ");
			sBuffer.append(" where rownum <= ").append(pageNo * pageSize);
			sBuffer.append(" ) where rowno5404aae525df42c9f5d38 >").append((pageNo - 1) * pageSize);
		} else if (DbType.postgresql.equals(dataBaseType)) {
			sBuffer.append(" from ( ");
			sBuffer.append(sql);
			sBuffer.append(" ) ");
			sBuffer.append(" limit ").append(pageSize).append(" offset ").append((pageNo - 1) * pageSize);
		}

		return sBuffer.toString();
	}

	/**
	 * 类转查询语句 (注意使用了驼峰)
	 * 
	 * @param <T>
	 * @param clazz           类
	 * @param paramMap        参数HashMap
	 * @param tableType       可选，填入时使用tableType表名
	 * @param queryConditions 自定义通用查询条件
	 * @return
	 */
	public static <T> String selectSql(Class<T> clazz, Map<String, Object> paramMap, TableType tableType,
			QueryCondition... queryConditions) {
		StringBuilder sBuilder = new StringBuilder();
		List<String> objFieldList = CommonSql.getConvertColNames(clazz, "select");
		String tableName;
		if (null == tableType) {
			Table table = clazz.getAnnotation(Table.class);
			tableName = table.name();
		} else {
			tableName = tableType.getTableName();
		}

		sBuilder.append("select ");
		for (int i = 0; i < objFieldList.size(); i++) {
			String fieldName = objFieldList.get(i);
			sBuilder.append(fieldName);
			if (i != objFieldList.size() - 1) {
				sBuilder.append(StringPool.COMMA);
			}
		}

		sBuilder.append(" from ");
		sBuilder.append(tableName);

		Iterator<Map.Entry<String, Object>> e = paramMap.entrySet().iterator();
		if (e.hasNext()) {
			Map.Entry<String, Object> param = e.next();
			sBuilder.append(" where ");
			sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
			sBuilder.append(" = :");
			sBuilder.append(param.getKey());
		}
		while (e.hasNext()) {
			Map.Entry<String, Object> param = e.next();
			sBuilder.append(" and ");
			sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
			sBuilder.append(" = :");
			sBuilder.append(param.getKey());
		}

		if (null != queryConditions) {
			boolean whereFlag = false;
			if (null == paramMap || paramMap.isEmpty()) {
				whereFlag = true;
			}
			for (int i = 0; i < queryConditions.length; i++) {
				QueryCondition queryCondition = queryConditions[i];
				if (null != queryCondition) {
					if (queryCondition instanceof OrderCondition) {
						continue;
					}
					String where;
					if (whereFlag) {
						where = "where";
						whereFlag = false;
					} else {
						where = "and";
					}
					String conditionSql = queryCondition.getSqlString(where);
					sBuilder.append(conditionSql);
					queryCondition.setParamMap(paramMap);// 将自定义条件的参数map加入总的参数map
				}
			}

			StringBuilder orderBuilder = new StringBuilder();
			for (int i = 0; i < queryConditions.length; i++) {
				QueryCondition queryCondition = queryConditions[i];
				if (null != queryCondition && queryCondition instanceof OrderCondition) {
					orderBuilder.append(queryCondition.getSqlString(StringPool.BLANK));
				}
			}
			if (orderBuilder.length() > 0) {
				sBuilder.append(" order by ");
				sBuilder.append(orderBuilder);
				sBuilder.deleteCharAt(sBuilder.lastIndexOf(StringPool.COMMA));
			}
		}

		return sBuilder.toString();
	}

	/**
	 * 获取某表数据量sql
	 * 
	 * @param tableName       表名
	 * @param queryConditions 自定义通用查询条件
	 * @return
	 */
	public static String getRecords(String tableName, Map<String, Object> paramMap, QueryCondition... queryConditions) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("select count(1) from ");
		sBuilder.append(tableName);
		Iterator<Map.Entry<String, Object>> e = paramMap.entrySet().iterator();
		if (e.hasNext()) {
			Map.Entry<String, Object> param = e.next();
			sBuilder.append(" where ");
			sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
			sBuilder.append(" = :");
			sBuilder.append(param.getKey());
		}
		while (e.hasNext()) {
			Map.Entry<String, Object> param = e.next();
			sBuilder.append(" and ");
			sBuilder.append(DaoUtil.camelToUnderline(param.getKey()));
			sBuilder.append(" = :");
			sBuilder.append(param.getKey());
		}

		if (null != queryConditions) {
			boolean whereFlag = false;
			if (null == paramMap || paramMap.isEmpty()) {
				whereFlag = true;
			}
			for (int i = 0; i < queryConditions.length; i++) {
				QueryCondition queryCondition = queryConditions[i];
				if (queryCondition instanceof OrderCondition) {
					continue;// 排序条件不用添加到记录数查询条件
				}
				if (null != queryCondition) {
					String where;
					if (whereFlag) {
						where = "where";
						whereFlag = false;
					} else {
						where = "and";
					}
					String conditionSql = queryCondition.getSqlString(where);
					sBuilder.append(conditionSql);
					queryCondition.setParamMap(paramMap);// 将自定义条件的参数map加入总的参数map
				}
			}
		}
		return sBuilder.toString();
	}

}
