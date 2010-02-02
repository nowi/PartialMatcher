package de.unima.semweb.partialmatcher.core.rewriters;

import org.semanticweb.owl.model.OWLDescription;
import de.unima.semweb.partialmatcher.core.strategies.contexts.GenericOWLDescriptionRewritingContext;

/**
 * User: nowi
 * Date: 29.02.2008
 * Time: 20:28:01
 */
public interface OWLDescriptionRewritingStrategy {
    public OWLDescription rewrite(GenericOWLDescriptionRewritingContext context);
}
