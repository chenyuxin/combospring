package com.github.chenyuxin.commonframework.daojpa.option.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.github.chenyuxin.commonframework.daojpa.option.QueryCondition;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * 将 QueryCondition 和 paramMap 转换为 JPA Specification 的工具类
 * 
 * @author chenyuxin
 */
public class SpecificationBuilder {

    /**
     * 构建 JPA Specification
     * 
     * @param <T>             实体类型
     * @param paramMap        等值查询参数 (key = 字段名, value = 字段值)
     * @param queryConditions 自定义查询条件数组
     * @return Specification 对象，如果没有任何条件则返回 null
     */
    public static <T> Specification<T> build(Map<String, Object> paramMap, QueryCondition[] queryConditions) {
        // 如果没有任何查询条件，返回 null
        if ((paramMap == null || paramMap.isEmpty())
                && (queryConditions == null || queryConditions.length == 0)) {
            return null;
        }

        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            List<Order> orders = new ArrayList<>();

            // 处理 paramMap 中的等值查询条件
            if (paramMap != null && !paramMap.isEmpty()) {
                for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                    String fieldName = entry.getKey();
                    Object fieldValue = entry.getValue();
                    if (fieldValue == null) {
                        predicates.add(cb.isNull(root.get(fieldName)));
                    } else {
                        predicates.add(cb.equal(root.get(fieldName), fieldValue));
                    }
                }
            }

            // 处理 QueryCondition 数组
            if (queryConditions != null && queryConditions.length > 0) {
                // 遍历 QueryCondition 数组
                for (QueryCondition condition : queryConditions) {
                    // 获取 Predicate
                    Predicate predicate = condition.getPredicate(root, cb);
                    // 如果 Predicate 不为空，添加到 predicates 列表中
                    if (predicate != null) {
                        predicates.add(predicate);
                    }
                    if (condition instanceof OrderCondition orderCondition) {
                        Order order = orderCondition.getOrder(root, cb);
                        if (order != null) {
                            orders.add(order);
                        }
                    }
                }
            }

            // 应用排序
            if (!orders.isEmpty()) {
                query.orderBy(orders);
            }

            // 返回所有条件的 AND 组合
            if (predicates.isEmpty()) {
                return null;
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
