/*  Copyright 2015 by the International Center for Computational Logic, Technical University Dresden.

    This file is part of Wolpertinger.

    Wolpertinger is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Wolpertinger is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Wolpertinger.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.semanticweb.wolpertinger;

import hierarchy.Hierarchy;
import hierarchy.HierarchyBuilder;
import hierarchy.HierarchyNode;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLFunctionalSyntaxOntologyFormat;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.FreshEntityPolicy;
import org.semanticweb.owlapi.reasoner.IndividualNodeSetPolicy;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.impl.OWLClassNode;
import org.semanticweb.owlapi.reasoner.impl.OWLClassNodeSet;
import org.semanticweb.owlapi.reasoner.impl.OWLNamedIndividualNodeSet;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.Version;
import org.semanticweb.wolpertinger.clingo.ClingoModelEnumerator;
import org.semanticweb.wolpertinger.clingo.ClingoSolver;
import org.semanticweb.wolpertinger.clingo.SolverFactory;
import org.semanticweb.wolpertinger.clingo.SolvingException;
import org.semanticweb.wolpertinger.model.AtomicConcept;
import org.semanticweb.wolpertinger.structural.OWLAxioms;
import org.semanticweb.wolpertinger.structural.OWLNormalization;
import org.semanticweb.wolpertinger.structural.OWLNormalizationWithTracer;
import org.semanticweb.wolpertinger.translation.SignatureMapper;
import org.semanticweb.wolpertinger.translation.debug.DebugTranslation;
import org.semanticweb.wolpertinger.translation.naive.ASP2CoreSymbols;
import org.semanticweb.wolpertinger.translation.naive.NaiveTranslation;

import uk.ac.manchester.cs.owl.owlapi.OWLClassAssertionAxiomImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectComplementOfImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectUnionOfImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLSubClassOfAxiomImpl;

/**
 * TODO: Detailed Description here.
 *
 * @author Lukas Schweizer
 * @author Satyadharma Tirtarasa
 */
public class Wolpertinger implements OWLReasoner {

	private OWLOntology rootOntology;
	// normalized kb
	private OWLAxioms axioms;

	private Configuration configuration;

	private NaiveTranslation naiveTranslation;

	private File tmpFile;
	private PrintWriter output;
	private boolean classified;
	private boolean baseProgramReady;

	private ClingoModelEnumerator enumerator;
	private OWLNormalization normalization;

	private boolean satisfiableClassesComputed;
	private List<String> satisfiableClasses;



	private HashSet<OWLClass> equalsToTopClasses;
	private HashSet<OWLClass> equalsToBottomClasses;

	private Hierarchy<OWLClass> classHierarchy;

	public Wolpertinger(OWLOntology rootOntology) {
		this(new Configuration(), rootOntology);
	}

	public Wolpertinger(Configuration configuration, OWLOntology rootOntology) {
		this.rootOntology = rootOntology;
		this.configuration = configuration;
		loadOntology();
		classified = false;
	}

	/**
	 * Load the root ontology and all imports and apply normalization.
	 */
	private void loadOntology() {
		clearState();

		axioms = new OWLAxioms();

		Collection<OWLOntology> importClosure = rootOntology.getImportsClosure();
		if(configuration.getDomainIndividuals() == null) {
			configuration.setDomainIndividuals(rootOntology.getIndividualsInSignature(true));
		}

		normalization = new OWLNormalization(rootOntology.getOWLOntologyManager().getOWLDataFactory(), axioms, 0, configuration.getDomainIndividuals());

		for (OWLOntology ontology : importClosure) {
			normalization.processOntology(ontology);
		}

		axioms.m_namedIndividuals.clear();
		axioms.m_namedIndividuals.addAll(configuration.getDomainIndividuals());

		try {
			tmpFile = File.createTempFile("wolpertinger-base-program", ".lp");
			tmpFile.deleteOnExit();
			output = new PrintWriter(tmpFile);
			naiveTranslation = new NaiveTranslation(configuration, output);
			naiveTranslation.translateOntology(axioms);
			baseProgramReady = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		enumerator = new ClingoModelEnumerator(new String[] {tmpFile.getAbsolutePath()});
	}

	private void clearState() {
		this.axioms = null;
	}

	private OWLDataFactory getOWLDataFactory() {
		return rootOntology.getOWLOntologyManager().getOWLDataFactory();
	}

	public void naiveTranslate(PrintWriter output) {
		NaiveTranslation translation = new NaiveTranslation(configuration, output);
		translation.translateOntology(axioms);
	}

	public void naffTranslate(PrintWriter output, boolean debugFlag) {
		clearState();

		OWLAxioms axioms = new OWLAxioms();

		Collection<OWLOntology> importClosure = rootOntology.getImportsClosure();
		if(configuration.getDomainIndividuals() == null) {
			configuration.setDomainIndividuals(rootOntology.getIndividualsInSignature(true));
		}

		OWLNormalizationWithTracer normalization = new OWLNormalizationWithTracer(rootOntology.getOWLOntologyManager().getOWLDataFactory(), axioms, 0, configuration.getDomainIndividuals());

		for (OWLOntology ontology : importClosure) {
			normalization.processOntology(ontology);
		}

		axioms.m_namedIndividuals.clear();
		axioms.m_namedIndividuals.addAll(configuration.getDomainIndividuals());

		DebugTranslation translation = new DebugTranslation(configuration, output, debugFlag, normalization);
		translation.translateOntology(axioms);
	}

	public Collection<String> enumerateAllModels() {
		return enumerator.enumerateAllModels();
	}

	public Collection<String> enumerateModels(int number) {
		return enumerator.enumerateModels(number);
	}

	public void classifyClasses() {
		if (classified) {
			return;
		}
		classified = true;
		equalsToTopClasses = new HashSet<OWLClass> ();
		equalsToBottomClasses = new HashSet<OWLClass> ();
		Collection<OWLClass> allClasses = rootOntology.getClassesInSignature(true);
		HashMap<OWLClass,HashSet<OWLClass>> superClassHierarchy = new HashMap<OWLClass,HashSet<OWLClass>> ();

		for (OWLClass cl : allClasses) {
			OWLObjectComplementOf negated = new OWLObjectComplementOfImpl(cl);
			if (!isSatisfiable(cl)) {
				equalsToBottomClasses.add(cl);
			}
			if (!isSatisfiable(negated)) {
				equalsToTopClasses.add(cl);
			} else {

			}
		}

		for (OWLClass cl : allClasses) {
			superClassHierarchy.put(cl, new HashSet<OWLClass> ());
		}

		// TODO optimize checking
		for (OWLClass subClassCandidate : allClasses) {
			HashSet<OWLClass> superClasses = superClassHierarchy.get(subClassCandidate);
			for (OWLClass superClassCandidate : allClasses) {
				if (subClassCandidate.equals(superClassCandidate)) {
					// same class, no need to check
					continue;
				}

				boolean entailed = isEntailed(new OWLSubClassOfAxiomImpl (subClassCandidate, superClassCandidate, new HashSet<OWLAnnotation> ()));
				if (entailed) {
					superClasses.add(superClassCandidate);
				} else {

				}
			}
		}

		// check equivalent classes
		HashMap<OWLClass, OWLClass> classRepresentative = new HashMap<OWLClass, OWLClass> ();
		HashMap<OWLClass, HashSet<OWLClass>> classRepresented = new HashMap<OWLClass, HashSet<OWLClass>> ();
		for (OWLClass cl : allClasses) {
			HashSet<OWLClass> superClasses = superClassHierarchy.get(cl);
			for (OWLClass equivalentCandidate : superClasses) {
				if (classRepresentative.keySet().contains(cl)) {
					continue;
				}
				if (superClassHierarchy.get(equivalentCandidate).contains(cl)) {
					classRepresentative.put(equivalentCandidate, cl);
					if(!classRepresented.containsKey(cl)) {
						HashSet<OWLClass> newSet = new HashSet<OWLClass> ();
						newSet.add(equivalentCandidate);
						classRepresented.put(cl, newSet);
					} else {
						HashSet<OWLClass> existedSet = classRepresented.get(cl);
						existedSet.add(equivalentCandidate);
					}

				}
			}
		}

		Set<OWLClass> representedClasses = classRepresentative.keySet();
		for (OWLClass cl : allClasses) {
			HashSet<OWLClass> superClasses = superClassHierarchy.get(cl);
			superClasses.removeAll(representedClasses);
		}

		for (OWLClass cl : representedClasses) {
			superClassHierarchy.remove(cl);
		}

		// compute transitive reduction of dag
		for (OWLClass cl : allClasses) {
			Collection<OWLClass> superClasses = superClassHierarchy.get(cl);
			if(superClasses == null) {
				continue;
			}
			OWLClass[] tmpSuperClasses = new OWLClass[superClasses.size()];

			for (OWLClass superClass : superClasses.toArray(tmpSuperClasses)) {
				if (!superClassHierarchy.get(cl).contains(superClass)){
					// has been removed
				}
				HashSet<OWLClass> marked = new HashSet<OWLClass> ();

		        // depth-first search using an explicit stack
		        Stack<OWLClass> stack = new Stack<OWLClass>();
		        marked.add(superClass);
		        stack.push(superClass);
		        while (!stack.isEmpty()) {
		            OWLClass v = stack.peek();
		            HashSet<OWLClass> superSuperClasses = superClassHierarchy.get(v);
		            for (OWLClass superSuperClass : superSuperClasses) {
		            	if (!marked.contains(superSuperClass)) {
		            		marked.add(superSuperClass);
		            		stack.add(superSuperClass);
		            		continue;
		            	}

		            }
            		stack.pop();
		        }
		        HashSet<OWLClass> directSuperClasses = superClassHierarchy.get(cl);
		        for (OWLClass indirectSuperClass : marked) {
		        	if (indirectSuperClass.equals(superClass)) {

		        	} else {
		        		directSuperClasses.remove(indirectSuperClass);
		        	}
		        }
			}
		}
		OWLClass thing = rootOntology.getOWLOntologyManager().getOWLDataFactory().getOWLThing();
		OWLClass nothing = rootOntology.getOWLOntologyManager().getOWLDataFactory().getOWLNothing();

		classHierarchy = HierarchyBuilder.buildHierarchy(superClassHierarchy, classRepresentative, thing, nothing, equalsToTopClasses, equalsToBottomClasses);
	}

	public void axiomatizeFDSemantics(File file){
		Set<OWLNamedIndividual> individuals = rootOntology.getIndividualsInSignature(true);

 		if (individuals.isEmpty()) {
 			System.out.println("No named individuals in given ontology!");
 			return;
 		}

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		OWLDifferentIndividualsAxiom allDiff = factory.getOWLDifferentIndividualsAxiom(individuals);
		PrefixManager pManager = new DefaultPrefixManager("");
		OWLClassExpression thing = factory.getOWLClass("owl:Thing", pManager);
		OWLObjectOneOf oneof = factory.getOWLObjectOneOf(individuals);
		OWLSubClassOfAxiom axiom = factory.getOWLSubClassOfAxiom(thing, oneof);

		manager.addAxiom(rootOntology, axiom);
		manager.addAxiom(rootOntology, allDiff);

		file = file.getAbsoluteFile();
	    BufferedOutputStream outputStream;
		try {
			outputStream = new BufferedOutputStream(new FileOutputStream(file));
			manager.saveOntology(rootOntology, new OWLFunctionalSyntaxOntologyFormat(), outputStream);
		} catch (FileNotFoundException e1) {
			System.out.println("There is something wrong with the given filename: " + file.toString());
		} catch (OWLOntologyStorageException e1) {
			System.out.println("Something went wrong when saving the ontology: " + e1.getMessage());
		}
	    System.out.println(file.toString() + " was successfully created!");
	}

//	public void outputNormalizedOntology(PrintWriter writer) {
//		NiceAxiomPrinter ontoPrinter = new NiceAxiomPrinter(writer);
//		for (OWLIndividualAxiom ia : axioms.m_facts) {
//			ia.accept(ontoPrinter);
//		}
//		for (OWLClassExpression[] inclusionAxiom : axioms.m_conceptInclusions) {
//			boolean isFirst=true;
//			for (OWLClassExpression expression : inclusionAxiom) {
//				if (!isFirst)
//					writer.print(", ");
//				expression.accept(ontoPrinter);
//				isFirst=false;
//			}
//			writer.print(".\n");
//		}
//	}

	// --------------
	// OWLReasoner implementations up from here
	// --------------

	////////////////////////////
	// Reasoner-related methods
	////////////////////////////
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getReasonerName() {
		return getClass().getPackage().getImplementationTitle();
	}

	@Override
	public Version getReasonerVersion() {
		String versionString = getClass().getPackage().getImplementationVersion();
        String[] splitted;
        int version[]=new int[4];
        if (versionString!=null) {
            splitted=versionString.split("\\.");
            for (int ii = 0; ii < 4; ii++) {
            	if (ii < splitted.length) {
            		version[ii]=Integer.parseInt(splitted[ii]);
            	} else {
            		version[ii]=0;
            	}
            }
        }
        return new Version(version[0],version[1],version[2],version[3]);
	}

	@Override
	public BufferingMode getBufferingMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OWLOntology getRootOntology() {
		return this.rootOntology;
	}

	////////////////////////////
	// Utility methods
	////////////////////////////
	@Override
	public FreshEntityPolicy getFreshEntityPolicy() {
		return FreshEntityPolicy.DISALLOW;
	}

	@Override
	public IndividualNodeSetPolicy getIndividualNodeSetPolicy() {
		return IndividualNodeSetPolicy.BY_NAME;
	}

	@Override
	public Set<OWLAxiom> getPendingAxiomAdditions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<OWLAxiom> getPendingAxiomRemovals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OWLOntologyChange> getPendingChanges() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<InferenceType> getPrecomputableInferenceTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getTimeOut() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void interrupt() {
		// TODO Auto-generated method stub

	}

	////////////////////////////
	// Ontology-related methods
	////////////////////////////

	@Override
	public boolean isConsistent() {
        Collection<String> models = enumerator.enumerateModels(1);
		return !models.isEmpty();
	}

	@Override
	public boolean isEntailed(OWLAxiom axiom) {
		HashSet<OWLAxiom> wrapper = new HashSet<OWLAxiom> ();
		wrapper.add(axiom);
		return isEntailed(wrapper);
	}

	@Override
	public boolean isEntailed(Set<? extends OWLAxiom> axiomSet) {
		File tmpEntailmentFile = null;
		PrintWriter entailmentOutput = null;

		// write the constraints
		try {
			tmpEntailmentFile = File.createTempFile("wolpertinger-entailment-program", ".lp");
			tmpEntailmentFile.deleteOnExit();
			entailmentOutput = new PrintWriter(tmpEntailmentFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (OWLAxiom axiom : axiomSet) {
			if (axiom instanceof OWLDeclarationAxiom) {

			} else if (axiom instanceof OWLSubClassOfAxiom) {
				OWLAxioms tempAxioms = new OWLAxioms ();
				Collection<OWLAxiom> wrapper = new HashSet<OWLAxiom> ();
				OWLNormalization tempNormalization = new OWLNormalization(rootOntology.getOWLOntologyManager().getOWLDataFactory(), tempAxioms, 0, configuration.getDomainIndividuals());
				// transform into union of ~A and B
				OWLSubClassOfAxiom subClassOfAxiom = (OWLSubClassOfAxiom) axiom;
				LinkedHashSet<OWLClassExpression> intersectionSet = new LinkedHashSet<OWLClassExpression> ();
				OWLClassExpression negatedSubClass = new OWLObjectComplementOfImpl (subClassOfAxiom.getSubClass());
				intersectionSet.add(negatedSubClass);
				intersectionSet.add(subClassOfAxiom.getSuperClass());
				OWLObjectUnionOfImpl intersection = new OWLObjectUnionOfImpl (intersectionSet);
				OWLClass thing = rootOntology.getOWLOntologyManager().getOWLDataFactory().getOWLThing();
				OWLSubClassOfAxiomImpl convertedSubClassOfAxiom = new OWLSubClassOfAxiomImpl(thing, intersection, new HashSet<OWLAnnotation> ());
				wrapper.add(convertedSubClassOfAxiom);
				tempNormalization.processAxioms(wrapper);
				NaiveTranslation axiomTranslation = new NaiveTranslation(configuration, entailmentOutput);
				axiomTranslation.translateEntailment(tempAxioms);
			} else if (axiom instanceof OWLClassAssertionAxiom) {
				OWLAxioms tempAxioms = new OWLAxioms ();
				Collection<OWLAxiom> wrapper = new HashSet<OWLAxiom> ();
				OWLNormalization tempNormalization = new OWLNormalization(rootOntology.getOWLOntologyManager().getOWLDataFactory(), tempAxioms, 0, configuration.getDomainIndividuals());

				OWLClassAssertionAxiom classAssertionAxiom = (OWLClassAssertionAxiom) axiom;
				OWLClassExpression classExpression = classAssertionAxiom.getClassExpression();
				OWLClass thing = rootOntology.getOWLOntologyManager().getOWLDataFactory().getOWLThing();
				OWLSubClassOfAxiomImpl subClassOfAxiom = new OWLSubClassOfAxiomImpl(thing, classExpression, new HashSet<OWLAnnotation> ());
				wrapper.add(subClassOfAxiom);
				tempNormalization.processAxioms(wrapper);
				NaiveTranslation axiomTranslation = new NaiveTranslation(configuration, entailmentOutput);
				axiomTranslation.individualAssertionMode((OWLNamedIndividual) classAssertionAxiom.getIndividual());
				axiomTranslation.translateEntailment(tempAxioms);
			} else {

			}
		}
		entailmentOutput.print(ASP2CoreSymbols.IMPLICATION);
		entailmentOutput.print(ASP2CoreSymbols.NAF);
		entailmentOutput.print(" violation.");
		entailmentOutput.close();

		ClingoModelEnumerator enumerator = new ClingoModelEnumerator(new String[] {tmpFile.getAbsolutePath(), tmpEntailmentFile.getAbsolutePath()});

		if (enumerator.enumerateModels(1).size() == 0) {
			tmpEntailmentFile.delete();
			return true;
		} else{
			tmpEntailmentFile.delete();			;
			return false;
		}
	}

	@Override
	public boolean isEntailmentCheckingSupported(AxiomType<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPrecomputed(InferenceType arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void precomputeInferences(InferenceType... arg0) {
		// TODO Auto-generated method stub
	}

	////////////////////////////
	// Class-related methods
	////////////////////////////
	@Override
	public Node<OWLClass> getBottomClassNode() {
		classifyClasses();
		return owlClassHierarchyNodeToNode(classHierarchy.getBottomNode());
	}

	@Override
	public NodeSet<OWLClass> getDisjointClasses(OWLClassExpression arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node<OWLClass> getEquivalentClasses(OWLClassExpression queryClassExpression) {
		if (queryClassExpression.isOWLNothing()) {
            return getBottomClassNode();
        } else if (queryClassExpression.isOWLThing()) {
        	return getTopClassNode();
        } else if (queryClassExpression instanceof OWLClass ){
        	OWLClass queryClass = (OWLClass) queryClassExpression;
        	return owlClassHierarchyNodeToNode(classHierarchy.getNodeForElement(queryClass));
        }
		return null;
	}

	@Override
	public NodeSet<OWLClass> getSubClasses(OWLClassExpression queryClassExpression, boolean direct) {
		if (queryClassExpression instanceof OWLClass) {
			Set<HierarchyNode<OWLClass>> result;
			OWLClass queryClass = (OWLClass) queryClassExpression;
			HierarchyNode<OWLClass> hierarchyNode = classHierarchy.getNodeForElement(queryClass);
			if (direct) {
				result = hierarchyNode.getChildNodes();
			} else {
				result=new HashSet<HierarchyNode<OWLClass>>(hierarchyNode.getDescendantNodes());
	            result.remove(hierarchyNode);
	        }
			return owlClassHierarchyNodesToNodeSet(result);
		}
		return null;
	}

	@Override
	public NodeSet<OWLClass> getSuperClasses(OWLClassExpression queryClassExpression, boolean direct) {
		if (queryClassExpression instanceof OWLClass) {
			Set<HierarchyNode<OWLClass>> result;
			OWLClass queryClass = (OWLClass) queryClassExpression;
			HierarchyNode<OWLClass> hierarchyNode = classHierarchy.getNodeForElement(queryClass);
			if (direct) {
				result = hierarchyNode.getParentNodes();
			} else {
				result=new HashSet<HierarchyNode<OWLClass>>(hierarchyNode.getAncestorNodes());
	            result.remove(hierarchyNode);
	        }
			return owlClassHierarchyNodesToNodeSet(result);
		}
		return null;
	}

	// taken from HermiT. instead of using AtomicConcept, we directly use OWLClass
	protected NodeSet<OWLClass> owlClassHierarchyNodesToNodeSet(Collection<HierarchyNode<OWLClass>> hierarchyNodes) {
        Set<Node<OWLClass>> result=new HashSet<Node<OWLClass>>();
        for (HierarchyNode<OWLClass> hierarchyNode : hierarchyNodes) {
            Node<OWLClass> node=owlClassHierarchyNodeToNode(hierarchyNode);
            if (node.getSize()!=0)
                result.add(node);
        }
        return new OWLClassNodeSet(result);
    }

	protected Node<OWLClass> owlClassHierarchyNodeToNode(HierarchyNode<OWLClass> hierarchyNode) {
        Set<OWLClass> result=new HashSet<OWLClass>();
        for (OWLClass concept : hierarchyNode.getEquivalentElements()) {
            result.add(concept);
        }
        return new OWLClassNode(result);
    }

	@Override
	public Node<OWLClass> getTopClassNode() {
		classifyClasses();
		return owlClassHierarchyNodeToNode(classHierarchy.getTopNode());
	}

	@Override
	public Node<OWLClass> getUnsatisfiableClasses() {
		return getBottomClassNode();
	}

	@Override
	public boolean isSatisfiable(OWLClassExpression classExpression) {
		SignatureMapper mapper = naiveTranslation.getSignatureMapper();
		if(!satisfiableClassesComputed) {
			ClingoSolver solver = SolverFactory.INSTANCE.createClingoBraveSolver();
			try {
				File satisfiableClassFile = File.createTempFile("wolpertinger-satisfiable-program", ".lp");
				satisfiableClassFile.deleteOnExit();
				PrintWriter satisfiableOutput = new PrintWriter(satisfiableClassFile);

				for (OWLClass c : rootOntology.getClassesInSignature(true)) {
					String className = mapper.getPredicateName(c);
					satisfiableOutput.write(String.format("%s :- %s(X).", className.toLowerCase(), className.toLowerCase()));
					satisfiableOutput.println();
					satisfiableOutput.write(String.format("not_%s :- -%s(X).", className.toLowerCase(), className.toLowerCase()));
					satisfiableOutput.println();
					satisfiableOutput.write("#show not_" + className.toLowerCase() + "/0.");
					satisfiableOutput.write("#show " + className.toLowerCase() + "/0.");
				}
				satisfiableOutput.close();
				Collection<String> results = solver.solve(new String[] {tmpFile.getAbsolutePath(), satisfiableClassFile.getAbsolutePath()}, 0);
				String[] resultsArray = new String[results.size()];
				resultsArray = results.toArray(resultsArray);
				satisfiableClasses = new ArrayList<String>(Arrays.asList(resultsArray[results.size() - 1].split(" ")));
				satisfiableClassesComputed = true;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SolvingException e) {
				e.printStackTrace();
			}
		}
		if (classExpression instanceof OWLClass) {
			String className = mapper.getPredicateName((OWLClass) classExpression);
			if (satisfiableClasses.contains(className)) {
				return true;
			}
		} else if (classExpression instanceof OWLObjectComplementOf &&
				   ((OWLObjectComplementOf) classExpression).getOperand() instanceof OWLClass) {
			OWLClassExpression cl = ((OWLObjectComplementOf) classExpression).getOperand();
			String className = mapper.getPredicateName((OWLClass) cl);
			if (satisfiableClasses.contains("not_" + className)) {
				return true;
			}
		}
		return false;
	}

	////////////////////////////
	// Object Property-related methods
	////////////////////////////

	@Override
	public Node<OWLObjectPropertyExpression> getBottomObjectPropertyNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeSet<OWLObjectPropertyExpression> getDisjointObjectProperties(
			OWLObjectPropertyExpression arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node<OWLObjectPropertyExpression> getEquivalentObjectProperties(
			OWLObjectPropertyExpression arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node<OWLObjectPropertyExpression> getInverseObjectProperties(
			OWLObjectPropertyExpression arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeSet<OWLObjectPropertyExpression> getSubObjectProperties(
			OWLObjectPropertyExpression arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeSet<OWLNamedIndividual> getObjectPropertyValues(
			OWLNamedIndividual arg0, OWLObjectPropertyExpression arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeSet<OWLClass> getObjectPropertyDomains(
			OWLObjectPropertyExpression arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeSet<OWLClass> getObjectPropertyRanges(
			OWLObjectPropertyExpression arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeSet<OWLObjectPropertyExpression> getSuperObjectProperties(
			OWLObjectPropertyExpression arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node<OWLObjectPropertyExpression> getTopObjectPropertyNode() {
		// TODO Auto-generated method stub
		return null;
	}

	////////////////////////////
	// Data Property-related methods
	////////////////////////////

	@Override
	public Node<OWLDataProperty> getBottomDataPropertyNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeSet<OWLClass> getDataPropertyDomains(OWLDataProperty arg0,
			boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<OWLLiteral> getDataPropertyValues(OWLNamedIndividual arg0,
			OWLDataProperty arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeSet<OWLNamedIndividual> getDifferentIndividuals(
			OWLNamedIndividual arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeSet<OWLDataProperty> getDisjointDataProperties(
			OWLDataPropertyExpression arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node<OWLDataProperty> getEquivalentDataProperties(
			OWLDataProperty arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeSet<OWLDataProperty> getSubDataProperties(OWLDataProperty arg0,
			boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeSet<OWLDataProperty> getSuperDataProperties(
			OWLDataProperty arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node<OWLDataProperty> getTopDataPropertyNode() {
		// TODO Auto-generated method stub
		return null;
	}

	////////////////////////////
	// Individual-related methods
	////////////////////////////


	@Override
	public NodeSet<OWLNamedIndividual> getInstances(OWLClassExpression classExpression,
			boolean arg1) {
		Set<OWLNamedIndividual> individuals = rootOntology.getIndividualsInSignature(true);
		OWLNamedIndividualNodeSet result = new OWLNamedIndividualNodeSet ();
		for (OWLNamedIndividual individual : individuals) {
			OWLClassAssertionAxiom impl = new OWLClassAssertionAxiomImpl (individual, classExpression, new HashSet<OWLAnnotation> ());
			if(isEntailed(impl)) {
				result.addEntity(individual);
			}
		}
		return result;
	}

	@Override
	public Node<OWLNamedIndividual> getSameIndividuals(OWLNamedIndividual arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeSet<OWLClass> getTypes(OWLNamedIndividual arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
