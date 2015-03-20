package org.zerotul.specification.test.mock;

import org.zerotul.specification.mapper.AbstractMapper;

import java.util.function.Supplier;

/**
 * Created by zerotul on 12.03.15.
 */
public class MockEntitySqlMapper<T extends Mock> extends AbstractMapper<T> {

    public MockEntitySqlMapper(Class<T> clazz) {
        super(clazz);
    }

    @Override
    protected void initInternal() {
        super.initInternal();
        addProperty(Mock::getField1, "field_1");
        addProperty(Mock::getField2, "field_2");
        addProperty(Mock::getField3, "field_3");
        addProperty(Mock::getField4, "field_4");
        addProperty(Mock::getId, "id");
        addRelation(Mock::getMock, "mock_id", (T mock) -> mock.getId());
    }

    @Override
    protected T getNewRecord() {
        return (T) new MockEntity();
    }

    @Override
    public String getMapName() {
        return "mock_entity";
    }
}
