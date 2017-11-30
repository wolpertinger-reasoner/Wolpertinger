package org.semanticweb.wolpertinger;

import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

/**
 * Simple test based on the example hierarchy defined in OWLReasoner documentation.
 */
public class HierarchyTest 
    extends TestCase
{
	private static String PREFIX = "http://www.semanticweb.org/wolpertinger";
	private static OWLOntology ontology = null;
	private static OWLDataFactory factory = OWLManager.getOWLDataFactory();
	private static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public HierarchyTest( String testName )
    {
        super( testName );
    }
    
    @Override
    protected void setUp() throws Exception {
    	super.setUp();
    	buildOntology();
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( HierarchyTest.class );
    }
    
    /**
     * Atomic clash
     */
    public void testkSubsetOfB() {
    	OWLClassExpression classK = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "K")));
    	OWLClassExpression classB = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "B")));
    	OWLSubClassOfAxiom kSubClassOfB = factory.getOWLSubClassOfAxiom(classK, classB);

    	Wolpertinger wolpertinger = new Wolpertinger(ontology);
    	assertTrue(wolpertinger.isEntailed(kSubClassOfB));
    }
    
    public void testFSubsetOfA() {
    	OWLClassExpression classF = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "F")));
    	OWLClassExpression classA = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "A")));
    	OWLSubClassOfAxiom kSubClassOfB = factory.getOWLSubClassOfAxiom(classF, classA);

    	Wolpertinger wolpertinger = new Wolpertinger(ontology);
		assertTrue(wolpertinger.isEntailed(kSubClassOfB));
    }
    
    public void testCSubsetOfE() {
    	OWLClassExpression classC = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "C")));
    	OWLClassExpression classE = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "E")));
    	OWLSubClassOfAxiom cSubClassOfE = factory.getOWLSubClassOfAxiom(classC, classE);

    	Wolpertinger wolpertinger = new Wolpertinger(ontology);
		assertFalse(wolpertinger.isEntailed(cSubClassOfE));
    }
    
    public void testThingSubsetOfNothing() {
    	OWLClassExpression thing = factory.getOWLThing();
    	OWLClassExpression nothing = factory.getOWLNothing();
    	OWLSubClassOfAxiom thingSubClassOfNothing = factory.getOWLSubClassOfAxiom(thing, nothing);
    	
    	Wolpertinger wolpertinger = new Wolpertinger(ontology);
		assertFalse(wolpertinger.isEntailed(thingSubClassOfNothing));
    }
    
    public void testThingSubsetOfNohing() {
    	OWLClassExpression thing = factory.getOWLThing();
    	OWLClassExpression nothing = factory.getOWLNothing();
    	OWLSubClassOfAxiom nothingSubClassOfThing = factory.getOWLSubClassOfAxiom(nothing, thing);
    	
    	Wolpertinger wolpertinger = new Wolpertinger(ontology);
		assertTrue(wolpertinger.isEntailed(nothingSubClassOfThing));
    }
    
    public void buildOntology() throws OWLOntologyCreationException {
    	OWLClassExpression classA = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "A")));
    	OWLClassExpression classB = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "B")));
    	OWLClassExpression classC = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "C")));
    	OWLClassExpression classD = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "D")));
    	OWLClassExpression classE = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "E")));
    	OWLClassExpression classF = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "F")));
    	OWLClassExpression classG = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "G")));
    	OWLClassExpression classK = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "K")));
    	OWLClassExpression thing = factory.getOWLThing();
    	OWLClassExpression nothing = factory.getOWLNothing();
    	
    	OWLSubClassOfAxiom gSubClOfThing = factory.getOWLSubClassOfAxiom(thing, classG);
    	OWLSubClassOfAxiom aSubClOfG = factory.getOWLSubClassOfAxiom(classA, classG);
		OWLSubClassOfAxiom cSubClOfB = factory.getOWLSubClassOfAxiom(classC, classB);
		OWLSubClassOfAxiom eSubClOfC = factory.getOWLSubClassOfAxiom(classE, classC);
		OWLSubClassOfAxiom dSubClOfA = factory.getOWLSubClassOfAxiom(classD, classA);
		
		OWLEquivalentClassesAxiom bEqToA = factory.getOWLEquivalentClassesAxiom(classA, classB);
		OWLEquivalentClassesAxiom fEqTod = factory.getOWLEquivalentClassesAxiom(classF, classD);
		OWLEquivalentClassesAxiom kEqToNothing = factory.getOWLEquivalentClassesAxiom(classK, nothing);
		
		OWLNamedIndividual indA = factory.getOWLNamedIndividual(IRI.create(String.format("%s#%s", PREFIX, "a")));
    	OWLNamedIndividual indB = factory.getOWLNamedIndividual(IRI.create(String.format("%s#%s", PREFIX, "b")));
    	OWLNamedIndividual indC = factory.getOWLNamedIndividual(IRI.create(String.format("%s#%s", PREFIX, "c")));
    	OWLNamedIndividual indD = factory.getOWLNamedIndividual(IRI.create(String.format("%s#%s", PREFIX, "d")));
    	
    	OWLIndividualAxiom fact1 = factory.getOWLClassAssertionAxiom(thing, indA);
    	OWLIndividualAxiom fact2 = factory.getOWLClassAssertionAxiom(thing, indB);
    	OWLIndividualAxiom fact3 = factory.getOWLClassAssertionAxiom(thing, indC);
    	OWLIndividualAxiom fact4 = factory.getOWLClassAssertionAxiom(thing, indD);
    	
		ontology = manager.createOntology();
		manager.addAxiom(ontology, gSubClOfThing);
		manager.addAxiom(ontology, aSubClOfG);
		manager.addAxiom(ontology, cSubClOfB);
		manager.addAxiom(ontology, eSubClOfC);
		manager.addAxiom(ontology, dSubClOfA);
		
		manager.addAxiom(ontology, bEqToA);
		manager.addAxiom(ontology, fEqTod);
		manager.addAxiom(ontology, kEqToNothing);
		
		manager.addAxiom(ontology, fact1);
		manager.addAxiom(ontology, fact2);
		manager.addAxiom(ontology, fact3);
		manager.addAxiom(ontology, fact4);
    }
}
