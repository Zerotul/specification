package org.zerotul.specification.order;

import org.zerotul.specification.recorder.Recorder;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zerotul on 13.03.15.
 */
public interface Order<T extends Serializable>  extends Serializable{

    public OrderType getOrderType();

    public List<String> getPropertyNames();

    public void setRecorder(Recorder<T> recorder);
}
