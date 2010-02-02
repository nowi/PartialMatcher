package de.unima.semweb.partialmatcher.core.rewriters;

import org.semanticweb.owl.model.*;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import de.unima.semweb.partialmatcher.core.strategies.contexts.PartialDescriptionRewritingApproximationTypeContext;
import de.unima.semweb.partialmatcher.core.strategies.contexts.GenericOWLDescriptionRewritingContext;
import de.unima.semweb.partialmatcher.core.rewriters.GenericOwlTermRewriter;
import de.unima.semweb.partialmatcher.core.Unfolder;
import de.unima.semweb.partialmatcher.core.ApproximationType;

/**
 * User: nowi
 * Date: 02.03.2008
 * Time: 19:09:39
 */
public class UpperLowerApproximatingTermRewriter extends GenericOwlTermRewriter {
    private Unfolder unfolder;


    public UpperLowerApproximatingTermRewriter(int max, OWLDataFactory owlDataFactory) {
        setOwlDataFactory(owlDataFactory);
        // setup with OWLPartialMatcherStrategy
        setRewritingStrategy(new OWLPartialMatcherDescriptionRewritingStrategy(max, getOwlDataFactory()));


    }


    @Override
    public void init() {
        super.init();
        // init unfolder
        unfolder = null;

//        unfolder = new Unfolder(getOntology());
//        unfolder.init();
    }


    @Override
    public OWLDescription rewrite(GenericOWLDescriptionRewritingContext context) {
        return doReplacing(context);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected OWLDescription doReplacing(GenericOWLDescriptionRewritingContext context) {
        // cast down the replacment context

        OWLDescription initialTerm = ((PartialDescriptionRewritingApproximationTypeContext) context).getOwlDescription();
        ApproximationType approximationType = ((PartialDescriptionRewritingApproximationTypeContext) context).getApproximationType();
        Set<URI> notS = ((PartialDescriptionRewritingApproximationTypeContext) context).getNotS();

        // perform replacement if possible
        OWLDescription replacement = getRewritingStrategy().rewrite(new PartialDescriptionRewritingApproximationTypeContext(initialTerm, approximationType, notS));

        if (replacement == null) {
            // there is a replacement for this term
            throw new IllegalArgumentException("Rewritingstrategy never should deliver null back");
        }
        // recurse down the object tree

        //If the term is negated, change the approx-type
        if (replacement  instanceof OWLObjectComplementOf) {
            OWLObjectComplementOf complement = (OWLObjectComplementOf) replacement ;
            OWLDescription filler = complement.getOperand();

            if (filler instanceof OWLObjectIntersectionOf) {
                OWLObjectIntersectionOf intersection = (OWLObjectIntersectionOf) filler;

                Set<OWLDescription> subterms = new HashSet<OWLDescription>();

                // recurse into operands
                for (OWLDescription subTerm : intersection.getOperands()) {
                    subterms.add(doReplacing(new PartialDescriptionRewritingApproximationTypeContext(subTerm, approximationType.invert(), notS)));
                }

                // return a freshly constructed Complementobject with intersection
                return getOwlDataFactory().getOWLObjectComplementOf(getOwlDataFactory().getOWLObjectIntersectionOf(subterms));

            } else if (filler instanceof OWLObjectUnionOf) {
                OWLObjectUnionOf union = (OWLObjectUnionOf) filler;
                Set<OWLDescription> subterms = new HashSet<OWLDescription>();

                // recurse into operands
                for (OWLDescription subTerm : union.getOperands()) {
                    subterms.add(doReplacing(new PartialDescriptionRewritingApproximationTypeContext(subTerm, approximationType.invert(), notS)));
                }

                // return a freshly constructed Complementobject with intersection
                return getOwlDataFactory().getOWLObjectComplementOf(getOwlDataFactory().getOWLObjectUnionOf(subterms));

            } else if (filler instanceof OWLObjectMaxCardinalityRestriction) {
                OWLObjectMaxCardinalityRestriction maxR = (OWLObjectMaxCardinalityRestriction) replacement ;
                // recurse into subterm
                // return a freshly constructed Complementobject with intersection
                return getOwlDataFactory().getOWLObjectComplementOf(
                        getOwlDataFactory().getOWLObjectMaxCardinalityRestriction(
                                maxR.getProperty(), maxR.getCardinality(),
                                doReplacing(new PartialDescriptionRewritingApproximationTypeContext(maxR.getFiller(), approximationType.invert().invert(), notS))));

            } else if (filler instanceof OWLObjectMinCardinalityRestriction) {
                OWLObjectMinCardinalityRestriction minR = (OWLObjectMinCardinalityRestriction) replacement ;
                return getOwlDataFactory().getOWLObjectComplementOf(
                        getOwlDataFactory().getOWLObjectMinCardinalityRestriction(
                                minR.getProperty(), minR.getCardinality(),
                                doReplacing(new PartialDescriptionRewritingApproximationTypeContext(minR.getFiller(), approximationType.invert(), notS))));
            }

        } else { // term is not negatated
            if (replacement instanceof OWLObjectIntersectionOf) {
                OWLObjectIntersectionOf intersection = (OWLObjectIntersectionOf) replacement ;

                Set<OWLDescription> subterms = new HashSet<OWLDescription>();

                // recurse into operands
                for (OWLDescription subTerm : intersection.getOperands()) {
                    subterms.add(doReplacing(new PartialDescriptionRewritingApproximationTypeContext(subTerm, approximationType, notS)));
                }

                // return a freshly constructed intersection
                return getOwlDataFactory().getOWLObjectIntersectionOf(subterms);


            } else if (replacement instanceof OWLObjectUnionOf) {
                OWLObjectUnionOf union = (OWLObjectUnionOf) replacement ;
                Set<OWLDescription> subterms = new HashSet<OWLDescription>();

                // recurse into operands
                for (OWLDescription subTerm : union.getOperands()) {
                    subterms.add(doReplacing(new PartialDescriptionRewritingApproximationTypeContext(subTerm, approximationType, notS)));
                }

                // return a freshly constructed Complementobject with intersection
                return getOwlDataFactory().getOWLObjectUnionOf(subterms);

            } else if (replacement instanceof OWLObjectMaxCardinalityRestriction) {
                OWLObjectMaxCardinalityRestriction maxR = (OWLObjectMaxCardinalityRestriction) replacement ;
                // recurse into subterm
                // return a freshly constructed Complementobject with intersection
                return
                        getOwlDataFactory().getOWLObjectMaxCardinalityRestriction(
                                maxR.getProperty(), maxR.getCardinality(),
                                doReplacing(new PartialDescriptionRewritingApproximationTypeContext(maxR.getFiller(), approximationType.invert(), notS)));

            } else if (replacement  instanceof OWLObjectMinCardinalityRestriction) {
                OWLObjectMinCardinalityRestriction minR = (OWLObjectMinCardinalityRestriction) replacement ;
                return
                        getOwlDataFactory().getOWLObjectMinCardinalityRestriction(
                                minR.getProperty(), minR.getCardinality(),
                                doReplacing(new PartialDescriptionRewritingApproximationTypeContext(minR.getFiller(), approximationType, notS)));
            }
        }

        // end recursion
        return replacement ;
    }
}
