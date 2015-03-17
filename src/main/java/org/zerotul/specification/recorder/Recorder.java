package org.zerotul.specification.recorder;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Created by zerotul on 13.03.15.
 */
public interface Recorder<T extends Serializable> extends Serializable {

    <R> String getPropertyName(Function<T, R> getter);

    <R> Class<R> getPropertyType(Function<T, R> getter);
}
