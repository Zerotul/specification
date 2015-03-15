package org.zerotul.specification.test;

import org.testng.annotations.Test;
import org.zerotul.specification.Specification;
import org.zerotul.specification.predicate.PredicateOperation;
import org.zerotul.specification.test.mock.MockEntity;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.zerotul.specification.Specifications.from;
import static org.zerotul.specification.order.Orders.desc;
import static org.zerotul.specification.restriction.Restrictions.equal;
import static org.zerotul.specification.restriction.Restrictions.like;
import static org.zerotul.specification.restriction.Restrictions.notEqual;

/**
 * Created by zerotul on 15.03.15.
 */
public class SpecificationTest {

    @Test
    public void testEquals(){
        MockEntity mockEntity = new MockEntity();
        mockEntity.setId("3333");

        Specification<MockEntity> specification1 = from(MockEntity.class)
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
                .endWhere().order(desc(MockEntity::getField2)).endFrom();

        Specification<MockEntity> specification2 = from(MockEntity.class)
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
                .endWhere().order(desc(MockEntity::getField2)).endFrom();

        assertEquals(specification1, specification2);
    }

    @Test
    public void testHashCode(){
        MockEntity mockEntity = new MockEntity();
        mockEntity.setId("3333");

        Specification<MockEntity> specification1 = from(MockEntity.class)
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
                .endWhere().order(desc(MockEntity::getField2)).endFrom();

        Specification<MockEntity> specification2 = from(MockEntity.class)
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
                .endWhere().order(desc(MockEntity::getField2)).endFrom();

        assertTrue(specification1.hashCode() == specification2.hashCode());
    }
}
