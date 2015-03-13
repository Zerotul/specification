package org.zerotul.specification.order;

import org.zerotul.specification.FromSpecification;
import org.zerotul.specification.recorder.Recorder;

import java.io.Serializable;

/**
 * Created by zerotul on 13.03.15.
 */
public class OrderSpecificationImpl<T extends Serializable> implements OrderSpecification<T> {

    private static final long serialVersionUID = 6232236891502443034L;

    private final FromSpecification<T> from;

    private final Recorder<T> recorder;

    private Order<T> order;

    public OrderSpecificationImpl(FromSpecification<T> from, Recorder<T> recorder) {
        this.from = from;
        this.recorder = recorder;
    }

    @Override
    public FromSpecification<T> order(Order<T> order) {
        this.order = order;
        this.order.setRecorder(recorder);
        return from;
    }

    @Override
    public Order<T> getOrder() {
        return order;
    }
}
