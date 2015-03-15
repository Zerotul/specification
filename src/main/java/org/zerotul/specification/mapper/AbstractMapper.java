package org.zerotul.specification.mapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by zerotul on 15.03.15.
 */
public abstract class AbstractMapper implements Mapper {

    private Map<String, PropertyMap> propertyMap;

    private Map<PropertyMap, Function> relationPropertyMap;


    public AbstractMapper() {
        propertyMap = new HashMap<>();
        relationPropertyMap = new HashMap<>();
        init();
        propertyMap = Collections.unmodifiableMap(propertyMap);
        relationPropertyMap = Collections.unmodifiableMap(relationPropertyMap);
    }

    protected void init() {

    }

    protected void addProperty(PropertyMap property) {
        if(property.isRelation()) throw new IllegalArgumentException("if the property isRelation = true, use method addRelation");
        this.propertyMap.put(property.getPropertyName(), property);
    }

    protected void addRelation(PropertyMap property, Function<? extends Object, ? extends Object> function){
        if(!property.isRelation()) throw new IllegalArgumentException("if the property isRelation = false, use method addProperty");
        propertyMap.put(property.getPropertyName(), property);
        relationPropertyMap.put(property, function);
    }

    @Override
    public PropertyMap getPropertyMap(String propertyName) {
        PropertyMap property = propertyMap.get(propertyName);
        return property!=null ? property : null;
    }

    @Override
    public Object convertRelationValue(PropertyMap propertyMap, Object value) {
        if(!propertyMap.isRelation()) return value;
        Function function = relationPropertyMap.get(propertyMap);
        return function!=null ? function.apply(value) : null;
    }
}
