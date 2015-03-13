package org.zerotul.specification.mapper;

/**
 * Created by zerotul on 12.03.15.
 */
public interface Mapper {

    public String getMapPropertyName(String propertyName);

    public int getPropertyIndex(String propertyName);

    public String getMapName();

    public Class getPropertyType(String propertyName);


}
