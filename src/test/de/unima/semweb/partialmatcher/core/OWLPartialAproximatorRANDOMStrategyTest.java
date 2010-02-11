package test.de.unima.semweb.partialmatcher.core;

/**
 * User: nowi
 * Date: 06.02.2008
 * Time: 15:27:11
 */


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.unima.semweb.partialmatcher.core.ApproximationStrategy;
import de.unima.semweb.partialmatcher.core.CTASetApproximationContext;
import de.unima.semweb.partialmatcher.core.IOWLTermRewriter;
import de.unima.semweb.partialmatcher.core.MatchingResult;
import de.unima.semweb.partialmatcher.core.NotSApproximationContext;
import de.unima.semweb.partialmatcher.core.OWLPartialApproximator;

import org.junit.Before;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLPropertyAxiom;

import de.unima.semweb.partialmatcher.core.ApproximationContext;
import de.unima.semweb.partialmatcher.core.rewriters.UpperLowerApproximatingTermRewriter;
import de.unima.semweb.partialmatcher.core.strategies.RandomInclusionOfConcepts;
import de.unima.semweb.partialmatcher.util.OWLUtils;

public class OWLPartialAproximatorRANDOMStrategyTest {
    private OWLOntologyManager manager;
    private OWLOntology ontology;
    private OWLReasoner reasoner;
    private OWLPartialApproximator owlApproximator;
    private Set<URI> vocabulary;
    private OWLClass request;
    private OWLClass sales;
    private OWLClass advert2;
    private OWLClass advert1;
    private List<OWLClass> classesToApproximate;


    @Before
    public void init() throws OWLOntologyCreationException {
        manager = OWLUtils.createOntologyManager(OWLUtils.COMPUTER_ONTOLOGY1);
        ontology = manager.getOntologies().iterator().next();

        // create reasoner
        // We need to create an instance of OWLReasoner.  OWLReasoner provides the basic
        // query functionality that we need, for example the ability obtain the subclasses
        // of a class etc.  See the createOWLReasoner method implementation for more details
        //
        // on how to instantiate the reasoner
        reasoner = OWLUtils.createOWLReasoner(manager);


        owlApproximator = new OWLPartialApproximator();
        owlApproximator.setOwlOntologyManager(manager);
        owlApproximator.setReasoner(reasoner);

        // determine highest Number in Ontology
        int max = OWLUtils.determineHighestNumberInOntology(ontology);

        // setup owltermrewriter
        IOWLTermRewriter upperLowerApproximatingTermRewriter = new UpperLowerApproximatingTermRewriter(max, manager.getOWLDataFactory());
        upperLowerApproximatingTermRewriter.setOwlOntology(ontology);
        upperLowerApproximatingTermRewriter.init();


        // configure the partial approximator with this rewriter

        owlApproximator.setOwlTermRewriter(upperLowerApproximatingTermRewriter);

        owlApproximator.init();


        request = manager.getOWLDataFactory().getOWLClass(URI.create(ontology.getURI() + "#Request"));
        sales = manager.getOWLDataFactory().getOWLClass(URI.create(ontology.getURI() + "#Sales"));
        advert1 = manager.getOWLDataFactory().getOWLClass(URI.create(ontology.getURI() + "#Advert1"));
        advert2 = manager.getOWLDataFactory().getOWLClass(URI.create(ontology.getURI() + "#Advert2"));


        Set<OWLDescription> adverts = sales.getSubClasses(ontology);

        classesToApproximate = new LinkedList<OWLClass>();

        for (OWLDescription advert : adverts) {
            if (advert instanceof OWLClass) {
                OWLClass owlClass = (OWLClass) advert;
                if (!owlClass.getURI().equals(request.getURI())) {
                    classesToApproximate.add(owlClass);
                }
            }
        }
        // define the vocabulary , form which the notS elements will be picked
        vocabulary = new HashSet<URI>() {
            {
                add(URI.create(ontology.getURI() + "#price"));
                add(URI.create(ontology.getURI() + "#hasMainMemory"));
            }
        };


    }

    @org.junit.Test
    public void testExactMatch() throws OWLReasonerException, OWLOntologyCreationException {
        // setup collaborators
        // empty notS set --> exact match
        Set<URI> notS = new HashSet<URI>();

        // create a RANDOM INCLUSION Strategy
        // create a RANDOM INCLUSION Strategy
        final ApproximationContext initialApproximationContext = new ApproximationContext(request, new NotSApproximationContext(request, notS, vocabulary), new CTASetApproximationContext(request, classesToApproximate));
        final ApproximationStrategy randomInclusion = new RandomInclusionOfConcepts(initialApproximationContext);

        final Map<ApproximationContext, MatchingResult> partialMatches = owlApproximator.getAllMatches(randomInclusion);


        assertTrue(partialMatches.size() == 3);
        // the partialmatches result map should contain 3 entrys
        // 1. Matching Classes using Approximation notS Set { nothing }
        // 2. Matching Classes using Approximation notS Set { price }
        // 2. Matching Classes using Approximation notS Set { price, hasMainMemory }

        // get the matchingresults
        Iterator<MatchingResult> iter = partialMatches.values().iterator();

        Set<OWLClass> partialMatch1 = iter.next().getMatchingOWLClasses();

        assertFalse(partialMatch1.contains(advert2));
        assertFalse(partialMatch1.contains(advert1));

        // the  matchresult 2
        Set<OWLClass> partialMatch2 = iter.next().getMatchingOWLClasses();
        // the matchingOWLClasses of this matching step cannot be predicted , because of the randomized order
        // the concepts are included into the notS set, but we can assume that only one of

        assertTrue(partialMatch2.contains(advert2) || partialMatch2.contains(advert1));
        assertFalse(partialMatch2.contains(advert2) && partialMatch2.contains(advert1));

        // the  matchresult 3
        Set<OWLClass> partialMatch3 = iter.next().getMatchingOWLClasses();

        assertTrue(partialMatch3.contains(advert2));
        assertTrue(partialMatch3.contains(advert1));

    }


    @org.junit.Test
    public void testAskIgnorePriceMatch() throws OWLReasonerException, OWLOntologyCreationException {
        // create the notS set
        // add price
        URI puri = URI.create(ontology.getURI() + "#price");
        Set<OWLPropertyAxiom> props = ontology.getObjectPropertyAxioms();
        OWLObjectProperty priceProp = manager.getOWLDataFactory().getOWLObjectProperty(puri);

        Set<URI> notS = new HashSet<URI>();
        notS.add(puri);// add price to the notS vocabulary

        // create a mockstrategy that always uses the inital ApproximationContext , therefore
        // the notS set is not permutated !
        final ApproximationContext initialApproximationContext = new ApproximationContext(request, new NotSApproximationContext(request, notS, vocabulary), new CTASetApproximationContext(request, classesToApproximate));
        final ApproximationStrategy randomInclusion = new RandomInclusionOfConcepts(initialApproximationContext);
        final Map<ApproximationContext, MatchingResult> partialMatches = owlApproximator.getAllMatches(randomInclusion);


        assertTrue(partialMatches.size() == 2);
        // should only contain the top concept and advert2Approx
        // the partialmatches result map should contain 2 entrys
        // 1. Matching Classes using Approximation notS Set { price }
        // 2. Matching Classes using Approximation notS Set { price, hasMemory }

        // the matchresult 1
        // get the matchingresults
        Iterator<MatchingResult> iter = partialMatches.values().iterator();

        Set<OWLClass> partialMatch1 = iter.next().getMatchingOWLClasses();

        assertTrue(partialMatch1.contains(advert2));
        assertFalse(partialMatch1.contains(advert1));// the first matchresult 1

        // the  matchresult 2
        Set<OWLClass> partialMatch2 = iter.next().getMatchingOWLClasses();
        assertTrue(partialMatch2.contains(advert2));
        assertTrue(partialMatch2.contains(advert1));

    }

    @org.junit.Test
    public void testAskIgnorePriceAndMainMemoryMatch() throws OWLReasonerException, OWLOntologyCreationException {
        // create the notS set
        // add price
        URI puri = URI.create(ontology.getURI() + "#price");
        Set<OWLPropertyAxiom> props = ontology.getObjectPropertyAxioms();
        OWLObjectProperty priceProp = manager.getOWLDataFactory().getOWLObjectProperty(puri);
        // add mainMemory
        URI mmuri = URI.create(ontology.getURI() + "#hasMainMemory");
        OWLObjectProperty mmProp = manager.getOWLDataFactory().getOWLObjectProperty(mmuri);

        Set<URI> notS = new HashSet<URI>();
        notS.add(puri);// add price to the notS vocabulary
        notS.add(mmuri);// add hasMainMemory to the notS vocabulary


        final ApproximationContext initialApproximationContext = new ApproximationContext(request, new NotSApproximationContext(request, notS, vocabulary), new CTASetApproximationContext(request, classesToApproximate));
        final ApproximationStrategy randomInclusion = new RandomInclusionOfConcepts(initialApproximationContext);

        final Map<ApproximationContext, MatchingResult> partialMatches = owlApproximator.getAllMatches(randomInclusion);



        // should only contain the top concept and both adverts

        // the partialmatches result map should contain only one entry , because we used a
        // constant approximationContext
        Set<OWLClass> partialMatch = partialMatches.get(initialApproximationContext).getMatchingOWLClasses();
        assertTrue(partialMatch.contains(advert1));
        assertTrue(partialMatch.contains(advert2));

    }
}