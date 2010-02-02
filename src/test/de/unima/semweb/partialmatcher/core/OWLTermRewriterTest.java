package test.de.unima.semweb.partialmatcher.core;
/**
 * User: nowi
 * Date: 06.02.2008
 * Time: 13:12:11
 */

import junit.framework.TestCase;

public class OWLTermRewriterTest extends TestCase {

//    public void testRewriteTerm() throws Exception {
//        OWLOntologyManager manager = OWLUtils.createOntologyManager(OWLUtils.COMPUTER_ONTOLOGY2);
//        OWLOntology ontology = manager.getOntologies().iterator().next();
//
//        // first determine highest number
//        int max = OWLUtils.determineHighestNumberInOntology(ontology);
//
//        // now get the Advert class
//        URI advert1URI = URI.create(ontology.getURI() + "#Advert2");
//        OWLClass advert1 = manager.getOWLDataFactory().getOWLClass(advert1URI);
//
//        // define the NOT(S) set
//        URI puri = URI.create(ontology.getURI() +"#price");
//        Set<OWLPropertyAxiom> props = ontology.getObjectPropertyAxioms();
//        OWLObjectProperty priceProp = manager.getOWLDataFactory().getOWLObjectProperty(puri);
//
//        Set<URI> notS = new HashSet<URI>();
//        notS.add(puri);
//
//        OWLPartialMatcherTermRewriter owlTermRewriter =new OWLPartialMatcherTermRewriter(max,notS,manager.getOWLDataFactory());
//        owlTermRewriter.setOwlOntology(ontology);
//        owlTermRewriter.init();
//
//        // TODO unfold the term first
//        // init unfolder
//        Unfolder unfolder = new Unfolder(ontology);
//        unfolder.init();
//
//        // first unfold the owl class if necessary
//        Set<OWLDescription> unfoldedSet = unfolder.unfoldOWLConcept(advert1);
//
//        // TODO check if ths is appropirate behavior
//        // use the first description in the unfoldedAdvert1 SET
//        OWLDescription unfoldedAdvert1 = unfoldedSet.iterator().next();
//
//
//        String rewrittenString = owlTermRewriter.rewrite(new OWLPartialMatcherReplacementContext(unfoldedAdvert1, ApproximationType.LOWER, notS)).toString();
//        System.out.println("BEFORE : " + unfoldedAdvert1);
//        System.out.println("AFTER : " + owlTermRewriter.rewrite(new OWLPartialMatcherReplacementContext(unfoldedAdvert1, ApproximationType.LOWER, notS)));
//
//        assertEquals("ObjectMaxCardinality(0 item ObjectComplementOf(ObjectIntersectionOf( Laptop ObjectMaxCardinality(0 price Thing) ObjectMinCardinality(256 hasCacheMemory Thing) ObjectMinCardinality(512 hasMainMemory Thing))))",rewrittenString);
//    }
}