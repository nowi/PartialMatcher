package de.unima.semweb.partialmatcher.core.strategies.contexts;

import org.semanticweb.owl.model.OWLDescription;

import java.net.URI;
import java.util.Map;

/**
 * User: nowi
 * Date: 19.04.2009
 * Time: 16:59:21
 */
public class OWLPartialRequestTaxConceptDescriptionRewritingContext extends GenericOWLDescriptionRewritingContext {
    private Map<URI, URI> replacmentMap;


    public OWLPartialRequestTaxConceptDescriptionRewritingContext(OWLDescription owlDescription, Map<URI, URI> replacmentMap) {
        super(owlDescription);
        this.replacmentMap = replacmentMap;
    }

    public Map<URI, URI> getReplacmentMap() {
        return replacmentMap;
    }

    @Override
    public OWLPartialRequestTaxConceptDescriptionRewritingContext copy() {
        return new OWLPartialRequestTaxConceptDescriptionRewritingContext(owlDescription, replacmentMap);
    }

    @Override
    public OWLPartialRequestTaxConceptDescriptionRewritingContext copy(OWLDescription newOwlDescription) {
        return new OWLPartialRequestTaxConceptDescriptionRewritingContext(newOwlDescription, replacmentMap);
    }
}


