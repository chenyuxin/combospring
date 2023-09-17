package com.github.chenyuxin.commonframework.dao.common.custom;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 添加自定义为等于筛选的查询条件
 */
public class QueryConditionCommonEquals extends QueryCondition {
	
	/**
	 * 使用 true为 <> 语句;false为 = 语句  
	 */
	private boolean not = false;
	
	/**
	 * 添加自定义为等于筛选的查询条件初始化
	 * @param fieldName 数据库字段名
	 * @param fieldValue 参数值(支持值为null的筛选)
	 * @param paramName 参数变量名,如定义的值与原本查询条件的参数变量相同，将覆盖原本查询条件的参数值！请注意！
	 * @param not 使用 true为 <> 语句;false为 = 语句 
	 */
	public QueryConditionCommonEquals(String fieldName, Object fieldValue, String paramName, boolean not) {
		super();
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.paramName = paramName;
		this.not = not;
	}
	
	/**
	 * 添加自定义为等于筛选的查询条件初始化
	 * @param fieldName 数据库字段名
	 * @param fieldValue 参数值(支持值为null的筛选)
	 * @param paramName 参数变量名,如定义的值与原本查询条件的参数变量相同，将覆盖原本查询条件的参数值！请注意！
	 */
	public QueryConditionCommonEquals(String fieldName, Object fieldValue, String paramName) {
		super();
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.paramName = paramName;
	}

	@Override
	public String getSqlString(String where) {
		StringBuilder sqlBuffer = new StringBuilder();
		if (StringUtils.isBlank(where) || ( !"where".equals(where) && !"on".equals(where) ) ) {
			where = "and";//筛选条件默认and开头
		}
		sqlBuffer.append(" ").append(where).append(" ");
		sqlBuffer.append(this.fieldName);
		if (this.not) {//使用not in语句
			if(null == this.fieldValue) {
				sqlBuffer.append(" is not null ");
			} else {
				sqlBuffer.append(" <> :").append(this.paramName).append(" ");
			}
		} else {
			if(null == this.fieldValue) {
				sqlBuffer.append(" is null ");
			} else {
				sqlBuffer.append(" = :").append(this.paramName).append(" ");
			}
		}
		return sqlBuffer.toString();
	}

	@Override
	public Map<String, Object> getParamMap() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(null != this.fieldValue) {//筛选条件值为null的不用添加参数变量
			paramMap.put(this.paramName, this.fieldValue);
		}
		return paramMap;
	}

}
