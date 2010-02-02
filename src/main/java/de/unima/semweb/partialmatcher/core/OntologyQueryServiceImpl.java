package de.unima.semweb.partialmatcher.core;

import org.semanticweb.owl.model.*;
import org.semanticweb.owl.inference.OWLReasonerAdapter;
import org.semanticweb.owl.util.InferredAxiomGenerator;
import org.semanticweb.owl.util.InferredSubClassAxiomGenerator;
import org.semanticweb.owl.util.InferredOntologyGenerator;
import org.mindswap.pellet.owlapi.Reasoner;
import org.mindswap.pellet.PelletOptions;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.query.*;
import org.openrdf.model.Value;
import org.openrdf.OpenRDFException;

import java.util.*;
import java.net.URI;
import java.net.URLEncoder;
import java.io.File;
import java.io.UnsupportedEncodingException;

import de.unima.semweb.partialmatcher.util.OWLUtils;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.HashMultimap;

/**
 * User: nowi
 * Date: 18.04.2009
 * Time: 12:51:43
 */
public class OntologyQueryServiceImpl implements OntologyQueryService {

    // COLLABORTATORS
    private OWLOntologyManager owlOntologyManager;


    // pellet reasoner
    private Reasoner reasoner;

    private HTTPRepository sesameRepository;

    private String sesameServer;

    private String repositoryID;

    private String ontologyPath;
    
    private boolean UNAEnabled;

    // seaseame repository


    public void init() {
        try {
            initSesameRepository();

            // load the onotlogy
            // load the pepperl ontology

            // create reasoner
            // We need to create an instance of OWLReasoner.  OWLReasoner provides the basic
            // query functionality that we need, for example the ability obtain the subclasses
            // of a class etc.  See the createOWLReasoner method implementation for more details
            // on how to instantiate the reasoner
            PelletOptions.USE_UNIQUE_NAME_ASSUMPTION = isUNAEnabled();
            reasoner = (Reasoner) OWLUtils.createOWLReasoner(owlOntologyManager);
            // load the reasoner with the ontologies
            reasoner.loadOntologies(owlOntologyManager.getOntologies());
        } catch (RepositoryException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    private void initSesameRepository() throws RepositoryException {
        sesameRepository = new HTTPRepository(sesameServer, repositoryID);
        sesameRepository.initialize();
    }

    public List<OWLClass> getOwlClassesWithSERQLQuery(String query, String owlClassColumnName) {
        List<OWLClass> found = new LinkedList<OWLClass>();

        try {
            RepositoryConnection con = sesameRepository.getConnection();

            try {

                TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SERQL, query);
                TupleQueryResult result = tupleQuery.evaluate();
                try {

                    // get the matchingOWLClasses
                    while (result.hasNext()) {

                        BindingSet bindingSet = result.next();
                        Value valueOfTax = bindingSet.getValue(owlClassColumnName);
                        OWLClass owlClass = createOWLClassWithURI(valueOfTax.stringValue());

                        if (owlClass != null) {
                            found.add(owlClass);
                        }

                    }

                }
                catch (Exception e) {
                    // handle exception
                    e.printStackTrace();

                }
                finally {
                    result.close();
                }
            }

            catch (Exception e) {
                // handle exception
                e.printStackTrace();

            }
            finally {
                con.close();


            }
        }

        catch (OpenRDFException e) {
            // handle e
            e.printStackTrace();

        }

        return found;

    }


    public String getRDFWithSERQLContructQuery(String query) {
        String rdfString = null;
        StringBuffer buf = new StringBuffer();
        try {
            RepositoryConnection con = sesameRepository.getConnection();

            try {

                TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SERQL, query);
                TupleQueryResult result = tupleQuery.evaluate();
                try {

                    // get the matchingOWLClasses
                    while (result.hasNext()) {

                        BindingSet bindingSet = result.next();
                        Set<String> bindingNames = bindingSet.getBindingNames();
                        for (String bindingName : bindingNames) {
                            Value value = bindingSet.getValue(bindingName);
                            buf.append(value);
                        }

                        buf.append("\n");

                    }
                    buf.append("\n");

                }
                catch (Exception e) {
                    // handle exception
                    e.printStackTrace();

                }
                finally {
                    result.close();
                }
            }

            catch (Exception e) {
                // handle exception
                e.printStackTrace();

            }
            finally {
                con.close();


            }
        }

        catch (OpenRDFException e) {
            // handle e
            e.printStackTrace();

        }


        rdfString = buf.toString();
        return rdfString;

    }


    public Set<OWLClass> getDescendantClasses(OWLClass owlClass) {
        return OWLReasonerAdapter.<OWLClass>flattenSetOfSets(reasoner.getDescendantClasses(owlClass));
    }


    public Set<OWLClass> getEquivalentClasses(OWLClass owlClass) {
        return reasoner.getEquivalentClasses(owlClass);
    }



    public Set<OWLIndividual> getIndividuals(OWLClass owlClass,boolean onlyDirect) {
        return reasoner.getIndividuals(owlClass,onlyDirect);
    }

    public Set<OWLClass> getSubClasses(OWLClass owlClass) {
        return OWLReasonerAdapter.<OWLClass>flattenSetOfSets(reasoner.getSubClasses(owlClass));
    }

   
    /**
     * Retrurns The complete taxonomy of the current ontology as a multimap
     * see http://google-collections.googlecode.com/svn/trunk/javadoc/index.html?com/google/common/collect/Multimap.html
     * for documentation of multimap
     * @return taxonomy of the main ontology as a multimap
     */
    public Multimap<OWLClass, OWLClass> getTaxonomyMap() {

        // get taxonomy owlOntology
        final OWLOntology taxOntology = getTaxonomyOntology();

        // create a new multimap
        Multimap<OWLClass, OWLClass> taxonomyMap = HashMultimap.create();

        for(OWLAxiom axiom : taxOntology.getAxioms() ) {
            if(axiom instanceof OWLSubClassAxiom) {
                OWLSubClassAxiom sca = (OWLSubClassAxiom) axiom;
                // needs unfolding ?
                final OWLDescription superClass = sca.getSuperClass();
                final OWLDescription subClass = sca.getSubClass();
                if ((superClass instanceof OWLClass) && (subClass instanceof OWLClass)) {
                    taxonomyMap.put(superClass.asOWLClass(), subClass.asOWLClass());
                } else {
                    // log this
                    System.out.println("This axiom needs to be folded first");
                }

            }

        }
        return taxonomyMap;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Retrurns The complete taxonomy of the current ontology as a multimap
     * see http://google-collections.googlecode.com/svn/trunk/javadoc/index.html?com/google/common/collect/Multimap.html
     * for documentation of multimap
     * @return taxonomy of the main ontology as a onltolgy
     */
    public OWLOntology getTaxonomyOntology() {
        // delegate to utils
        // always get the first ontology
        final OWLOntology sourceOntology = owlOntologyManager.getOntologies().iterator().next();
        return OWLUtils.getTaxonomyOntology(owlOntologyManager, reasoner, sourceOntology);
    }

    public Set<OWLClass> getReferencedClasses(OWLClass owlClass) {
        return reasoner.getClasses();
    }


    public Set<OWLProperty> getPropertiesForDomain(final OWLClass owlClass) {

        return new HashSet<OWLProperty>(){
            {
                // first get all object properties that have a domain containing the owlClass
                addAll(getObjectPropertiesForDomain(owlClass));
                // second get all data properties that have a domain containing the owlClass
                addAll(getDataPropertiesForDomain(owlClass));
            }
        };

    }

    public Set<OWLDescription> getClassesInRange(OWLObjectProperty owlObjectProp) {
        return reasoner.getRanges(owlObjectProp);
    }

    public Set<OWLDataRange> getDataTypesInRange(OWLDataProperty owlDataProperty) {
        return reasoner.getRanges(owlDataProperty);
    }


    public OWLClass createOWLClassWithURI(String uriString) {
        // protect agains illegal character , do url encoding
        String encoded = null;
        OWLClass owlClass = null;
        try {
            encoded = uriString.replace("&","_").replace("+","_").replace(" ","");
            owlClass = owlOntologyManager.getOWLDataFactory().getOWLClass(URI.create(encoded));
        } catch (IllegalArgumentException e1) {
            System.out.println("There was an error converting following uri to an owlClass : " +uriString);
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new IllegalArgumentException("Error encoding the provided uri to a valid OWLClass",e1);
        }

        return owlClass;
    }



    public OWLObjectProperty createOWLObjectPropertyWithURI(String uriString) {
        return owlOntologyManager.getOWLDataFactory().getOWLObjectProperty(URI.create(uriString));
    }

    public OWLDataProperty createOWLDataPropertyWithURI(String uriString) {
        return owlOntologyManager.getOWLDataFactory().getOWLDataProperty(URI.create(uriString));
    }




    public OWLProperty createOWLPropertyWithURI(String uriString) {
        // first try to construct a object prop
        OWLProperty prop = null;

        try {
            prop = createOWLObjectPropertyWithURI(uriString);
        } catch (Exception e) {
            // try to construct a data property
            try {
                prop = createOWLDataPropertyWithURI(uriString);
            } catch (Exception e1) {
                // not a data and not an objectproperty

            }
        }

        return prop;
    }

    private Set<OWLObjectProperty> getObjectPropertiesForDomain(OWLClass owlClass) {
        final Set<OWLObjectProperty> ops = new HashSet<OWLObjectProperty>();
        for (OWLObjectProperty op : reasoner.getObjectProperties()) {
            Set<OWLClass> parentClasses = OWLReasonerAdapter.flattenSetOfSets(reasoner.getSuperClasses(owlClass));
            for(OWLClass parentClass : parentClasses) {
                if (OWLReasonerAdapter.flattenSetOfSets(reasoner.getDomains(op)).contains(parentClass)) {
                    // check if the owlclass Or any of its descendants is in the domain of this property
                    // the owl class is contained in the domain of this op
                    // add this op
                    ops.add(op);
                }

            }
        }
        return ops;
    }

    private Set<OWLDataProperty> getDataPropertiesForDomain(OWLClass owlClass) {
        final Set<OWLDataProperty> dps = new HashSet<OWLDataProperty>();
        for (OWLDataProperty dp : reasoner.getDataProperties()) {
            if (OWLReasonerAdapter.flattenSetOfSets(reasoner.getDomains(dp)).contains(owlClass)) {
                // the owl class is contained in the domain of this op
                // add this op
                dps.add(dp);
            }
        }
        return dps;
    }


    public OWLOntologyManager getOwlOntologyManager() {
        return owlOntologyManager;
    }


    public void setOwlOntologyManager(OWLOntologyManager owlOntologyManager) {
        this.owlOntologyManager = owlOntologyManager;
    }

    
    public Reasoner getReasoner() {
        return reasoner;
    }

    public void setReasoner(Reasoner reasoner) {
        this.reasoner = reasoner;
    }

    public HTTPRepository getSesameRepository() {
        return sesameRepository;
    }

    public void setSesameRepository(HTTPRepository sesameRepository) {
        this.sesameRepository = sesameRepository;
    }

    public String getSesameServer() {
        return sesameServer;
    }

    public void setSesameServer(String sesameServer) {
        this.sesameServer = sesameServer;
    }

    public String getRepositoryID() {
        return repositoryID;
    }

    public void setRepositoryID(String repositoryID) {
        this.repositoryID = repositoryID;
    }

    public String getOntologyPath() {
        return ontologyPath;
    }

    public void setOntologyPath(String ontologyPath) {
        this.ontologyPath = ontologyPath;
    }


    public boolean isUNAEnabled() {
        return UNAEnabled;
    }

    public void setUNAEnabled(boolean UNAEnabled) {
        this.UNAEnabled = UNAEnabled;
    }
}
