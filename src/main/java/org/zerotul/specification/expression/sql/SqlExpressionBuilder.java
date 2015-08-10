package org.zerotul.specification.expression.sql;

import org.zerotul.specification.FromSpecification;
import org.zerotul.specification.expression.Expression;
import org.zerotul.specification.expression.ExpressionBuilder;
import org.zerotul.specification.mapper.PropertyMap;
import org.zerotul.specification.order.Order;
import org.zerotul.specification.predicate.PredicateOperation;
import org.zerotul.specification.WhereSpecification;
import org.zerotul.specification.exception.BuildException;
import org.zerotul.specification.mapper.Mapper;
import org.zerotul.specification.predicate.PredicateSpecification;
import org.zerotul.specification.restriction.Operator;
import org.zerotul.specification.restriction.Restriction;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.String;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zerotul on 12.03.15.
 */
public class SqlExpressionBuilder<T extends Serializable> implements ExpressionBuilder<T, Query> {

    private final Mapper mapper;

    private final List params;
    private static final Map<Operator, String> operatorMap;

    static {
        operatorMap = new HashMap<>();
        operatorMap.put(Operator.EQUAL, "=");
        operatorMap.put(Operator.LIKE, "LIKE");
        operatorMap.put(Operator.NOT_EQUAL, "<>");
        operatorMap.put(Operator.QT, ">");
    }
    public SqlExpressionBuilder(Mapper mapper) {
        this.mapper = mapper;
        this.params = new ArrayList<>();
    }

    @Override
    public Expression<Query> buildExpression(FromSpecification<? extends T> specification) throws BuildException {
        try {
            String fromClause = fromClause(specification);
            String whereClause = whereClause(specification);
            String orderClause = orderClause(specification);
            String pagingClause = pagingClause(specification);
            StringBuilder builder = new StringBuilder(fromClause)
                    .append(whereClause)
                    .append(orderClause)
                    .append(pagingClause);
            String rowCountQuery = new StringBuilder("SELECT count(*) FROM ")
                    .append(mapper.getMapName()).append(" ").append(whereClause).toString();
            return new SqlExpression(new Query(builder.toString(), params, rowCountQuery));
        } catch (IntrospectionException e) {
            throw new BuildException(e);
        }
    }

    private String fromClause(FromSpecification<? extends T> specification) throws IntrospectionException {
        StringBuilder builder = new StringBuilder("SELECT ");
        BeanInfo fromInfo = Introspector.getBeanInfo(specification.getFromClass());
        List<PropertyDescriptor> descriptors = Arrays.asList(fromInfo.getPropertyDescriptors());
        List<String> propertyNames = new ArrayList<>();
        for (PropertyDescriptor descriptor : descriptors) {
            PropertyMap property = mapper.getPropertyMap(descriptor.getName());
            if (property != null) {
                propertyNames.add(property.getPropertyMapName());
            }
        }

        String columns = String.join(", ", propertyNames);
        builder.append(columns);
        builder.append(" FROM ").append(mapper.getMapName());
        return builder.toString();
    }

    private String whereClause(FromSpecification<? extends T> specification) throws BuildException {
        WhereSpecification where = specification.getWhere();
        if (where == null) return "";

        StringBuilder builder = new StringBuilder(" WHERE ");
        do {
            if(where.hasStartBlock()){
                for(int i =0; i<where.startBlockCount(); i++) {
                    builder.append("(");
                }
            }
            Restriction restriction = where.getRestriction();
            Optional<PropertyMap> optional = Optional.of(mapper.getPropertyMap(restriction.getPropertyName()));
            builder.append(optional.get().getPropertyMapName());
            Optional<String> operator = Optional.of(operatorMap.get(restriction.getOperator()));
            builder.append(" ").append(operator.get()).append(" ");

            builder.append("?");
            Object value;
            if (optional.get().isRelation()) {
                value = mapper.convertRelationValue(optional.get(), restriction.getValue());
            } else {
                value = restriction.getValue();
            }
            params.add(value);

            if(where.hasEndBlock()){
                for(int i =0; i<where.endBlockCount(); i++) {
                    builder.append(")");
                }
            }
            if (!where.isLast()) {
                PredicateSpecification<T> predicate = where.getPredicate();
                PredicateOperation operation = predicate.getOperation();
                switch (operation) {
                    case AND:
                        builder.append(" AND ");
                        break;
                    case OR:
                        builder.append(" OR ");
                        break;
                }
                where = where.getPredicate().getAfterWhere();
            } else {
                where = null;
            }
        } while (where != null);
        return builder.toString();
    }

    private String orderClause(FromSpecification<? extends T> from) {
        if (from.getOrder() == null || from.getOrder().isEmpty()) return "";

        List<? extends Order<? extends T>> orders = from.getOrder();
        StringBuilder builder = new StringBuilder(" ORDER BY");

        orders.forEach((Order<? extends T> order) -> {
            builder.append(" ");
            List<String> propertyNames = order.getPropertyNames().stream()
                    .map((String propertyName) -> {
                        PropertyMap property = mapper.getPropertyMap(propertyName);
                        if (property == null)
                            throw new IllegalArgumentException("property not found, propertyName=" + propertyName);
                        return property.getPropertyMapName();
                    }).collect(Collectors.toList());
            builder.append(String.join(", ", propertyNames));
            builder.append(" ").append(order.getOrderType().toString());
        });

        return builder.toString();
    }

    private String pagingClause(FromSpecification<? extends T> from) {
        if (from.getMax() > 0) {
            StringBuilder builder = new StringBuilder(" LIMIT ")
                    .append(from.getMax()).append(" OFFSET ").append(from.getOffset());
            return builder.toString();
        }

        return "";
    }
}
