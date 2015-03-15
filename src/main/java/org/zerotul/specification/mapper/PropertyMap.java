package org.zerotul.specification.mapper;

/**
 * Created by zerotul on 15.03.15.
 */
public interface PropertyMap {

    public String getPropertyName();

    public String getPropertyMapName();

    public Class getPropertyType();

    public boolean isRelation();
}
