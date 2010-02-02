package de.unima.semweb.partialmatcher.core.strategies.contexts;

import org.semanticweb.owl.model.OWLDescription;

import java.net.URI;
import java.util.Set;

import de.unima.semweb.partialmatcher.core.strategies.contexts.OWLPartialReplacmentContext;
import de.unima.semweb.partialmatcher.core.ApproximationType;

/**
 * User: nowi
 * Date: 22.02.2008
 * Time: 15:18:50
 */
public class PartialDescriptionRewritingApproximationTypeContext extends OWLPartialReplacmentContext {
    private final ApproximationType approximationType;

    public PartialDescriptionRewritingApproximationTypeContext(OWLDescription original, ApproximationType approximationType, Set<URI> notS) {
        super(original, notS);
        this.approximationType = approximationType;
    }

    public ApproximationType getApproximationType() {
        return approximationType;
    }

    @Override
    public GenericOWLDescriptionRewritingContext copy() {
        return new PartialDescriptionRewritingApproximationTypeContext(owlDescription, approximationType, notS);    //To change body of overridden methods use File | Settings | File Templates.;
    }

    @Override
    public GenericOWLDescriptionRewritingContext copy(OWLDescription newOwlDescription) {
        return new PartialDescriptionRewritingApproximationTypeContext(newOwlDescription, approximationType, notS);    //To change body of overridden methods use File | Settings | File Templates.;
    }
}
