package test.de.unima.semweb.partialmatcher.core;
/**
 * User: nowi
 * Date: 13.03.2008
 * Time: 13:19:15
 */


import de.unima.semweb.partialmatcher.core.rewriters.OWLOntologyNNFRewriter;
import de.unima.semweb.partialmatcher.util.OWLUtils;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.testng.annotations.Test;


public class OWLOntologyNNFRewriterTest {

    @Test
    public void testRewriteOntology() throws Exception {
        final OWLOntologyManager manager = OWLUtils.createOntologyManager(OWLUtils.FOODSWAP_PARTITIONED);
        final OWLOntology ontology = manager.getOntologies().iterator().next();

        OWLOntologyNNFRewriter owlOntologyNNFRewriter = new OWLOntologyNNFRewriter(manager,ontology);
        owlOntologyNNFRewriter.rewriteOntology();

        for(OWLAxiom axiom : ontology.getAxioms()){
            System.out.println(axiom);
    }
}
}