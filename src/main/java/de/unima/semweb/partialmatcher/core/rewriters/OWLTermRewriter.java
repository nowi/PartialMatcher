package de.unima.semweb.partialmatcher.core.rewriters;

import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.*;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import de.unima.semweb.partialmatcher.core.Unfolder;
import de.unima.semweb.partialmatcher.core.ApproximationType;

/**
 * User: nowi
 * Date: 24.01.2008
 * Time: 10:36:22
 */
public class OWLTermRewriter {

    // REFERENCES
    private OWLDataFactory owlDataFactory;
    private Unfolder unfolder;
    private OWLOntology ontology;


    // INTERNAL STATE
    private final int max;
    /**
     * Holds all Classes which should not be in the S-Set
     */
    private Set<URI> notS;



    public OWLTermRewriter(int max) {
        this.max = max;


    }

    public void init() {
        if (owlDataFactory == null) {
            setOwlDataFactory(OWLManager.createOWLOntologyManager().getOWLDataFactory());
            // no owlfactory specified , using default !
        }

        if (ontology == null) {
            throw new IllegalStateException("No ontology has been supplied !");
        }

        // init unfolder
        unfolder = new Unfolder(ontology);
        unfolder.init();
    }


    OWLDescription constructReplacement(OWLDescription term, ApproximationType approximationType) {
        // perform replacement if possible
        OWLDescription replacement = getReplacement(term, approximationType);
        if (replacement != null) {
            // there is a replacement for this term
            term = replacement;
        }
        // recurse down the object tree

        //If the term is negated, change the approx-type
        if (term instanceof OWLObjectComplementOf) {
            OWLObjectComplementOf complement = (OWLObjectComplementOf) term;
            OWLDescription filler = complement.getOperand();

            if (filler instanceof OWLObjectIntersectionOf) {
                OWLObjectIntersectionOf intersection = (OWLObjectIntersectionOf) filler;

                Set<OWLDescription> subterms = new HashSet<OWLDescription>();

                // recurse into operands
                for (OWLDescription subTerm : intersection.getOperands()) {
                    subterms.add(constructReplacement(subTerm, approximationType.invert()));
                }

                // return a freshly constructed Complementobject with intersection
                return owlDataFactory.getOWLObjectComplementOf(owlDataFactory.getOWLObjectIntersectionOf(subterms));

            } else if (filler instanceof OWLObjectUnionOf) {
                OWLObjectUnionOf union = (OWLObjectUnionOf) filler;
                Set<OWLDescription> subterms = new HashSet<OWLDescription>();

                // recurse into operands
                for (OWLDescription subTerm : union.getOperands()) {
                    subterms.add(constructReplacement(subTerm, approximationType.invert()));
                }

                // return a freshly constructed Complementobject with intersection
                return owlDataFactory.getOWLObjectComplementOf(owlDataFactory.getOWLObjectUnionOf(subterms));

            } else if (filler instanceof OWLObjectMaxCardinalityRestriction) {
                OWLObjectMaxCardinalityRestriction maxR = (OWLObjectMaxCardinalityRestriction) term;
                // recurse into subterm
                // return a freshly constructed Complementobject with intersection
                return owlDataFactory.getOWLObjectComplementOf(
                        owlDataFactory.getOWLObjectMaxCardinalityRestriction(
                                maxR.getProperty(), maxR.getCardinality(),
                                constructReplacement(maxR.getFiller(), approximationType.invert().invert())));

            } else if (filler instanceof OWLObjectMinCardinalityRestriction) {
                OWLObjectMinCardinalityRestriction minR = (OWLObjectMinCardinalityRestriction) term;
                return owlDataFactory.getOWLObjectComplementOf(
                        owlDataFactory.getOWLObjectMinCardinalityRestriction(
                                minR.getProperty(), minR.getCardinality(),
                                constructReplacement(minR.getFiller(), approximationType.invert())));
            }

        } else { // term is not negatated
            if (term instanceof OWLObjectIntersectionOf) {
                OWLObjectIntersectionOf intersection = (OWLObjectIntersectionOf) term;

                Set<OWLDescription> subterms = new HashSet<OWLDescription>();

                // recurse into operands
                for (OWLDescription subTerm : intersection.getOperands()) {
                    subterms.add(constructReplacement(subTerm, approximationType));
                }

                // return a freshly constructed intersection
                return owlDataFactory.getOWLObjectIntersectionOf(subterms);


            } else if (term instanceof OWLObjectUnionOf) {
                OWLObjectUnionOf union = (OWLObjectUnionOf) term;
                Set<OWLDescription> subterms = new HashSet<OWLDescription>();

                // recurse into operands
                for (OWLDescription subTerm : union.getOperands()) {
                    subterms.add(constructReplacement(subTerm, approximationType));
                }

                // return a freshly constructed Complementobject with intersection
                return owlDataFactory.getOWLObjectUnionOf(subterms);

            } else if (term instanceof OWLObjectMaxCardinalityRestriction) {
                OWLObjectMaxCardinalityRestriction maxR = (OWLObjectMaxCardinalityRestriction) term;
                // recurse into subterm
                // return a freshly constructed Complementobject with intersection
                return
                        owlDataFactory.getOWLObjectMaxCardinalityRestriction(
                                maxR.getProperty(), maxR.getCardinality(),
                                constructReplacement(maxR.getFiller(), approximationType.invert()));

            } else if (term instanceof OWLObjectMinCardinalityRestriction) {
                OWLObjectMinCardinalityRestriction minR = (OWLObjectMinCardinalityRestriction) term;
                return
                        owlDataFactory.getOWLObjectMinCardinalityRestriction(
                                minR.getProperty(), minR.getCardinality(),
                                constructReplacement(minR.getFiller(), approximationType));
            }
        }

        // end recursion
        return term;

    }

    // return null if the term cannot be rewirttern
    public OWLDescription rewriteTerm(OWLClass clazz, ApproximationType approximationType) {
        // first unfold the owl class if necessary
        Set<OWLDescription> unfoldedSet = unfolder.unfoldOWLConcept(clazz);

        if (unfoldedSet == null || unfoldedSet.isEmpty()) {
            // if empty set then the supplied owlClass cannot be unfolded
            // therefore it cannot be rewritten
            return null;
        }

        // TODO check if ths is appropirate behavior
        // use the first description in the unfolded SET
        OWLDescription unfolded = unfoldedSet.iterator().next();

        // rewrite the unfolded term


        return constructReplacement(unfolded, approximationType);
    }


    private OWLDescription getReplacement(OWLDescription original, ApproximationType approximationType) {
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
            if (getNotS().contains(namedObject) && (namedObject instanceof OWLClass)) {
                if (approximationType == ApproximationType.LOWER)
                    return getOwlDataFactory().getOWLNothing();
                else //if (approxtype == APPROXTYPE_UPPER)
                    return getOwlDataFactory().getOWLThing();
            } else return null;
        } else if (original instanceof OWLObjectMinCardinalityRestriction) { // replace QNRs
            OWLObjectMinCardinalityRestriction minR = (OWLObjectMinCardinalityRestriction) original;
            OWLObjectProperty relation = minR.getProperty().asOWLObjectProperty();
            if (getNotS().contains(relation.getURI())) {
                return getOwlDataFactory().getOWLObjectMinCardinalityRestriction(minR.getProperty(), minRel, minR.getFiller());
            } else return null;

        } else if (original instanceof OWLObjectMaxCardinalityRestriction) {
            OWLObjectMaxCardinalityRestriction maxR = (OWLObjectMaxCardinalityRestriction) original;
            OWLObjectProperty relation = maxR.getProperty().asOWLObjectProperty();
            if (getNotS().contains(relation.getURI())) {
                return owlDataFactory.getOWLObjectMaxCardinalityRestriction(maxR.getProperty(), maxRel, maxR.getFiller());
            } else return null;

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
            return null;
        }

    }


    OWLDataFactory getOwlDataFactory() {
        return owlDataFactory;
    }


    void setOwlDataFactory(OWLDataFactory owlDataFactory) {
        this.owlDataFactory = owlDataFactory;
    }


    Set<URI> getNotS() {
        return notS;
    }

    public void setNotS(Set<URI> notS) {
        this.notS = notS;
    }

    public void setOwlOntology(OWLOntology ontology) {
        this.ontology = ontology;
    }


}