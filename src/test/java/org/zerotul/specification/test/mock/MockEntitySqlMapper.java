package org.zerotul.specification.test.mock;

import org.zerotul.specification.mapper.Mapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by zerotul on 12.03.15.
 */
public class MockEntitySqlMapper implements Mapper {

    private static final Map<String, Property> propertyMap;

    private static final Map<String, Function> relationPropertyMap;

    static {
        Map<String, Property> propertyHashMap = new HashMap<>();
        propertyHashMap.put("field1", new Property("field_1", String.class, 1));
        propertyHashMap.put("field2", new Property("field_2", String.class, 2));
        propertyHashMap.put("field3", new Property("field_3", String.class, 3));
        propertyHashMap.put("field4", new Property("field_4", Integer.class, 4));
        propertyHashMap.put("id", new Property("id", Integer.class, 5));
        propertyHashMap.put("mock", new Property("mock_id", MockEntity.class, 6));
        propertyMap = Collections.unmodifiableMap(propertyHashMap);

        HashMap<String, Function<? extends Object, ? extends Object>> rPropertyMap = new HashMap<>();
        rPropertyMap.put("mock", (MockEntity mock)->mock.getId());
        relationPropertyMap = Collections.unmodifiableMap(rPropertyMap);
    }

    {
       HashMap<String, Function> propertyHashMap = new HashMap<>();
    }

    @Override
    public String getMapPropertyName(String propertyName) {
        Property property = propertyMap.get(propertyName);
        return property!=null ? property.getName() : null;
    }

    @Override
    public int getPropertyIndex(String propertyName) {
        Property property = propertyMap.get(propertyName);
        return property!=null ? property.getIndex() : -1;
    }

    @Override
    public String getMapName() {
        return "mock_entity";
    }

    @Override
    public Class getPropertyType(String propertyName) {
        Property property = propertyMap.get(propertyName);
        return property!=null ? property.getType() : null;
    }

    public Object getIdValue(String propertyName, Object relation){
        Function function = relationPropertyMap.get(propertyName);
        return function!=null ? function.apply(relation) : null;
    }



    private static class Property {

        private final String name;

        private final Class type;

        private final int index;

        public Property(String name, Class type, int index) {
            this.name = name;
            this.type = type;
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public Class getType() {
            return type;
        }

        public int getIndex() {
            return index;
        }
    }
}
