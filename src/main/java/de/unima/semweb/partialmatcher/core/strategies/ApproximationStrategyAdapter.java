package de.unima.semweb.partialmatcher.core.strategies;

import de.unima.semweb.partialmatcher.core.ApproximationStrategy;
import de.unima.semweb.partialmatcher.core.ApproximationContext;
import de.unima.semweb.partialmatcher.core.NotSApproximationContext;
import de.unima.semweb.partialmatcher.core.CTASetApproximationContext;

import java.net.URI;
import java.util.*;

/**
 * User: nowi
 * Date: 16.04.2009
 * Time: 11:02:31
 */
public abstract class ApproximationStrategyAdapter implements ApproximationStrategy {// history of approximation contexts
    protected Stack<ApproximationContext> history = new Stack<ApproximationContext>();
    protected ApproximationContext initialApproximationContext;
    protected Boolean finished = false;

    protected List<URI> defaultConceptOrder;

    public ApproximationStrategyAdapter(ApproximationContext approximationContext) {
        initialApproximationContext = approximationContext;
        defaultConceptOrder = new LinkedList<URI>();
        history.push(approximationContext);

    }

    public ApproximationContext createNextContext() {

        final ApproximationContext approximationContext = history.peek();
        if (approximationContext != null) {
            // perform logic
            final ApproximationContext currentApproximationContext = mutate(approximationContext);
            // save in history
            history.push(currentApproximationContext);
            return currentApproximationContext;  //To change body of implemented methods use File | Settings | File Templates.
        } else {
            // the last context has been null , this Strategy will not produce more permutations
            throw new IllegalStateException("The last context has been null ,this Strategy will not produce more permutations");
        }


    }

    public ApproximationContext createNextContext(ApproximationContext lastApproximationContext) {


        if (lastApproximationContext != null) {
            // perform logic
            final ApproximationContext currentContext = mutate(lastApproximationContext);
            // save in history
            history.push(currentContext);
            return currentContext;  //To change body of implemented methods use File | Settings | File Templates.
        } else {
            // the last context has been null , this Strategy will not produce more permutations
            throw new IllegalStateException("The last context has been null ,this Strategy will not produce more permutations");
        }


    }

    public ApproximationContext getInitialContext() {
        return initialApproximationContext;
    }

    protected ApproximationContext mutate(ApproximationContext lastApproximationContext) {


        // first mutate the notS context
        final NotSApproximationContext mutatedNotSApproximationContext =  mutateNotSApproximationContext(lastApproximationContext.getNotSApproximationContext());
        // mutate the CTASetApproximation context
        final CTASetApproximationContext mutatedCtaSetApproximationContext = mutateCTASetApproximationContext(lastApproximationContext.getCtaSetApproximationContext());

        // if both context do not differ from their previours ones this strategy is done
        // mutating their contexts
        if (mutatedCtaSetApproximationContext.equals(lastApproximationContext.getCtaSetApproximationContext()) && mutatedNotSApproximationContext.equals(lastApproximationContext.getNotSApproximationContext())) {
            finished = true;
        }


        // cerate and return the new ApporximationContext vlaue object
        return new ApproximationContext(lastApproximationContext.getRequestClass(), mutatedNotSApproximationContext, mutatedCtaSetApproximationContext);



    }

    abstract NotSApproximationContext mutateNotSApproximationContext(final NotSApproximationContext notSApproximationContext);

    abstract CTASetApproximationContext mutateCTASetApproximationContext(final CTASetApproximationContext ctaSetApproximationContext);

    public Boolean isFinished() {
        return finished;
    }

    public List<URI> getDefaultConceptOrder() {
        return defaultConceptOrder;
    }

    public void setDefaultConceptOrder(List<URI> defaultConceptOrder) {
        this.defaultConceptOrder = defaultConceptOrder;
    }
}
