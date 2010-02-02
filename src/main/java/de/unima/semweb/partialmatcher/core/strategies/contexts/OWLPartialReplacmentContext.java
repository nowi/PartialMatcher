package de.unima.semweb.partialmatcher.core.strategies.contexts;

import org.semanticweb.owl.model.OWLDescription;

import java.net.URI;
import java.util.Set;

import de.unima.semweb.partialmatcher.core.strategies.contexts.GenericOWLDescriptionRewritingContext;

/**
 * User: nowi
 * Date: 21.11.2008
 * Time: 14:12:00
 */
public class OWLPartialReplacmentContext extends GenericOWLDescriptionRewritingContext {
    final Set<URI> notS;

    public OWLPartialReplacmentContext(OWLDescription owlDescription, Set<URI> notS) {
        super(owlDescription);
        this.notS = notS;
    }

    public Set<URI> getNotS() {
        return notS;
    }

    @Override
    public GenericOWLDescriptionRewritingContext copy() {
        return new OWLPartialReplacmentContext(owlDescription,notS);
    }

    @Override
    public GenericOWLDescriptionRewritingContext copy(OWLDescription newOwlDescription) {
        return new OWLPartialReplacmentContext(newOwlDescription, notS);
    }
}
