package test.de.unima.semweb.partialmatcher.util;


import de.unima.semweb.partialmatcher.core.strategies.contexts.GenericOWLDescriptionRewritingContext;
import de.unima.semweb.partialmatcher.core.rewriters.OWLNNFTermRewriter;
import de.unima.semweb.partialmatcher.util.OWLUtils;
import org.semanticweb.owl.model.*;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * User: nowi
 * Date: 02.03.2008
 * Time: 15:19:37
 */
public class OWLNNFTermRewriterTest {


    @Test
    public void testDoReplacing() throws OWLOntologyCreationException {
        // first load the specified ontology

        final OWLOntologyManager manager = OWLUtils.createOntologyManager(OWLUtils.PIZZA);
        final OWLOntology ontology = manager.getOntologies().iterator().next();

        OWLNNFTermRewriter nnfRewriter = new OWLNNFTermRewriter();
        nnfRewriter.setOwlOntology(ontology);
        nnfRewriter.setOwlDataFactory(manager.getOWLDataFactory());

        for (OWLClass clazz : ontology.getReferencedClasses()) {
            // get complex classes
            Set<OWLDescription> complex = clazz.getEquivalentClasses(ontology);
            if (!complex.isEmpty()) {
                OWLDescription complexDesc = complex.iterator().next() ;
                System.out.println("BEFORE : " + complexDesc);
                System.out.println("NNF : " + nnfRewriter.rewrite(new GenericOWLDescriptionRewritingContext(complexDesc)));
                System.out.println("-------------------------------------------------------------------------------");
            }
        }

    }
}
