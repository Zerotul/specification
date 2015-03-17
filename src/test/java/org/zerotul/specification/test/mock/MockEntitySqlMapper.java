package org.zerotul.specification.test.mock;

import org.zerotul.specification.mapper.AbstractMapper;
import org.zerotul.specification.mapper.Mapper;
import org.zerotul.specification.mapper.PropertyMap;
import org.zerotul.specification.mapper.PropertyMapImpl;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by zerotul on 12.03.15.
 */
public class MockEntitySqlMapper extends AbstractMapper<MockEntity> {

    public MockEntitySqlMapper(Class<MockEntity> clazz) {
        super(clazz);
    }

    @Override
    protected void init() {
        super.init();
        addProperty(MockEntity::getField1, "field_1");
        addProperty(MockEntity::getField2, "field_2");
        addProperty(MockEntity::getField3, "field_3");
        addProperty(MockEntity::getField4, "field_4");
        addProperty(MockEntity::getId, "id");
        addRelation(MockEntity::getMock, "mock_id", (MockEntity mock) -> mock.getId());
    }

    @Override
    protected Supplier<MockEntity> getEntityConsumer() {
        return MockEntity::new;
    }

    @Override
    public String getMapName() {
        return "mock_entity";
    }
}
