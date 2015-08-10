package org.zerotul.specification.expression.jpa;

import org.zerotul.specification.expression.Expression;

import javax.persistence.Query;

/**
 * Created by zerotul.
 */
public class JPQLExpression implements Expression<Query> {

    private final Query query;

    public JPQLExpression(Query query) {
        this.query = query;
    }

    @Override
    public Query toResult() {
        return query;
    }
}
