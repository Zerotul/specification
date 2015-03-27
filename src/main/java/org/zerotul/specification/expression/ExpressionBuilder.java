package org.zerotul.specification.expression;

import org.zerotul.specification.FromSpecification;
import org.zerotul.specification.exception.BuildException;

import java.io.Serializable;

/**
 * Created by zerotul on 11.03.15.
 */
public interface ExpressionBuilder<T extends Serializable, V>  {

    public Expression<V> buildExpression(FromSpecification<? extends T> specification) throws BuildException;


}
