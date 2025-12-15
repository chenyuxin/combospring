package com.github.chenyuxin.commonframework.daojpa.option;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.chenyuxin.commonframework.base.util.BaseUtil;
import com.github.chenyuxin.commonframework.daojpa.common.DaoConst;
import com.github.chenyuxin.commonframework.daojpa.common.TableType;
import com.github.chenyuxin.commonframework.daojpa.option.custom.GreaterThan;
import com.github.chenyuxin.commonframework.daojpa.option.custom.LessThan;
import com.github.chenyuxin.commonframework.daojpa.option.custom.QueryConditionCommonEquals;
import com.github.chenyuxin.commonframework.daojpa.option.custom.QueryConditionCommonLike;
import com.github.chenyuxin.commonframework.daojpa.option.custom.QueryConditionCommonOrder;

/**
 * dao其它选项
 * 
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
		List<QueryCondition> queryConditions = new ArrayList<>();
		Map<String, Object> paramMap = new HashMap<>();
		String dataSourceName = parseOptions(queryConditions, paramMap, daoOptions);
		init(dataSourceName, paramMap, queryConditions.toArray(new QueryCondition[0]));
	}

	private DaoOptions(Builder builder) {
		this.dataSourceName = BaseUtil.setNullString(builder.dataSourceName, DaoConst.defaultDataSourceName);
		this.paramMap = builder.paramMap;
		this.queryConditions = builder.queryConditions.toArray(new QueryCondition[0]);
		this.tableType = builder.tableType;
		this.runtimeException = builder.runtimeException;
	}

	/**
	 * 初始化DaoOptions
	 * 
	 * @param dataSourceName  如需切换数据源，其它数据源在spring配置文件bean的id
	 * @param paramMap        参数变量 = 的查询条件
	 * @param queryConditions 自定义通用查询条件
	 */
	private void init(String dataSourceName, Map<String, Object> paramMap, QueryCondition... queryConditions) {
		this.dataSourceName = BaseUtil.setNullString(dataSourceName, DaoConst.defaultDataSourceName);
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
	 * 
	 * @return 返回数据源
	 */
	@SuppressWarnings("unchecked")
	private String parseOptions(List<QueryCondition> queryConditions, Map<String, Object> paramMap,
			Object... daoOptions) {
		String dataSourceName = null;
		if (daoOptions == null) {
			return null;
		}

		for (Object object : daoOptions) {
			switch (object) {
				case String str -> dataSourceName = str;
				case Map<?, ?> map -> paramMap.putAll((Map<String, Object>) map);
				case List<?> list -> queryConditions.addAll((List<QueryCondition>) list);
				case QueryCondition qc -> queryConditions.add(qc);
				case QueryCondition[] qcs -> queryConditions.addAll(Arrays.asList(qcs));
				case TableType table -> this.tableType = table;
				case DaoEnumOptions option when option == DaoEnumOptions.RuntimeException ->
					this.runtimeException = true;
				case DaoOptions options -> {
					// Merge properties from another DaoOptions instance
					if (options.dataSourceName != null
							&& !DaoConst.defaultDataSourceName.equals(options.dataSourceName)) {
						dataSourceName = options.dataSourceName;
					}
					if (options.paramMap != null) {
						paramMap.putAll(options.paramMap);
					}
					if (options.queryConditions != null) {
						queryConditions.addAll(Arrays.asList(options.queryConditions));
					}
					if (options.tableType != null) {
						this.tableType = options.tableType;
					}
					if (options.runtimeException) {
						this.runtimeException = true;
					}
				}
				case Object[] objs when objs.length > 0 -> {
					String ds = parseOptions(queryConditions, paramMap, objs);
					if (ds != null) {
						dataSourceName = ds; // Last one wins or first one wins? Keeping logic consistent with loops
					}
				}
				case null, default -> {
				}
			}
		}
		return dataSourceName;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String dataSourceName;
		private Map<String, Object> paramMap = new HashMap<>();
		private List<QueryCondition> queryConditions = new ArrayList<>();
		private TableType tableType;
		private boolean runtimeException = false;

		public Builder dataSourceName(String dataSourceName) {
			this.dataSourceName = dataSourceName;
			return this;
		}

		public Builder paramMap(Map<String, Object> paramMap) {
			if (paramMap != null) {
				this.paramMap.putAll(paramMap);
			}
			return this;
		}

		public Builder queryConditions(List<QueryCondition> queryConditions) {
			if (queryConditions != null) {
				this.queryConditions.addAll(queryConditions);
			}
			return this;
		}

		public Builder addQueryCondition(QueryCondition queryCondition) {
			this.queryConditions.add(queryCondition);
			return this;
		}

		public Builder gt(String key, Object value) {
			QueryCondition queryCondition = new GreaterThan(key, value, key);
			this.queryConditions.add(queryCondition);
			return this;
		}

		public Builder gte(String key, Object value) {
			QueryCondition queryCondition = new GreaterThan(key, value, key, true);
			this.queryConditions.add(queryCondition);
			return this;
		}

		public Builder lt(String key, Object value) {
			QueryCondition queryCondition = new LessThan(key, value, key);
			this.queryConditions.add(queryCondition);
			return this;
		}

		public Builder lte(String key, Object value) {
			QueryCondition queryCondition = new LessThan(key, value, key, true);
			this.queryConditions.add(queryCondition);
			return this;
		}

		public Builder eq(String key, Object value) {
			this.paramMap.put(key, value);
			return this;
		}

		public Builder like(String key, Object value) {
			QueryCondition queryCondition = new QueryConditionCommonLike(key, value);
			this.queryConditions.add(queryCondition);
			return this;
		}

		public Builder ne(String key, Object value) {
			QueryCondition queryCondition = new QueryConditionCommonEquals(key, value, key, true);
			this.queryConditions.add(queryCondition);
			return this;
		}

		public Builder orderBy(String key, boolean isDesc) {
			QueryCondition queryCondition = new QueryConditionCommonOrder(isDesc, key);
			this.queryConditions.add(queryCondition);
			return this;
		}

		public Builder tableType(TableType tableType) {
			this.tableType = tableType;
			return this;
		}

		public Builder throwRuntimeException() {
			this.runtimeException = true;
			return this;
		}

		public DaoOptions build() {
			return new DaoOptions(this);
		}
	}

}
