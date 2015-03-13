package org.zerotul.specification.restriction;

import org.zerotul.specification.Recorder;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Created by zerotul on 12.03.15.
 */
public class SimpleRestriction<T extends Serializable, R> implements Restriction<T, R> {

    private static final long serialVersionUID = -3203049891220748885L;

    private final Function<T, R> getter;

    private final R value;

    private final Operator operator;

    private Recorder<T> recorder;

    public SimpleRestriction(Function<T, R> getter, R value, Operator operator) {
        this.getter = getter;
        this.value = value;
        this.operator = operator;
    }

    @Override
    public Operator getOperator() {
        return operator;
    }

    @Override
    public R getValue() {
        return value;
    }

    @Override
    public String getPropertyName() {
        return recorder.getPropertyName(getter);
    }

    @Override
    public void setRecorder(Recorder recorder) {
        this.recorder = recorder;
    }
}
