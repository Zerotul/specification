package org.zerotul.specification.expression;

import org.zerotul.specification.FromSpecification;
import org.zerotul.specification.exception.BuildException;

/**
 * Created by zerotul on 11.03.15.
 */
public interface ExpressionBuilder<T, V>  {

    public Expression<T, V> buildExpression(FromSpecification<T> specification) throws BuildException;
}
