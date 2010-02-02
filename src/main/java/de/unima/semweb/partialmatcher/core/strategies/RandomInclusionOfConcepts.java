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
 *
 * This Strategy does random inclusion of propertys into The not S set
 * This strategy does not mutatet the set of CTA
 *
 *
 */
public class RandomInclusionOfConcepts extends ApproximationStrategyAdapter {


    public RandomInclusionOfConcepts(ApproximationContext approximationContext) {
        super(approximationContext);
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

    protected  URI getNextConceptURI(NotSApproximationContext notSApproximationContext) {
        URI nextURI = null;
        if (defaultConceptOrder.iterator().hasNext()) {
            // use the next concept from the default order list
            nextURI = defaultConceptOrder.iterator().next();
            defaultConceptOrder.remove(nextURI);

        } else {
            Set<URI> unusedURIs = new HashSet<URI>(notSApproximationContext.getVocabulary());
            unusedURIs.removeAll(notSApproximationContext.getNotS());

            // check if there are still unused uris
            if (!unusedURIs.isEmpty()) {
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
}
