package org.zerotul.specification.order;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Created by zerotul on 13.03.15.
 */
public abstract class Orders {

    public static  <T extends Serializable> Order<T> desc(Function<T, Object>... getters){
        return new SimpleOrder<>(OrderType.DESC, getters);
    }

    public static <T extends Serializable> Order<T> asc(Function<T, Object>... getters){
        return new SimpleOrder<>(OrderType.ASC, getters);
    }
}
