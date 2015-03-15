package org.zerotul.specification;

import org.zerotul.specification.predicate.PredicateSpecification;
import org.zerotul.specification.predicate.PredicateSpecificationImpl;
import org.zerotul.specification.recorder.Recorder;
import org.zerotul.specification.restriction.Restriction;

import java.io.Serializable;

/**
 * Created by zerotul on 12.03.15.
 */
public class WhereSpecificationImpl<T extends Serializable> implements WhereSpecification<T>{

    private static final long serialVersionUID = 8026891595496221030L;

    private final FromSpecification<T> from;

    private Restriction restriction;

    private PredicateSpecification<T> predicate;

    private final Recorder<T> recorder;

    public WhereSpecificationImpl(FromSpecification<T> from, Recorder<T> recorder) {
        this.from = from;
        this.recorder = recorder;
    }

    @Override
    public PredicateSpecification<T> restriction(Restriction restriction) {
        if(this.restriction !=null) throw new IllegalStateException("the restriction clause can not be changed");
        this.restriction = restriction;
        this.restriction.setRecorder(this.recorder);
        return new PredicateSpecificationImpl<>(this, this.recorder);
    }

    @Override
    public FromSpecification<T> getFrom() {
        return from;
    }

    @Override
    public Restriction getRestriction() {
        return restriction;
    }

    @Override
    public PredicateSpecification<T> getPredicate() {
        return predicate;
    }

    public void setPredicate(PredicateSpecification<T> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean isLast() {
        return predicate==null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WhereSpecificationImpl that = (WhereSpecificationImpl) o;
        if (predicate != null ? !predicate.equals(that.predicate) : that.predicate != null) return false;
        if (restriction != null ? !restriction.equals(that.restriction) : that.restriction != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = restriction != null ? restriction.hashCode() : 0;
        result = 31 * result + (predicate != null ? predicate.hashCode() : 0);
        return result;
    }
}
