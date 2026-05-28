package com.github.chenyuxin.commonframework.daojpa.option;

import java.util.Map;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * 添加自定义通用条件查询
 */
public abstract class QueryCondition {

	/**
	 * 条件字段名
	 */
	public String fieldName;

	/**
	 * 条件字段值
	 */
	public Object fieldValue;

	/**
	 * 变量名称，对应条件查询paramMap的key值
	 * 如果paramName变量名称与原本查询条件的参数变量相同，将覆盖原本查询条件的参数值！请注意！
	 * 通常将paramName变量名称值添加后缀_custom以示区别
	 */
	public String paramName;

	/**
	 * 构造方法初始化,使属性赋值
	 */
	protected QueryCondition() {

	}

	/**
	 * 自定义需要添加的sql查询条件的sql语句拼接
	 * 
	 * @param where 筛选头默认 'and'
	 * @return
	 */
	public abstract String getSqlString(String where);

	/**
	 * 自定义需要添加的变量集合
	 * 参数paramName变量名称用以替换getSqlString里的引用变量。
	 * 如果paramName变量名称与原本查询条件的参数变量相同，将覆盖原本查询条件的参数值！请注意！
	 * 通常将paramName变量名称值添加后缀_custom以示区别
	 * 
	 * @return
	 */
	public abstract void setParamMap(Map<String, Object> params);

	public abstract <T> Predicate getPredicate(Root<T> root, CriteriaBuilder cb);

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Object getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

}
