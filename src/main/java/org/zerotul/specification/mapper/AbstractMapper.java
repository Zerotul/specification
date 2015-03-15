package org.zerotul.specification.mapper;

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


    public AbstractMapper() {
        propertyMap = new HashMap<>();
        relationPropertyMap = new HashMap<>();
        setters = new HashMap<>();
        init();
        propertyMap = Collections.unmodifiableMap(propertyMap);
        relationPropertyMap = Collections.unmodifiableMap(relationPropertyMap);
        setters = Collections.unmodifiableMap(setters);
    }

    protected void init() {

    }

    protected <U> void addProperty(PropertyMap property, BiConsumer<T, U> setter) {
        if (property.isRelation())
            throw new IllegalArgumentException("if the property isRelation = true, use method addRelation");
        this.propertyMap.put(property.getPropertyName(), property);
        if (setter != null) {
            setters.put(property, new Setter(setter));
        }
    }

    protected void addProperty(PropertyMap property) {
        addProperty(property, null);
    }

    protected void addRelation(PropertyMap property, Function<? extends Object, ? extends Object> function, BiConsumer<T, Object> setter) {
        if (!property.isRelation())
            throw new IllegalArgumentException("if the property isRelation = false, use method addProperty");
        propertyMap.put(property.getPropertyName(), property);
        relationPropertyMap.put(property, function);
        if (setter != null) {
            setters.put(property, new Setter(setter));
        }
    }

    protected void addRelation(PropertyMap property, Function<? extends Object, ? extends Object> function) {
        addRelation(property, function, null);
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

    protected abstract Supplier<T> getEntityConsumer();

    @Override
    public T convertToEntity(ResultSet rs, int rowIndex) {
        try {
            T entity = getEntityConsumer().get();
            for (Map.Entry<PropertyMap, Setter> entry : setters.entrySet()) {
                entry.getValue().accept(entity, rs.getObject(entry.getKey().getPropertyMapName()));
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
