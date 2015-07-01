package org.semanticweb.wolpertinger;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
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
import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.FreshEntityPolicy;
import org.semanticweb.owlapi.reasoner.IndividualNodeSetPolicy;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.Version;
import org.semanticweb.wolpertinger.structural.OWLAxioms;
import org.semanticweb.wolpertinger.structural.OWLNormalization;
import org.semanticweb.wolpertinger.structural.util.NiceAxiomPrinter;
import org.semanticweb.wolpertinger.translation.OWLOntologyTranslator;

/**
 * TODO: Detailed Description here.
 * 
 * @author Lukas Schweizer
 *
 */
public class Wolpertinger implements OWLReasoner {
	
	private OWLOntology rootOntology;
	// normalized kb
	private OWLAxioms axioms;
	
	private Configuration configuration;
	
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
		OWLNormalization normalization = new OWLNormalization(getOWLDataFactory(), this.axioms, 0);
		
		for (OWLOntology ontology : importClosure) {
			normalization.processOntology(ontology);
		}
	}
	
	private void clearState() {
		this.axioms = null;
	}
	
	private OWLDataFactory getOWLDataFactory() {
		return rootOntology.getOWLOntologyManager().getOWLDataFactory();
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
	public NodeSet<OWLNamedIndividual> getInstances(OWLClassExpression arg0,
			boolean arg1) {
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEntailed(OWLAxiom arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEntailed(Set<? extends OWLAxiom> arg0) {
		// TODO Auto-generated method stub
		return false;
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
