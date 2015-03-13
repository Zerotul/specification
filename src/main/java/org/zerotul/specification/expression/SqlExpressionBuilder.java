package org.zerotul.specification.expression;

import org.zerotul.specification.FromSpecification;
import org.zerotul.specification.PredicateOperation;
import org.zerotul.specification.WhereSpecification;
import org.zerotul.specification.exception.BuildException;
import org.zerotul.specification.mapper.Mapper;
import org.zerotul.specification.restriction.Operator;
import org.zerotul.specification.restriction.Restriction;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.String;
import java.util.*;

/**
 * Created by zerotul on 12.03.15.
 */
public class SqlExpressionBuilder<T> implements ExpressionBuilder<T, String> {

    private final Mapper mapper;

    public SqlExpressionBuilder(Mapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Expression<T, String> buildExpression(FromSpecification<T> specification) throws BuildException {
        try {
            StringBuilder builder = new StringBuilder(fromClause(specification));
            builder.append(whereClause(specification));
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

    private String whereClause(FromSpecification<T> specification){
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
            if(mapper.getPropertyType(restriction.getPropertyName()).equals(String.class)){
                builder.append("'").append(restriction.getValue()).append("'");
            }else{
                builder.append(restriction.getValue());
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
}
