package org.zerotul.specification;

import org.zerotul.specification.order.OrderSpecification;

import java.io.Serializable;

/**
 * Created by zerotul on 12.03.15.
 */
public interface FromSpecification<T extends Serializable> extends Serializable {

    public WhereSpecification<T> where();

    public OrderSpecification<T> order();

    public OrderSpecification<T> getOrder();

    public Class<T> getFromClass();

    public WhereSpecification<T> getWhere();

    public Specification<T> endFrom();
}
