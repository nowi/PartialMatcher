package de.unima.semweb.partialmatcher.core;

import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLOntology;
import de.unima.semweb.partialmatcher.core.strategies.contexts.GenericOWLDescriptionRewritingContext;
import de.unima.semweb.partialmatcher.core.rewriters.OWLDescriptionRewritingStrategy;

/**
 * User: nowi
 * Date: 03.03.2008
 * Time: 14:35:56
 */
public interface IOWLTermRewriter {
    OWLDescription rewrite(GenericOWLDescriptionRewritingContext context);

    void setOwlOntology(OWLOntology owlOntology);

    void init();

    void setRewritingStrategy(OWLDescriptionRewritingStrategy rewritingStrategy);
}
