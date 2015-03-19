package org.zerotul.specification.mapper;

import org.zerotul.specification.recorder.EnhancerRecorder;
import org.zerotul.specification.recorder.Recorder;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by zerotul on 15.03.15.
 */
public abstract class AbstractMapper<T extends Serializable> implements Mapper<T> {

    private Map<String, PropertyMap> propertyMap;

    private Map<PropertyMap, Function> relationPropertyMap;

    private  Map<PropertyMap, Setter> setters;

    private Recorder<T> recorder;


    public AbstractMapper(Class<T> clazz) {
        propertyMap = new HashMap<>();
        relationPropertyMap = new HashMap<>();
        setters = new HashMap<>();
        this.recorder = EnhancerRecorder.create(clazz);
        init();
        propertyMap = Collections.unmodifiableMap(propertyMap);
        relationPropertyMap = Collections.unmodifiableMap(relationPropertyMap);
        setters = Collections.unmodifiableMap(setters);
    }

    protected void init() {

    }

    protected <U> void addProperty(Function<T, U> getter, String mapName, BiConsumer<T, U> setter) {
        PropertyMap property = new PropertyMapImpl(recorder.getPropertyName(getter), mapName, recorder.getPropertyType(getter), false);
        this.propertyMap.put(property.getPropertyName(), property);
        if (setter != null) {
            setters.put(property, new Setter(setter));
        }
    }

    protected <U> void addProperty(Function<T, U> getter, String mapName) {
        addProperty(getter, mapName, null);
    }

    protected <U> void addRelation(Function<T, U> getter, String mapName, Function<T, ?> function, BiConsumer<T, U> setter) {
        PropertyMap property = new PropertyMapImpl(recorder.getPropertyName(getter), mapName, recorder.getPropertyType(getter), true);
        propertyMap.put(property.getPropertyName(), property);
        relationPropertyMap.put(property, function);
        if (setter != null) {
            setters.put(property, new Setter(setter));
        }
    }

    protected <U> void addRelation(Function<T, U> getter, String mapName,Function<T, ?> function) {
        addRelation(getter, mapName, function, null);
    }

    @Override
    public PropertyMap getPropertyMap(String propertyName) {
        PropertyMap property = propertyMap.get(propertyName);
        return property != null ? property : null;
    }

    @Override
    public Object convertRelationValue(PropertyMap propertyMap, Object value) {
        if (!propertyMap.isRelation()) return value;
        Function function = relationPropertyMap.get(propertyMap);
        return function != null ? function.apply(value) : null;
    }

    protected abstract T getNewRecord();

    @Override
    public T convertToEntity(ResultSet rs, int rowIndex) {
        try {
            T entity = getNewRecord();
            for (Map.Entry<PropertyMap, Setter> entry : setters.entrySet()) {
                String columnName = entry.getKey().getPropertyMapName();
                int index =columnName.indexOf(".");
                if(index>-1){
                    columnName = columnName.substring(index+1, columnName.length());
                }
                entry.getValue().accept(entity, rs.getObject(columnName));
            }
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static class Setter<T, U>{
        private final BiConsumer<T, U> setter;

        private Setter(BiConsumer<T, U> setter) {
            this.setter = setter;
        }

        public void accept(T obj, U value){
            setter.accept(obj, value);
        }
    }

}
