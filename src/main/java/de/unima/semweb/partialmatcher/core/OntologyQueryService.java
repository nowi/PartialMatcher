package de.unima.semweb.partialmatcher.core;

import org.semanticweb.owl.model.*;

import java.util.Set;
import java.util.List;

import com.google.common.collect.Multimap;

/**
 * User: nowi
 * Date: 25.05.2009
 * Time: 10:14:52
 */
public interface OntologyQueryService {
    Set<OWLClass> getDescendantClasses(OWLClass owlClass);

    Set<OWLIndividual> getIndividuals(OWLClass owlClass,boolean onlyDirect);

    Set<OWLClass> getEquivalentClasses(OWLClass owlClass);

    Set<OWLClass> getSubClasses(OWLClass owlClass);

    Set<OWLClass> getReferencedClasses(OWLClass owlClass);

    Set<OWLProperty> getPropertiesForDomain(final OWLClass owlClass);

    Set<OWLDescription> getClassesInRange(final OWLObjectProperty owlObjectProp);

    OWLObjectProperty createOWLObjectPropertyWithURI(String uriString);
    OWLDataProperty createOWLDataPropertyWithURI(String uriString);

    OWLClass createOWLClassWithURI(String uriString);

    Multimap<OWLClass, OWLClass> getTaxonomyMap();

    OWLOntology getTaxonomyOntology();

    String getRDFWithSERQLContructQuery(String query);

    public List<OWLClass> getOwlClassesWithSERQLQuery(String query, String owlClassColumnName);
}
