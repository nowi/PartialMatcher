package de.unima.semweb.partialmatcher.core.rewriters;

import org.semanticweb.owl.model.*;

import java.net.URI;
import java.util.Set;

import de.unima.semweb.partialmatcher.core.strategies.contexts.PartialDescriptionRewritingApproximationTypeContext;
import de.unima.semweb.partialmatcher.core.strategies.contexts.GenericOWLDescriptionRewritingContext;

/**
 * Rewriting strategy used for the eclassowl data , replaces Data Properties with top concept
 */
public class OWLDataValueRestrictionsRewritingStrategy implements OWLDescriptionRewritingStrategy {
    private final OWLDataFactory owlDataFactory;

    public OWLDataValueRestrictionsRewritingStrategy(OWLDataFactory owlDataFactory) {
        this.owlDataFactory = owlDataFactory;
    }

    public OWLDescription rewrite(GenericOWLDescriptionRewritingContext context) {
        final OWLDescription original = context.getOwlDescription();

        if (context instanceof PartialDescriptionRewritingApproximationTypeContext) {
            // cast down
            PartialDescriptionRewritingApproximationTypeContext partialReplacementContext = (PartialDescriptionRewritingApproximationTypeContext) context;
            final Set<URI> notS = partialReplacementContext.getNotS();

            if (original instanceof OWLDataValueRestriction) { // replace QNRs
                OWLDataValueRestriction valueR = (OWLDataValueRestriction) original;

                OWLDataProperty relation = valueR.getProperty().asOWLDataProperty();
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
