package org.zerotul.specification.test.mock;

import java.io.Serializable;

/**
 * Created by zerotul on 12.03.15.
 */
public class MockEntity implements Serializable{

    private String id;
    private String field1;
    private String field2;
    private String field3;
    private int field4;
    private MockEntity mock;

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public int getField4() {
        return field4;
    }

    public void setField4(int field4) {
        this.field4 = field4;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MockEntity getMock() {
        return mock;
    }

    public void setMock(MockEntity mock) {
        this.mock = mock;
    }
}
