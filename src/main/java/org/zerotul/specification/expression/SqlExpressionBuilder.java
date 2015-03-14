package org.zerotul.specification.expression;

import org.zerotul.specification.FromSpecification;
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
public class SqlExpressionBuilder<T extends Serializable> implements ExpressionBuilder<T, String> {

    private final Mapper mapper;

    public SqlExpressionBuilder(Mapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Expression<T, String> buildExpression(FromSpecification<T> specification) throws BuildException {
        try {
            StringBuilder builder = new StringBuilder(fromClause(specification))
            .append(whereClause(specification))
            .append(orderClause(specification));
            return new SqlExpression<>(builder.toString());
        } catch (IntrospectionException e) {
           throw new BuildException(e);
        }
    }

    private String fromClause(FromSpecification<T> specification) throws IntrospectionException {
        StringBuilder builder = new StringBuilder("SELECT ");
        BeanInfo fromInfo = Introspector.getBeanInfo(specification.getFromClass());
        List<PropertyDescriptor> descriptors = Arrays.asList(fromInfo.getPropertyDescriptors());
        Collections.sort(descriptors,(PropertyDescriptor property1, PropertyDescriptor property2)->{
           return Integer.valueOf(mapper.getPropertyIndex(property1.getName())).compareTo(mapper.getPropertyIndex(property1.getName()));
        });
        List<String> propertyNames = new ArrayList<>();
        for(PropertyDescriptor descriptor : descriptors){
            String propertyName = mapper.getMapPropertyName(descriptor.getName());
            if(propertyName!=null) {
                propertyNames.add(propertyName);
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
            Optional<String> optional = Optional.of(mapper.getMapPropertyName(restriction.getPropertyName()));
            builder.append(optional .get());
            Operator operator = restriction.getOperator();
            switch (operator){
                case EQUAL: builder.append(" = "); break;
                case LIKE: builder.append(" LIKE "); break;
                case NOT_EQUAL: builder.append(" <> "); break;
            }

            //TODO переделать на что более годное
            if(mapper.getPropertyType(restriction.getPropertyName()).equals(String.class)){
                builder.append("'").append(restriction.getValue()).append("'");
            }else if (Number.class.isAssignableFrom(mapper.getPropertyType(restriction.getPropertyName()))){
                builder.append(restriction.getValue());
            }else{
                Object idValue =  mapper.getIdValue(restriction.getPropertyName(), restriction.getValue());
                if(idValue==null)throw new BuildException("id value for "+restriction.getPropertyName()+"not found");
                if(idValue.getClass().equals(String.class)){
                    builder.append("'").append(idValue).append("'");
                }else if (Number.class.isAssignableFrom(idValue.getClass())){
                    builder.append(idValue);
                }
            }

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
                .map(mapper::getMapPropertyName).collect(Collectors.toList());
        builder.append(String.join(", ", propertyNames));
        builder.append(" ").append(order.getOrderType().toString());
        return builder.toString();
    }
}
