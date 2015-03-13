package org.zerotul.specification.order;

import org.zerotul.specification.FromSpecification;

import java.io.Serializable;

/**
 * Created by zerotul on 13.03.15.
 */
public interface OrderSpecification<T extends Serializable> extends Serializable {

    public FromSpecification<T> order(Order<T> order);

    public Order<T> getOrder();
}
