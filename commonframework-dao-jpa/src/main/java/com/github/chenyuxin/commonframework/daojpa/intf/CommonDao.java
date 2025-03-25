package com.github.chenyuxin.commonframework.daojpa.intf;

import com.github.chenyuxin.commonframework.daojpa.common.TableType;

/**
 * 通用dao用于操作java 类对象 或者 Map对象与数据库交互 <br>
 * 通用dao用于查询数据库 ,返回java 类对象 或者 Map对象 <br>
 * 参数名注意数据库下划线与Java驼峰命名的对应 <br>
 * 同时支持多种自定义sql的查询，分页查询 <br>
 */
public interface CommonDao {
	
	/**
	 * 判断表是否存在
	 * @param tableType 含有表名的表类型(必填)
	 * @param daoOptions 配置参数
	 * @param dataSourceName 其它数据源在spring配置文件bean的id
	 * @return
	 */
	boolean isTable(TableType tableType, Object... daoOptions);

}
