package org.zerotul.specification.order;

import org.zerotul.specification.recorder.Recorder;

import java.io.Serializable;
import java.util.ArrayList;
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
}
