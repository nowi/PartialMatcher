package de.unima.semweb.partialmatcher.core.rewriters;

import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChangeException;

/**
 * User: nowi
 * Date: 07.03.2008
 * Time: 21:22:52
 */
public interface OntologyRewriter {
    OWLOntology rewriteOntology() throws OWLOntologyChangeException;
}
