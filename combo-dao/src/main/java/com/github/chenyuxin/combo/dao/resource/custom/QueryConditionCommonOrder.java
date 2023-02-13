package com.github.chenyuxin.combo.dao.resource.custom;

import java.util.HashMap;
import java.util.Map;

/**
 * 添加自定义为排序条件
 * 排序条件放至最后
 */
public class QueryConditionCommonOrder extends QueryCondition {

	public String sqlString;
	
	/**
	 * 添加自定义为等于筛选的查询条件初始化
	 * @param order 是否反向排序desc,true为反向排序
	 * @param fieldNames 数据库字段名
	 */
	public QueryConditionCommonOrder (boolean isDesc,String... fieldNames) {
		super();
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(" order by ");
		for (int i=0;i<fieldNames.length;i++) {
			String fieldName = fieldNames[i];
			sqlBuffer.append(fieldName);
			if (isDesc) {
				sqlBuffer.append(" desc ");
			}
			if (i + 1 < fieldNames.length ) {
				sqlBuffer.append(", ");
			}
			
		}
		this.sqlString = sqlBuffer.toString();
	}
	
	@Override
	public String getSqlString(String where) {
		return this.sqlString;
	}

	@Override
	public Map<String, Object> getParamMap() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		//paramMap.put(this.paramName, this.fieldValue);
		return paramMap;
	}

}

