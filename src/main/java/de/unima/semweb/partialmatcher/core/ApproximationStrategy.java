package de.unima.semweb.partialmatcher.core;

import java.net.URI;
import java.util.List;

/**
 * User: nowi
 * Date: 06.05.2008
 * Time: 14:11:56
 */
public interface ApproximationStrategy {
    public ApproximationContext createNextContext();
    public ApproximationContext createNextContext(ApproximationContext approximationContext);
    public ApproximationContext getInitialContext();

    Boolean isFinished();

    List<URI> getDefaultConceptOrder();

    void setDefaultConceptOrder(List<URI> defaultConceptOrder);
}
