package com.github.chenyuxin.commonframework.daojpa.jparun;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.github.chenyuxin.commonframework.daojpa.common.DaoConst;
import com.github.chenyuxin.commonframework.daojpa.common.TableType;
import com.github.chenyuxin.commonframework.daojpa.option.QueryCondition;

public class JpaRunner {
	
	@SuppressWarnings("rawtypes")
	private final Class entityClass;
	
	/** 可以是单个实体，或者是集合，或者是Class类型 */
	private final Object entity;
	
	private final JpaRunType jpaRunType;
	
	private TableType tableType;
	
	private String dataSourceName = DaoConst.defaultDataSourceName;
	
	private List<QueryCondition> queryConditions = new ArrayList<>();
	
	JpaRunner (final JpaRunType jpaRunType, final Object entity) {
		this.jpaRunType = jpaRunType;
		this.entity = entity;
		if (entity instanceof Collection entityCollection) {
			Object next = entityCollection.iterator().next();
			this.entityClass = next.getClass();
		} else if (entity instanceof Class clazz) {
			this.entityClass = clazz;
		} else {
			this.entityClass = entity.getClass();
		}
		
	}
	
	@SuppressWarnings("rawtypes")
	public Class getEntityClass() {
		return entityClass;
	}

	public Object getEntity() {
		return entity;
	}

	public JpaRunType getJpaRunType() {
		return jpaRunType;
	}

	public TableType getTableType() {
		return tableType;
	}

	public JpaRunner setTableType(TableType tableType) {
		this.tableType = tableType;
		return this;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public JpaRunner setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
		return this;
	}

	public List<QueryCondition> getQueryConditions() {
		return queryConditions;
	}

	public JpaRunner setQueryConditions(List<QueryCondition> queryConditions) {
		this.queryConditions = queryConditions;
		return this;
	}
	
	public <T> T run() {
		return Jpa.run(this);
	}
	
}
