/**
 * 
 */
package org.semanticweb.wolpertinger.translation.naive;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectCardinalityRestriction;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.wolpertinger.Configuration;
import org.semanticweb.wolpertinger.Prefixes;
import org.semanticweb.wolpertinger.structural.OWLAxioms;
import org.semanticweb.wolpertinger.structural.OWLNormalization;
import org.semanticweb.wolpertinger.structural.OWLAxioms.DisjunctiveRule;
import org.semanticweb.wolpertinger.translation.OWLOntologyTranslator;
import org.semanticweb.wolpertinger.translation.SignatureMapper;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import uk.ac.manchester.cs.owl.owlapi.OWLAsymmetricObjectPropertyAxiomImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLDisjointObjectPropertiesAxiomImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLIrreflexiveObjectPropertyAxiomImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectComplementOfImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectMinCardinalityImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLReflexiveObjectPropertyAxiomImpl;

/**
 * Implementation of the naive translation proposed in our paper, for encoding 
 * a normalized OWL DL ontology into an answer set program.
 * <p>
 * 
 * </p>
 * 
 * @see OWLAxiomVisitor
 * @see OWLClassExpressionVisitor
 * 
 * @author Lukas Schweizer
 *
 */
public class NaiveTranslation implements OWLOntologyTranslator {
	
	//private Configuration configuration;
	private PrintWriter writer;
	private SignatureMapper mapper;
	private VariableIssuer var;
	
	private Set<OWLClass> auxClasses;
	// inclusions resutling from, e.g. resolving nominals
	private Collection<OWLClassExpression[]> newInclusions;
	private Configuration configuration;
	
	/**
	 * Creates a {@link NaiveTranslation} instance 
	 * @param configuration
	 * @param writer
	 */
	public NaiveTranslation(Configuration configuration, PrintWriter writer) {
		// TODO: based on config parameter instantiate the name mappers (nice,std)
		this.mapper = SignatureMapper.ASP2CoreMapping;
		this.newInclusions = new LinkedList<OWLClassExpression[]>();
		this.auxClasses = new HashSet<OWLClass>();
		this.configuration = configuration;
		this.writer = writer;
	}
	
	// ----------------------
	// BEGIN MEMBER methods
	// ----------------------
	
	/**
	 * Load the root ontology and all imports and apply normalization.
	 */
	private OWLAxioms loadOntology(OWLOntology rootOntology) {	
		OWLAxioms axioms = new OWLAxioms();
		
		Collection<OWLOntology> importClosure = rootOntology.getImportsClosure();
		OWLNormalization normalization = new OWLNormalization(rootOntology.getOWLOntologyManager().getOWLDataFactory(), axioms, 0);
		
		for (OWLOntology ontology : importClosure) {
			normalization.processOntology(ontology);
		}
		
		return axioms;
	}
	
	private void clearState() {
		this.newInclusions = new LinkedList<OWLClassExpression[]>();
		this.auxClasses = new HashSet<OWLClass>();
	}
	
	/**
	 * Translate the given {@link OWLOntology}.
	 * Note that the result is written to the PrintWriter, which was given in the constructor.
	 */
	@Override
	public void translateOntology(OWLOntology rootOntology) {
		translateOntology(loadOntology(rootOntology));
	}
	
	/**
	 * We have the OWLOntology (ies) now (normalized) in our internal data model representation.
	 * 
	 * @param normalizedOntology
	 */
	public void translateOntology(OWLAxioms normalizedOntology) {
		clearState();
		
		// thing assertions for all named individuals
		for (OWLNamedIndividual individual : normalizedOntology.m_namedIndividuals) {
			// TODO: avoid adding assertions to owl:Thing when there is already real owl:Thing assertions
			assertThing(individual);
			writer.println();
		}
		
		// ABox axioms
		for (OWLIndividualAxiom assertion : normalizedOntology.m_facts) {
			assertion.accept(this);
			writer.println();
		}
		
		// TBox axioms
		for (OWLClassExpression[] inclusion : normalizedOntology.m_conceptInclusions) {
			translateInclusion(inclusion);
		}
		
		// RBox
		for (OWLObjectPropertyExpression objectPropertyExp : normalizedOntology.m_complexObjectPropertyExpressions) {
			// TODO
		}
		for(OWLObjectPropertyExpression objPropertyExp : normalizedOntology.m_asymmetricObjectProperties) {
			//
			OWLAsymmetricObjectPropertyAxiomImpl asyProp = new OWLAsymmetricObjectPropertyAxiomImpl(objPropertyExp, new LinkedList<OWLAnnotation>());
			asyProp.accept(this);
			
			writer.println();
		}
		for (OWLObjectPropertyExpression objPropertyExp : normalizedOntology.m_irreflexiveObjectProperties) {
			OWLIrreflexiveObjectPropertyAxiomImpl irrProp = new OWLIrreflexiveObjectPropertyAxiomImpl(objPropertyExp, new LinkedList<OWLAnnotation>());
			irrProp.accept(this);
			
			writer.println();
		}
		for (OWLObjectPropertyExpression objPropertyExp : normalizedOntology.m_reflexiveObjectProperties) {
			OWLReflexiveObjectPropertyAxiomImpl refProp = new OWLReflexiveObjectPropertyAxiomImpl(objPropertyExp, new LinkedList<OWLAnnotation>());
			refProp.accept(this);
			
			writer.println();
		}
		for (OWLObjectPropertyExpression[] properties : normalizedOntology.m_disjointObjectProperties) {
			HashSet<OWLObjectPropertyExpression> props = new HashSet<OWLObjectPropertyExpression>();
			for (OWLObjectPropertyExpression property : properties) {
				props.add(property);
			}
			OWLDisjointObjectPropertiesAxiomImpl disProp = new OWLDisjointObjectPropertiesAxiomImpl(props, new LinkedList<OWLAnnotation>());
			disProp.accept(this);
			
			writer.println();
		}
		
		// translate remaining new inclusions, mainly dealing with auxiliary classes 
		for (OWLClassExpression[] inclusion : newInclusions) {
			translateInclusion(inclusion);
		}
		
		// add assertions of nominal guard classes
		for (OWLNamedIndividual individual : nominalGuards.keySet()) {
			OWLClass guard = nominalGuards.get(individual);
			String guardName = mapper.getPredicateName(guard);
			String indibName = mapper.getConstantName(individual);
			
			writer.write(guardName);
			writer.write(ASP2CoreSymbols.BRACKET_OPEN);
			writer.write(indibName);
			writer.write(ASP2CoreSymbols.BRACKET_CLOSE);
			writer.write(ASP2CoreSymbols.EOR);
			writer.println();
		}
		
		// Guessing
		for (OWLClass owlClass : normalizedOntology.m_classes) {
			createExtensionGuess(owlClass);
			var.reset();
			
			writer.println();
		}
		// Take care of auxiliary classes, if there are any...
		//if (null != auxClasses) {
			for (OWLClass owlClass : auxClasses) {
				createExtensionGuess(owlClass);
				var.reset();

				writer.println();
			}
		//}
		
		for (OWLObjectProperty property : normalizedOntology.m_objectProperties) {
			createExtensionGuess(property);
			var.reset();
			
			writer.println();
		}
		
		// SWRL Rules
		for (DisjunctiveRule rule : normalizedOntology.m_rules) {
			throw new NotImplementedException();
		}
		
		// add #showp/n.  satements if required
		for (IRI conceptIRI : configuration.getConceptNamesToProjectOn()) {
			String conceptName = mapper.getPredicateName(new OWLClassImpl(conceptIRI));
			
			writer.write("#show " + conceptName + "/1.");
			writer.println();
		}
		
		writer.flush();
	}
	
	private void translateInclusion(OWLClassExpression[] inclusion) {
		var = new VariableIssuer();
		writer.print(ASP2CoreSymbols.IMPLICATION);
		
		boolean isFirst=true;
		for (OWLClassExpression classExp : inclusion) {
			if (!isFirst) {
				writer.print(ASP2CoreSymbols.CONJUNCTION);
			}
			classExp.accept(this);
			isFirst=false;
		}
		
		writer.print(ASP2CoreSymbols.EOR);
		writer.println();
		
		var.reset();
	}
	
	/**
	 * For the given class, the extension guess of the form
	 * <code>
	 * -c(X) :- not c(X), thing(X).
	 * c(X) :- not -c(X), thing(X).
	 * </code>
	 * 
	 * TODO: Improve OWLThing handling
	 * @param owlClass
	 */
	private void createExtensionGuess(OWLClass owlClass) {
		String owlThing = "thing";
		
		String className = mapper.getPredicateName(owlClass);
		String negClassName = "-" + className;
		
		writer.print(className);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(var.currentVar());
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		
		writer.print(ASP2CoreSymbols.IMPLICATION);
		writer.print(ASP2CoreSymbols.NAF + " ");
		writer.print(negClassName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(var.currentVar());
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		
		writer.print(ASP2CoreSymbols.CONJUNCTION);
		
		writer.print(owlThing);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(var.currentVar());
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.EOR);
		
		writer.println();
		
		writer.print(negClassName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(var.currentVar());
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		
		writer.print(ASP2CoreSymbols.IMPLICATION);
		writer.print(ASP2CoreSymbols.NAF + " ");
		writer.print(className);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(var.currentVar());
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		
		writer.print(ASP2CoreSymbols.CONJUNCTION);
		
		writer.print(owlThing);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(var.currentVar());
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.EOR);
	}
	
	/**
	 * For the given property, create the extension guess of the form
	 * <code>
	 * r(X,Y) :- not -r(X,Y), thing(X), thing(Y).
	 * -r(X,Y) :- not r(X,Y), thing(X), thing(Y).
	 * </code>
	 * @param property
	 */
	private void createExtensionGuess(OWLObjectProperty property) {
		String owlThing = "thing";
		String propertyName = mapper.getPredicateName(property);
		String negPropertyName = "-" + propertyName;
		
		String currentVar = var.currentVar();
		String nextVar = var.nextVariable();
		
		// r(X,Y) :- not -r(X,Y), thing(X), thing(Y).
		writer.print(propertyName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		
		writer.print(ASP2CoreSymbols.IMPLICATION);
		
		writer.print(ASP2CoreSymbols.NAF);
		writer.print(" ");
		writer.print(negPropertyName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.CONJUNCTION);
		
		writer.print(owlThing);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.CONJUNCTION);
		
		writer.print(owlThing);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		//writer.print(ASP2CoreSymbols.CONJUNCTION);
		writer.print(ASP2CoreSymbols.EOR);
		
		writer.println();
		
		//-r(X,Y) :- not r(X,Y), thing(X), thing(Y).
		writer.print(negPropertyName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		
		writer.print(ASP2CoreSymbols.IMPLICATION);
		
		writer.print(ASP2CoreSymbols.NAF);
		writer.print(" ");
		writer.print(propertyName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.CONJUNCTION);
		
		writer.print(owlThing);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.CONJUNCTION);
		
		writer.print(owlThing);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.EOR);
	}
	
	private void assertThing(OWLNamedIndividual individual) {
		String owlThing = "thing";
		
		String individualName = mapper.getConstantName(individual);
		
		writer.print(owlThing);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(individualName);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.EOR);
	}
	
	/**
	 * Provides a sequence of variables X,Y,Y1,Y2,...
	 */
	private final class VariableIssuer {
		
		int counter = 0;
		String currentVar = "X";
		
		public VariableIssuer() {
			
		}
		
		public void reset() {
			counter = 0;
			currentVar = "X";
		}
		
		public String currentVar() {
			return currentVar;
		}
		
		public String nextVariable() {
			String nvar = "X";
			counter++;
			
			if (counter == 1) nvar = "Y";
			else if (counter > 1) nvar = "Y".concat(String.valueOf(counter-1));
			
			return currentVar = nvar;
		}
		
	}
	
	
	// ----------------------
	// BEGIN OWLAxiomVisitor methods
	// ----------------------

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAnnotationAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom)
	 */
	@Override
	public void visit(OWLAnnotationAssertionAxiom arg0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAnnotationAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom)
	 */
	@Override
	public void visit(OWLSubAnnotationPropertyOfAxiom arg0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAnnotationAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom)
	 */
	@Override
	public void visit(OWLAnnotationPropertyDomainAxiom arg0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAnnotationAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom)
	 */
	@Override
	public void visit(OWLAnnotationPropertyRangeAxiom arg0) {
		throw new NotImplementedException();
	}
	
	/**
	 * Determines, whether the given {@link OWLClass} is an auxiliary class name introduced
	 * while normalizing.
	 * 
	 * @param auxClass
	 * @return
	 */
	private boolean isAuxiliaryClass(OWLClass auxClass) {
		return Prefixes.isInternalIRI(auxClass.getIRI().toString());
	}
	

	/**
	 * According to the naive translation:<br/>
	 * <br/>
	 * <code>naive(A) := not A(X)</code>
	 * 
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLClass)
	 */
	@Override
	public void visit(OWLClass owlClass) {
		String predicateName = mapper.getPredicateName(owlClass);
		
		writer.print(ASP2CoreSymbols.NAF + " ");
		writer.print(predicateName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(var.currentVar());
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		
		if (isAuxiliaryClass(owlClass)) auxClasses.add(owlClass);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectIntersectionOf)
	 */
	@Override
	public void visit(OWLObjectIntersectionOf arg0) {
		// should not occur since axioms are normalized
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectUnionOf)
	 */
	@Override
	public void visit(OWLObjectUnionOf arg0) {
		// should not occur since axioms are normalized
		throw new NotImplementedException();
	}

	/** 
	 * We assume that we deal with a normalized axioms, i.e. they are in NNF and structural transformation took place.
	 * 
	 * Thereofre we test here whether the operand
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectComplementOf)
	 */
	@Override
	public void visit(OWLObjectComplementOf objComplementOf) {
		OWLClassExpression operand = objComplementOf.getOperand();
		if (operand instanceof OWLClass) {
			OWLClass owlClass = operand.asOWLClass();
			String predicateName = mapper.getPredicateName(owlClass);
			
			writer.print(predicateName);
			writer.print(ASP2CoreSymbols.BRACKET_OPEN);
			writer.print(var.currentVar());
			writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
			
			if (isAuxiliaryClass(owlClass)) auxClasses.add(owlClass);
		}
		//
		else if (operand instanceof OWLObjectHasSelf) {
			throw new NotImplementedException();
		}
		else if (operand instanceof OWLObjectOneOf) {
			throw new NotImplementedException();
		}
	}

	/**
	 * According to the naive translation:<br/>
	 * <br/>
	 * <code>naive(Exists r.A) := not r(X,Y), A(Y)</code>
	 * 
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom)
	 */
	@Override
	public void visit(OWLObjectSomeValuesFrom objExistential) {
		// we require normalized axioms, therefore we can do the following
		OWLObjectProperty property = objExistential.getProperty().asOWLObjectProperty();
		OWLClassExpression fillerClass = objExistential.getFiller();
		
		OWLObjectMinCardinality minCard = new OWLObjectMinCardinalityImpl(property, 1, fillerClass);
		visit(minCard);
	}

	/**
	 * According to the naive translation:<br/>
	 * <br/>
	 * <code>naive(ForAll r.A) := r(X,Y), not A(Y)</code>
	 * 
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectAllValuesFrom)
	 */
	@Override
	public void visit(OWLObjectAllValuesFrom allValFrom) {
		OWLObjectProperty property = allValFrom.getProperty().asOWLObjectProperty();
		OWLClassExpression filler = allValFrom.getFiller();
		
		String propertyName = mapper.getPredicateName(property);
		//String className = mapper.getPredicateName(fillerClass);
		writer.print(propertyName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		String cVar = var.currentVar();
		String nVar = var.nextVariable();
		writer.print(cVar);
		writer.print(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.print(nVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.CONJUNCTION);
		
		// distinguish -A or A
		// complex fillers are not possible anymore at this stage
		if (filler instanceof OWLObjectComplementOf) {
			OWLClass owlClass = ((OWLObjectComplementOf)filler).getOperand().asOWLClass();
			String predicateName = mapper.getPredicateName(owlClass);
			
			//writer.print(ASP2CoreSymbols.NAF + " ");
			writer.print(predicateName);
			writer.print(ASP2CoreSymbols.BRACKET_OPEN);
			writer.print(nVar);
			writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
			
			if (isAuxiliaryClass(owlClass)) auxClasses.add(owlClass);
		}
		else if (filler instanceof OWLObjectOneOf) {
			OWLObjectOneOf oneOf = (OWLObjectOneOf) filler;
			OWLClass auxOneOf = getOneOfAuxiliaryClass(oneOf);
			
			String auxOneOfName = mapper.getPredicateName(auxOneOf);
			
			writer.print(ASP2CoreSymbols.NAF + " ");
			writer.write(auxOneOfName);
			writer.write(ASP2CoreSymbols.BRACKET_OPEN);
			writer.write(nVar);
			writer.write(ASP2CoreSymbols.BRACKET_CLOSE);
		}
		else {
			assert filler instanceof OWLClass;
			String predicateName = mapper.getPredicateName(filler.asOWLClass());
			
			writer.print(ASP2CoreSymbols.NAF + " ");
			writer.print(predicateName);
			writer.print(ASP2CoreSymbols.BRACKET_OPEN);
			writer.print(nVar);
			writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
			
			if (isAuxiliaryClass(filler.asOWLClass())) 
				auxClasses.add(filler.asOWLClass());
		}
		
		var.reset();
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectHasValue)
	 */
	@Override
	public void visit(OWLObjectHasValue arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private HashMap<OWLObjectOneOf, OWLClass> oneOfAuxClasses = new HashMap<OWLObjectOneOf, OWLClass>();
	
	/**
	 * For a One-of Object {a,b,c,...} create and auxiliary class oo1, and
	 * and axiom <code>oo1 subSetOf guard_i_a or guard_i_b or ...</code>
	 * @param objectOneOf
	 * @return
	 */
	private OWLClass getOneOfAuxiliaryClass(OWLObjectOneOf objectOneOf) {
		if (oneOfAuxClasses.containsKey(objectOneOf))
			return oneOfAuxClasses.get(objectOneOf);
		
		OWLClass auxOneOf = new OWLClassImpl(IRI.create(INTERNAL_IRI_PREFIX + "#oneOfAux" + (oneOfAuxClasses.size()+1)));
		OWLClassExpression[] inclusion = new OWLClassExpression[2];
		
		inclusion[0] = new OWLObjectComplementOfImpl(auxOneOf);
		inclusion[1] = objectOneOf;

		//translateInclusion(inclusion);
		newInclusions.add(inclusion);
		
		// add to the set of class which needs to be guessed
		auxClasses.add(auxOneOf);
		return auxOneOf;
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectMinCardinality)
	 */
	@Override
	public void visit(OWLObjectMinCardinality minCardinality) {
		visitCardinalityRestriction(minCardinality);
	}
	
	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectExactCardinality)
	 */
	@Override
	public void visit(OWLObjectExactCardinality exactCardinality) {
		visitCardinalityRestriction(exactCardinality);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectMaxCardinality)
	 */
	@Override
	public void visit(OWLObjectMaxCardinality maxCardinality) {
		visitCardinalityRestriction(maxCardinality);
	}
	
	private void visitCardinalityRestriction(OWLObjectCardinalityRestriction cardinalityRestriction) {
		OWLClassExpression filler = cardinalityRestriction.getFiller();
		OWLObjectPropertyExpression property = cardinalityRestriction.getProperty();
		
		String fillerName, comperator;
		
		if (cardinalityRestriction instanceof OWLObjectMinCardinality)
			comperator = "<";
		else if (cardinalityRestriction instanceof OWLObjectMaxCardinality)
			comperator = ">";
		else
			comperator = "=";
		
		if (filler instanceof OWLObjectComplementOf){
			throw new NotImplementedException();
		}
		else if (filler instanceof OWLObjectOneOf) {
			//TODO: in case of a max-cardinality we will never end up within here,
			// since the normalization for max-cardinality uses an "optimization".
			OWLObjectOneOf oneOf = (OWLObjectOneOf) filler;
			OWLClass auxOneOf= getOneOfAuxiliaryClass(oneOf);
			
			fillerName = mapper.getPredicateName(auxOneOf);
		}
		else {
			assert filler instanceof OWLClass;
			
			fillerName = mapper.getPredicateName(filler.asOWLClass());
			
			if (isAuxiliaryClass(filler.asOWLClass()))
				auxClasses.add(filler.asOWLClass());
		}
		
		assert property instanceof OWLObjectProperty;
		String propertyName = mapper.getPredicateName(property.asOWLObjectProperty());
		
		String currentVar = var.currentVar();
		String nextVar = var.nextVariable();
		
		writer.print("#count{");
		writer.print(nextVar + "," + propertyName + ":");
		writer.print(propertyName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.CONJUNCTION);
		writer.print(fillerName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print("}" + comperator + cardinalityRestriction.getCardinality());
		
		var.reset();
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectHasSelf)
	 */
	@Override
	public void visit(OWLObjectHasSelf arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectOneOf)
	 */
	@Override
	public void visit(OWLObjectOneOf owlOneOf) {
		boolean isFirst=true;
		for (OWLIndividual individual : owlOneOf.getIndividuals()) {
			if (individual.isNamed()){
				if (!isFirst)
					writer.write(ASP2CoreSymbols.CONJUNCTION);
				
				OWLClass guard = getNominalGuard(individual.asOWLNamedIndividual());
				visit(guard);
				isFirst=false;
			}
		}
	}
	
	private static final String INTERNAL_IRI_PREFIX = "http://www.semanticweb.org/wolpertinger/internal";
	
	private HashMap<OWLNamedIndividual, OWLClass> nominalGuards = new HashMap<OWLNamedIndividual, OWLClass>(); 
	private OWLClass getNominalGuard(OWLNamedIndividual individual) {
		if (nominalGuards.containsKey(individual))
			return nominalGuards.get(individual);
		String className = "guard_" + mapper.getConstantName(individual);
		OWLClass guard = new OWLClassImpl(IRI.create(INTERNAL_IRI_PREFIX + "#" + className));
		nominalGuards.put(individual, guard);
		return guard;
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLDataSomeValuesFrom)
	 */
	@Override
	public void visit(OWLDataSomeValuesFrom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLDataAllValuesFrom)
	 */
	@Override
	public void visit(OWLDataAllValuesFrom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLDataHasValue)
	 */
	@Override
	public void visit(OWLDataHasValue arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLDataMinCardinality)
	 */
	@Override
	public void visit(OWLDataMinCardinality arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLDataExactCardinality)
	 */
	@Override
	public void visit(OWLDataExactCardinality arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLDataMaxCardinality)
	 */
	@Override
	public void visit(OWLDataMaxCardinality arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLDeclarationAxiom)
	 */
	@Override
	public void visit(OWLDeclarationAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLSubClassOfAxiom)
	 */
	@Override
	public void visit(OWLSubClassOfAxiom arg0) {
		throw new IllegalStateException("At this stage OWLSubClassOfAxioms should have been normalized and replaced by OWLObjectUnionOf");
	}

	/**
	 * 
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom)
	 */
	@Override
	public void visit(OWLNegativeObjectPropertyAssertionAxiom negObjPropertyAssertion) {
		//assert !negObjPropertyAssertion.getProperty().isAnonymous();
		
		OWLObjectProperty property = negObjPropertyAssertion.getProperty().asOWLObjectProperty();
		String propertyName = mapper.getPredicateName(property);
		
		OWLNamedIndividual subject = negObjPropertyAssertion.getSubject().asOWLNamedIndividual();
		OWLNamedIndividual object = negObjPropertyAssertion.getObject().asOWLNamedIndividual();
		
		String subjectName = mapper.getConstantName(subject);
		String objectName = mapper.getConstantName(object);
		
		writer.print(ASP2CoreSymbols.CLASSICAL_NEGATION);
		writer.print(propertyName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(subjectName);
		writer.print(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.print(objectName);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.EOR);
	}

	/**
	 * Asymetric Role Assertion <i>Asy(r)</i> is translated to:</br>
	 * </br>
	 * :- r(X,Y),r(Y,X).
	 * 
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLAsymmetricObjectPropertyAxiom asymetricProperty) {
		writer.write(ASP2CoreSymbols.IMPLICATION);
		
		OWLObjectPropertyExpression property = asymetricProperty.getProperty();
		
		String propertyName = mapper.getPredicateName(property.getNamedProperty());
		String cVar = var.currentVar();
		String nVar = var.nextVariable();
		
		writer.write(propertyName);
		writer.write(ASP2CoreSymbols.BRACKET_OPEN);
		writer.write(cVar);
		writer.write(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.write(nVar);
		writer.write(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.write(ASP2CoreSymbols.CONJUNCTION);
		//writer.write(ASP2CoreSymbols.NAF);
		//writer.write(ASP2CoreSymbols.SPACE);
		writer.write(propertyName);
		writer.write(ASP2CoreSymbols.BRACKET_OPEN);
		writer.write(nVar);
		writer.write(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.write(cVar);
		writer.write(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.write(ASP2CoreSymbols.EOR);
	}

	/**
	 * A reflexive role assertion<i>Ref(r)</i> is translated into the constraint:</br></br>
	 * 
	 * <code>
	 * :- not r(X,X), thing(X).
	 * </code>
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLReflexiveObjectPropertyAxiom refPropertyAxiom) {
		OWLObjectPropertyExpression property = refPropertyAxiom.getProperty();
		String propertyName = mapper.getPredicateName(property.getNamedProperty());
		String cVar = var.currentVar();
		
		writer.write(ASP2CoreSymbols.IMPLICATION);
		writer.write(ASP2CoreSymbols.NAF);
		writer.write(" ");
		writer.write(propertyName);
		writer.write(ASP2CoreSymbols.BRACKET_OPEN);
		writer.write(cVar);
		writer.write(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.write(cVar);
		writer.write(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.write(ASP2CoreSymbols.CONJUNCTION);
		writer.write("thing(");
		writer.write(cVar);
		writer.write(")"+ASP2CoreSymbols.EOR);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLDisjointClassesAxiom)
	 */
	@Override
	public void visit(OWLDisjointClassesAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom)
	 */
	@Override
	public void visit(OWLDataPropertyDomainAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom)
	 */
	@Override
	public void visit(OWLObjectPropertyDomainAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom)
	 */
	@Override
	public void visit(OWLEquivalentObjectPropertiesAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom)
	 */
	@Override
	public void visit(OWLNegativeDataPropertyAssertionAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom)
	 */
	@Override
	public void visit(OWLDifferentIndividualsAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom)
	 */
	@Override
	public void visit(OWLDisjointDataPropertiesAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Disjoint Properties are translated into constraints:</br>
	 * <code>:- r(X,Y), s(X,Y), ...</code>
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom)
	 */
	@Override
	public void visit(OWLDisjointObjectPropertiesAxiom disProperties) {
		writer.write(ASP2CoreSymbols.IMPLICATION);
		
		String cVar = var.currentVar();
		String nVar = var.nextVariable();
		
		boolean isFirst=true;
		for (OWLObjectPropertyExpression property : disProperties.getProperties()) {
			String propertyName = mapper.getPredicateName(property.getNamedProperty());
			
			if (!isFirst) {
				writer.write(ASP2CoreSymbols.CONJUNCTION);
				isFirst = false;
			}
			
			writer.write(propertyName);
			writer.write(ASP2CoreSymbols.BRACKET_OPEN);
			writer.write(cVar);
			writer.write(ASP2CoreSymbols.ARG_SEPERATOR);
			writer.write(nVar);
			writer.write(ASP2CoreSymbols.BRACKET_CLOSE);
		}
		
		writer.write(ASP2CoreSymbols.EOR);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom)
	 */
	@Override
	public void visit(OWLObjectPropertyRangeAxiom arg0) {
		throw new IllegalStateException("OWLObjectRangeAxiom should have been normalized (rewritten) already.");
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom)
	 */
	@Override
	public void visit(OWLObjectPropertyAssertionAxiom objPropertyAssertion) {
		OWLObjectProperty property = objPropertyAssertion.getProperty().asOWLObjectProperty();
		OWLNamedIndividual subject = objPropertyAssertion.getSubject().asOWLNamedIndividual();
		OWLNamedIndividual object = objPropertyAssertion.getObject().asOWLNamedIndividual();
		
		String propertyName = mapper.getPredicateName(property);
		String subjectName = mapper.getConstantName(subject);
		String objectName = mapper.getConstantName(object);
		
		writer.print(propertyName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(subjectName);
		writer.print(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.print(objectName);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.EOR);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLFunctionalObjectPropertyAxiom arg0) {
		throw new IllegalStateException("OWLFunctionalObjectPropertyAxiom should have been normalized (rewritten) already.");
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom)
	 */
	@Override
	public void visit(OWLSubObjectPropertyOfAxiom arg0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLDisjointUnionAxiom)
	 */
	@Override
	public void visit(OWLDisjointUnionAxiom arg0) {
		throw new IllegalStateException("OWLDisjointUnionAxiom should have been normalized (rewritten) already.");
	}

	/**
	 * Symetric Role Asstertion <i>Sym(r)</i>is translated as:
	 * </br></br>
	 * :- r(X,Y), not r(Y,X).</br>
	 * And thereby ruling out solutions with symetric paris (X,Y)and (Y,X).
	 * 
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLSymmetricObjectPropertyAxiom symmetricProperty) {
		writer.write(ASP2CoreSymbols.IMPLICATION);
		
		OWLObjectPropertyExpression property = symmetricProperty.getProperty();
		
		String propertyName = mapper.getPredicateName(property.getNamedProperty());
		String cVar = var.currentVar();
		String nVar = var.nextVariable();
		
		writer.write(propertyName);
		writer.write(ASP2CoreSymbols.BRACKET_OPEN);
		writer.write(cVar);
		writer.write(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.write(nVar);
		writer.write(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.write(ASP2CoreSymbols.CONJUNCTION);
		//writer.write(ASP2CoreSymbols.NAF);
		//writer.write(ASP2CoreSymbols.SPACE);
		writer.write(propertyName);
		writer.write(ASP2CoreSymbols.BRACKET_OPEN);
		writer.write(nVar);
		writer.write(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.write(cVar);
		writer.write(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.write(ASP2CoreSymbols.EOR);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom)
	 */
	@Override
	public void visit(OWLDataPropertyRangeAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom)
	 */
	@Override
	public void visit(OWLFunctionalDataPropertyAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom)
	 */
	@Override
	public void visit(OWLEquivalentDataPropertiesAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLClassAssertionAxiom)
	 */
	@Override
	public void visit(OWLClassAssertionAxiom classAssertion) {
		OWLClass owlClass = classAssertion.getClassExpression().asOWLClass();
		OWLNamedIndividual individual = classAssertion.getIndividual().asOWLNamedIndividual();
		
		writer.print(mapper.getPredicateName(owlClass));
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(mapper.getConstantName(individual));
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.EOR);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom)
	 */
	@Override
	public void visit(OWLEquivalentClassesAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom)
	 */
	@Override
	public void visit(OWLDataPropertyAssertionAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLTransitiveObjectPropertyAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * An irreflexive role assertion <i>Irr(r)</i> is tranlated into the constrained:</br>
	 * </br>
	 * <code>
	 * :- r(X,X).
	 * </code>
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLIrreflexiveObjectPropertyAxiom irrPropertyAxiom) {
		OWLObjectPropertyExpression property = irrPropertyAxiom.getProperty();
		String propertyName = mapper.getPredicateName(property.getNamedProperty());
		String cVar = var.currentVar();
		//String nVar = var.nextVariable();
		
		writer.write(ASP2CoreSymbols.IMPLICATION);
		writer.write(propertyName);
		writer.write(ASP2CoreSymbols.BRACKET_OPEN);
		writer.write(cVar);
		writer.write(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.write(cVar);
		writer.write(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.write(ASP2CoreSymbols.EOR);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom)
	 */
	@Override
	public void visit(OWLSubDataPropertyOfAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLInverseFunctionalObjectPropertyAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLSameIndividualAxiom)
	 */
	@Override
	public void visit(OWLSameIndividualAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom)
	 */
	@Override
	public void visit(OWLSubPropertyChainOfAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom)
	 */
	@Override
	public void visit(OWLInverseObjectPropertiesAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLHasKeyAxiom)
	 */
	@Override
	public void visit(OWLHasKeyAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom)
	 */
	@Override
	public void visit(OWLDatatypeDefinitionAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.SWRLRule)
	 */
	@Override
	public void visit(SWRLRule arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(OWLObjectProperty arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(OWLObjectInverseOf arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(OWLDataProperty arg0) {
		// TODO Auto-generated method stub
		
	}

}