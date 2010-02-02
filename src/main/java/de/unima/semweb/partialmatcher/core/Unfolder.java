package de.unima.semweb.partialmatcher.core;

import org.semanticweb.owl.model.*;

import java.util.*;


/**
 * User: nowi
 * Date: 15.01.2008
 * Time: 18:27:36
 */
public class Unfolder {

    private final OWLOntology ontology;

    private final Map<OWLClass, Set<OWLDescription>> unfoldingMap = new HashMap<OWLClass, Set<OWLDescription>>();

    private boolean isInitialized = false;

    boolean isInitialized() {
        return isInitialized;
    }

    public Unfolder(OWLOntology ontology) {
        this.ontology = ontology;
    }

    OWLOntology getOntology() {
        return ontology;
    }

    /**
    * Must be called after construction
    */
    public void init() {
        // fill up the unfolding replacement map
        // scan the ontology
        if (ontology == null) {
            throw new IllegalStateException("No ontology supplied to Unfolder !");
        }
        scanOntology();
        isInitialized = true;

    }

    public Set<OWLDescription> unfoldOWLConcept(OWLClass concept) {
        if(isInitialized()){
            Set<OWLDescription> found = unfoldingMap.get(concept);
            if (found != null) {
                return found;
            } else {
                return new HashSet<OWLDescription>();
            }
        }else
            throw new IllegalStateException("Run init() first !");
    }


    private void scanOntology() {
        // scan the whole ontology
        for (OWLClass cls : ontology.getReferencedClasses()) {
            scanRec(cls);
        }
    }

    private void scanRec(OWLDescription desc) {
        // scan the OWLDescription recursivly
        if (desc instanceof OWLClass) {
            OWLClass clazz = (OWLClass) desc;
            // check if there are Equivalent classes
            Set<OWLDescription> equalDescriptions = clazz.getEquivalentClasses(getOntology());
            if (equalDescriptions.isEmpty()) {
                // atomic !
                // there is no replacement for this concept
            } else {
                // non atomic ! replace with first description
                OWLDescription equalDescription = equalDescriptions.iterator().next();
                unfoldingMap.put(clazz, Collections.singleton(equalDescription));

                // continue scanning within each of the equal descriptions
//                scanRec(equalDescription);


            } // end if else

        } else if (desc instanceof OWLNaryBooleanDescription) {
            OWLNaryBooleanDescription bool = (OWLNaryBooleanDescription) desc;

            for (OWLDescription operand : bool.getOperands()) {
                scanRec(operand);
            }

        } else if (desc instanceof OWLQuantifiedRestriction) {
            OWLQuantifiedRestriction rest = (OWLQuantifiedRestriction) desc;
            Object filler = rest.getFiller();
            if (filler instanceof OWLDescription) {
                scanRec((OWLDescription) filler);
            }

        }
    }


    // manually add to unfolding map
    public void addMapping(OWLClass key , OWLDescription value) {
        unfoldingMap.put(key, Collections.singleton(value));
    }




}

