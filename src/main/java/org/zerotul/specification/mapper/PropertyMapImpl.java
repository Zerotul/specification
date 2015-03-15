package org.zerotul.specification.mapper;

import java.io.Serializable;

/**
 * Created by zerotul on 15.03.15.
 */
public class PropertyMapImpl implements PropertyMap, Serializable {

    private static final long serialVersionUID = -7121542958802295215L;

    private final String propertyName;

    private final String propertyMapName;

    private final Class propertyType;

    private final boolean isRelation;

    public PropertyMapImpl(String propertyName, String propertyMapName, Class propertyType, boolean isRelation) {
        this.propertyName = propertyName;
        this.propertyMapName = propertyMapName;
        this.propertyType = propertyType;
        this.isRelation= isRelation;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public String getPropertyMapName() {
        return propertyMapName;
    }

    @Override
    public Class getPropertyType() {
        return propertyType;
    }

    @Override
    public boolean isRelation() {
        return this.isRelation;
    }
}
