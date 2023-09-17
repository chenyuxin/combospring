package com.github.chenyuxin.commonframework.dao.common.sql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.druid.DbType;
import com.github.chenyuxin.commonframework.base.constant.StringPool;
import com.github.chenyuxin.commonframework.dao.common.DaoUtil;
import com.github.chenyuxin.commonframework.dao.common.TableType;
import com.github.chenyuxin.commonframework.dao.common.anotation.Column;
import com.github.chenyuxin.commonframework.dao.common.anotation.Table;
import com.github.chenyuxin.commonframework.dao.common.custom.QueryCondition;
import com.github.chenyuxin.commonframework.dao.common.custom.QueryConditionCommonOrder;
import com.github.chenyuxin.commonframework.dao.common.option.JoinOptions;


/**
 * 通用sql
 */
public class CommonSql {
	
	/**
	 * 判断类属性名如何转换成数据库字段名
	 * @param clazz
	 * @param dbType 数据库操作方式 insert 、 select 和 totalselect
	 * @return List<String>
	 */
	public static <T> List<String> getConvertColNames(Class<T> clazz,String dbType){
		List<String> objFieldList = new ArrayList<String>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String colName = DaoUtil.camelToUnderline(field.getName());
			Column column = field.getAnnotation(Column.class);
			if (null != column){
				if (StringPool.BLANK.equals(column.name())){//若字段名为""，则在数据库中不使用该字段
					continue;
				} else {
					if ("select".equals(dbType)) {
						colName = column.name().toLowerCase().concat(" as \"").concat(colName).concat("\" ");
					} else if ("insert".equals(dbType)) {
						colName = column.name().toLowerCase();
					} else if ("totalselect".equals(dbType)){//用于关联查询返回的字段名
						//有嵌套时外层查询直接使用as后的字段field.getName()
						colName =  column.name().toLowerCase().concat(" as \"").concat(field.getName()).concat("\" ");
					}
				}
			} else {
				if ("totalselect".equals(dbType)){//用于关联查询返回的字段名
					colName =  colName.concat(" as \"").concat(field.getName()).concat("\" ");
				}
			}
			
			
			objFieldList.add(new String(colName.getBytes()));
		}
		
		return objFieldList;
	}
	
	
	/**
	 * 类转插入语句
	 * @param <T> 保存的对象
	 * @param tableName 表名 ,可选自己定义,可选填null,默认使用javaBean定义的表名
	 * @return
	 */
	public static <T> String insertSql(Class<T> clazz, TableType tableType){
		StringBuffer sBuffer = new StringBuffer();
		List<String> objFieldList = getConvertColNames(clazz,"insert");
		List<String> objFieldListO = new ArrayList<String>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String name = new String(field.getName());
			Column column = field.getAnnotation(Column.class);
			if (null != column){
				if (StringPool.BLANK.equals(column.name())){//若字段名为""，则在数据库中不使用该字段
					continue;
				} else {
					name = column.name().toLowerCase();
				}	
			}
			objFieldListO.add(name);
		}
		
		sBuffer.append("insert into ");
		String tableName = tableType.getTableName();
		sBuffer.append(tableName);
		sBuffer.append(" (");
		
		for(int i =0;i<objFieldList.size();i++){
			String fieldName = objFieldList.get(i);
			sBuffer.append(fieldName);
			if (i != objFieldList.size()-1){
				sBuffer.append(",");
			}
		}
		sBuffer.append(") values (:");
		for(int i =0;i<objFieldListO.size();i++){
			sBuffer.append(objFieldListO.get(i));
			if (i != objFieldListO.size()-1){
				sBuffer.append(",:");
			}
		}
		sBuffer.append(")");
		
		return sBuffer.toString();
	}
	
	/**
	 * map转插入语句(注意不转驼峰)
	 * @param clazz
	 * @param tableType
	 * @return
	 */
	public static <T> String insertSql4Map(Map<String,Object> map, TableType tableType) {
		StringBuffer sBuffer = new StringBuffer();
		List<String> objFieldList = new ArrayList<String>();
		
		sBuffer.append("insert into ");
		sBuffer.append(tableType.getTableName());
		sBuffer.append(" (");
		
		for (Map.Entry<String,Object> e : map.entrySet() ) {
			objFieldList.add(e.getKey());
		}
		
		/*可能的遍历方法
		Iterator<Map.Entry<String, Object>> e = map.entrySet().iterator();
		while (e.hasNext()) {
			Map.Entry<String, Object> entry = e.next();
			String fieldName = entry.getKey();
			sBuffer.append(fieldName);
			if (e.hasNext()) {
				sBuffer.append(",");
			}
		}
		*/
		
		for(int i =0;i<objFieldList.size();i++){
			String fieldName = objFieldList.get(i);
			//sBuffer.append(fieldName);
			sBuffer.append(DaoUtil.camelToUnderline(fieldName));
			
			if (i != objFieldList.size()-1){
				sBuffer.append(",");
			}
		}
		sBuffer.append(") values (:");
		for(int i =0;i<objFieldList.size();i++){
			sBuffer.append(objFieldList.get(i));
			if (i != objFieldList.size()-1){
				sBuffer.append(",:");
			}
		}
		sBuffer.append(")");
		
		return sBuffer.toString();
	}
	
	/**
	 * 获取更新语句(注意驼峰)
	 * @param paramMap 参数Map
	 * @param tableName 数据库表名
	 * @param fieldNameByIds 根据某字段名作为筛选条件
	 * @return
	 */
	public static String updateSql(Map<String,Object> paramMap, String tableName, String... fieldNameByIds) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("update ").append(tableName).append(" set ");
		Iterator<Map.Entry<String, Object>> e = paramMap.entrySet().iterator();
		while (e.hasNext()) {
			Map.Entry<String, Object> param = e.next();
			sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
			sBuffer.append(" = :").append(param.getKey());
			if (e.hasNext()) {
				sBuffer.append(",");
			}
		}
		
		boolean whereFlag = true;
		for (int i = 0; i < fieldNameByIds.length; i++) {
			String fieldNameById = fieldNameByIds[i];
			if (null != fieldNameById && !StringPool.BLANK.equals(fieldNameById)) {
				if(whereFlag){
					sBuffer.append(" where ").append(DaoUtil.camelToUnderline(fieldNameById)).append(" = :").append(fieldNameById);
					whereFlag = false;
				}else {
					sBuffer.append(" and ").append(DaoUtil.camelToUnderline(fieldNameById)).append(" = :").append(fieldNameById);
				}
			}
			
		}
			
		String sql = sBuffer.toString();
		if ( !sql.contains("where")) {
			return StringPool.BLANK;//没有筛选条件，fieldNameById的值不合适
		}
		
		return  sql;
		
	}

	/**
	 * 获取新增或更新语句(注意驼峰),mysql数据库必须数据库表建立主键才能正确使用此方法 
	 * @param paramMap 参数Map
	 * @param tableName 数据库表名 
	 * @param dataBaseType 数据库类型
	 * @param fieldNameByIds 根据某字段名作为筛选条件
	 * @return
	 * 
	 */
	public static String saveOrUpdateSql(Map<String, Object> paramMap, String tableName, DbType dataBaseType, String... fieldNameByIds) {
		StringBuffer sBuffer = new StringBuffer();
		if (DbType.mysql.equals(dataBaseType)){//必须mysql数据库建立主键 
			//insert into test (TEST_ID, TEST_NAME) values ('223','insert') ON DUPLICATE KEY UPDATE TEST_NAME = 'update';
			sBuffer.append(" insert into ").append(tableName).append(" ( ");
			Iterator<Map.Entry<String, Object>> eIn = paramMap.entrySet().iterator();
			while (eIn.hasNext()) {
				Map.Entry<String, Object> param = eIn.next();
				sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
				if (eIn.hasNext()) {
					sBuffer.append(",");
				}
			}
			sBuffer.append(" ) values ( :");
			Iterator<Map.Entry<String, Object>> eValues = paramMap.entrySet().iterator();
			while (eValues.hasNext()) {
				Map.Entry<String, Object> param = eValues.next();
				sBuffer.append(param.getKey());
				if (eValues.hasNext()) {
					sBuffer.append(", :");
				}
			}
			sBuffer.append(" ) ON DUPLICATE KEY UPDATE ");
			Iterator<Map.Entry<String, Object>> eSet = paramMap.entrySet().iterator();
			a:
			while (eSet.hasNext()) {
				Map.Entry<String, Object> param = eSet.next();
				for (int i = 0; i < fieldNameByIds.length; i++) {
					if (param.getKey().equals(fieldNameByIds[i])) {
						if (!eSet.hasNext()) {
							//删除 之前添加"," 
							sBuffer.deleteCharAt(sBuffer.lastIndexOf(","));
						}
						continue a;
					}
				}
				
				
				sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
				sBuffer.append(" = :").append(param.getKey());
				if (eSet.hasNext()) {
					sBuffer.append(",");
				}
			}
			
		} else if (DbType.oracle.equals(dataBaseType)) {
			sBuffer.append(" merge into ").append(tableName).append(" t1 using ( select ");
			Iterator<Map.Entry<String, Object>> e = paramMap.entrySet().iterator();
			while (e.hasNext()) {
				Map.Entry<String, Object> param = e.next();
				sBuffer.append(" :").append(param.getKey());
				sBuffer.append(" AS \"");
				sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) ).append(StringPool.QUOTE);
				if (e.hasNext()) {
					sBuffer.append(",");
				}
			}
			sBuffer.append(" from dual ) t2 on ( ");
			for (int i = 0; i < fieldNameByIds.length; i++) {
				String fieldNameById = fieldNameByIds[i];
				if (null != fieldNameById && !StringPool.BLANK.equals(fieldNameById)) {
					sBuffer.append(" t1.").append(DaoUtil.camelToUnderline(fieldNameById)).append(" = t2.").append(DaoUtil.camelToUnderline(fieldNameById));
					if ( i != fieldNameByIds.length -1 && null != fieldNameByIds[i+1] && !StringPool.BLANK.equals(fieldNameByIds[i+1]) ) {
						sBuffer.append(" and ");
					}
				
				}
			}
			sBuffer.append(" ) ");
			if (paramMap.size() == fieldNameByIds.length ){
				//主键字段就是全部字段的情况，就什么都不做
			} else {
				sBuffer.append(" when matched then update set "); 
				Iterator<Map.Entry<String, Object>> eSet = paramMap.entrySet().iterator();
				a:
				while (eSet.hasNext()) {
					Map.Entry<String, Object> param = eSet.next();
					for (int i = 0; i < fieldNameByIds.length; i++) {
						if (param.getKey().equals(fieldNameByIds[i])) {
							if (!eSet.hasNext()) {
								//删除 之前添加"," 
								sBuffer.deleteCharAt(sBuffer.lastIndexOf(","));
							}
							continue a;
						}
					}
					
					
					sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
					sBuffer.append(" = :").append(param.getKey());
					if (eSet.hasNext()) {
						sBuffer.append(",");
					}
				}
			}
			sBuffer.append(" when not matched then insert ( ");
			Iterator<Map.Entry<String, Object>> eIn = paramMap.entrySet().iterator();
			while (eIn.hasNext()) {
				Map.Entry<String, Object> param = eIn.next();
				sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
				if (eIn.hasNext()) {
					sBuffer.append(",");
				}
			}
			sBuffer.append(" ) values ( :");
			Iterator<Map.Entry<String, Object>> eValues = paramMap.entrySet().iterator();
			while (eValues.hasNext()) {
				Map.Entry<String, Object> param = eValues.next();
				sBuffer.append(param.getKey());
				if (eValues.hasNext()) {
					sBuffer.append(", :");
				}
			}
			sBuffer.append(" ) ");
		} else if (DbType.postgresql.equals(dataBaseType)) {//必须POSTGREPSQL数据库建立主键或唯一索引
			//insert into test1 (name,id) values ('测试name','XN_JZtcKHmJWHSxZVeQrsg') on conflict (id) do update set name='测试name',id='XN-JZtcKHmJWHSxZVeQrsg';
			sBuffer.append(" insert into ").append(tableName).append(" ( ");
			Iterator<Map.Entry<String, Object>> eIn = paramMap.entrySet().iterator();
			while (eIn.hasNext()) {
				Map.Entry<String, Object> param = eIn.next();
				sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
				if (eIn.hasNext()) {
					sBuffer.append(",");
				}
			}
			sBuffer.append(" ) values ( :");
			Iterator<Map.Entry<String, Object>> eValues = paramMap.entrySet().iterator();
			while (eValues.hasNext()) {
				Map.Entry<String, Object> param = eValues.next();
				sBuffer.append(param.getKey());
				if (eValues.hasNext()) {
					sBuffer.append(", :");
				}
			}
			sBuffer.append(" ) on conflict ( ");
			for (int i = 0; i < fieldNameByIds.length; i++) {
				sBuffer.append(fieldNameByIds[i]);
				if (i < fieldNameByIds.length-1) {
					sBuffer.append(",");
				}
			}
			sBuffer.append(")");
			if (paramMap.size() == fieldNameByIds.length ){
				sBuffer.append(" do nothing ");//主键字段就是全部字段的情况，就什么都不做
			} else {
				sBuffer.append(" do update set ");
				Iterator<Map.Entry<String, Object>> eSet = paramMap.entrySet().iterator();
				a:
				while (eSet.hasNext()) {
					Map.Entry<String, Object> param = eSet.next();
					for (int i = 0; i < fieldNameByIds.length; i++) {
						if (param.getKey().equals(fieldNameByIds[i])) {
							if (!eSet.hasNext()) {
								//删除 在while循环的最后添加的"," 
								sBuffer.deleteCharAt(sBuffer.lastIndexOf(","));
							}
							continue a;
						}
					}
					
					
					sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
					sBuffer.append(" = :").append(param.getKey());
					if (eSet.hasNext()) {
						sBuffer.append(",");
					}
				}
				
			}
			
			
			
		}
		return sBuffer.toString();
	}
	
	/**
	 * 获取删除语句(注意驼峰)
	 * @param tableName 数据库表名
	 * @param fieldNameByIds 根据某字段名作为筛选条件
	 * @return
	 */
	public static String deleteSql(String tableName, String[] fieldNameByIds) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("delete from ").append(tableName);
		boolean whereFlag = true;
		for (int i = 0; i < fieldNameByIds.length; i++) {
			String fieldNameById = fieldNameByIds[i];
			if (null != fieldNameById && !StringPool.BLANK.equals(fieldNameById)) {
				if(whereFlag){
					sBuffer.append(" where ").append(DaoUtil.camelToUnderline(fieldNameById)).append(" = :").append(fieldNameById);
					whereFlag = false;
				}else {
					sBuffer.append(" and ").append(DaoUtil.camelToUnderline(fieldNameById)).append(" = :").append(fieldNameById);
				}
			}
			
		}
			
		String sql = sBuffer.toString();
		if ( !sql.contains("where")) {
			return StringPool.BLANK;//没有筛选条件，fieldNameById的值不合适
		}
		
		return sql;
	}
	
	/**
	 * 查询sql获取实体模型Map 带条件查询(注意paramMap会驼峰转下划线)
	 * @param attributeNames 属性list，数据库字段名
	 * @param tableName 数据库表名
	 * @param paramMap 查询条件参数Map(传入的对象会被修改！)
	 * @param queryConditions 自定义通用查询条件 (并且将查询条件参数并入paramMap)
	 */
	public static String selectSql4Map(List<String> attributeNames, String tableName, Map<String, Object> paramMap,QueryCondition... queryConditions) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("select ");
		for(int i=0;i<attributeNames.size();i++){
			sBuffer.append(attributeNames.get(i)).append(" as \"").append(attributeNames.get(i)).append("\" ");//注意oacle此处as保证attributeName的在map里面的key的大小写准确;
			if (i!=attributeNames.size()-1){
				sBuffer.append(",");
			}
		}
		sBuffer.append(" from ").append(tableName);
		Iterator<Map.Entry<String, Object>> e = paramMap.entrySet().iterator();
		if (e.hasNext()){
			Map.Entry<String, Object> param = e.next();
			sBuffer.append(" where ");
			sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
			sBuffer.append(" = :");
			sBuffer.append(param.getKey());
		}
		while (e.hasNext()) {
			Map.Entry<String, Object> param = e.next();
			sBuffer.append(" and ");
			sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
			sBuffer.append(" = :");
			sBuffer.append(param.getKey());
		}
		
		if (null != queryConditions){
			boolean whereFlag = false;
			if (null == paramMap || paramMap.isEmpty()) {
				whereFlag = true;
			}
			for (int i=0;i<queryConditions.length;i++) {
				QueryCondition queryCondition = queryConditions[i];
				if (null != queryCondition) {
					String where;
					if (whereFlag) {
						where = "where";
						whereFlag = false;
					} else {
						where = "and";
					}
					String conditionSql = queryCondition.getSqlString(where);
					sBuffer.append(conditionSql);
					paramMap.putAll(queryCondition.getParamMap());//将自定义条件的参数map加入总的参数map
				}
			}
		}
		return sBuffer.toString();
		
	}
	
	

	/**
	 * 查询sql获取实体模型Map封装的List,带参数条件
	 * @param attributeNames 属性list
	 * @param tableName 数据库表名
	 * @param pageNo 第几页
	 * @param pageSize 分页数，页面大小
	 * @param dataBaseType 数据库类型
	 * @param paramMap 条件参数查询
	 * @param queryConditions 自定义通用查询条件 (并且将查询条件参数并入paramMap)
	 * @return
	 */
	public static String selectSql4Map(List<String> attributeNames, String tableName, int pageNo, int pageSize,
			DbType dataBaseType, Map<String, Object> paramMap, QueryCondition... queryConditions) {
		StringBuffer sBuffer = new StringBuffer();
		if (DbType.mysql.equals(dataBaseType)){
			sBuffer.append("select ");
			for(int i=0;i<attributeNames.size();i++){
				sBuffer.append(attributeNames.get(i));
				if (i!=attributeNames.size()-1){
					sBuffer.append(",");
				}
			}
			sBuffer.append(" from ").append(tableName);
			
			Iterator<Map.Entry<String, Object>> e = paramMap.entrySet().iterator();
			if (e.hasNext()){
				Map.Entry<String, Object> param = e.next();
				sBuffer.append(" where ");
				sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
				sBuffer.append(" = :");
				sBuffer.append(param.getKey());
			}
			while (e.hasNext()) {
				Map.Entry<String, Object> param = e.next();
				sBuffer.append(" and ");
				sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
				sBuffer.append(" = :");
				sBuffer.append(param.getKey());
			}
			
			if (null != queryConditions){
				boolean whereFlag = false;
				if (null == paramMap || paramMap.isEmpty()) {
					whereFlag = true;
				}
				for (int i=0;i<queryConditions.length;i++) {
					QueryCondition queryCondition = queryConditions[i];
					if (null != queryCondition) {
						String where;
						if (whereFlag) {
							where = "where";
							whereFlag = false;
						} else {
							where = "and";
						}
						String conditionSql = queryCondition.getSqlString(where);
						sBuffer.append(conditionSql);
						paramMap.putAll(queryCondition.getParamMap());//将自定义条件的参数map加入总的参数map
					}
				}
			}
			
			sBuffer.append(" limit ").append((pageNo-1)*pageSize).append(",").append(pageSize);
			
		} else if (DbType.oracle.equals(dataBaseType)) {
			sBuffer.append("select ");
			for(int i=0;i<attributeNames.size();i++){
				sBuffer.append(attributeNames.get(i)).append(" as \"").append(attributeNames.get(i)).append("\" ");//注意oacle此处as保证attributeName的在map里面的key的大小写准确
				if (i!=attributeNames.size()-1){
					sBuffer.append(",");
				}
			}
			sBuffer.append( " from (");
			sBuffer.append("select rownum as rowno, ");
			for(int i=0;i<attributeNames.size();i++){
				sBuffer.append(attributeNames.get(i));
				if (i!=attributeNames.size()-1){
					sBuffer.append(",");
				}
			}
			sBuffer.append(" from ( select ");
			for(int i=0;i<attributeNames.size();i++){
				sBuffer.append(attributeNames.get(i));
				if (i!=attributeNames.size()-1){
					sBuffer.append(",");
				}
			}
			sBuffer.append(" from ").append(tableName);
			Iterator<Map.Entry<String, Object>> e = paramMap.entrySet().iterator();
			if (e.hasNext()){
				Map.Entry<String, Object> param = e.next();
				sBuffer.append(" where ");
				sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
				sBuffer.append(" = :");
				sBuffer.append(param.getKey());
			}
			while (e.hasNext()) {
				Map.Entry<String, Object> param = e.next();
				sBuffer.append(" and ");
				sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
				sBuffer.append(" = :");
				sBuffer.append(param.getKey());
			}
			if (null != queryConditions){
				boolean whereFlag = false;
				if (null == paramMap || paramMap.isEmpty()) {
					whereFlag = true;
				}
				for (int i=0;i<queryConditions.length;i++) {
					QueryCondition queryCondition = queryConditions[i];
					if (null != queryCondition) {
						String where;
						if (whereFlag) {
							where = "where";
							whereFlag = false;
						} else {
							where = "and";
						}
						String conditionSql = queryCondition.getSqlString(where);
						sBuffer.append(conditionSql);
						paramMap.putAll(queryCondition.getParamMap());//将自定义条件的参数map加入总的参数map
					}
				}
			}
			sBuffer.append(") where rownum <= ").append(pageNo*pageSize);
			sBuffer.append(" ) where rowno > ").append((pageNo-1)*pageSize);
			
		} else if (DbType.postgresql.equals(dataBaseType)){
			sBuffer.append("select ");
			for(int i=0;i<attributeNames.size();i++){
				sBuffer.append(attributeNames.get(i));
				if (i!=attributeNames.size()-1){
					sBuffer.append(",");
				}
			}
			sBuffer.append(" from ").append(tableName);
			
			Iterator<Map.Entry<String, Object>> e = paramMap.entrySet().iterator();
			if (e.hasNext()){
				Map.Entry<String, Object> param = e.next();
				sBuffer.append(" where ");
				sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
				sBuffer.append(" = :");
				sBuffer.append(param.getKey());
			}
			while (e.hasNext()) {
				Map.Entry<String, Object> param = e.next();
				sBuffer.append(" and ");
				sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
				sBuffer.append(" = :");
				sBuffer.append(param.getKey());
			}
			
			if (null != queryConditions){
				boolean whereFlag = false;
				if (null == paramMap || paramMap.isEmpty()) {
					whereFlag = true;
				}
				for (int i=0;i<queryConditions.length;i++) {
					QueryCondition queryCondition = queryConditions[i];
					if (null != queryCondition) {
						String where;
						if (whereFlag) {
							where = "where";
							whereFlag = false;
						} else {
							where = "and";
						}
						String conditionSql = queryCondition.getSqlString(where);
						sBuffer.append(conditionSql);
						paramMap.putAll(queryCondition.getParamMap());//将自定义条件的参数map加入总的参数map
					}
				}
			}
			
			sBuffer.append(" limit ").append(pageSize).append(" offset ").append((pageNo-1)*pageSize);
		}
		
		return sBuffer.toString();
	}

	/**
	 * 查询sql获取实体对象list
	 * @param clazz 对象
	 * @param pageNo 第几页
	 * @param pageSize 每页大小,分页大小
	 * @param dataBaseType 数据库类型
	 * @param queryConditions 自定义通用查询条件
	 * @return
	 */
	public static <T> String selectSql4Obj(Class<T> clazz, int pageNo, int pageSize, DbType dataBaseType, TableType tableType,
			Map<String, Object> paramMap, QueryCondition... queryConditions) {
		StringBuffer sBuffer = new StringBuffer();
		List<String> objFieldList = CommonSql.getConvertColNames(clazz, "select");
		if (null == tableType) {
			Table table = clazz.getAnnotation(Table.class);
			tableType = TableType.c(table.name());
		}
		
		if (DbType.mysql.equals(dataBaseType)){
			sBuffer.append("select ");
			for(int i=0;i<objFieldList.size();i++){
				String fieldName = objFieldList.get(i);
				sBuffer.append(fieldName);
				if (i!=objFieldList.size()-1){
					sBuffer.append(",");
				}
			}
			sBuffer.append(" from ").append(tableType.getTableName());
			
			Iterator<Map.Entry<String, Object>> e = paramMap.entrySet().iterator();
			if (e.hasNext()){
				Map.Entry<String, Object> param = e.next();
				sBuffer.append(" where ");
				sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
				sBuffer.append(" = :");
				sBuffer.append(param.getKey());
			}
			while (e.hasNext()) {
				Map.Entry<String, Object> param = e.next();
				sBuffer.append(" and ");
				sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
				sBuffer.append(" = :");
				sBuffer.append(param.getKey());
			}
			
			if (null != queryConditions){
				boolean whereFlag = false;
				if (null == paramMap || paramMap.isEmpty()) {
					whereFlag = true;
				}
				for (int i=0;i<queryConditions.length;i++) {
					QueryCondition queryCondition = queryConditions[i];
					if (null != queryCondition) {
						String where;
						if (whereFlag) {
							where = "where";
							whereFlag = false;
						} else {
							where = "and";
						}
						String conditionSql = queryCondition.getSqlString(where);
						sBuffer.append(conditionSql);
						paramMap.putAll(queryCondition.getParamMap());//将自定义条件的参数map加入总的参数map
					}
				}
			}
			
			sBuffer.append(" limit ").append((pageNo-1)*pageSize).append(",").append(pageSize);
			
		} else if (DbType.oracle.equals(dataBaseType)) {
			sBuffer.append("select ");
			for(int i=0;i<objFieldList.size();i++){
				String fieldName = objFieldList.get(i);
				if (fieldName.contains(" as ")){
					int beginIndex = fieldName.lastIndexOf(" as ")+4;
					fieldName = fieldName.substring(beginIndex);
					if(fieldName.contains(StringPool.QUOTE)){
						fieldName.replaceAll(StringPool.QUOTE, StringPool.BLANK);
					}
				}
				sBuffer.append(fieldName);
				if (i!=objFieldList.size()-1){
					sBuffer.append(",");
				}
			}
			
			sBuffer.append(" from ( select rownum as rowno, ");
			for(int i=0;i<objFieldList.size();i++){
				String fieldName = objFieldList.get(i);
				sBuffer.append(fieldName);
				if (i!=objFieldList.size()-1){
					sBuffer.append(",");
				}
			}
			
			sBuffer.append( " from (");
			sBuffer.append("select ");
			for(int i=0;i<objFieldList.size();i++){
				String fieldName = objFieldList.get(i);
				sBuffer.append(fieldName);
				if (i!=objFieldList.size()-1){
					sBuffer.append(",");
				}
			}
			sBuffer.append(" from ").append(tableType.getTableName());
			//sBuffer.append(" where rownum<= ").append(pageNo*pageSize);
			Iterator<Map.Entry<String, Object>> e = paramMap.entrySet().iterator();
			if (e.hasNext()) {
				Map.Entry<String, Object> param = e.next();
				sBuffer.append(" where ");
				sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
				sBuffer.append(" = :");
				sBuffer.append(param.getKey());
			}
			while (e.hasNext()) {
				Map.Entry<String, Object> param = e.next();
				sBuffer.append(" and ");
				sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
				sBuffer.append(" = :");
				sBuffer.append(param.getKey());
			}
			if (null != queryConditions){
				boolean whereFlag = false;
				if (null == paramMap || paramMap.isEmpty()) {
					whereFlag = true;
				}
				for (int i=0;i<queryConditions.length;i++) {
					QueryCondition queryCondition = queryConditions[i];
					if (null != queryCondition) {
						String where;
						if (whereFlag) {
							where = "where";
							whereFlag = false;
						} else {
							where = "and";
						}
						String conditionSql = queryCondition.getSqlString(where);
						sBuffer.append(conditionSql);
						paramMap.putAll(queryCondition.getParamMap());
					}
				}
			}
			sBuffer.append(") where rownum <= ").append(pageNo*pageSize);
			sBuffer.append(") where rowno >").append((pageNo-1)*pageSize);
		}  else if (DbType.postgresql.equals(dataBaseType)){
			sBuffer.append("select ");
			for(int i=0;i<objFieldList.size();i++){
				String fieldName = objFieldList.get(i);
				sBuffer.append(fieldName);
				if (i!=objFieldList.size()-1){
					sBuffer.append(",");
				}
			}
			sBuffer.append(" from ").append(tableType.getTableName());
			
			Iterator<Map.Entry<String, Object>> e = paramMap.entrySet().iterator();
			if (e.hasNext()){
				Map.Entry<String, Object> param = e.next();
				sBuffer.append(" where ");
				sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
				sBuffer.append(" = :");
				sBuffer.append(param.getKey());
			}
			while (e.hasNext()) {
				Map.Entry<String, Object> param = e.next();
				sBuffer.append(" and ");
				sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
				sBuffer.append(" = :");
				sBuffer.append(param.getKey());
			}
			
			if (null != queryConditions){
				boolean whereFlag = false;
				if (null == paramMap || paramMap.isEmpty()) {
					whereFlag = true;
				}
				for (int i=0;i<queryConditions.length;i++) {
					QueryCondition queryCondition = queryConditions[i];
					if (null != queryCondition) {
						String where;
						if (whereFlag) {
							where = "where";
							whereFlag = false;
						} else {
							where = "and";
						}
						String conditionSql = queryCondition.getSqlString(where);
						sBuffer.append(conditionSql);
						paramMap.putAll(queryCondition.getParamMap());//将自定义条件的参数map加入总的参数map
					}
				}
			}
			
			sBuffer.append(" limit ").append(pageSize).append(" offset ").append((pageNo-1)*pageSize);
		}
		
		return sBuffer.toString();
	}
	
	/**
	 * 将自定义查询sql分页后返回
	 * @param sql 分页前自定义的查询sql
	 * @param pageNo 第几页
	 * @param pageSize 每页大小,分页大小
	 * @param dataBaseType 数据库类型 
	 * @return
	 */
	public static String pageing(String sql, int pageNo, int pageSize, DbType dataBaseType) {
		if (!sql.contains("select")) {//只能操作查询语句
			if (!sql.contains("SELECT")) {
				return "传入的sql有误,只能操作查询语句";
			}
		}
		
		StringBuffer sBuffer = new StringBuffer();
		List<String> objFieldList = DaoSqlReader.getSelectColumns(sql, dataBaseType.name().toLowerCase());
		sBuffer.append("select ");
		for(int i=0;i<objFieldList.size();i++){
			String fieldName = objFieldList.get(i);
			sBuffer.append(fieldName);
			if (i!=objFieldList.size()-1){
				sBuffer.append(",");
			}
		}
		
		if (DbType.mysql.equals(dataBaseType)){
			sBuffer.append(" from ( ");
			sBuffer.append(sql);
			sBuffer.append(" ) ");
			sBuffer.append(" limit ").append((pageNo-1)*pageSize).append(",").append(pageSize);
		} else if (DbType.oracle.equals(dataBaseType)) {
			sBuffer.append(" from (select rownum as rowno5404aae525df42c9f5d38, pagingsql5404aae525df42c9f5d38.* from ( ");
			sBuffer.append(sql);
			sBuffer.append(" ) pagingsql5404aae525df42c9f5d38 ");
			sBuffer.append(" where rownum <= ").append(pageNo*pageSize);
			sBuffer.append(" ) where rowno5404aae525df42c9f5d38 >").append((pageNo-1)*pageSize);
		} else if (DbType.postgresql.equals(dataBaseType)) {
			sBuffer.append(" from ( ");
			sBuffer.append(sql);
			sBuffer.append(" ) ");
			sBuffer.append(" limit ").append(pageSize).append(" offset ").append((pageNo-1)*pageSize);
		}
		
		return sBuffer.toString();
	}

	/**
	 * 类转查询语句 (注意使用了驼峰)
	 * @param <T>
	 * @param clazz 类
	 * @param paramMap 参数HashMap
	 * @param tableType 可选，填入时使用tableType表名
	 * @param queryConditions 自定义通用查询条件
	 * @return
	 */
	public static <T> String selectSql(Class<T> clazz, Map<String, Object> paramMap, TableType tableType,QueryCondition... queryConditions) {
		StringBuffer sBuffer = new StringBuffer();
		List<String> objFieldList = CommonSql.getConvertColNames(clazz, "select");
		String tableName;
		if (null == tableType) {
			Table table = clazz.getAnnotation(Table.class);
			tableName = table.name();
		} else {
			tableName = tableType.getTableName();
		}
		
		sBuffer.append("select ");
		for(int i =0;i<objFieldList.size();i++){
			String fieldName = objFieldList.get(i);
			sBuffer.append(fieldName);
			if (i != objFieldList.size()-1){
				sBuffer.append(",");
			}
		}
		
		sBuffer.append(" from ");
		sBuffer.append(tableName);
		
		
		Iterator<Map.Entry<String, Object>> e = paramMap.entrySet().iterator();
		if (e.hasNext()){
			Map.Entry<String, Object> param = e.next();
			sBuffer.append(" where ");
			sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
			sBuffer.append(" = :");
			sBuffer.append(param.getKey());
		}
		while (e.hasNext()) {
			Map.Entry<String, Object> param = e.next();
			sBuffer.append(" and ");
			sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
			sBuffer.append(" = :");
			sBuffer.append(param.getKey());
		}
		
		if (null != queryConditions){
			boolean whereFlag = false;
			if (null == paramMap || paramMap.isEmpty()) {
				whereFlag = true;
			}
			for (int i=0;i<queryConditions.length;i++) {
				QueryCondition queryCondition = queryConditions[i];
				if (null != queryCondition) {
					String where;
					if (whereFlag) {
						where = "where";
						whereFlag = false;
					} else {
						where = "and";
					}
					String conditionSql = queryCondition.getSqlString(where);
					sBuffer.append(conditionSql);
					paramMap.putAll(queryCondition.getParamMap());//将自定义条件的参数map加入总的参数map
				}
			}
		}
		
		return sBuffer.toString();
	}

	/**
	 * 获取某表数据量sql
	 * @param tableName 表名
	 * @param queryConditions 自定义通用查询条件 
	 * @return
	 */
	public static String getRecords(String tableName,Map<String, Object> paramMap, QueryCondition... queryConditions) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("select count(1) from ");
		sBuffer.append(tableName);
		Iterator<Map.Entry<String, Object>> e = paramMap.entrySet().iterator();
		if (e.hasNext()){
			Map.Entry<String, Object> param = e.next();
			sBuffer.append(" where ");
			sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
			sBuffer.append(" = :");
			sBuffer.append(param.getKey());
		}
		while (e.hasNext()) {
			Map.Entry<String, Object> param = e.next();
			sBuffer.append(" and ");
			sBuffer.append(DaoUtil.camelToUnderline(param.getKey()) );
			sBuffer.append(" = :");
			sBuffer.append(param.getKey());
		}
		
		if (null != queryConditions){
			boolean whereFlag = false;
			if (null == paramMap || paramMap.isEmpty()) {
				whereFlag = true;
			}
			for (int i=0;i<queryConditions.length;i++) {
				QueryCondition queryCondition = queryConditions[i];
				if (queryCondition instanceof QueryConditionCommonOrder) {
					continue;//排序条件不用添加到记录数查询条件
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
					sBuffer.append(conditionSql);
					paramMap.putAll(queryCondition.getParamMap());//将自定义条件的参数map加入总的参数map
				}
			}
		}
		return sBuffer.toString();
	}

	/**
	 * 将自定义查询sql替换成统计数据量的sql
	 * @param sql
	 * @return
	 */
	public static String countSql(String sql) {
		sql = SqlReader.formatSQL(sql);
		if (!sql.contains("select")) {//只能操作查询语句
			if (!sql.contains("SELECT")) {
				return "传入的sql有误,只能操作查询语句";
			}
		}
		
		sql = SqlReader.removeOrderBy(sql);
		
		
		StringBuffer sBuffer = new StringBuffer();
		
		//最原始统计数据量sql
//		sBuffer.append("select count(1) from ( ");
//		sBuffer.append(sql);
//		sBuffer.append(" ) ");
		
		sBuffer.append("select count(1) from ( select 1 from ");
		
		int fromCursor = SqlReader.getCountFromCursor(sql,0);
		sBuffer.append(sql.substring(fromCursor,sql.length()));
		
		sBuffer.append(" ) ");
		
		return sBuffer.toString();
	}
	
	
	
	
	/**
	 * 关联查询生成sql,填充参数变量
	 * @param joinOptions 关联查询配置
	 * @param paramMap 放入装变量的paramMap
	 * @return sql
	 */
	public static String joinSql(JoinOptions joinOptions,Map<String, Object> paramMap){
		StringBuffer totalSelectColSql = new StringBuffer("select ");
		StringBuffer totalJoinSql = new StringBuffer();
		joinOptions.setJoinOrder(1);
		joinCtrl(joinOptions, totalSelectColSql, totalJoinSql, null, paramMap);
		return totalSelectColSql.append(totalJoinSql).toString();
	}
	
	/**
	 * 拼接关联查询sql,填充参数变量
	 * @param joinOptions 关联查询配置
	 * @param totalSelectColSql 前半段获取字段关联查询sql
	 * @param totalJoinSql 后半段关联各表关联查询sql
	 * @param parentJoinOptions 上级关联查询配置
	 * @param paramMap 参数变量
	 */
	private static void joinCtrl( JoinOptions joinOptions, StringBuffer totalSelectColSql, StringBuffer totalJoinSql, JoinOptions parentJoinOptions, Map<String, Object> paramMap){
		int joinOrderCode = joinOptions.getJoinOrder();//本表别名
		int panrenOrderCode = null==parentJoinOptions?-1:parentJoinOptions.getJoinOrder();//连接前表别名
		Object currentTable = joinOptions.getCurrentTable();
		if ( currentTable instanceof Class<?> table ) {
			List<String> colList = CommonSql.getConvertColNames(table, "totalselect");
			//totalSelectColSql.append(StringUtils.join(colList,','));
			for(int i=0;i<colList.size();i++){
				if (i==0 && null == joinOptions.getJoinType()) {
					//主表 的头个字段前面不加 ‘,’ 其它字段都要拼接。
				} else {
					totalSelectColSql.append(", ");
				}
				totalSelectColSql.append(" t");
				totalSelectColSql.append(joinOrderCode).append(".");
				totalSelectColSql.append(colList.get(i));
			}
			
			String currentSql = joinOptions.getCurrentSql().replace(":", ":"+joinOrderCode);
			
			if (null == joinOptions.getJoinType()){
				totalJoinSql.append(" from ( ").append(currentSql).append(" ) t").append(joinOrderCode);
			} else {
				totalJoinSql.append(" ").append(joinOptions.getJoinType().name);
				totalJoinSql.append(" ( ").append(currentSql).append(" ) t").append(joinOrderCode);
				
				boolean onFlag = false;
				Iterator<Entry<String, String>> e = joinOptions.getOnCol().entrySet().iterator();
				while (e.hasNext()){
					if (onFlag) {
						totalJoinSql.append(" and ");
					} else {
						totalJoinSql.append(" on ");
						onFlag = true;
					}
					Entry<String, String> entry = e.next();
					entry.getKey();
					totalJoinSql.append(" t").append(panrenOrderCode).append(".").append(entry.getKey());
					totalJoinSql.append(" = ").append(" t").append(joinOrderCode).append(".").append(entry.getValue());
					
					
				}
				
				
			}
			
			Iterator<Map.Entry<String, Object>> e = joinOptions.getCurrentDaoOptions().getParamMap().entrySet().iterator();
			
			if (e.hasNext()){
				Map.Entry<String, Object> param = e.next();
				paramMap.put(joinOrderCode + param.getKey(), param.getValue());
			}
			
			
			for (int i=0;i<joinOptions.getApendJoinOptions().size();i++) {
				JoinOptions joinOptionsChild = joinOptions.getApendJoinOptions().get(i);
				joinOptionsChild.setJoinOrder(joinOptions.getJoinOrder()+1+i);
				joinCtrl(joinOptionsChild, totalSelectColSql, totalJoinSql, joinOptions, paramMap);
				
			}
			
			
		} else if ( currentTable instanceof Map ) {
			
		}
		
	}


}
