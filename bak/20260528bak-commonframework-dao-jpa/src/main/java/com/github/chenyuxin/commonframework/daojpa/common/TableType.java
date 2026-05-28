package com.github.chenyuxin.commonframework.daojpa.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.chenyuxin.commonframework.base.constant.StringPool;

/**
 * 操作数据库表类型
 * 表名
 * 
 * 产生实例可以通过下面方式
 * TableType.of("表名") 
 */
public class TableType {
	
	private String schema;//数据库用户
	
	private String tableName;//对应数据库表名,可以使用 schema.tableName 的形式
	
	private TableType (String tableName,String schema) {
		this.tableName = tableName;
		this.schema = schema;
	}
	
	/**
	 * 获取表名
	 */
	public String getTableName() {
		if (null != schema && !StringPool.BLANK.equals(schema) && !tableName.contains(StringPool.PERIOD)) {
			return schema.concat(StringPool.PERIOD).concat(tableName);
		} else {
			return tableName;
		}
	}
	
	/**
	 * 通过表名返回 TableType 
	 * @param tableName 如果带有schema， tableName可以是 schema.tableName的形式
	 * @return
	 */
	public static TableType of(String tableName){
		tableName = tableName.toUpperCase();
		
		TableType tableType = tableTypeMap.get(tableName);
		if(null == tableType){
			int schemaSplit = tableName.indexOf(StringPool.PERIOD);
			String schema = StringPool.BLANK;
			if (schemaSplit > 0) {
				schema = tableName.substring(0,tableName.indexOf(StringPool.PERIOD));
			} 
			tableType = new TableType(tableName,schema);
		}
		return tableType;
	}
	
	/**
	 * 据库表类型 池
	 * 线程安全map
	 * 将数据库操作成功的表的 TableType 放入缓存表池
	 * commonDao使用时只保存默认数据源的TableType
	 */
	private static final Map<String, TableType> tableTypeMap = new ConcurrentHashMap<String, TableType>();
	
	/**
	 * 移除表类型池中的 表
	 * @param tableType
	 */
	public static void delTableType(TableType tableType){
		tableTypeMap.remove(tableType.getTableName());
	}
	
	/**
	 * commonDao调用，执行成功的表类型 放入表类型池
	 * @param tableType
	 */
	public static void putTableType(TableType tableType){
		if (tableTypeMap.containsKey(tableType.getTableName())){
			return;
		}
		tableTypeMap.put(tableType.getTableName(), tableType);
	}

}
