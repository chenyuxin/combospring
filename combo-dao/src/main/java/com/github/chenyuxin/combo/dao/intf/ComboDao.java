package com.github.chenyuxin.combo.dao.intf;

import java.util.List;
import java.util.Map;

import com.github.chenyuxin.combo.base.type.database.TableType;
import com.github.chenyuxin.combo.dao.resource.options.JoinOptions;
import com.github.chenyuxin.combo.dao.resource.options.PageBean;

/**
 * 通用dao用于操作java 类对象 或者 Map对象与数据库交互
 * 
 * 通用dao用于查询数据库 ,返回java 类对象 或者 Map对象
 * 
 * 参数名注意数据库下划线与Java驼峰命名的对应
 * 
 * 同时支持多种自定义sql的查询，分页查询
 */
public interface ComboDao {
	
	/**
	 * 判断表是否存在
	 * @param tableType 含有表名的表类型(必填)
	 * @param daoOptions 配置参数
	 * @param dataSourceName 其它数据源在spring配置文件bean的id
	 * @return
	 */
	boolean isTable(TableType tableType, Object... daoOptions);
	
	/**
	 * 删除指定表(drop表),向java程序员提供一个跑路的机会
	 * @param tableType 含有表名的表类型(必填)
	 * @param daoOptions 配置参数
	 * @param dataSourceName 其它数据源在spring配置文件bean的id
	 */
	String delDropTable(TableType tableType, Object... daoOptions);
	
	/**
	 * 通过建表语句创建表 或插入语句  或者 执行特殊修改操作的sql 带参数,使用其它配置的数据源
	 * 此方法会默认有修改表操作，在有查询缓存时，移除对应表的程序缓存
	 * @param ctrlSql 建表语句,或插入语句,或者 执行特殊修改操作的sql
	 * @param daoOptions 可选设置参数 {paramMap 条件查询参数驼峰会转下划线  ,dataSourceName 其它数据源在spring配置文件bean的id}
	 * @param paramMap 参数
	 * @param dataSourceName 其它数据源在spring配置文件bean的id
	 * @return
	 */
	String useTable(String ctrlSql, Object... daoOptions);
	
	
	
	
	
	
	
	/**
	 * 保存单个对象数据,
	 * 保存list数据,
	 * 一种是Map<String,Object>的list或 单个Map接收的(注意驼峰转下划线)。
	 * 一种是实体类的对象list或对象 接收的
	 * @param object list或者单实体
	 * @param daoOptions 可选设置参数
	 * @param tableType 数据库表名(当传入为Map必填) (当传入的T对象为javaBean且定义了Table标签,可选自己定义,可选填,默认使用javaBean定义的表名)
	 * @param dataSourceName 使用其它配置的数据源可选,其它数据源在spring配置文件bean的id
	 * @return
	 */
	String saveObj(Object object, Object... daoOptions);
	
	/**
	 * 更新对象  对象类需要有@Table@colnum@Id等标注
	 * @param object 更新单个对象 或者 list对象
	 * @param daoOptions 可选设置参数
	 * @param dataSourceName 使用其它配置的数据源可选, 其它数据源在spring配置文件bean的id字符串
	 * @return
	 */
	String updateObj(Object object, Object... daoOptions);
	
	/**
	 * 更新map map包含需要更新的字段和条件筛选字段
	 * @param object 更新list<map>或者map 对应字段(包含筛选条件字段)
	 * @param fieldNameByIds 条件筛选字段（字段属性——驼峰会转下划线）
	 * @param tableType 数据库表名(必填)
	 * @param daoOptions 可选设置参数
	 * @param dataSourceName 使用其它配置的数据源可选, 其它数据源在spring配置文件bean的id字符串
	 * @return
	 */
	String updateMap(Object object, String[] fieldNameByIds, TableType tableType, Object... daoOptions);
	
	/**
	 * 新增或保存 数据库做新增或保存,mysql数据库必须数据库表建立主键才能正确使用此方法 
	 * 对象类需要有@Table、@Id等标注
	 * @param object 更新或保存 单个或List对象
	 * @param daoOptions 可选设置参数
	 * @param dataSourceName 使用其它配置的数据源可选, 其它数据源在spring配置文件bean的id字符串
	 */
	String saveOrUpdateObj(Object object, Object... daoOptions);
	
	/**
	 * 新增或保存 数据库做新增或保存,mysql数据库必须数据库表建立主键才能正确使用此方法 
	 * @param object 更新或保存 单个或List的  Map数据
	 * @param fieldNameByIds 条件筛选字段（字段属性——驼峰会转下划线） mysql数据库必须在数据量建立主键
	 * @param tableType 数据库表名(必填)
	 * @param daoOptions 可选设置参数
	 * @param dataSourceName 使用其它配置的数据源可选, 其它数据源在spring配置文件bean的id字符串
	 * @return
	 */
	String saveOrUpdateMap(Object object, String[] fieldNameByIds,TableType tableType, Object... daoOptions);
	
	/**
	 * 删除对象  对象类需要有@Table、@Id等标注
	 * @param object 目前只能删除单个对象,其它删除操作请使用useTable写删除sql语句
	 * @param daoOptions 可选设置参数
	 * @param dataSourceName 使用其它配置的数据源可选, 其它数据源在spring配置文件bean的id字符串 
	 * @return
	 */
	String deleteObj(Object object, Object... daoOptions);
	
	/**
	 * 清空表 truncate,干就对了！
	 * @param tableType 数据库表类型  表名
	 * @param daoOptions 可选设置参数
	 * @param dataSourceName 使用其它配置的数据源可选, 其它数据源在spring配置文件bean的id字符串 
	 * @return
	 */
	String delTruncateTable(TableType tableType, Object... daoOptions);
	
	
	
	
	
	
	
	/**
	 * 自定义写查询sql
	 * 查询获取实体模型数据，实体模型用HashMap封装(注意paramMap会驼峰转下划线)
	 * @param sql 当连接的是oracle数据库时sql中select的字段一定要使用<  as "大小写属性名" > 才能获取大小写正确的key, sql中使用命名参数样例  `:paramName` 
	 * @param daoOptions 可选设置参数 {paramMap 条件查询参数驼峰会转下划线  ,dataSourceName 其它数据源在spring配置文件bean的id}
	 * @param paramMap 参数变量
	 * @param dataSourceName 其它数据源在spring配置文件bean的id 
	 * @return
	 */
	List<Map<String, Object>> selectObjMap(String sql, Object... daoOptions);
	
	/**
	 * 查询获取实体模型数据，实体模型用HashMap封装(注意paramMap会驼峰转下划线)
	 * @param attributeNames 数据库字段名(模型属性名)的List(不区分驼峰)
	 * @param tableType 模型表名
	 * @param daoOptions 设置参数 {paramMap 条件查询参数驼峰会转下划线  ,dataSourceName 其它数据源在spring配置文件bean的id,queryConditions 自定义通用查询条件}
	 * @param paramMap 条件查询参数(驼峰会转下划线)
	 * @param dataSourceName 其它数据源在spring配置文件bean的id
	 * @param queryConditions 自定义通用查询条件
	 * @return
	 */
	List<Map<String, Object>> selectObjMap(List<String> attributeNames,TableType tableType, Object... daoOptions);
	
	/**
	 * 查询获取分页实体模型数据，实体模型用HashMap封装
	 * @param currentPage 第几页
	 * @param pageSize 分页数（页面大小）
	 * @param attributeNames 数据库字段名(模型属性名)的List(不区分驼峰)
	 * @param tableType 模型表名
	 * @param daoOptions 设置参数 {paramMap 条件查询参数驼峰会转下划线  ,dataSourceName 其它数据源在spring配置文件bean的id,queryConditions 自定义通用查询条件} 
	 * @param paramMap 条件查询参数(驼峰会转下划线)
	 * @param dataSourceName 其它数据源在spring配置文件bean的id
	 * @param queryConditions 自定义通用查询条件
	 * @return
	 */
	PageBean<Map<String, Object>> selectObjMap(int currentPage, int pageSize, List<String> attributeNames, TableType tableType, Object... daoOptions);
	
	/**
	 * 查询获取分页实体模型数据，实体模型用类封装
	 * @param currentPage 第几页
	 * @param pageSize 分页数（页面大小）
	 * @param clazz 类名
	 * @param daoOptions 设置参数 {paramMap 条件查询参数驼峰会转下划线  ,dataSourceName 其它数据源在spring配置文件bean的id,queryConditions 自定义通用查询条件}
	 * @param paramMap 条件查询参数(驼峰会转下划线)
	 * @param dataSourceName 其它数据源在spring配置文件bean的id
	 * @param queryConditions 自定义通用查询条件
	 * @return
	 */
	<T> PageBean<T> selectObj(int currentPage, int pageSize, Class<T> clazz, Object... daoOptions);
	
	/**
	 * 自定义sql,查询获取分页实体模型数据(某些自定义sql会影响顺序，请谨慎使用本方法，谨慎测试)
	 * @param sql 分页前自定义的查询sql
	 * @param currentPage 第几页
	 * @param pageSize 分页数（页面大小）
	 * @param clazz 返回的封装数据实体类，也可以是null或者Map.class,则返回数据会用Map<String,Object>封装
	 * @param daoOptions 设置参数 {paramMap 条件查询参数驼峰会转下划线  ,dataSourceName 其它数据源在spring配置文件bean的id}
	 * @param paramMap 条件查询参数
	 * @param dataSourceName 其它数据源在spring配置文件bean的id
	 * @return
	 */
	<T> PageBean<T> selectObj(String sql, int currentPage, int pageSize, Class<T> clazz,
			Object... daoOptions);
	
	
	/**
	 * 自定义sql查询获取实体模型数据，实体模型用类封装
	 * @param sql sql中使用命名参数样例  `:paramName`  
	 * @param clazz 获取的对象
	 * @param daoOptions 设置参数 {paramMap 条件查询参数驼峰会转下划线  ,dataSourceName 其它数据源在spring配置文件bean的id,queryConditions 自定义通用查询条件}
	 * @param paramMap 条件查询参数(驼峰会转下划线)
	 * @param dataSourceName 其它数据源在spring配置文件bean的id
	 * @return
	 */
	<T> List<T> selectObj(String sql, Class<T> clazz, Object... daoOptions);
	
	/**
	 * 查询获取实体模型数据，实体模型用类封装
	 * @param clazz
	 * @param daoOptions 设置参数 {paramMap 条件查询参数驼峰会转下划线  ,dataSourceName 其它数据源在spring配置文件bean的id,queryConditions 自定义通用查询条件}
	 * @param paramMap 条件查询参数(驼峰会转下划线)
	 * @param dataSourceName 其它数据源在spring配置文件bean的id
	 * @param queryConditions 自定义通用查询条件
	 * @return
	 */
	<T> List<T> selectObj(Class<T> clazz, Object... daoOptions);
	
	/**
	 * 查询获取单个对象，实体模型用类封装
	 * @param clazz
	 * @param daoOptions 设置参数 {paramMap 条件查询参数驼峰会转下划线  ,dataSourceName 其它数据源在spring配置文件bean的id,queryConditions 自定义通用查询条件}
	 * @param paramMap 条件查询参数(驼峰会转下划线)
	 * @param dataSourceName 其它数据源在spring配置文件bean的id
	 * @param queryConditions 自定义通用查询条件
	 * @return 
	 */
	<T> T selectObjSingle(Class<T> clazz, Object... daoOptions);
	
	/**
	 * 自定义sql查询获取单个对象，实体模型用类封装
	 * @param sql sql中使用命名参数样例  `:paramName`  
	 * @param clazz 获取的对象
	 * @param daoOptions 设置参数 {paramMap 条件查询参数驼峰会转下划线  ,dataSourceName 其它数据源在spring配置文件bean的id}
	 * @param paramMap 条件查询参数(驼峰会转下划线)
	 * @param dataSourceName 其它数据源在spring配置文件bean的id
	 * @return 
	 */
	<T> T selectObjSingle(String sql, Class<T> clazz, Object... daoOptions);
	
	/**
	 * 自定义sql查询获取基本类
	 * @param sql sql中使用命名参数样例  `:paramName` 
	 * @param clazz 获取的对象 是String,Intger,Double等
	 * @param daoOptions 设置参数 {paramMap 条件查询参数驼峰会转下划线  ,dataSourceName 其它数据源在spring配置文件bean的id}
	 * @param paramMap 条件查询参数(驼峰会转下划线)
	 * @param dataSourceName 其它数据源在spring配置文件bean的id
	 * @return 
	 */
	<T> T selectBaseObj(String sql, Class<T> clazz, Object... daoOptions);
	
	/**
	 * 自定义sql查询获取基本类List
	 * @param sql sql中使用命名参数样例  `:paramName` 
	 * @param clazz 获取的对象 是String,Intger,Double等
	 * @param daoOptions 设置参数 {paramMap 条件查询参数驼峰会转下划线  ,dataSourceName 其它数据源在spring配置文件bean的id}
	 * @param paramMap 条件查询参数(驼峰会转下划线)
	 * @param dataSourceName 其它数据源在spring配置文件bean的id
	 * @return
	 */
	<T> List<T> selectBaseObjs(String sql, Class<T> clazz, Object... daoOptions);
	
	/**
	 * 获取某表数据量,根据当前查询条件
	 * @param daoOptions 设置参数 {paramMap 条件查询参数驼峰会转下划线  ,dataSourceName 其它数据源在spring配置文件bean的id,queryConditions 自定义通用查询条件}
	 * @param tableType 表名 必填
	 * @param paramMap 条件查询参数(驼峰会转下划线)
	 * @param dataSourceName 其它数据源在spring配置文件bean的id
	 * @param queryConditions 自定义通用查询条件
	 * @return
	 */
	int getRecords(TableType tableType, Object... daoOptions);
	
	
	
	
	/**
	 * 关联查询返回数据
	 * @param joinOptions
	 * @return
	 */
	List<Map<String, Object>> selectJoin(JoinOptions joinOptions);

	
	

}
