package org.zerotul.specification.expression;

/**
 * Created by zerotul on 12.03.15.
 */
public class SqlExpression<T> implements Expression<T, String> {

    private final String sql;

    public SqlExpression(String sql) {
        this.sql = sql;
    }

    @Override
    public String toResult() {
        return this.sql;
    }
}
