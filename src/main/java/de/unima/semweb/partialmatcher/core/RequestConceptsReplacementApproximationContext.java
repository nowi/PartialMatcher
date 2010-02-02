package de.unima.semweb.partialmatcher.core;

import org.semanticweb.owl.model.OWLClass;

import java.util.Map;
import java.net.URI;

/**
 * User: nowi
 * Date: 19.04.2009
 * Time: 16:30:54
 */
public class RequestConceptsReplacementApproximationContext extends ApproximationContext {
    private final Map<URI, URI> conceptReplacingMap;

    public RequestConceptsReplacementApproximationContext(OWLClass requestClass, NotSApproximationContext notSApproximationContext, CTASetApproximationContext ctaSetApproximationContext, Map<URI, URI> conceptReplacingMap) {
        super(requestClass, notSApproximationContext, ctaSetApproximationContext);
        this.conceptReplacingMap = conceptReplacingMap;
    }

    public RequestConceptsReplacementApproximationContext(ApproximationContext context) {
        super(context.requestClass, context.notSApproximationContext, context.ctaSetApproximationContext);
        // cast down
        if (context instanceof RequestConceptsReplacementApproximationContext) {
            this.conceptReplacingMap = ((RequestConceptsReplacementApproximationContext)context).conceptReplacingMap;

        } else {
            throw new IllegalArgumentException("Cannot copy construct a superclass context");
        }


    }

    @Override
    public String toString() {
        return "RequestConceptsReplacementApproximationContext{" +
                "conceptReplacingMap=" + conceptReplacingMap +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestConceptsReplacementApproximationContext)) return false;
        if (!super.equals(o)) return false;

        RequestConceptsReplacementApproximationContext that = (RequestConceptsReplacementApproximationContext) o;

        if (conceptReplacingMap != null ? !conceptReplacingMap.equals(that.conceptReplacingMap) : that.conceptReplacingMap != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (conceptReplacingMap != null ? conceptReplacingMap.hashCode() : 0);
        return result;
    }

    public Map<URI, URI> getConceptReplacingMap() {
        return conceptReplacingMap;
    }
}
