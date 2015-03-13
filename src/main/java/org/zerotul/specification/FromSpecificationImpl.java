package org.zerotul.specification;


import org.zerotul.specification.order.OrderSpecification;
import org.zerotul.specification.order.OrderSpecificationImpl;
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

    private OrderSpecification<T> order;

    private final Recorder<T> recorder;

    public FromSpecificationImpl(Class<T> clazz) {
        this.clazz = clazz;
        this.recorder = EnhancerRecorder.create(clazz);
    }

    @Override
    public WhereSpecification<T> where() {
        this.where = new WhereSpecificationImpl<>(this, recorder);
        return this.where;
    }

    @Override
    public OrderSpecification<T> order() {
        this.order = new OrderSpecificationImpl<>(this, recorder);
        return this.order;
    }

    @Override
    public OrderSpecification<T> getOrder() {
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
