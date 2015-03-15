package org.zerotul.specification.expression.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by zerotul on 15.03.15.
 */
public interface JdbcSetter<T> {
    public void apply(PreparedStatement stmt, int index, T t) throws SQLException;
}
