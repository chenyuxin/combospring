package com.github.chenyuxin.commonframework.daojpa.jparun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.chenyuxin.commonframework.daojpa.common.TableType;
import com.github.chenyuxin.commonframework.daojpa.option.QueryCondition;

public class JpaRunner<T> {
	
	private final T entity;
	
	private final JpaRunType jpaRunType;
	
	private TableType tableType;
	
	private String dataSourceName;
	
	private Map<String, Object> paramMap = new HashMap<>();
	
	private List<QueryCondition> queryConditions = new ArrayList<>();
	
	JpaRunner (final JpaRunType jpaRunType, final T entity) {
		this.jpaRunType = jpaRunType;
		this.entity = entity;
	}

	public T getEntity() {
		return entity;
	}

	public JpaRunType getJpaRunType() {
		return jpaRunType;
	}

	public TableType getTableType() {
		return tableType;
	}

	public JpaRunner<T> setTableType(TableType tableType) {
		this.tableType = tableType;
		return this;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public JpaRunner<T> setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
		return this;
	}

	public Map<String, Object> getParamMap() {
		return paramMap;
	}

	public JpaRunner<T> setParamMap(Map<String, Object> paramMap) {
		this.paramMap.putAll(paramMap);
		return this;
	}

	public List<QueryCondition> getQueryConditions() {
		return queryConditions;
	}

	public JpaRunner<T> setQueryConditions(List<QueryCondition> queryConditions) {
		this.queryConditions = queryConditions;
		return this;
	}
	
	public String run() {
		return Jpa.run(this);
	}
	
}
