package org.zerotul.specification.predicate;

import org.zerotul.specification.FromSpecification;
import org.zerotul.specification.WhereSpecification;

import java.io.Serializable;

/**
 * Created by zerotul on 12.03.15.
 */
public interface PredicateSpecification<T extends Serializable> extends Serializable {

      public WhereSpecification<T> predicate(PredicateOperation operation);

      public PredicateOperation getOperation();

      public FromSpecification<T> endWhere();

      public WhereSpecification<T> getBeforeWhere();

      public WhereSpecification<T> getAfterWhere();
}
