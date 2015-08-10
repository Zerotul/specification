package org.zerotul.specification;


import org.zerotul.specification.order.Order;
import org.zerotul.specification.recorder.EnhancerRecorder;
import org.zerotul.specification.recorder.Recorder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zerotul on 12.03.15.
 */
public class FromSpecificationImpl<T extends Serializable> implements FromSpecification<T> {

    private static final long serialVersionUID = 1065037407384874090L;

    private final Class<T> clazz;

    private WhereSpecification<T> where;

    private List<Order<T>> orders;

    private int max;

    private int offset = 0;

    private final Recorder<T> recorder;

    public FromSpecificationImpl(Class<T> clazz) {
        this.clazz = clazz;
        this.recorder = EnhancerRecorder.create(clazz);
        this.orders = new ArrayList<>();
    }

    @Override
    public WhereSpecification<T> where() {
        if (this.where!=null) throw new IllegalStateException("the where clause can not be changed");
        this.where = new WhereSpecificationImpl<>(this, recorder);
        return this.where;
    }

    @Override
    public FromSpecification<T> order(Order<T> order) {
        order.setRecorder(recorder);
        orders.add(order);
        return this;
    }

    @Override
    public List<Order<T>> getOrder() {
        return orders;
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

    @Override
    public FromSpecification<T> max(int max) {
        this.max = max;
        return this;
    }

    @Override
    public FromSpecification<T> offset(int offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public int getMax() {
        return max;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FromSpecificationImpl that = (FromSpecificationImpl) o;

        if (clazz != null ? !clazz.equals(that.clazz) : that.clazz != null) return false;
        if (orders != null ? !orders.equals(that.orders) : that.orders != null) return false;
        if (where != null ? !where.equals(that.where) : that.where != null) return false;
        if (max != that.max) return false;
        if (offset != that.offset) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = clazz != null ? clazz.hashCode() : 0;
        result = 31 * result + (where != null ? where.hashCode() : 0);
        result = 31 * result + (orders != null ? orders.hashCode() : 0);
        return result;
    }
}
