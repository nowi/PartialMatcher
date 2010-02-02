package de.unima.semweb.partialmatcher.core.rewriters;

import org.semanticweb.owl.model.OWLDescription;
import de.unima.semweb.partialmatcher.core.strategies.contexts.GenericOWLDescriptionRewritingContext;

/**
 * User: nowi
 * Date: 21.11.2008
 * Time: 14:23:59
 */
public interface OWLDescriptionRewritingContext {
    GenericOWLDescriptionRewritingContext copy();

    GenericOWLDescriptionRewritingContext copy(OWLDescription newOwlDescription);
}
