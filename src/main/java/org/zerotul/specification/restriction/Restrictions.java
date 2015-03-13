package org.zerotul.specification.restriction;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Created by zerotul on 12.03.15.
 */
public abstract class Restrictions {

    public static  <T extends Serializable, R> Restriction<T, R> equal(Function<T, R> getter, R value){
        return new SimpleRestriction<>(getter, value, Operator.EQUAL);
    }

    public static  <T extends Serializable, R> Restriction<T, R> like(Function<T, R> getter, R value){
        return new SimpleRestriction<>(getter, value, Operator.LIKE);
    }

    public static  <T extends Serializable, R> Restriction<T, R> notEqual(Function<T, R> getter, R value){
        return new SimpleRestriction<>(getter, value, Operator.NOT_EQUAL);
    }
}
