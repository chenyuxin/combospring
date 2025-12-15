package com.github.chenyuxin.commonframework.daojpa.option.custom;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.github.chenyuxin.commonframework.daojpa.option.QueryCondition;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * 添加自定义为大于筛选的查询条件
 * 支持 > 和 >= 两种比较操作
 */
public class GreaterThan extends QueryCondition {

    /**
     * 是否包含等于
     * true: 使用 >= 语句
     * false: 使用 > 语句
     */
    private boolean isEqual = false;

    /**
     * 添加自定义为大于筛选的查询条件初始化
     * 
     * @param fieldName  数据库字段名
     * @param fieldValue 参数值(必须是Comparable类型)
     * @param paramName  参数变量名,如定义的值与原本查询条件的参数变量相同，将覆盖原本查询条件的参数值！请注意！
     */
    public GreaterThan(String fieldName, Object fieldValue, String paramName) {
        super();
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.paramName = paramName;
    }

    /**
     * 添加自定义为大于或等于筛选的查询条件初始化
     * 
     * @param fieldName  数据库字段名
     * @param fieldValue 参数值(必须是Comparable类型)
     * @param paramName  参数变量名,如定义的值与原本查询条件的参数变量相同，将覆盖原本查询条件的参数值！请注意！
     * @param isEqual    true: 使用 >= 语句; false: 使用 > 语句
     */
    public GreaterThan(String fieldName, Object fieldValue, String paramName, boolean isEqual) {
        super();
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.paramName = paramName;
        this.isEqual = isEqual;
    }

    @Override
    public String getSqlString(String where) {
        StringBuilder sqlBuilder = new StringBuilder();
        if (StringUtils.isBlank(where) || (!"where".equals(where) && !"on".equals(where))) {
            where = "and";// 筛选条件默认and开头
        }
        sqlBuilder.append(" ").append(where).append(" ");
        sqlBuilder.append(this.fieldName);
        if (this.isEqual) {
            // 大于等于
            sqlBuilder.append(" >= :").append(this.paramName).append(" ");
        } else {
            // 大于
            sqlBuilder.append(" > :").append(this.paramName).append(" ");
        }
        return sqlBuilder.toString();
    }

    @Override
    public void setParamMap(Map<String, Object> params) {
        if (null != this.fieldValue) {
            params.put(this.paramName, this.fieldValue);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <T> Predicate getPredicate(Root<T> root, CriteriaBuilder cb) {
        if (null == this.fieldValue) {
            return null;
        }
        if (this.isEqual) {
            // 大于等于 >=
            return cb.greaterThanOrEqualTo(root.get(this.fieldName), (Comparable) this.fieldValue);
        } else {
            // 大于 >
            return cb.greaterThan(root.get(this.fieldName), (Comparable) this.fieldValue);
        }
    }

}
