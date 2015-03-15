package org.zerotul.specification.restriction;

import org.zerotul.specification.recorder.Recorder;

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

    private String propertyName;

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
        if(propertyName==null){
            propertyName = recorder.getPropertyName(getter);
        }
        return propertyName;
    }

    @Override
    public void setRecorder(Recorder<T> recorder) {
        this.recorder = recorder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleRestriction that = (SimpleRestriction) o;

        if (operator != that.operator) return false;
        if (propertyName != null ? !propertyName.equals(that.propertyName) : that.propertyName != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (operator != null ? operator.hashCode() : 0);
        result = 31 * result + (propertyName != null ? propertyName.hashCode() : 0);
        return result;
    }
}
