package de.unima.semweb.partialmatcher.core.rewriters;


import de.unima.semweb.partialmatcher.util.OWLUtils;
import org.semanticweb.owl.model.*;
import org.semanticweb.owl.util.OWLAxiomVisitorAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: nowi
 * Date: 13.03.2008
 * Time: 11:15:21
 */
public class OWLRewritingAxiomVisitor extends OWLAxiomVisitorAdapter {
    private final OWLRewritingDescriptionVisitor descriptionVisitor;

    private final OWLDataFactory owlDataFactory;
    private final OWLOntology owlOntology;

    private List<OWLAxiomChange> axiomChanges = new ArrayList<OWLAxiomChange>();


    public OWLRewritingAxiomVisitor(OWLRewritingDescriptionVisitor descriptionVisitor, OWLOntology owlOntology, OWLDataFactory owlDataFactory) {
        this.descriptionVisitor = descriptionVisitor;
        this.owlDataFactory = owlDataFactory;
        this.owlOntology = owlOntology;

    }

    @Override
    public void visit(OWLSubClassAxiom owlSubClassAxiom) {
        // get the complex description out of the axiom
        List<OWLDescription> descriptions = new ArrayList<OWLDescription>(owlSubClassAxiom.getDescriptions());

        for (OWLDescription description : descriptions) {
            // visit the description and record replacment
            description.accept(descriptionVisitor);

        }

        // construct new Axiom
        OWLDescription subReplace = descriptionVisitor.getReplacements().get(owlSubClassAxiom.getSubClass());
        OWLDescription superReplace = descriptionVisitor.getReplacements().get(owlSubClassAxiom.getSuperClass());

        if (subReplace == null) {
            subReplace = owlSubClassAxiom.getSubClass();
        }
        if (superReplace == null) {
            superReplace = owlSubClassAxiom.getSuperClass();
        }

        OWLSubClassAxiom replacementAxiom = owlDataFactory.getOWLSubClassAxiom(subReplace, superReplace);

        // create the changeevents that have to be performed on the ontology
        try {
            List<OWLAxiomChange> replaceEvent = OWLUtils.createReplaceAxiomEvents(owlOntology, owlSubClassAxiom, replacementAxiom);
            axiomChanges.addAll(replaceEvent);


        } catch (OWLOntologyChangeException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // clear the description visitor
        descriptionVisitor.clear();


        super.visit(owlSubClassAxiom);


    }

    @Override
    public void visit(OWLEquivalentClassesAxiom owlEquivalentClassesAxiom) {
        // get the complex description out of the axiom
        List<OWLDescription> descriptions = new ArrayList<OWLDescription>(owlEquivalentClassesAxiom.getDescriptions());

        Set<OWLDescription> replacements = new HashSet<OWLDescription>();
        for (OWLDescription description : descriptions) {
            // visit the description and record replacment
            description.accept(descriptionVisitor);
            OWLDescription replacement = descriptionVisitor.getReplacements().get(description);
            if (replacement != null)
                replacements.add(replacement);
            else
                replacements.add(description);

        }

        // construct new Axiom
        OWLEquivalentClassesAxiom replacementAxiom = owlDataFactory.getOWLEquivalentClassesAxiom(replacements);

        // create the changeevents that have to be performed on the ontology
        try {
            List<OWLAxiomChange> replaceEvent = OWLUtils.createReplaceAxiomEvents(owlOntology, owlEquivalentClassesAxiom, replacementAxiom);
            axiomChanges.addAll(replaceEvent);


        } catch (OWLOntologyChangeException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // clear the description visitor
        descriptionVisitor.clear();


        super.visit(owlEquivalentClassesAxiom);

    }


    public List<OWLAxiomChange> getAxiomChanges() {
        return axiomChanges;
    }
}
