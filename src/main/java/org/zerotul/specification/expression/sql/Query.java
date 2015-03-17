package org.zerotul.specification.expression.sql;

import java.util.List;

/**
 * Created by zerotul on 15.03.15.
 */
public class Query {

    private final String query;

    private final List params;

    private final String rowCountQuery;

    public Query(String query, List params, String rowCountQuery) {
        this.query = query;
        this.params = params;
        this.rowCountQuery = rowCountQuery;
    }

    public String getQuery() {
        return query;
    }

    public List getParams() {
        return params;
    }

    public String getRowCountQuery() {
        return rowCountQuery;
    }
}
