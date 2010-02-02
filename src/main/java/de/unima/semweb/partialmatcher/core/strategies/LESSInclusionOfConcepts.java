package de.unima.semweb.partialmatcher.core.strategies;

import de.unima.semweb.partialmatcher.core.strategies.ApproximationStrategyAdapter;
import de.unima.semweb.partialmatcher.core.ApproximationContext;
import de.unima.semweb.partialmatcher.core.NotSApproximationContext;
import de.unima.semweb.partialmatcher.core.CTASetApproximationContext;

import java.net.URI;
import java.util.*;

/**
 * User: nowi
 * Date: 06.05.2008
 * Time: 14:15:35
 * <p/>
 * This Strategy does random inclusion of propertys into The not S set
 * This strategy does not mutatet the set of CTA
 */
public class LESSInclusionOfConcepts extends ApproximationStrategyAdapter {
    
    private final Map<URI, Integer> conceptCardinalityMap;


    public LESSInclusionOfConcepts(ApproximationContext approximationContext, Map<URI, Integer> conceptCardinalityMap) {
        super(approximationContext);
        this.conceptCardinalityMap = conceptCardinalityMap;
    }


    protected NotSApproximationContext mutateNotSApproximationContext(final NotSApproximationContext notSApproximationContext) {
        // include new concepts into the set, include the one with the highest cardinality

        final URI nextURI = getNextConceptURI(notSApproximationContext);

        // if nextURI is null , then we are finisehd permutatuing

        final NotSApproximationContext mutatedNotSApproximationContext;
        if (nextURI != null) {
            Set<URI> newNotS = new HashSet<URI>();
            newNotS.addAll(notSApproximationContext.getNotS());
            newNotS.add(nextURI);
            mutatedNotSApproximationContext = new NotSApproximationContext(notSApproximationContext.getRequestClass(), newNotS, notSApproximationContext.getVocabulary());

        } else {
            // threre are no more concetps to include , return the old context
            mutatedNotSApproximationContext = new NotSApproximationContext(notSApproximationContext);

        }

        return mutatedNotSApproximationContext;
    }

    protected URI getNextConceptURI(NotSApproximationContext notSApproximationContext) {
        URI nextURI = null;
        if (defaultConceptOrder.iterator().hasNext()) {
            // use the next concept from the default order list
            nextURI = defaultConceptOrder.iterator().next();
            // remove this uri from the default concepts
            defaultConceptOrder.remove(nextURI);

        } else {
            List<URI> unusedURIs = new LinkedList<URI>(notSApproximationContext.getVocabulary());
            unusedURIs.removeAll(notSApproximationContext.getNotS());

            // check if there are still unused uris
            if (!unusedURIs.isEmpty()) {
                // sort the unusedURIS
                Collections.<URI>sort(unusedURIs, new Comparator<URI>() {
                    public int compare(URI o1, URI o2) {
                        Integer cardinality1 = conceptCardinalityMap.get(o1);
                        Integer cardinality2 = conceptCardinalityMap.get(o2);
                        if (cardinality1 < cardinality2) return -1;
                        else if (cardinality1 > cardinality2) return 1;
                        else return 0;
                    }
                });

                nextURI = unusedURIs.iterator().next();

            }

        }
        return nextURI;
    }

    protected CTASetApproximationContext mutateCTASetApproximationContext(final CTASetApproximationContext ctaSetApproximationContext) {
        // just clone
        return new CTASetApproximationContext(ctaSetApproximationContext);
    }


    public Boolean isFinished() {
        return finished;
    }

    public Map<URI, Integer> getConceptCardinalityMap() {
        return conceptCardinalityMap;
    }
}
