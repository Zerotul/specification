package org.zerotul.specification;

import org.zerotul.specification.order.Order;

import java.io.Serializable;

/**
 * Created by zerotul on 12.03.15.
 */
public interface FromSpecification<T extends Serializable> extends Serializable {

    public WhereSpecification<T> where();

    public FromSpecification<T> order(Order<T> order);

    public Order<T> getOrder();

    public Class<T> getFromClass();

    public WhereSpecification<T> getWhere();

    public Specification<T> endFrom();

    public FromSpecification<T> max(int max);

    public FromSpecification<T> offset(int offset);

    public int getMax();

    public int getOffset();
}
