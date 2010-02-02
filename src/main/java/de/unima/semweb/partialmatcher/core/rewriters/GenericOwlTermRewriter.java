package de.unima.semweb.partialmatcher.core.rewriters;

import org.semanticweb.owl.model.*;

import java.util.HashSet;
import java.util.Set;

import de.unima.semweb.partialmatcher.core.strategies.contexts.GenericOWLDescriptionRewritingContext;
import de.unima.semweb.partialmatcher.core.rewriters.OWLDescriptionRewritingStrategy;
import de.unima.semweb.partialmatcher.core.IOWLTermRewriter;

/**
 * User: nowi
 * Date: 21.11.2008
 * Time: 13:41:29
 */
public class GenericOwlTermRewriter implements IOWLTermRewriter {
    // REFERENCES
    private OWLDataFactory owlDataFactory;

    private OWLOntology ontology;

    public OWLDescriptionRewritingStrategy getRewritingStrategy() {
        return rewritingStrategy;
    }

    private OWLDescriptionRewritingStrategy rewritingStrategy;


    // STATE

    private boolean isInitialized = false;

    public OWLDataFactory getOwlDataFactory() {
        return owlDataFactory;
    }

    public void setOwlDataFactory(OWLDataFactory owlDataFactory) {
        this.owlDataFactory = owlDataFactory;
    }


    public void setOwlOntology(OWLOntology ontology) {
        this.ontology = ontology;
    }


    public OWLOntology getOntology() {
        return ontology;
    }

    public void setRewritingStrategy(OWLDescriptionRewritingStrategy rewritingStrategy) {
        this.rewritingStrategy = rewritingStrategy;
    }


    public GenericOwlTermRewriter(OWLDataFactory owlDataFactory) {
        this.owlDataFactory = owlDataFactory;
    }

    public GenericOwlTermRewriter() {
    }

    public void init() {
        
    }


    public OWLDescription rewrite(GenericOWLDescriptionRewritingContext context) {
        return doReplacing(context);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected OWLDescription doReplacing(GenericOWLDescriptionRewritingContext context) {

        // perform replacement if possible
        OWLDescription replacement = getRewritingStrategy().rewrite(context);

        if (replacement == null) {
            // there is a replacement for this term
            throw new IllegalArgumentException("Rewritingstrategy never should deliver null back");
        }
        // recurse down the object tree
        if (replacement instanceof OWLObjectIntersectionOf) {
            OWLObjectIntersectionOf intersection = (OWLObjectIntersectionOf) replacement;

            Set<OWLDescription> subterms = new HashSet<OWLDescription>();

            // recurse into operands
            for (OWLDescription subTerm : intersection.getOperands()) {
                subterms.add(doReplacing(context.copy(subTerm)));
            }

            // return a freshly constructed intersection
            return getOwlDataFactory().getOWLObjectIntersectionOf(subterms);


        } else if (replacement instanceof OWLObjectUnionOf) {
            OWLObjectUnionOf union = (OWLObjectUnionOf) replacement;
            Set<OWLDescription> subterms = new HashSet<OWLDescription>();

            // recurse into operands
            for (OWLDescription subTerm : union.getOperands()) {
                subterms.add(doReplacing(context.copy(subTerm)));
            }

            // return a freshly constructed Complementobject with intersection
            return getOwlDataFactory().getOWLObjectUnionOf(subterms);

        } else if (replacement instanceof OWLObjectMaxCardinalityRestriction) {
            OWLObjectMaxCardinalityRestriction maxR = (OWLObjectMaxCardinalityRestriction) replacement;
            // recurse into subterm
            // return a freshly constructed Complementobject with intersection
            return
                    getOwlDataFactory().getOWLObjectMaxCardinalityRestriction(
                            maxR.getProperty(), maxR.getCardinality(),
                            doReplacing(context.copy(maxR.getFiller())));

        } else if (replacement instanceof OWLObjectMinCardinalityRestriction) {
            OWLObjectMinCardinalityRestriction minR = (OWLObjectMinCardinalityRestriction) replacement;
            return
                    getOwlDataFactory().getOWLObjectMinCardinalityRestriction(
                            minR.getProperty(), minR.getCardinality(),
                            doReplacing(context.copy(minR.getFiller())));
        } else if (replacement instanceof OWLObjectAllRestriction) {
            OWLObjectAllRestriction allRestriction = (OWLObjectAllRestriction) replacement;
            // recurse into subterm
            // return a freshly constructed Complementobject with intersection
            return
                    getOwlDataFactory().getOWLObjectAllRestriction(
                            allRestriction.getProperty(),
                            doReplacing(context.copy(allRestriction.getFiller())));

        } else if (replacement instanceof OWLObjectSomeRestriction) {
            OWLObjectSomeRestriction someRestriction = (OWLObjectSomeRestriction) replacement;
            return
                    getOwlDataFactory().getOWLObjectSomeRestriction(
                            someRestriction.getProperty(),
                            doReplacing(context.copy(someRestriction.getFiller())));
        }


        // end recursion
        return replacement;
    }
}
