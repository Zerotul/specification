package org.zerotul.specification.expression.jpa;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.zerotul.specification.exception.BuildException;
import org.zerotul.specification.expression.Expression;
import org.zerotul.specification.expression.ExpressionBuilder;
import org.zerotul.specification.expression.sql.SqlExpressionBuilder;
import org.zerotul.specification.predicate.PredicateOperation;
import org.zerotul.specification.test.mock.MockEntity;
import org.zerotul.specification.test.mock.MockEntitySqlMapper;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.*;
import static org.zerotul.specification.Specifications.from;
import static org.zerotul.specification.order.Orders.asc;
import static org.zerotul.specification.order.Orders.desc;
import static org.zerotul.specification.restriction.Restrictions.*;

/**
 * Created by zerotul.
 */
public class JPQLExpressionBuilderTest {

    public static final String FROM_CLAUSE = "SELECT mockentity_ FROM MockEntity";

    @Mock
    EntityManager entityManager;

    @BeforeTest
    public void before() {
        initMocks(this);
        reset(entityManager);
    }

    @Test
    public void testBuildExpression() throws Exception {
        Query resultQuery = mock(Query.class);
        String expectedQuery = FROM_CLAUSE + " WHERE mockentity_.field1 = :field1 AND mockentity_.field2 <> :field2";
        when(entityManager.createQuery(expectedQuery)).thenReturn(resultQuery);

        Expression<Query> expression = from(MockEntity.class)
                .where()
                .restriction(equal(MockEntity::getField1, "value1"))
                .predicate(PredicateOperation.AND)
                .restriction(notEqual(MockEntity::getField2, "value2")).endWhere().endFrom().isSatisfied(new JPQLExpressionBuilder<>(entityManager));

        if (expression.toResult() == null) fail();

        verify(resultQuery).setParameter("field1", "value1");
        verify(resultQuery).setParameter("field2", "value2");



        reset(entityManager);
        reset(resultQuery);
        expectedQuery = FROM_CLAUSE + " WHERE mockentity_.field1 = :field1 AND mockentity_.field2 <> :field2 OR mockentity_.field3 > :field3 AND mockentity_.field4 LIKE :field4";
        when(entityManager.createQuery(expectedQuery)).thenReturn(resultQuery);

         expression = from(MockEntity.class)
                .where()
                .restriction(equal(MockEntity::getField1, "value1"))
                .predicate(PredicateOperation.AND)
                .restriction(notEqual(MockEntity::getField2, "value2"))
                .predicate(PredicateOperation.OR)
                .restriction(qt(MockEntity::getField3, "value3"))
                .predicate(PredicateOperation.AND)
                .restriction(like(MockEntity::getField4, "%value4")).endWhere().endFrom().isSatisfied(new JPQLExpressionBuilder<>(entityManager));

        if (expression.toResult() == null) fail();

        verify(resultQuery).setParameter("field1", "value1");
        verify(resultQuery).setParameter("field2", "value2");
        verify(resultQuery).setParameter("field3", "value3");
        verify(resultQuery).setParameter("field4", "%value4");
    }

    @Test
    public void testOrderBuildExpression() throws BuildException {
        Query resultQuery = mock(Query.class);
        String expectedQuery = FROM_CLAUSE +
                " WHERE mockentity_.field1 = :field1 AND " +
                "mockentity_.field2 <> :field2 ORDER BY mockentity_.field1 DESC";

        when(entityManager.createQuery(expectedQuery)).thenReturn(resultQuery);
        Expression<Query> expression = from(MockEntity.class)
                .where()
                .restriction(equal(MockEntity::getField1, "value1"))
                .predicate(PredicateOperation.AND)
                .restriction(notEqual(MockEntity::getField2, "value2"))
                .endWhere()
                .order(desc(MockEntity::getField1))
                .endFrom().isSatisfied(new JPQLExpressionBuilder<>(entityManager));

        if(expression.toResult() == null) fail();

        expectedQuery = FROM_CLAUSE +
                " WHERE mockentity_.field1 = :field1 AND " +
                "mockentity_.field2 <> :field2 ORDER BY mockentity_.field1 ASC";

        reset(entityManager);
        reset(resultQuery);
        when(entityManager.createQuery(expectedQuery)).thenReturn(resultQuery);

        expression = from(MockEntity.class)
                .where()
                .restriction(equal(MockEntity::getField1, "value1"))
                .predicate(PredicateOperation.AND)
                .restriction(notEqual(MockEntity::getField2, "value2"))
                .endWhere()
                .order(asc(MockEntity::getField1))
                .endFrom().isSatisfied(new JPQLExpressionBuilder<>(entityManager));

        if(expression.toResult() == null) fail();

        expectedQuery = FROM_CLAUSE+" ORDER BY mockentity_.field1 DESC";

        reset(entityManager);
        reset(resultQuery);
        when(entityManager.createQuery(expectedQuery)).thenReturn(resultQuery);
        expression = from(MockEntity.class)
                .order(desc(MockEntity::getField1))
                .endFrom().isSatisfied(new JPQLExpressionBuilder<>(entityManager));

        if(expression.toResult() == null) fail();


        expectedQuery = FROM_CLAUSE+" ORDER BY mockentity_.field1 ASC";
        reset(entityManager);
        reset(resultQuery);
        when(entityManager.createQuery(expectedQuery)).thenReturn(resultQuery);

        expression = from(MockEntity.class)
                .order(asc(MockEntity::getField1))
                .endFrom().isSatisfied(new JPQLExpressionBuilder<>(entityManager));

        if(expression.toResult() == null) fail();

        expectedQuery = FROM_CLAUSE+" ORDER BY mockentity_.field1, mockentity_.field2 ASC mockentity_.field3 DESC";

        reset(entityManager);
        reset(resultQuery);
        when(entityManager.createQuery(expectedQuery)).thenReturn(resultQuery);

        expression = from(MockEntity.class)
                .order(asc(MockEntity::getField1, MockEntity::getField2))
                .order(desc(MockEntity::getField3))
                .endFrom().isSatisfied(new JPQLExpressionBuilder<>(entityManager));
        if(expression.toResult() == null) fail();
    }

    @Test
    public void testBlockBuildExpression() throws BuildException {
        Query resultQuery = mock(Query.class);
        String expectedQuery = FROM_CLAUSE +
                " WHERE (mockentity_.field1 = :field1 AND " +
                "(mockentity_.field2 <> :field2 AND mockentity_.field3 = :field3)) OR (mockentity_.field4 = :field4)";

        when(entityManager.createQuery(expectedQuery)).thenReturn(resultQuery);

        Expression<Query> expression = from(MockEntity.class)
                .where()
                .startBlock()
                .restriction(equal(MockEntity::getField1, "value1"))
                .predicate(PredicateOperation.AND)
                .startBlock()
                .restriction(notEqual(MockEntity::getField2, "value2"))
                .predicate(PredicateOperation.AND)
                .restriction(equal(MockEntity::getField3, "value3"))
                .endBlock()
                .endBlock()
                .predicate(PredicateOperation.OR)
                .startBlock()
                .restriction(equal(MockEntity::getField4, "value4"))
                .endBlock().endWhere().endFrom().isSatisfied(new JPQLExpressionBuilder<>(entityManager));

        if(expression.toResult() == null) fail();

        expectedQuery = FROM_CLAUSE +
                " WHERE ((mockentity_.field1 = :field1 OR mockentity_.field2 = :field2) AND " +
                "(mockentity_.field2 <> :field2_1 AND mockentity_.field3 = :field3)) OR (mockentity_.field4 = :field4)";

        reset(entityManager);
        reset(resultQuery);
        when(entityManager.createQuery(expectedQuery)).thenReturn(resultQuery);

        expression = from(MockEntity.class)
                .where()
                .startBlock()
                .startBlock()
                .restriction(equal(MockEntity::getField1, "value1"))
                .predicate(PredicateOperation.OR)
                .restriction(equal(MockEntity::getField2, "value2"))
                .endBlock()
                .predicate(PredicateOperation.AND)
                .startBlock()
                .restriction(notEqual(MockEntity::getField2, "value2"))
                .predicate(PredicateOperation.AND)
                .restriction(equal(MockEntity::getField3, "value3"))
                .endBlock()
                .endBlock()
                .predicate(PredicateOperation.OR)
                .startBlock()
                .restriction(equal(MockEntity::getField4, "value4"))
                .endBlock().endWhere().endFrom().isSatisfied(new JPQLExpressionBuilder<>(entityManager));

        if(expression.toResult() == null) fail();
    }
}