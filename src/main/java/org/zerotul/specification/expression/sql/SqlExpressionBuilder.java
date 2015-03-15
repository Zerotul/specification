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

    public SqlExpressionBuilder(Mapper mapper) {
        this.mapper = mapper;
        this.params = new ArrayList<>();
    }

    @Override
    public Expression<T, Query> buildExpression(FromSpecification<T> specification) throws BuildException {
        try {
            StringBuilder builder = new StringBuilder(fromClause(specification))
            .append(whereClause(specification))
            .append(orderClause(specification));
            return new SqlExpression<>(new Query(builder.toString(), params));
        } catch (IntrospectionException e) {
           throw new BuildException(e);
        }
    }

    private String fromClause(FromSpecification<T> specification) throws IntrospectionException {
        StringBuilder builder = new StringBuilder("SELECT ");
        BeanInfo fromInfo = Introspector.getBeanInfo(specification.getFromClass());
        List<PropertyDescriptor> descriptors = Arrays.asList(fromInfo.getPropertyDescriptors());
        List<String> propertyNames = new ArrayList<>();
        for(PropertyDescriptor descriptor : descriptors){
            PropertyMap property = mapper.getPropertyMap(descriptor.getName());
            if(property!=null) {
                propertyNames.add(property.getPropertyMapName());
            }
        }

        String columns = String.join(", ", propertyNames);
        builder.append(columns);
        builder.append(" FROM ").append(mapper.getMapName());
        return builder.toString();
    }

    private String whereClause(FromSpecification<T> specification) throws BuildException {
       WhereSpecification where =  specification.getWhere();
       if(where == null)return "";

        StringBuilder builder = new StringBuilder(" WHERE ");
        do {
            Restriction restriction = where.getRestriction();
            Optional<PropertyMap> optional = Optional.of(mapper.getPropertyMap(restriction.getPropertyName()));
            builder.append(optional.get().getPropertyMapName());
            Operator operator = restriction.getOperator();
            switch (operator){
                case EQUAL: builder.append(" = "); break;
                case LIKE: builder.append(" LIKE "); break;
                case NOT_EQUAL: builder.append(" <> "); break;
            }
            builder.append("?");
            Object value;
            if(optional.get().isRelation()){
                value = mapper.convertRelationValue(optional.get(), restriction.getValue());
            }else{
                value = restriction.getValue();
            }
            params.add(value);

            if(!where.isLast()){
                PredicateOperation operation = where.getPredicate().getOperation();
                switch (operation){
                    case AND: builder.append(" AND ");break;
                    case OR: builder.append(" OR ");break;
                }
                where = where.getPredicate().getAfterWhere();
            }else{
                where = null;
            }
        }while (where!=null);
        return builder.toString();
    }

    private String orderClause(FromSpecification<T> from){
        if(from.getOrder()==null) return "";

        Order<T> order = from.getOrder();
        StringBuilder builder = new StringBuilder(" ORDER BY ");
        List<String> propertyNames = order.getPropertyNames().stream()
                .map((String propertyName) -> {
                    PropertyMap property = mapper.getPropertyMap(propertyName);
                    if(property==null) throw new IllegalArgumentException("property not found, propertyName="+propertyName);
                    return property.getPropertyMapName();
                }).collect(Collectors.toList());
        builder.append(String.join(", ", propertyNames));
        builder.append(" ").append(order.getOrderType().toString());
        return builder.toString();
    }
}
