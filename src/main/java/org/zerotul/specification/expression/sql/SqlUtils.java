package org.zerotul.specification.expression.sql;

import com.google.common.collect.ImmutableMap;

import java.sql.PreparedStatement;
import java.util.Map;

/**
 * Created by zerotul on 15.03.15.
 */
public class SqlUtils {

    private static final Map<Class<?>, JdbcSetter> jdbcSetters = ImmutableMap.<Class<?>,JdbcSetter>builder()
            .put(String.class, (JdbcSetter<String>) PreparedStatement::setString)
            .put(Integer.class, (JdbcSetter<Integer>)PreparedStatement::setInt)
            .put(Float.class, (JdbcSetter<Float>)PreparedStatement::setFloat)
            .put(Double.class, (JdbcSetter<Double>) PreparedStatement::setDouble)
            .put(Long.class, (JdbcSetter<Long>)PreparedStatement::setLong)
            .put(Character.class, (JdbcSetter<Integer>)PreparedStatement::setInt)
            .put(Byte.class, (JdbcSetter<Integer>)PreparedStatement::setInt)
            .put(int.class, (JdbcSetter<Integer>)PreparedStatement::setInt)
            .put(float.class, (JdbcSetter<Float>)PreparedStatement::setFloat)
            .put(double.class, (JdbcSetter<Double>)PreparedStatement::setDouble)
            .put(long.class, (JdbcSetter<Long>)PreparedStatement::setLong)
            .put(char.class, (JdbcSetter<Integer>)PreparedStatement::setInt)
            .put(byte.class, (JdbcSetter<Integer>)PreparedStatement::setInt)
            .build();

    public static <T> JdbcSetter<T> getSetter(Class<T> cls) {
        return jdbcSetters.get(cls);
    }
}
