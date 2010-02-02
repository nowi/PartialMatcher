package de.unima.semweb.partialmatcher.core;

import org.semanticweb.owl.model.OWLClass;

import java.util.Set;
import java.util.Date;

/**
 * User: nowi
 * Date: 20.04.2009
 * Time: 16:27:23
 */
public class MatchingResult {
    final ApproximationContext approximationContext;

    String createdBy;

    @Override
    public String toString() {
        return "MatchingResult{" +
                "approximationContext=" + approximationContext +
                ", matchingOWLClasses=" + matchingOWLClasses +
                ", createdAt=" + createdAt +
                ", createdBy=" + createdBy +
                '}';
    }

    protected MatchingResult(ApproximationContext approximationContext, Set<OWLClass> matchingOWLClasses) {
        this.approximationContext = approximationContext;
        this.matchingOWLClasses = matchingOWLClasses;
    }

    public static MatchingResult createMatchingResult(ApproximationContext approximationContext, Set<OWLClass> results) {
        return new MatchingResult(approximationContext, results);
    }

    public ApproximationContext getApproximationContext() {
        return approximationContext;
    }

    public Set<OWLClass> getMatchingOWLClasses() {
        return matchingOWLClasses;
    }

    final Set<OWLClass> matchingOWLClasses;

    Date createdAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
