package de.unima.semweb.partialmatcher.core.strategies;

import org.semanticweb.owl.model.*;

import java.net.URI;
import java.util.Map;

import de.unima.semweb.partialmatcher.core.rewriters.OWLDescriptionRewritingStrategy;
import de.unima.semweb.partialmatcher.core.strategies.contexts.GenericOWLDescriptionRewritingContext;
import de.unima.semweb.partialmatcher.core.strategies.contexts.OWLPartialRequestTaxConceptDescriptionRewritingContext;

/**
 * User: nowi
 * Date: 19.04.2009
 * Time: 17:03:04
 */
public class OWLRequestTaxonomyConceptReplacingStrategy implements OWLDescriptionRewritingStrategy {

    private final OWLDataFactory owlDataFactory;

    public OWLRequestTaxonomyConceptReplacingStrategy(OWLDataFactory owlDataFactory) {
        this.owlDataFactory = owlDataFactory;
    }

    public OWLDescription rewrite(GenericOWLDescriptionRewritingContext context) {
        final OWLDescription original = context.getOwlDescription();

        // only rewrite if encountering the special RequestTaxonomyReplacingContext
        if (context instanceof OWLPartialRequestTaxConceptDescriptionRewritingContext) {
            // cast
            OWLPartialRequestTaxConceptDescriptionRewritingContext requestTaxConcept = (OWLPartialRequestTaxConceptDescriptionRewritingContext) context;


            final Map<URI, URI> replacementMap = requestTaxConcept.getReplacmentMap();

            if (original instanceof OWLNamedObject) {   // replace primitve
                OWLNamedObject namedObject = (OWLNamedObject) original;
                if (replacementMap.containsKey(namedObject.getURI()) && (namedObject instanceof OWLClass)) {
                    return getOwlDataFactory().getOWLClass(replacementMap.get(namedObject.getURI()));
                }

            }

        }

        return original;


    }


    public OWLDataFactory getOwlDataFactory() {
        return owlDataFactory;
    }
}
