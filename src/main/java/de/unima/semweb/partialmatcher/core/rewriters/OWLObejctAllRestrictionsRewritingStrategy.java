package de.unima.semweb.partialmatcher.core.rewriters;

import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLObjectAllRestriction;
import org.semanticweb.owl.model.OWLObjectProperty;

import java.net.URI;
import java.util.Set;

import de.unima.semweb.partialmatcher.core.strategies.contexts.GenericOWLDescriptionRewritingContext;
import de.unima.semweb.partialmatcher.core.strategies.contexts.OWLPartialReplacmentContext;

/**
 * User: nowi
 * Date: 20.11.2008
 * Time: 21:35:01
 */
public class OWLObejctAllRestrictionsRewritingStrategy implements OWLDescriptionRewritingStrategy{
    private final OWLDataFactory owlDataFactory;


    public OWLObejctAllRestrictionsRewritingStrategy(OWLDataFactory owlDataFactory) {

        this.owlDataFactory = owlDataFactory;
    }

    public OWLDescription rewrite(GenericOWLDescriptionRewritingContext context) {

        final OWLDescription original = context.getOwlDescription();
        if (context instanceof OWLPartialReplacmentContext) {
            // cast
            OWLPartialReplacmentContext partialReplacmentContext = (OWLPartialReplacmentContext) context;
            final Set<URI> notS = partialReplacmentContext.getNotS();

            if (original instanceof OWLObjectAllRestriction) { // replace QNRs
                OWLObjectAllRestriction allRestriction = (OWLObjectAllRestriction) original;
                OWLObjectProperty relation = allRestriction.getProperty().asOWLObjectProperty();
                if (notS.contains(relation.getURI())) {
                    return getOwlDataFactory().getOWLThing();
                }
            }
        } else {
            System.out.println(this + " does not handle context " + context);
        }


        return original;

    }


    public OWLDataFactory getOwlDataFactory() {
        return owlDataFactory;
    }

}
