package com.github.chenyuxin.commonframework.daojpa.option.custom;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

public interface OrderCondition {

    public <T> Order getOrder(Root<T> root, CriteriaBuilder cb);

}
