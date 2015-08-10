package org.zerotul.specification;

import org.zerotul.specification.predicate.PredicateSpecification;
import org.zerotul.specification.restriction.Restriction;

import java.io.Serializable;

/**
 * Created by zerotul on 12.03.15.
 */
public interface WhereSpecification<T extends Serializable> extends Serializable{

    public <R> PredicateSpecification<T> restriction(Restriction<T, R> restriction);

    public FromSpecification<T> getFrom();

    public Restriction getRestriction();

    public PredicateSpecification<T> getPredicate();

    public void setPredicate(PredicateSpecification<T> predicate);

    public boolean isLast();

    public WhereSpecification<T> startBlock();

    public int startBlockCount();

    public int endBlockCount();

    boolean hasStartBlock();

    boolean hasEndBlock();

    WhereSpecification<T> endBlock();
}
