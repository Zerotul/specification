package org.zerotul.specification.test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.zerotul.specification.expression.sql.Query;
import org.zerotul.specification.predicate.PredicateOperation;
import org.zerotul.specification.exception.BuildException;
import org.zerotul.specification.expression.Expression;
import org.zerotul.specification.expression.sql.SqlExpressionBuilder;
import org.zerotul.specification.test.mock.MockEntity;
import org.zerotul.specification.test.mock.MockEntitySqlMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;
import static org.zerotul.specification.Specifications.from;
import static org.zerotul.specification.order.Orders.asc;
import static org.zerotul.specification.order.Orders.desc;
import static org.zerotul.specification.restriction.Restrictions.equal;
import static org.zerotul.specification.restriction.Restrictions.like;
import static org.zerotul.specification.restriction.Restrictions.notEqual;

/**
 * Created by zerotul on 12.03.15.
 */

public class SqlExpressionBuilderTest {

    private final String FROM_CLAUSE = "select field_1, field_2, field_3, field_4, id, mock_id from mock_entity";

    @Mock
    Connection mockConnection;
    @Mock
    PreparedStatement mockStatement;
    @Mock
    ResultSet mockResults;

    @BeforeTest
    public void before() {
        initMocks(this);
        Mockito.reset(mockConnection);
        Mockito.reset(mockStatement);
        Mockito.reset(mockResults);
    }

    @Test
    public void testBuildExpression() throws BuildException, SQLException {
        String resultSql = FROM_CLAUSE+
                " where field_1 = ? and " +
                "field_2 <> ? or " +
                "field_3 like ? and " +
                "field_4 = ? LIMIT 4 OFFSET 0";
        MockEntitySqlMapper<MockEntity> mockMapper = new MockEntitySqlMapper(MockEntity.class);
        mockMapper.init();
        Expression<MockEntity, Query> expression = from(MockEntity.class)
                .where()
                 .restriction(equal(MockEntity::getField1, "value1"))
                .predicate(PredicateOperation.AND)
                 .restriction(notEqual(MockEntity::getField2, "value2"))
                .predicate(PredicateOperation.OR)
                 .restriction(like(MockEntity::getField3, "%value3%"))
                .predicate(PredicateOperation.AND)
                 .restriction(equal(MockEntity::getField4, 0))
                .endWhere().max(4).offset(0).endFrom().isSatisfied(new SqlExpressionBuilder<>(mockMapper));

        Query query = expression.toResult();
        assertEquals(resultSql.toLowerCase().trim(), query.getQuery().toLowerCase().trim());
        assertEquals("value1", query.getParams().get(0));
        assertEquals("value2", query.getParams().get(1));
        assertEquals("%value3%", query.getParams().get(2));
        assertEquals(0, query.getParams().get(3));

        resultSql = FROM_CLAUSE;
        expression = from(MockEntity.class).endFrom().isSatisfied(new SqlExpressionBuilder<>(mockMapper));
        assertEquals(resultSql.toLowerCase().trim(), expression.toResult().getQuery().toLowerCase().trim());

        resultSql = FROM_CLAUSE+
                " where field_1 = ? and " +
                "field_2 <> ? or " +
                "field_3 like ? and " +
                "field_4 = ? AND mock_id = ?";

        MockEntity mockEntity = new MockEntity();
        mockEntity.setId("3333");
        expression = from(MockEntity.class)
                .where()
                .restriction(equal(MockEntity::getField1, "value1"))
                .predicate(PredicateOperation.AND)
                .restriction(notEqual(MockEntity::getField2, "value2"))
                .predicate(PredicateOperation.OR)
                .restriction(like(MockEntity::getField3, "%value3%"))
                .predicate(PredicateOperation.AND)
                .restriction(equal(MockEntity::getField4, 0))
                .predicate(PredicateOperation.AND)
                .restriction(equal(MockEntity::getMock, mockEntity))
                .endWhere().endFrom().isSatisfied(new SqlExpressionBuilder<>(mockMapper));

        query = expression.toResult();

        assertEquals(resultSql.toLowerCase().trim(), query.getQuery().toLowerCase().trim());
        assertEquals(resultSql.toLowerCase().trim(), query.getQuery().toLowerCase().trim());
        assertEquals("value1", query.getParams().get(0));
        assertEquals("value2", query.getParams().get(1));
        assertEquals("%value3%", query.getParams().get(2));
        assertEquals(0, query.getParams().get(3));
        assertEquals("3333", query.getParams().get(4));
    }

    @Test
    public void testOrderBuildExpression() throws BuildException {
        String resultSql = FROM_CLAUSE +
                " where field_1 = ? and " +
                "field_2 <> ? order by field_1 desc";
        MockEntitySqlMapper<MockEntity> mockMapper = new MockEntitySqlMapper(MockEntity.class);
        mockMapper.init();
        Expression<MockEntity, Query> expression = from(MockEntity.class)
                .where()
                .restriction(equal(MockEntity::getField1, "value1"))
                .predicate(PredicateOperation.AND)
                .restriction(notEqual(MockEntity::getField2, "value2"))
                .endWhere()
                .order(desc(MockEntity::getField1))
                .endFrom().isSatisfied(new SqlExpressionBuilder<>(mockMapper));

        assertEquals(resultSql.toLowerCase().trim(), expression.toResult().getQuery().toLowerCase().trim());

        resultSql = FROM_CLAUSE +
                " where field_1 = ? and " +
                "field_2 <> ? order by field_1 asc";

        expression = from(MockEntity.class)
                .where()
                .restriction(equal(MockEntity::getField1, "value1"))
                .predicate(PredicateOperation.AND)
                .restriction(notEqual(MockEntity::getField2, "value2"))
                .endWhere()
                .order(asc(MockEntity::getField1))
                .endFrom().isSatisfied(new SqlExpressionBuilder<>(mockMapper));

        assertEquals(resultSql.toLowerCase().trim(), expression.toResult().getQuery().toLowerCase().trim());

        resultSql = FROM_CLAUSE+" order by field_1 desc";
        expression = from(MockEntity.class)
                .order(desc(MockEntity::getField1))
                .endFrom().isSatisfied(new SqlExpressionBuilder<>(mockMapper));
        assertEquals(resultSql.toLowerCase().trim(), expression.toResult().getQuery().toLowerCase().trim());

        resultSql = FROM_CLAUSE+" order by field_1 asc";
        expression = from(MockEntity.class)
                .order(asc(MockEntity::getField1))
                .endFrom().isSatisfied(new SqlExpressionBuilder<>(mockMapper));
        assertEquals(resultSql.toLowerCase().trim(), expression.toResult().getQuery().toLowerCase().trim());

        resultSql = FROM_CLAUSE+" order by field_1, field_2 asc";
        expression = from(MockEntity.class)
                .order(asc(MockEntity::getField1, MockEntity::getField2))
                .endFrom().isSatisfied(new SqlExpressionBuilder<>(mockMapper));
        assertEquals(resultSql.toLowerCase().trim(), expression.toResult().getQuery().toLowerCase().trim());
    }



}
