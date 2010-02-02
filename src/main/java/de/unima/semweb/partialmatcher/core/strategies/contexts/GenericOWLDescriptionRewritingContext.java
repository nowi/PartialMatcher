package de.unima.semweb.partialmatcher.core.strategies.contexts;

import org.semanticweb.owl.model.OWLDescription;
import de.unima.semweb.partialmatcher.core.rewriters.OWLDescriptionRewritingContext;

/**OWLReplacementContext is a object holding information that is needed by termrewriting strategies
 * this object is immutable
 *
 * User: nowi
 * Date: 22.02.2008
 * Time: 15:17:12
 */

public class GenericOWLDescriptionRewritingContext implements OWLDescriptionRewritingContext {

    protected final OWLDescription owlDescription;

    public GenericOWLDescriptionRewritingContext(OWLDescription owlDescription) {
        this.owlDescription = owlDescription;
    }

    public OWLDescription getOwlDescription() {
        return owlDescription;
    }


    public GenericOWLDescriptionRewritingContext copy() {
        return new GenericOWLDescriptionRewritingContext(owlDescription);
    }

    public GenericOWLDescriptionRewritingContext copy(OWLDescription newOwlDescription) {
        return new GenericOWLDescriptionRewritingContext(newOwlDescription);
    }

}
