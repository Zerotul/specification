package org.zerotul.specification.mapper;

/**
 * Created by zerotul on 12.03.15.
 */
public interface Mapper {

    public PropertyMap getPropertyMap(String propertyName);

    public String getMapName();

    public Object convertRelationValue(PropertyMap propertyMap, Object value);
}
