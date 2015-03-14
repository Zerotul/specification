package org.zerotul.specification;


import org.zerotul.specification.order.Order;
import org.zerotul.specification.recorder.EnhancerRecorder;
import org.zerotul.specification.recorder.Recorder;

import java.io.Serializable;

/**
 * Created by zerotul on 12.03.15.
 */
public class FromSpecificationImpl<T extends Serializable> implements FromSpecification<T> {

    private static final long serialVersionUID = 1065037407384874090L;

    private final Class<T> clazz;

    private WhereSpecification<T> where;

    private Order<T> order;

    private final Recorder<T> recorder;

    public FromSpecificationImpl(Class<T> clazz) {
        this.clazz = clazz;
        this.recorder = EnhancerRecorder.create(clazz);
    }

    @Override
    public WhereSpecification<T> where() {
        if (this.where!=null) throw new IllegalStateException("the where clause can not be changed");
        this.where = new WhereSpecificationImpl<>(this, recorder);
        return this.where;
    }

    @Override
    public FromSpecification<T> order(Order<T> order) {
        if (this.order!=null) throw new IllegalStateException("the order clause can not be changed");
        this.order = order;
        this.order.setRecorder(recorder);
        return this;
    }

    @Override
    public Order<T> getOrder() {
        return order;
    }


    @Override
    public Class<T> getFromClass() {
        return clazz;
    }

    @Override
    public WhereSpecification<T> getWhere() {
        return where;
    }

    @Override
    public Specification<T> endFrom() {
        return new SpecificationImpl<>(this);
    }
}
