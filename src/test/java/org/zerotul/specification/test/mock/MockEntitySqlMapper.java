package org.zerotul.specification.test.mock;

import org.zerotul.specification.mapper.AbstractMapper;
import org.zerotul.specification.mapper.Mapper;
import org.zerotul.specification.mapper.PropertyMap;
import org.zerotul.specification.mapper.PropertyMapImpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by zerotul on 12.03.15.
 */
public class MockEntitySqlMapper extends AbstractMapper {

    @Override
    protected void init() {
        super.init();
        addProperty(new PropertyMapImpl("field1", "field_1", String.class, false));
        addProperty(new PropertyMapImpl("field2", "field_2", String.class, false));
        addProperty(new PropertyMapImpl("field3", "field_3", String.class, false));
        addProperty(new PropertyMapImpl("field4", "field_4", Integer.class, false));
        addProperty(new PropertyMapImpl("id", "id", Integer.class, false));
        addRelation(new PropertyMapImpl("mock", "mock_id", MockEntity.class, true), (MockEntity mock)->mock.getId());
    }

    @Override
    public String getMapName() {
        return "mock_entity";
    }

}
