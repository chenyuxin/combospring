package com.github.chenyuxin.commonframework.dao.common.custom;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.github.chenyuxin.commonframework.base.constant.StringPool;

/**
 * 添加自定义为等于筛选的查询条件
 */
public class QueryConditionCommonLike extends QueryCondition {
	
	/**
	 * 是否右模糊,默认全模糊 
	 * <br> 0 为全模糊
	 * <br> 1 为右模糊
	 * <br> -1 为左模糊
	 */
	private int rightLike = 0;
	
	/**
	 * 使用 true为 not like 语句;false为 like 语句  
	 */
	private boolean not = false;
	
	/**
	 * 添加自定义为等于筛选的查询条件初始化
	 * @param fieldName 数据库字段名
	 * @param fieldValue 参数值(支持值为null的筛选)
	 * @param paramName 参数变量名,如定义的值与原本查询条件的参数变量相同，将覆盖原本查询条件的参数值！请注意！
	 * @param rightLike 是否右模糊,默认全模糊 0为全模糊，1为右模糊，-1为左模糊
	 */
	public QueryConditionCommonLike(String fieldName, Object fieldValue, Object... options) {
		super();
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		
		String currentParamName = null;
		for (Object option : options) {
			if ( option instanceof String currentParamNameO ) {
				currentParamName = currentParamNameO;
			} else if ( option instanceof Integer currentRightLikeO ) {
				this.rightLike = currentRightLikeO;
			} else if ( option instanceof Boolean currentNotO ) {
				this.not = currentNotO;
			}
			
		}
		
		if ( null == currentParamName || currentParamName.length() == 0 ) {
			this.paramName = fieldName.concat("_custom");
		} else {
			this.paramName = currentParamName;
		}
		
	}

	@Override
	public String getSqlString(String where) {
		StringBuilder sqlBuffer = new StringBuilder();
		if (StringUtils.isBlank(where) || ( !"where".equals(where) && !"on".equals(where) ) ) {
			where = "and";//筛选条件默认and开头
		}
		sqlBuffer.append(StringPool.SPACE).append(where).append(StringPool.SPACE);
		sqlBuffer.append(this.fieldName);
		if (this.not) {//使用not like语句
			if(null == this.fieldValue) {
				sqlBuffer.append(" is not null ");
			} else {
				sqlBuffer.append(" not like :").append(this.paramName).append(StringPool.SPACE);
			}
		} else {
			if(null == this.fieldValue) {
				sqlBuffer.append(" is null ");
			} else {
				sqlBuffer.append(" like :").append(this.paramName).append(StringPool.SPACE);
			}
		}
		return sqlBuffer.toString();
	}

	@Override
	public Map<String, Object> getParamMap() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(null != this.fieldValue) {//筛选条件值为null的不用添加参数变量
			String likeValue = null;
			if (this.rightLike == 1) {//右模糊
				likeValue = this.fieldValue + StringPool.PERCENT;
			} else if (this.rightLike == -1) {//左模糊
				likeValue = StringPool.PERCENT + this.fieldValue;
			} else {//全模糊
				likeValue = StringPool.PERCENT + this.fieldValue + StringPool.PERCENT;
			}
			paramMap.put(this.paramName, likeValue);
		}
		return paramMap;
	}
	
}
