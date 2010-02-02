package de.unima.semweb.partialmatcher.core.strategies.contexts;

import org.semanticweb.owl.model.OWLDescription;

/**
 * User: nowi
 * Date: 19.04.2009
 * Time: 16:48:18
 */
public class RequestOWLConceptsReplacementContext {
    protected final OWLDescription owlDescription;

    public RequestOWLConceptsReplacementContext(OWLDescription owlDescription) {
        this.owlDescription = owlDescription;
    }

    public OWLDescription getOwlDescription() {
        return owlDescription;
    }


    public RequestOWLConceptsReplacementContext copy() {
        return new RequestOWLConceptsReplacementContext(owlDescription);
    }

    public RequestOWLConceptsReplacementContext copy(OWLDescription newOwlDescription) {
        return new RequestOWLConceptsReplacementContext(newOwlDescription);
    }
}
