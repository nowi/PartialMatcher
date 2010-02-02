package de.unima.semweb.partialmatcher.core;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLOntology;

import java.util.List;

/**
 * User: nowi
 * Date: 08.04.2009
 * Time: 14:36:49
 */
public class CTASetApproximationContext {
    private final OWLClass requestClass;

    @Override
    public String toString() {
        return "CTASetApproximationContext{" +
                "requestClass=" + requestClass +
                ", classesToApproximate=" + classesToApproximate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CTASetApproximationContext)) return false;

        CTASetApproximationContext that = (CTASetApproximationContext) o;

        if (classesToApproximate != null ? !classesToApproximate.equals(that.classesToApproximate) : that.classesToApproximate != null)
            return false;
        if (requestClass != null ? !requestClass.equals(that.requestClass) : that.requestClass != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = requestClass != null ? requestClass.hashCode() : 0;
        result = 31 * result + (classesToApproximate != null ? classesToApproximate.hashCode() : 0);
        return result;
    }

    public OWLClass getRequestClass() {

        return requestClass;
    }


    public List<OWLClass> getClassesToApproximate() {
        return classesToApproximate;
    }

    private final List<OWLClass> classesToApproximate;

    public CTASetApproximationContext(OWLClass requestClass, List<OWLClass> classesToApproximate) {
        this.requestClass = requestClass;
        this.classesToApproximate = classesToApproximate;
    }

    public CTASetApproximationContext(CTASetApproximationContext context) {
        this.requestClass = context.requestClass;
        this.classesToApproximate = context.classesToApproximate;
    }


    

    
}
