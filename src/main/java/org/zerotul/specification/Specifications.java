package org.zerotul.specification;

import java.io.Serializable;

/**
 * Created by zerotul on 12.03.15.
 */
public abstract class Specifications {

    public static <T extends Serializable> FromSpecification<T> from(Class<T> clazz){
        return new FromSpecificationImpl<>(clazz);
    }
}
