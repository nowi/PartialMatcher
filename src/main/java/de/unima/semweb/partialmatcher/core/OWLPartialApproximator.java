/**
 * Created by IntelliJ IDEA.
 * User: nowi
 * Date: 10.01.2008
 * Time: 14:16:35
 * To change this template use File | Settings | File Templates.
 */
package de.unima.semweb.partialmatcher.core;

import org.mindswap.pellet.owlapi.Reasoner;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerAdapter;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.*;

import java.net.URI;
import java.util.*;

import de.unima.semweb.partialmatcher.core.strategies.contexts.PartialDescriptionRewritingApproximationTypeContext;
import de.unima.semweb.partialmatcher.core.strategies.contexts.OWLPartialRequestTaxConceptDescriptionRewritingContext;

/**
 * Created by IntelliJ IDEA.
 * User: nowi
 * Date: 10.01.2008
 * Time: 12:52:06
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings({"UnusedReturnValue"})
public class OWLPartialApproximator extends Observable {


    // COLLABORTATORS
    private OWLOntologyManager owlOntologyManager;

    private OWLOntology owlOntology;

    private IOWLTermRewriter owlTermRewriter;

    private Unfolder unfolder;

         
    /**
     * The OWL-API Reasoner
     */
    private OWLReasoner reasoner;

    /**
     * Holds all the Classes which are potential "Answers" to the request. Only those
     * Classes are beeing approxmated at all , this List is normally empty
     */
//    private List<OWLClass> classesToApproximate = new ArrayList<OWLClass>();
    private static final String APPROXTAG = "_APPROXIMATED";

    private boolean alreadyApproximated = false;

    // the required collaborators that have to be set before calling init
    private Object required[] = {owlOntologyManager};

    private boolean isCancelled;


    private boolean isRunning;

    public void init() {
        // check state
        // check if any of the required collaborators is null
        if (owlOntologyManager == null) {
            // incomplete state
            throw new IllegalStateException("Object has not been set up properly !");
        }

        // setup the owlOntology
        // get the first ontology loaded by the manager
        Set<OWLOntology> ontologies = owlOntologyManager.getOntologies();
        if (ontologies.size() != 1) {
            throw new IllegalStateException("Supply a valid OntologyManager with ONE ontology already loaded");
        }

        // setup our main ontology
        owlOntology = owlOntologyManager.getOntologies().iterator().next();


        // init unfolder
        unfolder = new Unfolder(owlOntology);
        unfolder.init();


    }

    private void reset() throws OWLOntologyCreationException, OWLReasonerException {
        // reaload ontology
        URI uri = owlOntologyManager.getPhysicalURIForOntology(owlOntology);
        owlOntologyManager.removeOntology(owlOntology.getURI());
        owlOntologyManager.loadOntologyFromPhysicalURI(uri);
        assert owlOntologyManager.getOntologies().size() == 1;
        owlOntology = owlOntologyManager.getOntologies().iterator().next();
        // clear reasoner
        reasoner.clearOntologies();

    }

    public  void cancel() {
        if (isCancelled) {
            System.out.println("Already cancelling , ignoring this call");
        } else {
            isCancelled = true;
        }
    }

    public void start() {
        if (isRunning) {
            System.out.println("Already started , ignoring this call");
        } else {
            isRunning = true;
            isCancelled = false;
        }
    }

    private void stop(){
        if(!isRunning){
            System.out.println("Already stopped , ignoring this call");

        } else {
            isCancelled = false;
            isRunning = false;
        }
    }


    // IMTERNAL STATE
    /**
     * Holds all Classes which should not be in the S-Set
     * @param approximationContext - the approximationContext which includes the classes that should be approximated
     */
    public void approximateClasses(ApproximationContext approximationContext) {
        if (approximationContext.getNotSApproximationContext() != null && !approximationContext.getNotSApproximationContext().getNotS().isEmpty()) {
            for (OWLClass c : approximationContext.getCtaSetApproximationContext().getClassesToApproximate()) {
                try {
                    System.out.println("Approximating: " + c.getURI().toString());

                    // unfold class
                    OWLDescription unfoldet = unfolder.unfoldOWLConcept(c).iterator().next();

                    // approximate using a owlTermRewriter configured with the custom PartialMatcher Term Rewriting Strategy
                    // change the notS Set according to our currently used ApproximationContext
                    OWLDescription approximate = owlTermRewriter.rewrite(
                            new PartialDescriptionRewritingApproximationTypeContext(unfoldet, ApproximationType.LOWER, approximationContext.getNotSApproximationContext().getNotS()));

                    System.out.println("Approximated Class: " + approximate);

                    final OWLDataFactory factory = owlOntologyManager.getOWLDataFactory();

                    // add approximated class to the ontology
                    OWLClass dummy = owlOntologyManager.getOWLDataFactory().getOWLClass(URI.create(c.getURI() + APPROXTAG + approximationContext.hashCode()));


                    // add to unfolder
                    unfolder.addMapping(dummy, approximate);


                    Set<OWLDescription> equivalentClasses = new HashSet<OWLDescription>();
                    equivalentClasses.add(dummy);
                    equivalentClasses.add(approximate);

                    // Now create the axiom
                    OWLAxiom axiom = factory.getOWLEquivalentClassesAxiom(equivalentClasses);
                    // We now add the axiom to the ontology, so that the ontology states that
                    AddAxiom addAxiom = new AddAxiom(owlOntology, axiom);
                    // We now use the manager to apply the change
                    owlOntologyManager.applyChange(addAxiom);

                } catch (OWLOntologyChangeException e) {
                    e.printStackTrace();//To change body of catch statement use File | Settings | File Templates.
                }
            }

        } else {
            System.out.printf("The approximationContexts notS set : %s does not contain any elements , skipping approximation of classes", approximationContext.getNotSApproximationContext());
        }
    }


    /**
     * The Main Approximating function
     * <p/>
     * Returns all parital matches for a given request , uses the provided strategy for
     * NOTS set permutations
     *
     * @param strategy
     * @return
     * @throws org.semanticweb.owl.inference.OWLReasonerException
     *
     */
    public Map<ApproximationContext, MatchingResult> getAllMatches(ApproximationStrategy strategy) throws OWLReasonerException {
        start();
        // get the current ApproximationContext from the ApproximationStrategy
        ApproximationContext approximationContext = strategy.getInitialContext();

        OWLClass requestClass = approximationContext.getRequestClass();

        // result Map , currentContext --> Set of Approximated Subclasses
        Map<ApproximationContext, MatchingResult> resultMap = new LinkedHashMap<ApproximationContext, MatchingResult>();

        do {
            System.out.println("Current Approximation Context : " + approximationContext);

            // approximate all classes with the current ApproximationContext
            approximateClasses(approximationContext);

            // approximate the request with the current ApproximationContext
            final OWLClass requestAPPROX = approximateRequest(requestClass, approximationContext);
            classifyKB();
            setChanged();

            // getAllMatches the KB
            final Set<OWLClass> matchingOwlClasses = askKB(requestAPPROX, approximationContext);

            // create a new matching result
            final MatchingResult matchingResult = MatchingResult.createMatchingResult(approximationContext, matchingOwlClasses);
            // configure additional infos
            matchingResult.createdAt = new Date(System.currentTimeMillis());
            matchingResult.createdBy = this.toString();



            final Map<ApproximationContext, MatchingResult> currentMatches = new HashMap<ApproximationContext, MatchingResult>();
            currentMatches.put(approximationContext, matchingResult);


            resultMap.putAll(currentMatches);
            // notify observers about the currently matched classes
            notifyObservers(currentMatches);


            try {
                reset();
            } catch (OWLOntologyCreationException e) {
                e.printStackTrace();//To change body of catch statement use File | Settings | File Templates.
            }

            // update with next mutation of the approximation Context
            approximationContext = strategy.createNextContext();
        } while (!strategy.isFinished() && !isCancelled);


        // finished
        stop();


        return resultMap;


    }

public Set<OWLClass> askKB(final OWLClass requestClass,final ApproximationContext context) throws OWLReasonerException {
        // Now use the reasoner to obtain the subclasses of request class.  Note the reasoner
        // returns a set of sets.  Each set represents a subclass of request Class where the
        // classes in the set represent equivalence classes.  For example, if we asked for the
        // subclasses of A and got back {{B, C}, {D}} then A would have essentially to subclasses.
        // One of these subclasses would be equivalent to the class D, and the other would be the class that
        // was equivalent to class B and class C.
        Set<OWLClass> matches = new HashSet<OWLClass>();
        // getAll subclass Matches the KB

        Set<OWLClass> subclassMatches = OWLReasonerAdapter.<OWLClass>flattenSetOfSets(reasoner.getSubClasses(requestClass));
        // add all sublclassmatches , resolve the approximated classes
        for (OWLClass owlClass : subclassMatches) {
            if (isApproximatedClass(owlClass)) {
                OWLClass original = resolveApproximatedOWLClassToOriginalOWLClass(owlClass, context);
                matches.add(original);
            } else {
                matches.add(owlClass);
            }

        }

        Set<OWLClass> equalclassMatches = reasoner.getEquivalentClasses(requestClass);
        // add all equalclassMatches , resolve the approximated classes
        for (OWLClass owlClass : equalclassMatches) {
            if (isApproximatedClass(owlClass)) {
                OWLClass original = resolveApproximatedOWLClassToOriginalOWLClass(owlClass, context);
                matches.add(original);
            } else {
                matches.add(owlClass);
            }

        }


        // Finally remove the requesting class
        matches.remove(context.getRequestClass());



        return matches;
    }

    public void classifyKB() {
        // load the ontology into the reasoner
        // prepare knowledgebase
        try {
            reasoner.loadOntologies(Collections.singleton(owlOntology));
//            reasoner.classify();

            ((Reasoner) reasoner).refresh();


        } catch (Exception e) {
            e.printStackTrace();//To change body of catch statement use File | Settings | File Templates.
        }
    }

    private OWLClass resolveApproximatedOWLClassToOriginalOWLClass(OWLClass approximated, ApproximationContext context) {
        // precondition
        if (!isApproximatedClass(approximated)) {
            throw new IllegalArgumentException(String.format("OwlClass %s seems not to be an approximated class", approximated));
        }

        String approxURIString = approximated.getURI().toString();
        String originalURIString = approxURIString.replace((APPROXTAG + context.hashCode()),"") ;

        OWLClass resolved = owlOntologyManager.getOWLDataFactory().getOWLClass(URI.create(originalURIString));

        return resolved;
    }


    private boolean isApproximatedClass(OWLClass owlClass) {
        String identifier = owlClass.getURI().getFragment();
        return (identifier.contains(APPROXTAG));

    }

    private OWLClass approximateRequest(OWLClass requestClass, ApproximationContext currentContext) {

        // DIRTY HACK TO ALLOW CUSTOMIZED REWRITING OF THE REQUEST
        // TODO IMPORTANT, EXTERNALIZE THIS LOGIC !!!! REDESIGNE THE INNER PARTIAL MATCHER CORE _
        // hmm really , think again about the interactions of approximationcontexts, termreplacmentcontexts
        // but this has to be improved , changes in rewriting logic should be acomplished only by modifiing contexts
        // and strategies

        if (currentContext instanceof RequestConceptsReplacementApproximationContext) {
            // cast down
            RequestConceptsReplacementApproximationContext requestReplacementContext = (RequestConceptsReplacementApproximationContext) currentContext;

            Map<URI, URI> replacementMap = requestReplacementContext.getConceptReplacingMap();

            // unfold the request
            OWLDescription unfoldedRequest = unfolder.unfoldOWLConcept(requestClass).iterator().next();

            //approximate the request class
            //first rewrite with the tradional methods
            OWLDescription approximatedRequest = owlTermRewriter.rewrite(new PartialDescriptionRewritingApproximationTypeContext(unfoldedRequest, ApproximationType.UPPER, currentContext.getNotSApproximationContext().getNotS()));//Request MUST be upper approximated

            if (replacementMap != null && !replacementMap.isEmpty()) {
                System.out.println("REPLACING TAXONOMIC CONCPETS IN THE REQUEST ACCORDING TO SPECIFIED REPLACEMENT MAP IN CONTEXT : " + requestReplacementContext.toString());
                approximatedRequest = owlTermRewriter.rewrite(new OWLPartialRequestTaxConceptDescriptionRewritingContext(approximatedRequest,replacementMap));//Request MUST be upper approximated
            }
            // add approximated class to the ontology
            OWLClass dummy = owlOntologyManager.getOWLDataFactory().getOWLClass(URI.create(requestClass.getURI() + APPROXTAG + currentContext.hashCode()));

            Set<OWLDescription> equivalentClasses = new HashSet<OWLDescription>();
            equivalentClasses.add(dummy);
            equivalentClasses.add(approximatedRequest);

            // Now create the axiom
            OWLAxiom axiom = owlOntologyManager.getOWLDataFactory().getOWLEquivalentClassesAxiom(equivalentClasses);
            // We now add the axiom to the ontology, so that the ontology states that
            AddAxiom addAxiom = new AddAxiom(owlOntology, axiom);
            // We now use the manager to apply the change
            try {
                owlOntologyManager.applyChange(addAxiom);
            } catch (OWLOntologyChangeException e) {
                e.printStackTrace();//To change body of catch statement use File | Settings | File Templates.
            }

            return dummy;

        } else {

            // unfold the request
            OWLDescription unfoldedRequest = unfolder.unfoldOWLConcept(requestClass).iterator().next();

            //approximate the request class
            OWLDescription approximatedRequest = owlTermRewriter.rewrite(new PartialDescriptionRewritingApproximationTypeContext(unfoldedRequest, ApproximationType.UPPER, currentContext.getNotSApproximationContext().getNotS()));//Request MUST be upper approximated

            // add approximated class to the ontology
            OWLClass dummy = owlOntologyManager.getOWLDataFactory().getOWLClass(URI.create(requestClass.getURI() + APPROXTAG + currentContext.hashCode()));

            Set<OWLDescription> equivalentClasses = new HashSet<OWLDescription>();
            equivalentClasses.add(dummy);
            equivalentClasses.add(approximatedRequest);

            // Now create the axiom
            OWLAxiom axiom = owlOntologyManager.getOWLDataFactory().getOWLEquivalentClassesAxiom(equivalentClasses);
            // We now add the axiom to the ontology, so that the ontology states that
            AddAxiom addAxiom = new AddAxiom(owlOntology, axiom);
            // We now use the manager to apply the change
            try {
                owlOntologyManager.applyChange(addAxiom);
            } catch (OWLOntologyChangeException e) {
                e.printStackTrace();//To change body of catch statement use File | Settings | File Templates.
            }

            return dummy;
        }


    }

    public void setOwlOntologyManager(OWLOntologyManager owlOntologyManager) {
        this.owlOntologyManager = owlOntologyManager;
    }


    public void setReasoner(OWLReasoner reasoner) {
        this.reasoner = reasoner;
    }

    public Unfolder getUnfolder() {
        return unfolder;
    }


    public void setUnfolder(Unfolder unfolder) {
        this.unfolder = unfolder;
    }

    public void setOwlTermRewriter(IOWLTermRewriter owlTermRewriter) {
        this.owlTermRewriter = owlTermRewriter;
    }

    //    }
    //        return classesToApproximate;
    public boolean isCancelled() {
        return isCancelled;
    }

    public boolean isRunning() {
        return isRunning;
    }
    
}
