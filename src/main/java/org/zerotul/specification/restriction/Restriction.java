package org.zerotul.specification.restriction;

import org.zerotul.specification.Recorder;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Created by zerotul on 12.03.15.
 */

public interface Restriction<T, R> extends Serializable {

    public Operator getOperator();

    public R getValue();

    public String getPropertyName();

    public void setRecorder(Recorder recorder);
}
