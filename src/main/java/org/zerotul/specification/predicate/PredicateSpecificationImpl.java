package org.zerotul.specification.predicate;

import org.zerotul.specification.FromSpecification;
import org.zerotul.specification.recorder.Recorder;
import org.zerotul.specification.WhereSpecification;
import org.zerotul.specification.WhereSpecificationImpl;

import java.io.Serializable;

/**
 * Created by zerotul on 12.03.15.
 */
public class PredicateSpecificationImpl<T extends Serializable> implements PredicateSpecification<T> {

    private static final long serialVersionUID = 4035586674850127896L;

    private final WhereSpecification<T> beforeWhere;

    private WhereSpecification<T> afterWhere;

    private PredicateOperation operation;

    private final Recorder<T> recorder;

    public PredicateSpecificationImpl(WhereSpecification<T> where, Recorder<T> recorder) {
        this.beforeWhere = where;
        this.recorder = recorder;
    }

    @Override
    public WhereSpecification<T> predicate(PredicateOperation operation) {
        this.operation = operation;
        beforeWhere.setPredicate(this);
        afterWhere = new WhereSpecificationImpl<>(this.beforeWhere.getFrom(), this.recorder);
        return afterWhere;
    }

    @Override
    public PredicateOperation getOperation() {
        return operation;
    }

    @Override
    public FromSpecification<T> endWhere() {
        return this.beforeWhere.getFrom();
    }

    @Override
    public WhereSpecification<T> getBeforeWhere() {
        return beforeWhere;
    }

    @Override
    public WhereSpecification<T> getAfterWhere() {
        return afterWhere;
    }
}
