package org.zerotul.specification.test;

import org.testng.annotations.Test;
import org.zerotul.specification.predicate.PredicateOperation;
import org.zerotul.specification.exception.BuildException;
import org.zerotul.specification.expression.Expression;
import org.zerotul.specification.expression.SqlExpressionBuilder;
import org.zerotul.specification.test.mock.MockEntity;
import org.zerotul.specification.test.mock.MockEntitySqlMapper;

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

    @Test
    public void testBuildExpression() throws BuildException {
        String resultSql = "select field_1, field_2, field_3, field_4 from mock_entity " +
                "where field_1 = 'value1' and " +
                "field_2 <> 'value2' or " +
                "field_3 like '%value3%' and " +
                "field_4 = 0";
        Expression<MockEntity, String> expression = from(MockEntity.class)
                .where()
                 .restriction(equal(MockEntity::getField1, "value1"))
                .predicate(PredicateOperation.AND)
                 .restriction(notEqual(MockEntity::getField2, "value2"))
                .predicate(PredicateOperation.OR)
                 .restriction(like(MockEntity::getField3, "%value3%"))
                .predicate(PredicateOperation.AND)
                 .restriction(equal(MockEntity::getField4, "0"))
                .endWhere().endFrom().isSatisfied(new SqlExpressionBuilder<>(new MockEntitySqlMapper()));

        assertEquals(resultSql.toLowerCase().trim(), expression.toResult().toLowerCase().trim());

        resultSql = "select field_1, field_2, field_3, field_4 from mock_entity";
        expression = from(MockEntity.class).endFrom().isSatisfied(new SqlExpressionBuilder<>(new MockEntitySqlMapper()));
        assertEquals(resultSql.toLowerCase().trim(), expression.toResult().toLowerCase().trim());
    }

    @Test
    public void testOrderBuildExpression() throws BuildException {
        String resultSql = "select field_1, field_2, field_3, field_4 from mock_entity " +
                "where field_1 = 'value1' and " +
                "field_2 <> 'value2' order by field_1 desc";
        Expression<MockEntity, String> expression = from(MockEntity.class)
                .where()
                .restriction(equal(MockEntity::getField1, "value1"))
                .predicate(PredicateOperation.AND)
                .restriction(notEqual(MockEntity::getField2, "value2"))
                .endWhere()
                .order(desc(MockEntity::getField1))
                .endFrom().isSatisfied(new SqlExpressionBuilder<>(new MockEntitySqlMapper()));

        assertEquals(resultSql.toLowerCase().trim(), expression.toResult().toLowerCase().trim());

        resultSql = "select field_1, field_2, field_3, field_4 from mock_entity " +
                "where field_1 = 'value1' and " +
                "field_2 <> 'value2' order by field_1 asc";

        expression = from(MockEntity.class)
                .where()
                .restriction(equal(MockEntity::getField1, "value1"))
                .predicate(PredicateOperation.AND)
                .restriction(notEqual(MockEntity::getField2, "value2"))
                .endWhere()
                .order(asc(MockEntity::getField1))
                .endFrom().isSatisfied(new SqlExpressionBuilder<>(new MockEntitySqlMapper()));

        assertEquals(resultSql.toLowerCase().trim(), expression.toResult().toLowerCase().trim());

        resultSql = "select field_1, field_2, field_3, field_4 from mock_entity order by field_1 desc";
        expression = from(MockEntity.class)
                .order(desc(MockEntity::getField1))
                .endFrom().isSatisfied(new SqlExpressionBuilder<>(new MockEntitySqlMapper()));
        assertEquals(resultSql.toLowerCase().trim(), expression.toResult().toLowerCase().trim());

        resultSql = "select field_1, field_2, field_3, field_4 from mock_entity order by field_1 asc";
        expression = from(MockEntity.class)
                .order(asc(MockEntity::getField1))
                .endFrom().isSatisfied(new SqlExpressionBuilder<>(new MockEntitySqlMapper()));
        assertEquals(resultSql.toLowerCase().trim(), expression.toResult().toLowerCase().trim());

        resultSql = "select field_1, field_2, field_3, field_4 from mock_entity order by field_1, field_2 asc";
        expression = from(MockEntity.class)
                .order(asc(MockEntity::getField1, MockEntity::getField2))
                .endFrom().isSatisfied(new SqlExpressionBuilder<>(new MockEntitySqlMapper()));
        assertEquals(resultSql.toLowerCase().trim(), expression.toResult().toLowerCase().trim());
    }


}
