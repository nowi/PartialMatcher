package de.unima.semweb.partialmatcher.core;

import org.semanticweb.owl.model.OWLClass;

/**
 * User: nowi
 * Date: 08.04.2009
 * Time: 14:50:08
 */
public class ApproximationContext {

    final NotSApproximationContext notSApproximationContext;

    final CTASetApproximationContext ctaSetApproximationContext;

    


    final OWLClass requestClass;

    public ApproximationContext(OWLClass requestClass, NotSApproximationContext notSApproximationContext, CTASetApproximationContext ctaSetApproximationContext) {
        if (notSApproximationContext != null) {
            if (!requestClass.equals(notSApproximationContext.getRequestClass())) {
                throw new IllegalArgumentException("The requestclasses of the ApproximationContext and the supplied notSApproximationContext do not match");
            }
        }

        if (ctaSetApproximationContext != null) {
            if (!requestClass.equals(ctaSetApproximationContext.getRequestClass())) {
                throw new IllegalArgumentException("The requestclasses of the ApproximationContext and the supplied ctasetcontext not do not match");
            }
        }

        this.requestClass = requestClass;
        this.notSApproximationContext = notSApproximationContext;
        this.ctaSetApproximationContext = ctaSetApproximationContext;
    }

    public ApproximationContext(ApproximationContext context) {
        this.requestClass = context.requestClass;
        this.notSApproximationContext = context.notSApproximationContext;
        this.ctaSetApproximationContext = context.ctaSetApproximationContext;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApproximationContext)) return false;

        ApproximationContext that = (ApproximationContext) o;

        if (ctaSetApproximationContext != null ? !ctaSetApproximationContext.equals(that.ctaSetApproximationContext) : that.ctaSetApproximationContext != null)
            return false;
        if (notSApproximationContext != null ? !notSApproximationContext.equals(that.notSApproximationContext) : that.notSApproximationContext != null)
            return false;
        if (requestClass != null ? !requestClass.equals(that.requestClass) : that.requestClass != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = notSApproximationContext != null ? notSApproximationContext.hashCode() : 0;
        result = 31 * result + (ctaSetApproximationContext != null ? ctaSetApproximationContext.hashCode() : 0);
        result = 31 * result + (requestClass != null ? requestClass.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ApproximationContext{" +
                "\nnotSApproximationContext=" + notSApproximationContext +
                "\n, ctaSetApproximationContext=" + ctaSetApproximationContext +
                "\n, requestClass=" + requestClass +
                '}';
    }

    public NotSApproximationContext getNotSApproximationContext() {
        return notSApproximationContext;
    }

    public CTASetApproximationContext getCtaSetApproximationContext() {
        return ctaSetApproximationContext;
    }

    public OWLClass getRequestClass() {
        return requestClass;
    }
}
