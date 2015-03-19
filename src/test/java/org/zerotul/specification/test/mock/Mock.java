package org.zerotul.specification.test.mock;

import java.io.Serializable;

/**
 * Created by zerotul on 19.03.15.
 */
public interface Mock extends Serializable{

    public String getField1();

    public String getField2();

    public void setField2(String field2);

    public String getField3();

    public void setField3(String field3);

    public int getField4();
    public void setField4(int field4);

    public String getId();

    public void setId(String id);

    public MockEntity getMock();

    public void setMock(MockEntity mock);
}
