package org.zerotul.specification.expression.sql;

import java.util.List;

/**
 * Created by zerotul on 15.03.15.
 */
public class Query {

    private final String query;

    private final List params;

    public Query(String query, List params) {
        this.query = query;
        this.params = params;
    }

    public String getQuery() {
        return query;
    }

    public List getParams() {
        return params;
    }
}
