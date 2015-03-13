package org.zerotul.specification.restriction;

import org.zerotul.specification.recorder.Recorder;

import java.io.Serializable;

/**
 * Created by zerotul on 12.03.15.
 */

public interface Restriction<T extends Serializable , R> extends Serializable {

    public Operator getOperator();

    public R getValue();

    public String getPropertyName();

    public void setRecorder(Recorder<T> recorder);
}
