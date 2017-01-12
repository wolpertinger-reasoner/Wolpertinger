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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.coode.owlapi.obo.parser.IntersectionOfHandler;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.FreshEntityPolicy;
import org.semanticweb.owlapi.reasoner.IndividualNodeSetPolicy;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.impl.OWLNamedIndividualNodeSet;
import org.semanticweb.owlapi.util.Version;
import org.semanticweb.wolpertinger.clingo.ClingoModelEnumerator;
import org.semanticweb.wolpertinger.structural.OWLAxioms;
import org.semanticweb.wolpertinger.structural.OWLNormalization;
import org.semanticweb.wolpertinger.structural.util.NiceAxiomPrinter;
import org.semanticweb.wolpertinger.translation.OWLOntologyTranslator;
import org.semanticweb.wolpertinger.translation.debug.DebugTranslation;
import org.semanticweb.wolpertinger.translation.naive.NaiveTranslation;

import uk.ac.manchester.cs.owl.owlapi.OWLClassAssertionAxiomImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectComplementOfImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectIntersectionOfImpl;
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

	private DebugTranslation debugTranslation;
	private NaiveTranslation naiveTranslation;

	private File tmpFile;
	private PrintWriter output;
	private boolean baseProgramReady;

	private ClingoModelEnumerator enumerator;
	private OWLNormalization normalization;

	public Wolpertinger(OWLOntology rootOntology) {
		this(new Configuration(), rootOntology);
	}

	public Wolpertinger(Configuration configuration, OWLOntology rootOntology) {
		this.rootOntology = rootOntology;
		this.configuration = configuration;
		loadOntology();
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
		DebugTranslation translation = new DebugTranslation(configuration, output, debugFlag);
		translation.translateOntology(axioms);
	}

	public Collection<String> enumerateAllModels() {
		return enumerator.enumerateAllModels();
	}

	public Collection<String> enumerateModels(int number) {
		return enumerator.enumerateModels(number);
	}

	public void classify() {
		Collection<OWLClass> allClasses = rootOntology.getClassesInSignature();
		for (OWLClass subClass : allClasses) {
			for (OWLClass superClass : allClasses) {
				boolean entailed = isEntailed(new OWLSubClassOfAxiomImpl (subClass, superClass, new HashSet<OWLAnnotation> ()));
				System.out.print(subClass.getIRI().getFragment() + " -> " + superClass.getIRI().getFragment());
				if (entailed) {
					System.out.println(" YES");
				} else {
					System.out.println(" NO");
				}
			}
		}
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

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub

	}

	@Override
	public Node<OWLClass> getBottomClassNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node<OWLDataProperty> getBottomDataPropertyNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node<OWLObjectPropertyExpression> getBottomObjectPropertyNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BufferingMode getBufferingMode() {
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
	public NodeSet<OWLClass> getDisjointClasses(OWLClassExpression arg0) {
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
	public NodeSet<OWLObjectPropertyExpression> getDisjointObjectProperties(
			OWLObjectPropertyExpression arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node<OWLClass> getEquivalentClasses(OWLClassExpression arg0) {
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
	public Node<OWLObjectPropertyExpression> getEquivalentObjectProperties(
			OWLObjectPropertyExpression arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FreshEntityPolicy getFreshEntityPolicy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IndividualNodeSetPolicy getIndividualNodeSetPolicy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeSet<OWLNamedIndividual> getInstances(OWLClassExpression classExpression,
			boolean arg1) {
		Set<OWLNamedIndividual> individuals = rootOntology.getIndividualsInSignature();
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
	public Node<OWLObjectPropertyExpression> getInverseObjectProperties(
			OWLObjectPropertyExpression arg0) {
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
	public NodeSet<OWLNamedIndividual> getObjectPropertyValues(
			OWLNamedIndividual arg0, OWLObjectPropertyExpression arg1) {
		// TODO Auto-generated method stub
		return null;
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
	public String getReasonerName() {
		return getClass().getPackage().getImplementationTitle();
	}

	@Override
	public Version getReasonerVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OWLOntology getRootOntology() {
		return this.rootOntology;
	}

	@Override
	public Node<OWLNamedIndividual> getSameIndividuals(OWLNamedIndividual arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeSet<OWLClass> getSubClasses(OWLClassExpression arg0, boolean arg1) {
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
	public NodeSet<OWLObjectPropertyExpression> getSubObjectProperties(
			OWLObjectPropertyExpression arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeSet<OWLClass> getSuperClasses(OWLClassExpression arg0,
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
	public NodeSet<OWLObjectPropertyExpression> getSuperObjectProperties(
			OWLObjectPropertyExpression arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getTimeOut() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Node<OWLClass> getTopClassNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node<OWLDataProperty> getTopDataPropertyNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node<OWLObjectPropertyExpression> getTopObjectPropertyNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeSet<OWLClass> getTypes(OWLNamedIndividual arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node<OWLClass> getUnsatisfiableClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void interrupt() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isConsistent() {
        Collection<String> models = enumerator.enumerateAllModels();
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
			if(axiom instanceof OWLSubClassOfAxiom) {
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
			}
		}
		entailmentOutput.close();
		enumerator = new ClingoModelEnumerator(new String[] {tmpFile.getAbsolutePath(), tmpEntailmentFile.getAbsolutePath()});
		if (enumerator.enumerateModels(1).size() == 0) {
			tmpEntailmentFile.delete();
			return true;
		} else{
			tmpEntailmentFile.delete();
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
	public boolean isSatisfiable(OWLClassExpression arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void precomputeInferences(InferenceType... arg0) {
		// TODO Auto-generated method stub

	}

}
