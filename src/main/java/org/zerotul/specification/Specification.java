package org.zerotul.specification;

import org.zerotul.specification.exception.BuildException;
import org.zerotul.specification.expression.Expression;
import org.zerotul.specification.expression.ExpressionBuilder;

import java.io.Serializable;

/**
 * Created by zerotul on 11.03.15.
 */
public interface Specification<T extends Serializable> extends Serializable{

    public <E> Expression<E> isSatisfied(ExpressionBuilder<? super T, E> expressionBuilder) throws BuildException;

    public Class<? extends T> getResultClass();
}
