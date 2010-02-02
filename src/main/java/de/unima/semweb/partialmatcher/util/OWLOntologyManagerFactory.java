package de.unima.semweb.partialmatcher.util;

import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.apibinding.OWLManager;

import java.net.URI;

/**
 * User: nowi
 * Date: 18.04.2009
 * Time: 20:54:19
 */
public class OWLOntologyManagerFactory {
    public String getOntologyPath() {
        return ontologyPath;
    }

    public void setOntologyPath(String ontologyPath) {
        this.ontologyPath = ontologyPath;
    }

    String ontologyPath;

    OWLOntologyManager createInstance() throws OWLOntologyCreationException {
        // create a instance that is ready to use
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        // We load an ontology from a physical URI - in this case we'll load the pizza
        // ontology.
        URI physicalURI = URI.create(ontologyPath);
        // Now getAllMatches the manager to load the ontology
        manager.loadOntologyFromPhysicalURI(physicalURI);

        return (OWLOntologyManager)manager;


    }


}
