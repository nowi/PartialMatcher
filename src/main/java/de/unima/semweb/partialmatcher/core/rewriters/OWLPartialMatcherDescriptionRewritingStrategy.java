package de.unima.semweb.partialmatcher.core.rewriters;

import org.semanticweb.owl.model.*;
import de.unima.semweb.partialmatcher.core.strategies.contexts.GenericOWLDescriptionRewritingContext;
import de.unima.semweb.partialmatcher.core.strategies.contexts.PartialDescriptionRewritingApproximationTypeContext;
import de.unima.semweb.partialmatcher.core.ApproximationType;

import java.net.URI;
import java.util.Set;

/**
 * User: nowi
 * Date: 14.07.2009
 * Time: 10:32:48
 */
public class OWLPartialMatcherDescriptionRewritingStrategy implements OWLDescriptionRewritingStrategy {
    private final OWLDataFactory owlDataFactory;

    private final int max;

    public OWLPartialMatcherDescriptionRewritingStrategy(int max,OWLDataFactory owlDataFactory) {
        this.max = max;
        this.owlDataFactory = owlDataFactory;
    }

    public OWLDescription rewrite(GenericOWLDescriptionRewritingContext context) {
        if(! (context instanceof PartialDescriptionRewritingApproximationTypeContext)){
            throw new IllegalArgumentException("Supplied Rewriting context has to be of Type OWLPartialMatcherRewritingContext");
        }

        final OWLDescription original = context.getOwlDescription();
        final ApproximationType approximationType = ((PartialDescriptionRewritingApproximationTypeContext)context).getApproximationType();
        final Set<URI> notS = ((PartialDescriptionRewritingApproximationTypeContext)context).getNotS();

        //Following defines what kind of approximation has which cause in changing the qualified number restriction
        int minRel, maxRel;
        if (approximationType == ApproximationType.LOWER) {
            minRel = max;
            maxRel = 0;
        } else { // UPPER APPROXIMATION
            minRel = 0;
            maxRel = max;
        }
        // check if the supplied term needs to be replaced, if so return the replacment , else return null
        if (original instanceof OWLNamedObject) {   // replace primitve
            OWLNamedObject namedObject = (OWLNamedObject) original;
            if (notS.contains(namedObject.getURI()) && (namedObject instanceof OWLClass)) {
                if (approximationType == ApproximationType.LOWER)
                    return getOwlDataFactory().getOWLNothing();
                else //if (approxtype == APPROXTYPE_UPPER)
                    return getOwlDataFactory().getOWLThing();
            } else return original;
        } else if (original instanceof OWLObjectMinCardinalityRestriction) { // replace QNRs
            OWLObjectMinCardinalityRestriction minR = (OWLObjectMinCardinalityRestriction) original;
            OWLObjectProperty relation = minR.getProperty().asOWLObjectProperty();
            if (notS.contains(relation.getURI())) {
                return getOwlDataFactory().getOWLObjectMinCardinalityRestriction(minR.getProperty(), minRel, minR.getFiller());
            } else return original;

        } else if (original instanceof OWLObjectMaxCardinalityRestriction) {
            OWLObjectMaxCardinalityRestriction maxR = (OWLObjectMaxCardinalityRestriction) original;
            OWLObjectProperty relation = maxR.getProperty().asOWLObjectProperty();
            if (notS.contains(relation.getURI())) {
                return owlDataFactory.getOWLObjectMaxCardinalityRestriction(maxR.getProperty(), maxRel, maxR.getFiller());
            } else return original;

        } else if (original instanceof OWLDataMinCardinalityRestriction) { // replace QNRs
            OWLDataMinCardinalityRestriction minR = (OWLDataMinCardinalityRestriction) original;
            OWLDataProperty relation = minR.getProperty().asOWLDataProperty();
            if (notS.contains(relation.getURI())) {
                return getOwlDataFactory().getOWLDataMinCardinalityRestriction(minR.getProperty(), minRel, minR.getFiller());
            } else return original;

        } else if (original instanceof OWLDataMaxCardinalityRestriction) {
            OWLDataMaxCardinalityRestriction maxR = (OWLDataMaxCardinalityRestriction) original;
            OWLDataProperty relation = maxR.getProperty().asOWLDataProperty();
            if (notS.contains(relation.getURI())) {
                return owlDataFactory.getOWLDataMaxCardinalityRestriction(maxR.getProperty(), maxRel, maxR.getFiller());
            } else return original;

        } else if (original instanceof OWLObjectAllRestriction) {
            OWLObjectAllRestriction allR = (OWLObjectAllRestriction) original;
            final OWLDescription negatedFiller = owlDataFactory.getOWLObjectComplementOf(allR.getFiller());
            final OWLObjectPropertyExpression propertyExp = allR.getProperty();
            return owlDataFactory.getOWLObjectMaxCardinalityRestriction(propertyExp, 0, negatedFiller);

        } else if (original instanceof OWLObjectSomeRestriction) {
            OWLObjectSomeRestriction someR = (OWLObjectSomeRestriction) original;
            return owlDataFactory.getOWLObjectMinCardinalityRestriction((someR.getProperty()), 1, someR.getFiller());
        } else {
            // unknown OWLTYPE
            System.out.println("Unknown OWLType .. no rewriting");
            return original;
        }
    }


    public OWLDataFactory getOwlDataFactory() {
        return owlDataFactory;
    }

}
