package org.zerotul.specification.expression.sql;

import org.zerotul.specification.expression.Expression;

/**
 * Created by zerotul on 12.03.15.
 */
public class SqlExpression<T> implements Expression<T, Query> {

    private final Query query;

    public SqlExpression(Query query) {
        this.query = query;
    }

    @Override
    public Query toResult() {
        return this.query;
    }
}
