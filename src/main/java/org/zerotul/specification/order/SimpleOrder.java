package org.zerotul.specification.order;

import org.zerotul.specification.recorder.Recorder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Created by zerotul on 13.03.15.
 */
public class SimpleOrder<T extends Serializable> implements Order<T> {

    private static final long serialVersionUID = 6389338661401617902L;

    private final OrderType orderType;

    private final Function<T, Object>[] getters;

    private Recorder<T> recorder;

    public SimpleOrder(OrderType orderType, Recorder<T> recorder, Function<T, Object>[] getters) {
        this.orderType = orderType;
        this.recorder = recorder;
        this.getters = getters;
    }

    public SimpleOrder(OrderType orderType, Function<T, Object>[] getters) {
        this.orderType = orderType;
        this.getters = getters;
    }

    @Override
    public OrderType getOrderType() {
        return orderType;
    }

    @Override
    public List<String> getPropertyNames() {
        List<String> propertyNames = new ArrayList<>();
        for(Function getter : getters){
            propertyNames.add(recorder.getPropertyName(getter));
        }
        return propertyNames;
    }

    @Override
    public void setRecorder(Recorder<T> recorder) {
         this.recorder = recorder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleOrder that = (SimpleOrder) o;

        if (!Arrays.equals(getters, that.getters)) return false;
        if (orderType != that.orderType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = orderType != null ? orderType.hashCode() : 0;
        result = 31 * result + (getters != null ? Arrays.hashCode(getters) : 0);
        return result;
    }
}
