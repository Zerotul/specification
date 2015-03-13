package org.zerotul.specification;

import java.io.Serializable;

/**
 * Created by zerotul on 12.03.15.
 */
public interface FromSpecification<T> extends Serializable {

    public WhereSpecification<T> where();

    public Class<T> getFromClass();

    public WhereSpecification<T> getWhere();

    public Specification<T> endFrom();
}
