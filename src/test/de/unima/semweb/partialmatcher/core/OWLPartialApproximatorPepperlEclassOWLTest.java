//package test.de.unima.semweb.partialmatcher.core;
//
///**
// * User: nowi
// * Date: 06.02.2008
// * Time: 15:27:11
// */
//
//
//import de.unima.semweb.partialmatcher.core.*;
//import de.unima.semweb.partialmatcher.core.strategies.CompoundOWLDescriptionRewritingStrategy;
//import de.unima.semweb.partialmatcher.core.strategies.LESSInclusionOfConcepts;
//import de.unima.semweb.partialmatcher.core.strategies.MOREInclusionOfConcepts;
//import de.unima.semweb.partialmatcher.core.strategies.RandomInclusionOfConcepts;
//import de.unima.semweb.partialmatcher.util.OWLUtils;
//import org.junit.Before;
//import org.semanticweb.owl.inference.OWLReasoner;
//import org.semanticweb.owl.inference.OWLReasonerException;
//import org.semanticweb.owl.model.*;
//import static org.testng.Assert.assertFalse;
//import static org.testng.Assert.assertTrue;
//
//import java.net.URI;
//import java.util.*;
//
//public class OWLPartialApproximatorPepperlEclassOWLTest {
//    private OWLOntologyManager manager;
//    private OWLOntology ontology;
//    private OWLReasoner reasoner;
//    private OWLPartialApproximator owlApproximator;
//    private Set<URI> vocabulary;
//    private OWLClass request;
//    private OWLClass sales;
//    private OWLClass advert2;
//    private OWLClass advert1;
//
//    @Before
//    public void testEclassDatabaseConnection() {
////        System.out.println("Adding property :" + EclassNameService.getName("P_BAD840001"));
////        System.out.println("Adding property :" + EclassNameService.getName("P_BAD866001"));
////        System.out.println("Adding property :" + EclassNameService.getName("P_BAD947001"));
//    }
//
//
//    @Before
////    @Test
//public void init() throws OWLOntologyCreationException {
//
//
//        manager = OWLUtils.createOntologyManager(OWLUtils.PEPPERLECLASSOWL);
//        ontology = manager.getOntologies().iterator().next();
//
//
//        // create reasoner
//        // We need to create an instance of OWLReasoner.  OWLReasoner provides the basic
//        // query functionality that we need, for example the ability obtain the subclasses
//        // of a class etc.  See the createOWLReasoner method implementation for more details
//        // on how to instantiate the reasoner
//        reasoner = OWLUtils.createOWLReasoner(manager);
//
//
//        owlApproximator = new OWLPartialApproximator();
//
//        owlApproximator = new OWLPartialApproximator();
//        owlApproximator.setOwlOntologyManager(manager);
//        owlApproximator.setReasoner(reasoner);
//
//
//        // setup owltermrewriter, we use the generic termrewirter and configure it with a custom
//        // compound term rewriting strategy that will remove all datatype restriction , and
//        // remove all objectproperty restrictions with regards to the current notS set
//
//        IOWLTermRewriter genericTermRewriter = new GenericOwlTermRewriter(manager.getOWLDataFactory());
//        genericTermRewriter.setOwlOntology(ontology);
//        genericTermRewriter.init();
//        genericTermRewriter.setRewritingStrategy(
//                new CompoundOWLDescriptionRewritingStrategy(
//                        Arrays.asList(
//                                new OWLDataValueRestrictionsRewritingStrategy(manager.getOWLDataFactory()),
//                                new OWLObejctAllRestrictionsRewritingStrategy(manager.getOWLDataFactory())
//                        )
//
//                )
//        );
//
//
//        // configure the partial approximator with this rewriter
//
//        owlApproximator.setOwlTermRewriter(genericTermRewriter);
//
//
//        owlApproximator.setOwlOntologyManager(manager);
//        owlApproximator.setReasoner(reasoner);
//        owlApproximator.init();
//
//
//    }
//
//
//    public void testApproximatePepper801132ApproximateMAXMinTemperature() throws OWLReasonerException, OWLOntologyCreationException {
//        // test the ontology with some basic queries , no approximation here
//
//        // get an pepperlinstance
//
        OWLClass pepplerRequestInstance109128 = manager.getOWLDataFactory().getOWLClass(URI.create(ontology.getURI() + "#Pepperl_801132"));

        OWLClass pepperlInstance2 = manager.getOWLDataFactory().getOWLClass(URI.create(ontology.getURI() + "#Pepperl_801131"));
        OWLClass pepperlInstance3 = manager.getOWLDataFactory().getOWLClass(URI.create(ontology.getURI() + "#Pepperl_801130"));
        OWLClass pepperlInstance4 = manager.getOWLDataFactory().getOWLClass(URI.create(ontology.getURI() + "#Pepperl_801135"));


        final List<OWLClass> classesToApproximate = new LinkedList<OWLClass>();
        classesToApproximate.add(pepperlInstance2);
        classesToApproximate.add(pepperlInstance4);
        classesToApproximate.add(pepperlInstance4);


        // define the vocabulary , form which the notS elements will be picked
        System.out.println("Creating vocabulary");

        //
        vocabulary = new HashSet<URI>() {
            {
                add(URI.create(ontology.getURI() + "#P_BAA039001"));  // max umgebungs temp

                add(URI.create(ontology.getURI() + "#P_BAA038001"));  // min umgebungs Temp

//                add(URI.create(ontology.getURI() + "#P_BAC907001"));  // REICHWEITE
//
//                add(URI.create(ontology.getURI() + "#P_BAD849001"));  // hoehe des sensors
//
//                add(URI.create(ontology.getURI() + "#P_BAD823001"));  // breite des sensors
//
//                add(URI.create(ontology.getURI() + "#P_BAD847001"));  // hersteller artikelnummer
//
//                add(URI.create(ontology.getURI() + "#P_BAA316001"));  // produkt name
//
//                add(URI.create(ontology.getURI() + "#P_BAA001001"));  // hersteller name
//
//                add(URI.create(ontology.getURI() + "#P_BAA469001"));  // spannungsart
//
//                add(URI.create(ontology.getURI() + "#P_BAD899001"));  // ausfuehrung des schaltelemtns

//                add(URI.create(ontology.getURI() + "#P_BAD831001"));  // ausfuehrung des elektrischen anschlusses
//
            }
        };

        // include everyth8ing
        Set<URI> notS = new HashSet<URI>() {
            {
//                addAll(vocabulary);

            }
        };


        // create a RANDOM INCLUSION Strategy
        final ApproximationContext initialApproximationContext = new ApproximationContext(pepplerRequestInstance109128, new NotSApproximationContext(pepplerRequestInstance109128, notS, vocabulary), new CTASetApproximationContext(pepplerRequestInstance109128, classesToApproximate));
        final ApproximationStrategy randomInclusion = new RandomInclusionOfConcepts(initialApproximationContext);
        final Map<ApproximationContext, Set<OWLClass>> partialMatches = owlApproximator.getAllMatches(randomInclusion);

        // print the matches
        for (ApproximationContext approximationContext : partialMatches.keySet()) {
//            System.out.printf("Approximated Matches with regard to Context : %s for request : %s \n", currentContext,pepperlInstance1Name);
            for (OWLClass owlConcept : partialMatches.get(approximationContext)) {
                System.out.println(owlConcept);
            }
        }
//
//
//        assertTrue(partialMatches.size() == 2);
//
//
//    }
//
////    @org.junit.Test
//    public void testApproximatePepper801132ApproximateMAXMinTemperatureWITHMOREINclusion() throws OWLReasonerException, OWLOntologyCreationException {
//        // test the ontology with some basic queries , no approximation here
//
//        // get an pepperlinstance
//
//        OWLClass pepplerRequestInstance109128 = manager.getOWLDataFactory().getOWLClass(URI.create(ontology.getURI() + "#Pepperl_801132"));
//
//        OWLClass pepperlInstance2 = manager.getOWLDataFactory().getOWLClass(URI.create(ontology.getURI() + "#Pepperl_801131"));
//        OWLClass pepperlInstance3 = manager.getOWLDataFactory().getOWLClass(URI.create(ontology.getURI() + "#Pepperl_801130"));
//        OWLClass pepperlInstance4 = manager.getOWLDataFactory().getOWLClass(URI.create(ontology.getURI() + "#Pepperl_801135"));
//
//
//        final List<OWLClass> classesToApproximate = new LinkedList<OWLClass>();
//        classesToApproximate.add(pepperlInstance2);
//        classesToApproximate.add(pepperlInstance4);
//        classesToApproximate.add(pepperlInstance4);
//
//
//        // define the vocabulary , form which the notS elements will be picked
//        System.out.println("Creating vocabulary");
//
//        //
//        vocabulary = new HashSet<URI>() {
//            {
//                add(URI.create(ontology.getURI() + "#P_BAA039001"));  // max umgebungs temp
//
//                add(URI.create(ontology.getURI() + "#P_BAA038001"));  // min umgebungs Temp
//
////                add(URI.create(ontology.getURI() + "#P_BAC907001"));  // REICHWEITE
////
////                add(URI.create(ontology.getURI() + "#P_BAD849001"));  // hoehe des sensors
////
////                add(URI.create(ontology.getURI() + "#P_BAD823001"));  // breite des sensors
////
////                add(URI.create(ontology.getURI() + "#P_BAD847001"));  // hersteller artikelnummer
////
////                add(URI.create(ontology.getURI() + "#P_BAA316001"));  // produkt name
////
////                add(URI.create(ontology.getURI() + "#P_BAA001001"));  // hersteller name
////
////                add(URI.create(ontology.getURI() + "#P_BAA469001"));  // spannungsart
////
////                add(URI.create(ontology.getURI() + "#P_BAD899001"));  // ausfuehrung des schaltelemtns
//
////                add(URI.create(ontology.getURI() + "#P_BAD831001"));  // ausfuehrung des elektrischen anschlusses
////
//            }
//        };
//
//        // include everyth8ing
//        Set<URI> notS = new HashSet<URI>() {
//            {
////                addAll(vocabulary);
//
//            }
//        };
//
//
//        // crate a conceptCardinalityMap , rank max temperatur above min temp
//
//        final Map<URI, Integer> conceptCardinalityMap = new HashMap<URI, Integer>(){
//            {
//                put(URI.create(ontology.getURI() + "#P_BAA039001"),50 );  // max umgebungs temp
//
//                put(URI.create(ontology.getURI() + "#P_BAA038001"),10);  // min umgebungs Temp
//            }
//        };
//
//
//        // create a MORE INCLUSION Strategy
//        final ApproximationContext initialApproximationContext = new ApproximationContext(pepplerRequestInstance109128, new NotSApproximationContext(pepplerRequestInstance109128, notS, vocabulary), new CTASetApproximationContext(pepplerRequestInstance109128, classesToApproximate));
//        final ApproximationStrategy moreInclusion = new MOREInclusionOfConcepts(initialApproximationContext, conceptCardinalityMap);
//        final Map<ApproximationContext, Set<OWLClass>> partialMatches = owlApproximator.getAllMatches(moreInclusion);
//
//        // print the matches
//        for (ApproximationContext approximationContext : partialMatches.keySet()) {
////            System.out.printf("Approximated Matches with regard to Context : %s for request : %s \n", currentContext,pepperlInstance1Name);
//            for (OWLClass owlConcept : partialMatches.get(approximationContext)) {
//                System.out.println(owlConcept);
//            }
//        }
//
//
//        assertTrue(partialMatches.size() == 2);
//
//
//    }
//
////    @org.junit.Test
//    public void testApproximatePepper801132ApproximateMAXMinTemperatureWITHLESSINclusion() throws OWLReasonerException, OWLOntologyCreationException {
//        // test the ontology with some basic queries , no approximation here
//
//        // get an pepperlinstance
//
//        OWLClass pepplerRequestInstance109128 = manager.getOWLDataFactory().getOWLClass(URI.create(ontology.getURI() + "#Pepperl_801132"));
//
//        OWLClass pepperlInstance2 = manager.getOWLDataFactory().getOWLClass(URI.create(ontology.getURI() + "#Pepperl_801131"));
//        OWLClass pepperlInstance3 = manager.getOWLDataFactory().getOWLClass(URI.create(ontology.getURI() + "#Pepperl_801130"));
//        OWLClass pepperlInstance4 = manager.getOWLDataFactory().getOWLClass(URI.create(ontology.getURI() + "#Pepperl_801135"));
//
//
//        final List<OWLClass> classesToApproximate = new LinkedList<OWLClass>();
//        classesToApproximate.add(pepperlInstance2);
//        classesToApproximate.add(pepperlInstance4);
//        classesToApproximate.add(pepperlInstance4);
//
//
//        // define the vocabulary , form which the notS elements will be picked
//        System.out.println("Creating vocabulary");
//
//        //
//        vocabulary = new HashSet<URI>() {
//            {
//                add(URI.create(ontology.getURI() + "#P_BAA039001"));  // max umgebungs temp
//
//                add(URI.create(ontology.getURI() + "#P_BAA038001"));  // min umgebungs Temp
//
////                add(URI.create(ontology.getURI() + "#P_BAC907001"));  // REICHWEITE
////
////                add(URI.create(ontology.getURI() + "#P_BAD849001"));  // hoehe des sensors
////
////                add(URI.create(ontology.getURI() + "#P_BAD823001"));  // breite des sensors
////
////                add(URI.create(ontology.getURI() + "#P_BAD847001"));  // hersteller artikelnummer
////
////                add(URI.create(ontology.getURI() + "#P_BAA316001"));  // produkt name
////
////                add(URI.create(ontology.getURI() + "#P_BAA001001"));  // hersteller name
////
////                add(URI.create(ontology.getURI() + "#P_BAA469001"));  // spannungsart
////
////                add(URI.create(ontology.getURI() + "#P_BAD899001"));  // ausfuehrung des schaltelemtns
//
////                add(URI.create(ontology.getURI() + "#P_BAD831001"));  // ausfuehrung des elektrischen anschlusses
////
//            }
//        };
//
//        // include everyth8ing
//        Set<URI> notS = new HashSet<URI>() {
//            {
////                addAll(vocabulary);
//
//            }
//        };
//
//
//        // crate a conceptCardinalityMap , rank max temperatur above min temp
//
//        final Map<URI, Integer> conceptCardinalityMap = new HashMap<URI, Integer>(){
//            {
//                put(URI.create(ontology.getURI() + "#P_BAA039001"),50 );  // max umgebungs temp
//
//                put(URI.create(ontology.getURI() + "#P_BAA038001"),10);  // min umgebungs Temp
//            }
//        };
//
//        // set a default order , this will override the default behaviuor
//        final List<URI> conceptDefaultOrder = new LinkedList<URI>(){
//            {
//                add(URI.create(ontology.getURI() + "#P_BAA039001"));  // max umgebungs temp
//
//            }
//        };
//
//        // create a MORE INCLUSION Strategy
//        final ApproximationContext initialApproximationContext = new ApproximationContext(pepplerRequestInstance109128, new NotSApproximationContext(pepplerRequestInstance109128, notS, vocabulary), new CTASetApproximationContext(pepplerRequestInstance109128,  classesToApproximate));
//        final ApproximationStrategy lessInclusionOfConcepts = new LESSInclusionOfConcepts(initialApproximationContext, conceptCardinalityMap);
//        lessInclusionOfConcepts.setDefaultConceptOrder(conceptDefaultOrder);
//        final Map<ApproximationContext, Set<OWLClass>> partialMatches = owlApproximator.getAllMatches(lessInclusionOfConcepts);
//
//
//
//
//        // print the matches
//        for (ApproximationContext approximationContext : partialMatches.keySet()) {
////            System.out.printf("Approximated Matches with regard to Context : %s for request : %s \n", currentContext,pepperlInstance1Name);
//            for (OWLClass owlConcept : partialMatches.get(approximationContext)) {
//                System.out.println(owlConcept);
//            }
//        }
//
//
//        assertTrue(partialMatches.size() == 2);
//
//
//    }
//    @org.junit.Test
//    public void testDescendantClasses() throws OWLReasonerException, OWLOntologyCreationException {
//        // this test should check if there is a difference in first classifiying then determining the descendants , or
//        // it this thing classifys on laod already
//
//
//        // get descendants of C_ AFZ578003-tax without classifying first
//
//        reasoner.loadOntologies(Collections.singleton(ontology));
//        OWLClass C_AFZ578003_tax = manager.getOWLDataFactory().getOWLClass(URI.create(ontology.getURI() + "#C_AKP252002-tax"));
//
//
//        Set descendants = reasoner.getDescendantClasses(C_AFZ578003_tax);
//
//        // now classify
//
//        reasoner.classify();
//
//
//        //
//        Set descendantsClassified = reasoner.getDescendantClasses(C_AFZ578003_tax);
//
//
//        assertTrue(descendantsClassified.size() == descendants.size());
//
//
//
//
//    }
//
////    @org.junit.Test
//    public void testSubclassesClasses() throws OWLReasonerException, OWLOntologyCreationException {
//        // this test should check if there is a difference in first classifiying then determining the subclasses , or
//        // it this thing classifys on laod already
//
//
//        // get subclasses of C_ AFZ578003-tax without classifying first
//            reasoner.loadOntologies(Collections.singleton(ontology));
//
//        OWLClass C_AFZ578003_tax = manager.getOWLDataFactory().getOWLClass(URI.create(ontology.getURI() + "#C_AFZ578003-tax"));
//
//
//        Set subclasses = reasoner.getSubClasses(C_AFZ578003_tax);
//
//        // now classify
//
//        reasoner.classify();
//
//
//        //
//        Set subclassesClassified = reasoner.getSubClasses(C_AFZ578003_tax);
//
//
//        assertTrue(subclassesClassified.size() == subclasses.size());
//
//
//
//
//    }
//
//
//}