package com.github.chenyuxin.commonframework.daojpa.option.custom;

import java.util.Map;

import com.github.chenyuxin.commonframework.daojpa.option.QueryCondition;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * 添加自定义为排序条件
 * 排序条件放至最后
 */
public class QueryConditionCommonOrder extends QueryCondition implements OrderCondition {

	private String sqlString;
	private boolean isDesc;

	/**
	 * 添加自定义为等于筛选的查询条件初始化
	 * 
	 * @param order      是否反向排序desc,true为反向排序
	 * @param fieldNames 数据库字段名
	 */
	public QueryConditionCommonOrder(boolean isDesc, String fieldName) {
		super();
		this.isDesc = isDesc;
		this.fieldName = fieldName;
		StringBuilder sqlBuilder = new StringBuilder(" ");
		sqlBuilder.append(fieldName);
		if (isDesc) {
			sqlBuilder.append(" desc, ");
		} else {
			sqlBuilder.append(" asc, ");
		}
		this.sqlString = sqlBuilder.toString();
	}

	@Override
	public String getSqlString(String where) {
		if (" order by ".equals(where)) {
			return where + this.sqlString;
		} else if (", ".equals(where)) {
			return this.sqlString.substring(0, this.sqlString.length() - 2);
		}
		return this.sqlString;
	}

	@Override
	public void setParamMap(Map<String, Object> params) {
		// doNothing
	}

	@Override
	public <T> Predicate getPredicate(Root<T> root, CriteriaBuilder cb) {
		// doNothing
		return null;
	}

	@Override
	public <T> Order getOrder(Root<T> root, CriteriaBuilder cb) {
		if (this.isDesc) {
			return cb.desc(root.get(this.fieldName));
		}
		return cb.asc(root.get(this.fieldName));
	}

}
