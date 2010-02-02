package de.unima.semweb.partialmatcher.core.rewriters;

import org.semanticweb.owl.model.*;

import java.util.List;
import java.util.Set;
import java.net.URI;

import de.unima.semweb.partialmatcher.core.strategies.contexts.GenericOWLDescriptionRewritingContext;
import de.unima.semweb.partialmatcher.core.strategies.contexts.PartialDescriptionRewritingApproximationTypeContext;
import de.unima.semweb.partialmatcher.core.ApproximationType;

/**
 * User: nowi
 * Date: 13.03.2008
 * Time: 10:45:40
 */
public class OWLOntologyNNFRewriter implements OntologyRewriter {
    private final OWLNNFTermRewriter owlnnfTermRewriter;
    private final OWLOntology owlOntology;

    private OWLRewritingAxiomVisitor axiomVisitor;
    private OWLRewritingDescriptionVisitor descriptionVisitor;
    private OWLOntologyManager owlOntologyManager;


    public OWLOntologyNNFRewriter(OWLOntologyManager manager, OWLOntology owlOntology) {
        this.owlOntology = owlOntology;
        this.owlOntologyManager = manager;


        owlnnfTermRewriter = new OWLNNFTermRewriter();
        owlnnfTermRewriter.setOwlDataFactory(owlOntologyManager.getOWLDataFactory());
        owlnnfTermRewriter.setOwlOntology(this.owlOntology);

        // init the visitors
        descriptionVisitor = new OWLRewritingDescriptionVisitor(owlnnfTermRewriter);

        axiomVisitor = new OWLRewritingAxiomVisitor(descriptionVisitor, owlOntology, owlOntologyManager.getOWLDataFactory());


    }

    public OWLOntology rewriteOntology() throws OWLOntologyChangeException {
        // transform each term in this ontology into NNF
        for (OWLAxiom axiom : owlOntology.getAxioms()) {
            axiom.accept(axiomVisitor);
        }

        // perform the collected changes on the onotlogy
        owlOntologyManager.applyChanges(axiomVisitor.getAxiomChanges());

        return owlOntology;

    }


}
