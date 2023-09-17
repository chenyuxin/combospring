package com.github.chenyuxin.commonframework.dao.common.option;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.chenyuxin.commonframework.base.util.BaseUtil;
import com.github.chenyuxin.commonframework.dao.common.DaoConst;
import com.github.chenyuxin.commonframework.dao.common.TableType;
import com.github.chenyuxin.commonframework.dao.common.custom.QueryCondition;

/**
 * dao其它选项
 * @author chenyuxin
 *
 */
public class DaoOptions {
	
	/**
	 * 可选配置（使用其他数据源）
	 * 当需要多数据源查询时使用
	 * 其它数据源在spring配置文件bean的id
	 */
	private String dataSourceName;
	
	/**
	 * 生成键值对参数，用于=的查询条件 和 替换参数变量。
	 */
	private Map<String, Object> paramMap;
	
	/**
	 * 生成键值对参数，用于自定义或特殊的查询条件 和 替换参数变量。
	 */
	private QueryCondition[] queryConditions;
	
	/**
	 * 数据库 表类型，获取数据库表名
	 */
	private TableType tableType;
	
	/**
	 * 当发生异常或错误时默认执行结果返回String错误消息，查询结果默认返回null，
	 * 若传入异常或错误则commonDao将错误消息封装，抛出RuntimeException。
	 */
	private boolean runtimeException = false;
	
	/**
	 * CommonDao查询方法，查询方式设置。
	 * 无任何设置
	 */
	@SuppressWarnings("unused")
	private DaoOptions() {
		init(null, null, new QueryCondition[0]);
	}
	
	/**
	 * 
	 * @param daoOptions
	 */
	public DaoOptions(Object... daoOptions) {
		List<QueryCondition> queryConditions = new ArrayList<QueryCondition>();
		Map<String, Object> paramMap = new HashMap<String,Object>();
		String dataSourceName = putDaoOptions(queryConditions,paramMap,daoOptions);
		init(dataSourceName, paramMap, queryConditions.toArray(new QueryCondition[queryConditions.size()]));
	}

	/**
	 * 初始化DaoOptions
	 * @param dataSourceName 如需切换数据源，其它数据源在spring配置文件bean的id
	 * @param paramMap 参数变量 = 的查询条件
	 * @param queryConditions 自定义通用查询条件
	 */
	private void init(String dataSourceName, Map<String, Object> paramMap, QueryCondition... queryConditions){
		this.dataSourceName = BaseUtil.setNullString(dataSourceName,DaoConst.defaultDataSourceName);
		this.paramMap = paramMap;
		this.queryConditions = queryConditions;
	}
	
	/**
	 * 可选配置（使用其他数据源）
	 * 当需要多数据源查询时使用
	 * 其它数据源在spring配置文件bean的id
	 */
	public String getDataSourceName() {
		return dataSourceName;
	}

	/**
	 * 生成键值对参数，用于=的查询条件 和 替换参数变量。
	 */
	public Map<String, Object> getParamMap() {
		return paramMap;
	}

	/**
	 * 生成键值对参数，用于自定义或特殊的查询条件 和 替换参数变量。
	 */
	public QueryCondition[] getQueryConditions() {
		return queryConditions;
	}
	
	/**
	 * 数据库 表类型，获取数据库表名
	 */
	public TableType getTableType() {
		return tableType;
	}
	
	
	/**
	 * 获取异常对象<br>
	 * 
	 * 当发生异常或错误时默认执行结果返回String错误消息，查询结果默认返回null，<br>
	 * 若传入异常或错误则commonDao将错误消息封装，抛出RuntimeException。<br>
	 */
	public boolean isThrowException() {
		return this.runtimeException;
	}

	/**
	 * 放入当前的Dao配置
	 * @return 返回数据源
	 */
	@SuppressWarnings("unchecked")
	private String putDaoOptions(List<QueryCondition> queryConditions,Map<String, Object> paramMap,Object... daoOptions) {
		String dataSourceName = null;
		for (int i=0;i<daoOptions.length;i++) {
			Object object = daoOptions[i];
			if (object instanceof String str) {
				dataSourceName = str;
			} else if ( object instanceof Map ) {
				paramMap.putAll( (Map<String, Object>)object );
			} else if ( object instanceof List ) {
				queryConditions.addAll((List<QueryCondition>)object);
			} else if ( object instanceof QueryCondition queryCondition) {
				queryConditions.add(queryCondition);
			} else if ( object instanceof QueryCondition[] queryConditionsA) {
				for (int j=0;j<queryConditionsA.length;j++) {
					queryConditions.add(queryConditionsA[j]);
				}
			} else if ( object instanceof TableType table) {
				this.tableType = table;
			} else if ( object instanceof DaoEnumOptions) {
				this.runtimeException = true;
			} else if ( object instanceof Object[] objs) {
				if (objs.length>0) {
					dataSourceName = putDaoOptions(queryConditions, paramMap, objs);
				}
			}
		}
		return dataSourceName;
	}

}