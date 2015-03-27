package org.zerotul.specification;

import org.zerotul.specification.exception.BuildException;
import org.zerotul.specification.expression.Expression;
import org.zerotul.specification.expression.ExpressionBuilder;

import java.io.Serializable;

/**
 * Created by zerotul on 12.03.15.
 */
public class SpecificationImpl<T extends Serializable> implements Specification<T> {

    private static final long serialVersionUID = 42922816840472647L;

    private final FromSpecification<T> from;

    public SpecificationImpl(FromSpecification<T> from) {
        this.from = from;
    }

    @Override
    public <V> Expression<V> isSatisfied(ExpressionBuilder<? super T, V> expressionBuilder) throws BuildException {
        return  expressionBuilder.buildExpression(from);
    }

    @Override
    public Class<T> getResultClass() {
        return from.getFromClass();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpecificationImpl that = (SpecificationImpl) o;

        if (from != null ? !from.equals(that.from) : that.from != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return from != null ? from.hashCode() : 0;
    }
}
