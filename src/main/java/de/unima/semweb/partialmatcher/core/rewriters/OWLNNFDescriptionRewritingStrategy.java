package de.unima.semweb.partialmatcher.core.rewriters;

import de.unima.semweb.partialmatcher.util.OWLUtils;
import de.unima.semweb.partialmatcher.core.strategies.contexts.GenericOWLDescriptionRewritingContext;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLObjectComplementOf;

/**
 * User: nowi
 * Date: 29.02.2008
 * Time: 20:28:58
 */
public class OWLNNFDescriptionRewritingStrategy implements OWLDescriptionRewritingStrategy {

    public OWLDescription rewrite(GenericOWLDescriptionRewritingContext context) {
        OWLDescription description = context.getOwlDescription();
        // do cheks based on runtime type
        if (description instanceof OWLObjectComplementOf) {
            // check if filler is atomic , if not negate the filler , if atomic return null
            OWLObjectComplementOf complement = (OWLObjectComplementOf)description;
            OWLDescription operand = complement.getOperand();
            if (OWLUtils.isNested(operand)) {
                // negate the operand
                return OWLUtils.negate(operand);
            } else {
                // the operand is atomic , this term is in nnf
                return null;
            }

        } return null; // its not an complement , nothing to replace , return null

    }
  
}
