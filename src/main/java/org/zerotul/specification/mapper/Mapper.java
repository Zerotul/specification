package org.zerotul.specification.mapper;

import java.io.Serializable;
import java.sql.ResultSet;

/**
 * Created by zerotul on 12.03.15.
 */
public interface Mapper<T extends Serializable> {

    public PropertyMap getPropertyMap(String propertyName);

    public String getMapName();

    public Object convertRelationValue(PropertyMap propertyMap, Object value);

    public T convertToEntity(ResultSet rs, int rowIndex);
}
