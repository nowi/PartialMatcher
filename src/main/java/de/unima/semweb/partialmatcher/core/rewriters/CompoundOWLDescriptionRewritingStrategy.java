package de.unima.semweb.partialmatcher.core.rewriters;

import org.semanticweb.owl.model.OWLDescription;

import java.util.List;

import de.unima.semweb.partialmatcher.core.strategies.contexts.GenericOWLDescriptionRewritingContext;

/**
 * User: nowi
 * Date: 14.07.2009
 * Time: 10:32:10
 */
/**
 * User: nowi
 * Date: 20.11.2008
 * Time: 22:02:34
 */
public class CompoundOWLDescriptionRewritingStrategy implements OWLDescriptionRewritingStrategy {
    List<OWLDescriptionRewritingStrategy> strategies;

    public CompoundOWLDescriptionRewritingStrategy(List<OWLDescriptionRewritingStrategy> strategies) {
        this.strategies = strategies;
    }


    public OWLDescription rewrite(GenericOWLDescriptionRewritingContext context) {
        GenericOWLDescriptionRewritingContext nextContext = context;

        for (OWLDescriptionRewritingStrategy strategy : strategies) {
            nextContext = nextContext.copy(strategy.rewrite(nextContext));
        }

        return nextContext.getOwlDescription();
    }
}
