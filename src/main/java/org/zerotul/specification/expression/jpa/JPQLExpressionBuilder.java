package org.zerotul.specification.expression.jpa;

import org.zerotul.specification.FromSpecification;
import org.zerotul.specification.WhereSpecification;
import org.zerotul.specification.exception.BuildException;
import org.zerotul.specification.expression.Expression;
import org.zerotul.specification.expression.ExpressionBuilder;
import org.zerotul.specification.mapper.PropertyMap;
import org.zerotul.specification.order.Order;
import org.zerotul.specification.predicate.PredicateOperation;
import org.zerotul.specification.predicate.PredicateSpecification;
import org.zerotul.specification.restriction.Operator;
import org.zerotul.specification.restriction.Restriction;


import javax.persistence.Query;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.zerotul.specification.Utils.hasText;

/**
 * Created by zerotul.
 */
public class JPQLExpressionBuilder<T extends Serializable> implements ExpressionBuilder<T, Query> {

    private final EntityManager em;

    private static final Map<Operator, String> operatorMap;
    private static final Map<PredicateOperation, String> predicateMap;

    private String entityName;

    private String entityAlias;

    private static final String ENTITY_ALIAS_POSTFIX = "_";

    private Map<String, Object> whereParams;

    static {
        operatorMap = new HashMap<>();
        operatorMap.put(Operator.EQUAL, "=");
        operatorMap.put(Operator.LIKE, "LIKE");
        operatorMap.put(Operator.NOT_EQUAL, "<>");
        operatorMap.put(Operator.QT, ">");

        predicateMap = new HashMap<>();
        predicateMap.put(PredicateOperation.AND, "AND");
        predicateMap.put(PredicateOperation.OR, "OR");
    }

    public JPQLExpressionBuilder(EntityManager em) {
        this.em = em;
        whereParams = new HashMap<>();
    }


    @Override
    public Expression<Query> buildExpression(FromSpecification<? extends T> specification) throws BuildException {
        String fromClause = buildFromClause(specification);
        String whereClause = buildWhereClause(specification);
        String orderClause = buildOrderClause(specification);

        StringBuilder queryString = new StringBuilder(fromClause)
                .append(whereClause)
                .append(orderClause);
        Query query = em.createQuery(queryString.toString());
        whereParams.forEach((String key, Object value) -> {
            query.setParameter(key, value);
        });
        if(specification.getMax()>0) {
            query.setMaxResults(specification.getMax());
        }
        if(specification.getOffset()>0){
            query.setFirstResult(specification.getOffset());
        }
        return new JPQLExpression(query);
    }

    protected String buildFromClause(FromSpecification<? extends T> specification){
        StringBuilder builder = new StringBuilder("SELECT ");
        Class<? extends T> fromClass = specification.getFromClass();
        Entity entity = fromClass.getAnnotation(Entity.class);
        if(entity!=null && hasText(entity.name())){
            entityName = entity.name();
        }else{
            entityName = fromClass.getSimpleName();
        }
        entityAlias = entityName.toLowerCase()+ENTITY_ALIAS_POSTFIX;
        return builder
                .append(entityAlias)
                .append(" FROM ")
                .append(entityName).toString();
    }

    protected String buildWhereClause(FromSpecification<? extends T> specification){
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
            String propertyName = restriction.getPropertyName();
            builder.append(entityAlias)
                    .append(".")
                    .append(propertyName);
            Optional<String> operator = Optional.of(operatorMap.get(restriction.getOperator()));
            builder.append(" ").append(operator.get());

            String paramName;
            if(whereParams.containsKey(propertyName)){
                long paramCount = whereParams.keySet().stream().filter((String key)->key.indexOf(propertyName, 0)>0).count();
                paramCount++;
                paramName = propertyName +"_"+paramCount;
            }else{
                paramName = propertyName;

            }
            whereParams.put(paramName, restriction.getValue());
            builder.append(" :").append(paramName);

            if(where.hasEndBlock()){
                for(int i =0; i<where.endBlockCount(); i++) {
                    builder.append(")");
                }
            }
            if (!where.isLast()) {
                PredicateSpecification<T> predicate = where.getPredicate();
                PredicateOperation operation = predicate.getOperation();
                Optional<String> stringOperation = Optional.of(predicateMap.get(operation));
                builder.append(" ").append(stringOperation.get()).append(" ");
                where = where.getPredicate().getAfterWhere();
            } else {
                where = null;
            }
        } while (where != null);
        return builder.toString();
    }


    protected String buildOrderClause(FromSpecification<? extends T> from) {
        if (from.getOrder() == null || from.getOrder().isEmpty()) return "";

        List<? extends Order<? extends T>> orders = from.getOrder();
        StringBuilder builder = new StringBuilder(" ORDER BY");
        orders.forEach((Order<? extends T> order) -> {
            builder.append(" ");
            List<String> propertyNames = order.getPropertyNames();
            propertyNames = propertyNames.stream().map((String propertyName)->entityAlias+"."+propertyName).collect(Collectors.toList());
            builder.append(String.join(", ", propertyNames));
            builder.append(" ").append(order.getOrderType().toString());
        });

        return builder.toString();
    }
}
