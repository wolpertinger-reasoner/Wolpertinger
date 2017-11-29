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
 * Unit test for simple App.
 */
public class WolpertingerTest 
    extends TestCase
{
	private static String PREFIX = "http://www.semanticweb.org/wolpertinger";
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public WolpertingerTest( String testName )
    {
        super( testName );
    }
    
    @Override
    protected void setUp() throws Exception {
    	super.setUp();
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( WolpertingerTest.class );
    }
    
    /**
     * Atomic clash
     */
    public void testUnsatifiabilityDueToClashInABoxAssertions() {
    	OWLDataFactory factory = OWLManager.getOWLDataFactory();
    	OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    	
    	OWLClassExpression expr1 = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "A")));
    	OWLClassExpression expr2 = factory.getOWLObjectComplementOf(expr1);
    	OWLNamedIndividual indiv = factory.getOWLNamedIndividual(IRI.create(String.format("%s#%s", PREFIX, "a")));
    	
    	OWLIndividualAxiom fact1 = factory.getOWLClassAssertionAxiom(expr1, indiv);
    	OWLIndividualAxiom fact2 = factory.getOWLClassAssertionAxiom(expr2, indiv);
    	
    	try {
			OWLOntology ontology = manager.createOntology();
			manager.addAxiom(ontology, fact1);
			manager.addAxiom(ontology, fact2);
			
			Wolpertinger wolpertinger = new Wolpertinger(ontology);
			
			assertFalse(wolpertinger.isConsistent());
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			fail();
		}
    }
    
    public void testUnsatisfiabilityDuetoSimpleSubsumptionViolation() {
    	OWLDataFactory factory = OWLManager.getOWLDataFactory();
    	OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    	
    	OWLClassExpression classA = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "A")));
    	OWLClassExpression classB = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "B")));
    	OWLClassExpression complClassB = factory.getOWLObjectComplementOf(classB);
    	
    	OWLNamedIndividual indiv = factory.getOWLNamedIndividual(IRI.create(String.format("%s#%s", PREFIX, "a")));
    	
    	OWLIndividualAxiom fact1 = factory.getOWLClassAssertionAxiom(classA, indiv);
    	OWLIndividualAxiom fact2 = factory.getOWLClassAssertionAxiom(complClassB, indiv);
    	
    	OWLSubClassOfAxiom subClOf = factory.getOWLSubClassOfAxiom(classA, classB);
    	
    	try {
			OWLOntology ontology = manager.createOntology();
			manager.addAxiom(ontology, fact1);
			manager.addAxiom(ontology, fact2);
			manager.addAxiom(ontology, subClOf);
			
			Wolpertinger wolpertinger = new Wolpertinger(ontology);
			
			assertFalse(wolpertinger.isConsistent());
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			fail();
		}
    }
    
    /**
     * Smth like:
     *    A subClassOf B
     *    A subClassOf C
     *    C disjoint with B
     *    ...
     */
    public void testUnsatisfiabilityDueToConflictingAxioms1() {
    	OWLDataFactory factory = OWLManager.getOWLDataFactory();
    	OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    	
    	OWLClassExpression classA = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "A")));
    	OWLClassExpression classB = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "B")));
    	OWLClassExpression classC = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "C")));
    	OWLNamedIndividual indiv = factory.getOWLNamedIndividual(IRI.create(String.format("%s#%s", PREFIX, "a")));
    	
    	OWLIndividualAxiom fact1 = factory.getOWLClassAssertionAxiom(classA, indiv);
    	OWLSubClassOfAxiom axmAsubB = factory.getOWLSubClassOfAxiom(classA, classB);
    	OWLSubClassOfAxiom axmAsubC = factory.getOWLSubClassOfAxiom(classA, classC);
    	OWLDisjointClassesAxiom axmBdisC = factory.getOWLDisjointClassesAxiom(classB, classC);
    	
    	try {
			OWLOntology ontology = manager.createOntology();
			manager.addAxiom(ontology, fact1);
			manager.addAxiom(ontology, axmAsubB);
			manager.addAxiom(ontology, axmAsubC);
			manager.addAxiom(ontology, axmBdisC);
			
			Wolpertinger wolpertinger = new Wolpertinger(ontology);
			
			assertFalse(wolpertinger.isConsistent());
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			fail();
		}
    }
    
    /**
     * Smth like
     *    A subClassOf r min 5 B
     *  But we have only a domain with 4 elements ...
     */
    public void testUnsatisfiabilityDoToFixedDomain1() {
    	OWLDataFactory factory = OWLManager.getOWLDataFactory();
    	OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    	
    	OWLClassExpression classA = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "A")));
    	OWLClassExpression classB = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "B")));
    	OWLObjectPropertyExpression roleR = factory.getOWLObjectProperty(IRI.create(String.format("%s#%s", PREFIX, "r")));
    	
    	OWLNamedIndividual indA = factory.getOWLNamedIndividual(IRI.create(String.format("%s#%s", PREFIX, "a")));
    	OWLNamedIndividual indB = factory.getOWLNamedIndividual(IRI.create(String.format("%s#%s", PREFIX, "b")));
    	OWLNamedIndividual indC = factory.getOWLNamedIndividual(IRI.create(String.format("%s#%s", PREFIX, "c")));
    	OWLNamedIndividual indD = factory.getOWLNamedIndividual(IRI.create(String.format("%s#%s", PREFIX, "d")));

    	OWLIndividualAxiom fact1 = factory.getOWLClassAssertionAxiom(classA, indA);
    	OWLIndividualAxiom fact2 = factory.getOWLClassAssertionAxiom(classA, indB);
    	OWLIndividualAxiom fact3 = factory.getOWLClassAssertionAxiom(classA, indC);
    	OWLIndividualAxiom fact4 = factory.getOWLClassAssertionAxiom(classA, indD);
    	
    	OWLObjectMinCardinality exprRmin5B = factory.getOWLObjectMinCardinality(5, roleR, classB);
    	OWLSubClassOfAxiom axmAsubRsomeB = factory.getOWLSubClassOfAxiom(classA, exprRmin5B);
    	
    	try {
			OWLOntology ontology = manager.createOntology();
			manager.addAxiom(ontology, fact1);
			manager.addAxiom(ontology, fact2);
			manager.addAxiom(ontology, fact3);
			manager.addAxiom(ontology, fact4);
			manager.addAxiom(ontology, axmAsubRsomeB);
			
			Wolpertinger wolpertinger = new Wolpertinger(ontology);
			
			assertFalse(wolpertinger.isConsistent());
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			fail();
		}
    }
    
    private OWLOntology createSimpleGraphColoring() {
    	OWLOntology ontoColoring = null;
    	OWLDataFactory factory = OWLManager.getOWLDataFactory();
    	OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    	
    	OWLObjectPropertyExpression edgeProp = factory.getOWLObjectProperty(IRI.create(String.format("%s#%s", PREFIX, "edge")));
    	OWLClassExpression classNode = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "Node")));
    	OWLClassExpression classBlue = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "Blue")));
    	OWLClassExpression classRed = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "Red")));
    	OWLClassExpression classGreen = factory.getOWLClass(IRI.create(String.format("%s#%s", PREFIX, "Green")));
    	
    	OWLNamedIndividual indNode1 = factory.getOWLNamedIndividual(IRI.create(String.format("%s#%s", PREFIX, "node1")));
    	OWLNamedIndividual indNode2 = factory.getOWLNamedIndividual(IRI.create(String.format("%s#%s", PREFIX, "node2")));
    	OWLNamedIndividual indNode3 = factory.getOWLNamedIndividual(IRI.create(String.format("%s#%s", PREFIX, "node3")));
    	OWLNamedIndividual indNode4 = factory.getOWLNamedIndividual(IRI.create(String.format("%s#%s", PREFIX, "node4")));
    	
    	// now the facts
    	// nodes
    	OWLIndividualAxiom axmNodeInst4 =factory.getOWLClassAssertionAxiom(classNode, indNode4);
    	OWLIndividualAxiom axmNodeInst3 =factory.getOWLClassAssertionAxiom(classNode, indNode3);
    	OWLIndividualAxiom axmNodeInst2 =factory.getOWLClassAssertionAxiom(classNode, indNode2);
    	OWLIndividualAxiom axmNodeInst1 =factory.getOWLClassAssertionAxiom(classNode, indNode1);
    	
    	// 1
    	// | \
    	// |  3 - 4
    	// | /
    	// 2
    	//
    	OWLIndividualAxiom axmEdge12 = factory.getOWLObjectPropertyAssertionAxiom(edgeProp, indNode1, indNode2);
    	OWLIndividualAxiom axmEdge13 = factory.getOWLObjectPropertyAssertionAxiom(edgeProp, indNode1, indNode3);
    	OWLIndividualAxiom axmEdge23 = factory.getOWLObjectPropertyAssertionAxiom(edgeProp, indNode2, indNode3);
    	OWLIndividualAxiom axmEdge34 = factory.getOWLObjectPropertyAssertionAxiom(edgeProp, indNode3, indNode4);
    	
    	// symmetry of edge property
    	OWLObjectPropertyAxiom axmEdgeSym =  factory.getOWLSymmetricObjectPropertyAxiom(edgeProp);
    	
    	// axioms
    	OWLObjectUnionOf exprColorUnion = factory.getOWLObjectUnionOf(classBlue, classRed, classGreen);
    	OWLSubClassOfAxiom axmNodeColorings = factory.getOWLSubClassOfAxiom(classNode, exprColorUnion);
    	
    	// coloring constraints
    	OWLSubClassOfAxiom axmRedConstraint = factory.getOWLSubClassOfAxiom(classRed, factory.getOWLObjectAllValuesFrom(edgeProp, factory.getOWLObjectUnionOf(classGreen, classBlue)));
    	OWLSubClassOfAxiom axmBlueConstraint = factory.getOWLSubClassOfAxiom(classBlue, factory.getOWLObjectAllValuesFrom(edgeProp, factory.getOWLObjectUnionOf(classGreen, classRed)));
    	OWLSubClassOfAxiom axmGreenConstraint = factory.getOWLSubClassOfAxiom(classGreen, factory.getOWLObjectAllValuesFrom(edgeProp, factory.getOWLObjectUnionOf(classRed, classBlue)));
    	OWLDisjointClassesAxiom axmDisColors = factory.getOWLDisjointClassesAxiom(classRed, classBlue, classGreen);
    	
    	try {
			ontoColoring = manager.createOntology(); 
			
			manager.addAxiom(ontoColoring, axmNodeInst1);
			manager.addAxiom(ontoColoring, axmNodeInst2);
			manager.addAxiom(ontoColoring, axmNodeInst3);
			manager.addAxiom(ontoColoring, axmNodeInst4);
			
			manager.addAxiom(ontoColoring, axmEdge12);
			manager.addAxiom(ontoColoring, axmEdge13);
			manager.addAxiom(ontoColoring, axmEdge23);
			manager.addAxiom(ontoColoring, axmEdge34);
			
			manager.addAxiom(ontoColoring, axmEdgeSym);
			manager.addAxiom(ontoColoring, axmNodeColorings);
			manager.addAxiom(ontoColoring, axmRedConstraint);
			manager.addAxiom(ontoColoring, axmBlueConstraint);
			manager.addAxiom(ontoColoring, axmGreenConstraint);
			manager.addAxiom(ontoColoring, axmDisColors);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
    	
    	return ontoColoring;
    }
    
    public void testSatisfiabilityOfSimpleGraphColring3Colors() {
    	OWLOntology simpleGraphOntology = createSimpleGraphColoring();
    	
    	Wolpertinger wolpert  = new Wolpertinger(simpleGraphOntology);
    	
    	assertTrue(wolpert.isConsistent());
    }
    
    public void testUnsatisfiabilityOfGraphColoringOverDomainOfSizeTwo() {
    	OWLDataFactory factory = OWLManager.getOWLDataFactory();
    	
    	HashSet<OWLNamedIndividual> domain = new HashSet<OWLNamedIndividual>();
    	domain.add(factory.getOWLNamedIndividual(IRI.create(String.format("%s#%s", PREFIX, "d1"))));
    	domain.add(factory.getOWLNamedIndividual(IRI.create(String.format("%s#%s", PREFIX, "d2"))));
    	
    	OWLOntology simpleGraphOntology = createSimpleGraphColoring();
    	
    	Configuration config = new Configuration(new HashSet<IRI>(), domain);
    	
    	Wolpertinger wolpert = new Wolpertinger(config, simpleGraphOntology);
    	
    	assertFalse(wolpert.isConsistent());
    }
    
    public void testSimple3ColoringHas24AnswerSets() {
    	OWLOntology coloringOnto = createSimpleGraphColoring();
    	
    	Wolpertinger wolpert = new Wolpertinger(coloringOnto);
    	
    	assertEquals(24, wolpert.enumerateAllModels().size());
    }
    
    

}
