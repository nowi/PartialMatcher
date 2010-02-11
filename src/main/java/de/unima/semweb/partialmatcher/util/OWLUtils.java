package de.unima.semweb.partialmatcher.util;


import de.unima.semweb.partialmatcher.core.MyJXPathExtenstionFunctions;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.iterators.FilterIterator;
import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.JXPathContext;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.*;
import org.semanticweb.owl.util.*;
import org.mindswap.pellet.owlapi.Reasoner;
import uk.ac.manchester.cs.owl.inference.dig11.DIGReasoner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.io.File;
import java.io.IOException;

/**
 * User: nowi
 * Date: 23.01.2008
 * Time: 13:15:28
 */
@SuppressWarnings({"WeakerAccess"})
public class OWLUtils {
    public static final String OWLECLASSONTOLOGYPATH = "file:///Users/nowi/Documents/Hiwi/PartialMatcher/files/eclassOWL_51en/eclass_51en.owl";

    public static final String COMPUTER_ONTOLOGY1 = "file:///computer_final.owl";
    public static final String PIZZA = "file:///pizza.owl";
    public static String COMPUTER_ONTOLOGY2 = "file:///computer_final3.owl";
    public static String ALC_ONTOLOGY = "file:///alc.owl";
    public static String FOODSWAP = "file:///alc1.owl";
    public static String FOODSWAP_PARTITIONED = "file:///foodswap_partitioned.owl";
    public static String FOODSWAP_PARTITIONEDDEF = "file:///foodswapDefpartitioned.owl";

    public static String DICE_ONTOLOGY = "file:///dice.owl";
    public static String DICE_NODOMAIN_ONTOLOGY = "file:///dice_ded_no-domain.owl";

    public static String PEPPERLECLASSOWL = "file:///eclass_51en.owl";
    public static String PEPPERLECLASSOWLSTRIPPED = "file:///pepperlstripped.owl";

    public static URL DIGURL;

    static {
        try {
            DIGURL = new URL("http://localhost:3490");
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    public static OWLOntology cloneOntology(OWLOntology ontology) throws OWLOntologyCreationException {
        // We first need to obtain a copy of an OWLOntologyManager, which, as the
        // name suggests, manages a set of ontologies.  An ontology is unique within
        // an ontology manager.  To load multiple copies of an ontology, multiple managers
        // would have to be used.
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

        // Now create the ontology - we use the ontology URI (not the physical URI)

        return manager.createOntology(ontology.getURI());

    }


    public static OWLReasoner createOWLReasoner(OWLOntologyManager man) {
        try {
            // The following code is a little overly complicated.  The reason for using
            // reflection to create an instance of pellet is so that there is no compile time
            // dependency (since the pellet libraries aren't contained in the OWL API repository).
            // Normally, one would simply create an instance using the following incantation:
            //
            //     OWLReasoner reasoner = new Reasoner()
            //
            // Where the full class name for Reasoner is org.mindswap.pellet.owlapi.Reasoner
            //
            // Pellet requires the Pellet libraries  (pellet.jar, aterm-java-x.x.jar) and the
            // XSD libraries that are bundled with pellet: xsdlib.jar and relaxngDatatype.jar
            String reasonerClassName = "org.mindswap.pellet.owlapi.Reasoner";
            Class reasonerClass = Class.forName(reasonerClassName);
            Constructor<OWLReasoner> con = reasonerClass.getConstructor(OWLOntologyManager.class);
            return con.newInstance(man);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public static OWLReasoner createDIGReasoner(OWLOntologyManager man) {
        try {
            // The following code is a little overly complicated.  The reason for using
            // reflection to create an instance of pellet is so that there is no compile time
            // dependency (since the pellet libraries aren't contained in the OWL API repository).
            // Normally, one would simply create an instance using the following incantation:
            //
            //     OWLReasoner reasoner = new Reasoner()
            //
            // Where the full class name for Reasoner is org.mindswap.pellet.owlapi.Reasoner
            //
            // Pellet requires the Pellet libraries  (pellet.jar, aterm-java-x.x.jar) and the
            // XSD libraries that are bundled with pellet: xsdlib.jar and relaxngDatatype.jar
            String reasonerClassName = "uk.ac.manchester.cs.owl.inference.dig11.DIGReasoner";
            Class reasonerClass = Class.forName(reasonerClassName);
            Constructor<DIGReasoner> con = reasonerClass.getConstructor(OWLOntologyManager.class);
            DIGReasoner digReasoner = con.newInstance(man);

            // setup dig reasoner
            digReasoner.getReasoner().setReasonerURL(DIGURL);


            return digReasoner;
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }


    public static boolean isNested(OWLDescription description) {
        return (description instanceof OWLObjectIntersectionOf) || (description instanceof OWLObjectUnionOf) || (description instanceof OWLObjectSomeRestriction) || (description instanceof OWLObjectAllRestriction) || (description instanceof OWLObjectComplementOf);
    }


    // checks if a owldescription is nested according to given level. This means a description like
    // (X AND Y) has a nesting level of 1 , ((X AND Y) AND Z) has nesting level of 2
    public static boolean isNested(OWLDescription description, final int level) {
        int nestingLevel = nestingLevelRec(description);
        return nestingLevel > level;
    }

    private static int nestingLevelRec(OWLDescription description) {
        boolean isNested = (description instanceof OWLObjectIntersectionOf) || (description instanceof OWLObjectUnionOf) || (description instanceof OWLObjectSomeRestriction) || (description instanceof OWLObjectAllRestriction) || (description instanceof OWLObjectComplementOf);
        if (!isNested) { // end recursion
            return 1;
        } else {
            // recurse into operands of nested descriptions
            if (description instanceof OWLObjectIntersectionOf) {
                OWLObjectIntersectionOf owlObjectIntersectionOf = (OWLObjectIntersectionOf) description;
                List<Integer> nestingLevels = new ArrayList<Integer>();
                for (OWLDescription owlOperand : owlObjectIntersectionOf.getOperands()) {
                    nestingLevels.add(nestingLevelRec(owlOperand));
                }
                return 1 + Collections.max(nestingLevels);

            } else if (description instanceof OWLObjectUnionOf) {
                OWLObjectUnionOf objectUnionOf = (OWLObjectUnionOf) description;
                List<Integer> nestingLevels = new ArrayList<Integer>();
                for (OWLDescription owlOperand : objectUnionOf.getOperands()) {
                    nestingLevels.add(nestingLevelRec(owlOperand));
                }
                return 1 + Collections.max(nestingLevels);
            } else if (description instanceof OWLObjectSomeRestriction) {
                OWLObjectSomeRestriction objectSomeRestriction = (OWLObjectSomeRestriction) description;
                return 1 + nestingLevelRec(objectSomeRestriction.getFiller());
            } else if (description instanceof OWLObjectAllRestriction) {
                OWLObjectAllRestriction objectAllRestriction = (OWLObjectAllRestriction) description;
                return 1 + nestingLevelRec(objectAllRestriction.getFiller());
            } else if (description instanceof OWLObjectComplementOf) {
                OWLObjectComplementOf objectComplementOf = (OWLObjectComplementOf) description;
                return 1 + nestingLevelRec(objectComplementOf.getOperand());
            } else
                throw new IllegalArgumentException("Unknown OWLDescription type : " + description.getClass());
        }
    }


    public static boolean isNested(OWLSubClassAxiom subClassAxiom) {
        Set<OWLDescription> descriptions = subClassAxiom.getDescriptions();

        for (OWLDescription desc : descriptions) {
            if (isNested(desc)) return true;
        }

        // no complex found
        return false;
    }

    // checks if a owl subclass axiom contains
    public static boolean isNested(OWLSubClassAxiom subClassAxiom, int level) {
        Set<OWLDescription> descriptions = subClassAxiom.getDescriptions();
        // if ANY of the operands has a nesting level higher as the parameter level , return true
        // else false
        for (OWLDescription desc : descriptions) {
            if (isNested(desc, level)) return true;
        }
        // no nested description with higher nesting than the threshold found
        return false;
    }

    public static Iterator<OWLDescription> getComplexElements(Collection<OWLDescription> elements) {
        Iterator<OWLDescription> iterator = elements.iterator();
        Predicate isComplex = new Predicate() {
            public boolean evaluate(Object o) {
                return isNested((OWLDescription) o);
            }
        };
        return (Iterator<OWLDescription>) new FilterIterator(iterator, isComplex);


    }


    public static OWLDescription negate(OWLDescription description) {
        // TODO implement negation of Cardinality restricttions

        final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        final OWLDataFactory owlDataFactory = manager.getOWLDataFactory();

        if (description instanceof OWLObjectIntersectionOf) {
            OWLObjectIntersectionOf intersection = (OWLObjectIntersectionOf) description;
            Set<OWLDescription> negatedOperands = new HashSet<OWLDescription>();
            for (OWLDescription operand : intersection.getOperands()) {
                // negate each operand
                negatedOperands.add(negate(operand));
            }
            return owlDataFactory.getOWLObjectUnionOf(negatedOperands);

        } else if (description instanceof OWLObjectUnionOf) {
            OWLObjectUnionOf union = (OWLObjectUnionOf) description;
            Set<OWLDescription> negatedOperands = new HashSet<OWLDescription>();
            for (OWLDescription operand : union.getOperands()) {
                // negate each operand
                negatedOperands.add(negate(operand));
            }
            return owlDataFactory.getOWLObjectIntersectionOf(negatedOperands);

        } else if (description instanceof OWLObjectComplementOf) {
            OWLObjectComplementOf complement = (OWLObjectComplementOf) description;
            return complement.getOperand();

        } else if (description instanceof OWLObjectSomeRestriction) {
            OWLObjectSomeRestriction someR = (OWLObjectSomeRestriction) description;
            return owlDataFactory.getOWLObjectAllRestriction(someR.getProperty(), negate(someR.getFiller()));

        } else if (description instanceof OWLObjectAllRestriction) {
            OWLObjectAllRestriction allR = (OWLObjectAllRestriction) description;
            return owlDataFactory.getOWLObjectSomeRestriction(allR.getProperty(), negate(allR.getFiller()));


        } else if (description instanceof OWLClass) {
            OWLClass clazz = (OWLClass) description;
            if (clazz.isOWLNothing())
                return owlDataFactory.getOWLThing();
            else if (clazz.isOWLThing())
                return owlDataFactory.getOWLNothing();
            else return owlDataFactory.getOWLObjectComplementOf(description);


        } else if (description instanceof OWLObjectMinCardinalityRestriction) {
            throw new IllegalArgumentException("Min Restrictions are currently not supported");


        } else if (description instanceof OWLObjectMaxCardinalityRestriction) {
            throw new IllegalArgumentException("Max Restrictions are currently not supported");


        } else return description; // ignore


    }


    public static List<OWLAxiomChange> createReplaceAxiomEvents(OWLOntology ontology, OWLAxiom original, OWLAxiom replacment) throws OWLOntologyChangeException {
        // create a change event
        RemoveAxiom removeAxiom = new RemoveAxiom(ontology, original);
        AddAxiom addAxiom = new AddAxiom(ontology, replacment);
        return Arrays.<OWLAxiomChange>asList(removeAxiom, addAxiom);
    }


    public static OWLOntologyManager createOntologyManager(String fileName) throws OWLOntologyCreationException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        // We load an ontology from a physical URI - in this case we'll load the pizza
        // ontology.
        URI physicalURI = URI.create(fileName);
        // Now getAllMatches the manager to load the ontology
        manager.loadOntologyFromPhysicalURI(physicalURI);
        return manager;
    }

    /**
     * Determines the Highest Number in Ontology and saves it to the internal attribute "max"
     * In bigger Ontologies, this may take some time. You should call this method before approximating!
     *
     * @param ontology -- The ontology for which the highest number in any ONR is determined
     * @return ontologyMaxQNR -- The highest cardinatlity of a QNR
     */
    public static int determineHighestNumberInOntology(OWLOntology ontology) {
// get all referenced OWLClasses in this Ontology
        int ontologyMaxQNR = 0;
        MyOWLCardinalityMaxFunctionVisitor visitor = new MyOWLCardinalityMaxFunctionVisitor();
        for (OWLClass cls : ontology.getReferencedClasses()) {
            Set<OWLDescription> s = ((OWLClass) cls).getEquivalentClasses(ontology);
            if (!s.isEmpty()) {
                for (OWLDescription owlDescription : s) {

                    if (owlDescription instanceof OWLObjectSomeRestriction)
                        visitor.visit((OWLObjectSomeRestriction) owlDescription);
                    else if (owlDescription instanceof OWLObjectAllRestriction) {
                        visitor.visit((OWLObjectAllRestriction) owlDescription);
                    } else {
                        throw new IllegalArgumentException("Unknown OWLDescription structure describing this catalog entityt");
                    }

                }
            }


        }
        // get the computed max
        ontologyMaxQNR = visitor.getMax();
        return ontologyMaxQNR;

    }


    public static OWLOntology getTaxonomyOntology(OWLOntologyManager owlOntologyManager, Reasoner reasoner, OWLOntology sourceOntology) {
        try {

            // To generate an inferred sourceOntology we use implementations of inferred axiom generators
            // to generate the parts of the sourceOntology we want (e.g. subclass axioms, equivalent classes
            // axioms, class assertion axiom etc. - see the org.semanticweb.owl.util package for more
            // implementations).
            // Set up our list of inferred axiom generators

            List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
            gens.add(new InferredSubClassAxiomGenerator());

            // Put the inferred axiomns into a fresh empty sourceOntology - note that there
            // is nothing stopping us stuffing them back into the original asserted sourceOntology
            // if we wanted to do this.
            OWLOntology infOnt = owlOntologyManager.createOntology(URI.create(sourceOntology.getURI() + "_inferred"));

            // Now get the inferred sourceOntology generator to generate some inferred axioms
            // for us (into our fresh sourceOntology).  We specify the reasoner that we want
            // to use and the inferred axiom generators that we want to use.
            InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner, gens);
            iog.fillOntology(owlOntologyManager, infOnt);

            // Save the inferred sourceOntology. (Replace the URI with one that is appropriate for your setup)
            return infOnt;

        }
        catch (InferredAxiomGeneratorException e) {
            e.printStackTrace();
        }
        catch (OWLOntologyChangeException e) {
            e.printStackTrace();
        }
        catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        catch (OWLReasonerException e) {
            e.printStackTrace();
        }

        return sourceOntology;
    }


    private static class MyOWLCardinalityMaxFunctionVisitor extends OWLDescriptionVisitorAdapter {

        static int max = 0;

        static public int getMax() {
            return max;
        }

        @Override
        public void visit(OWLObjectSomeRestriction restriction) {
            super.visit(restriction);
            try {
                DynDispatch.sendMessage("visit", this, restriction.getFiller());
            } catch (Throwable throwable) {
                throwable.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        @Override
        public void visit(OWLObjectAllRestriction restriction) {
            super.visit(restriction);
            // descend further down this owl structure visit filler
            try {
                DynDispatch.sendMessage("visit", this, restriction.getFiller());
            } catch (Throwable throwable) {
                throwable.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        @Override
        public void visit(OWLObjectMinCardinalityRestriction restriction) {
            super.visit(restriction);
            if (restriction.getCardinality() >= max)
                max = restriction.getCardinality() + 1;
            // descend further down this owl structure visit filler
            try {
                DynDispatch.sendMessage("visit", this, restriction.getFiller());
            } catch (Throwable throwable) {
                throwable.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }


        }


        @Override
        public void visit(OWLObjectExactCardinalityRestriction restriction) {
            super.visit(restriction);
            if (restriction.getCardinality() >= max)
                max = restriction.getCardinality() + 1;

            try {
                DynDispatch.sendMessage("visit", this, restriction.getFiller());
            } catch (Throwable throwable) {
                throwable.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }

        @Override
        public void visit(OWLObjectMaxCardinalityRestriction restriction) {
            super.visit(restriction);
            if (restriction.getCardinality() >= max)
                max = restriction.getCardinality() + 1;
            // descend further down this owl structure visit filler
            try {
                DynDispatch.sendMessage("visit", this, restriction.getFiller());
            } catch (Throwable throwable) {
                throwable.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        @Override
        public void visit(OWLObjectIntersectionOf owlObjectIntersectionOf) {
            super.visit(owlObjectIntersectionOf);
            // descend further down this owl structure visit filler

            // descend further down this owl structure visit filler
            for (OWLDescription operand : owlObjectIntersectionOf.getOperands()) {
                try {
                    DynDispatch.sendMessage("visit", this, operand);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }


        }

        @Override
        public void visit(OWLObjectUnionOf owlObjectUnionOf) {
            super.visit(owlObjectUnionOf);
            // descend further down this owl structure visit filler
            for (OWLDescription operand : owlObjectUnionOf.getOperands()) {
                try {
                    DynDispatch.sendMessage("visit", this, operand);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }

        @Override
        public void visit(OWLObjectComplementOf owlObjectComplementOf) {
            super.visit(owlObjectComplementOf);    //To change body of overridden methods use File | Settings | File Templates.
            try {
                DynDispatch.sendMessage("visit", this, owlObjectComplementOf.getOperand());
            } catch (Throwable throwable) {
                throwable.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }


}
