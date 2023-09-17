package com.github.chenyuxin.commonframework.dao.common;

/**
 * dao常量
 * @author chenyuxin
 */
public interface DaoConst {
	
	public static final String delDropTable_SUCCESS_MESSAGE = "drop table completed 移除表已完成";
	public static final String delDropTable_FAILED_MESSAGE = "drop table failed 移除表已失败";
	public static final String useTable_SUCCESS_MESSAGE = "use table completed 表操作已完成";
	public static final String useTable_FAILED_MESSAGE = "use table failed 表操作已失败";
	public static final String saveObj_SUCCESS_MESSAGE = "save completed 数据保存完成";
	public static final String saveObj_FAILED_MESSAGE = "save error 数据保存出错";
	public static final String updateObj_SUCCESS_MESSAGE = "update completed 数据更新完成";
	public static final String updateObj_FAILED_MESSAGE = "update error 数据更新出错";
	public static final String mergeObj_SUCCESS_MESSAGE = "merge completed 数据新增或更新完成";
	public static final String mergeObj_FAILED_MESSAGE = "merge error 数据新增或更新出错";
	public static final String delObj_SUCCESS_MESSAGE = "delete completed 删除完成";
	public static final String delObj_FAILED_MESSAGE = "delete error 删除出错";
	public static final String truncateTable_SUCCESS_MESSAGE = "truncate table completed 清空表完成";
	public static final String truncateTable_FAILED_MESSAGE = "truncate table error 清空表出错";
	
	
	
	/**
	 * 获取默认数据源name
	 */
	public static final String defaultDataSourceName = "dataSource";
	
//	public static final String OracleJdbcDriverClassName = " oracle.jdbc.driver.OracleDriver oracle.jdbc.OracleDriver ";
//	public static final String MysqlJdbcDriverClassName = " com.mysql.jdbc.Driver ";
//	public static final String PostgreJdbcDriverClassName = " org.postgresql.Driver ";
//	
//	public static final String OracleJdbcDriverClassName1 = "oracle.jdbc.OracleDriver";
//	public static final String MysqlJdbcDriverClassName1 = "com.mysql.jdbc.Driver";
//	public static final String PostgreJdbcDriverClassName1 = "org.postgresql.Driver";
	
	/**
	 * 默认使用数据源name+TransactionManager生成对应事务管理beanName
	 */
	public static final String TransactionManager = "TransactionManager";
	
	/**
	 * 下划线转驼峰正则
	 */
	public static final String underlineToCamelRegex = "([A-Za-z\\d]+)(_)?";
	
	/**
	 * 驼峰法转下划线正则
	 */
	public static final String camelToUnderlineRegex = "[A-Z]([a-z_\\d]+)?";
	

}
