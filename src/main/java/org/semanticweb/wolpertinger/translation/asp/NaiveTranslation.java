/**
 * 
 */
package org.semanticweb.wolpertinger.translation.asp;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

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
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
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

import uk.ac.manchester.cs.owl.owlapi.OWLObjectMinCardinalityImpl;

/**
 * Implementation of the naive translation proposed for encoding 
 * a normalized ontology into an answer set program.
 * 
 * 
 * 
 * @see OWLAxiomVisitor
 * @see OWLClassExpressionVisitor
 * 
 * @author Lukas Schweizer
 *
 */
public class NaiveTranslation implements ASPTranslation {
	
	private Configuration configuration;
	private PrintWriter writer;
	private SignatureMapper mapper;
	private VariableIssuer var;
	
	private Set<OWLClass> auxClasses;
	
	/**
	 * Default constructor. Creates a {@link NaiveTranslation} with 
	 * default configuration and outputs to {@link System#out}.
	 */
	public NaiveTranslation() {
		this(new Configuration(), new PrintWriter(System.out));
	}
	
	/**
	 * Creates a {@link NaiveTranslation} instance with default configuration.
	 * 
	 * @param writer
	 */
	public NaiveTranslation(PrintWriter writer) {
		this (new Configuration(), writer);
	}
	
	/**
	 * Creates a {@link NaiveTranslation} instance 
	 * @param configuration
	 * @param writer
	 */
	public NaiveTranslation(Configuration configuration, PrintWriter writer) {
		this.configuration = configuration;
		this.writer = writer;
		this.mapper = SignatureMapper.ASP2CoreMapping;
	}
	
	// ----------------------
	// BEGIN MEMBER methods
	// ----------------------
	
	/**
	 * Translate the given axioms into a corresponding answer set program.
	 * 
	 * @param normalizedOntology
	 */
	public void translateOntology(OWLAxioms normalizedOntology) {
		// 
		for (OWLNamedIndividual individual : normalizedOntology.m_namedIndividuals) {
			assertThing(individual);
			writer.print("\n");
		}
		
		// ABox axioms
		for (OWLIndividualAxiom assertion : normalizedOntology.m_facts) {
			assertion.accept(this);
			writer.println();
		}
		
		// TBox axioms
		for (OWLClassExpression[] inclusion : normalizedOntology.m_conceptInclusions) {
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
		
		// RBox
		//TODO
		
		// Guessing
		for (OWLClass owlClass : normalizedOntology.m_classes) {
			createExtensionGuess(owlClass);
			var.reset();
			
			writer.println();
		}
		
		for (OWLObjectProperty property : normalizedOntology.m_objectProperties) {
			createExtensionGuess(property);
			var.reset();
			
			writer.println();
		}
		
		writer.flush();
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
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAnnotationAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom)
	 */
	@Override
	public void visit(OWLSubAnnotationPropertyOfAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAnnotationAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom)
	 */
	@Override
	public void visit(OWLAnnotationPropertyDomainAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAnnotationAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom)
	 */
	@Override
	public void visit(OWLAnnotationPropertyRangeAxiom arg0) {
		// TODO Auto-generated method stub
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
		
		if (isAuxiliaryClass(owlClass)) {
			if (auxClasses == null) auxClasses = new HashSet<OWLClass>();
			auxClasses.add(owlClass);
		}
		
		writer.print(ASP2CoreSymbols.NAF + " ");
		writer.print(predicateName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(var.currentVar());
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectIntersectionOf)
	 */
	@Override
	public void visit(OWLObjectIntersectionOf arg0) {
		// TODO Auto-generated method stub
		writer.print("OWLObectIntersectionOf");
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectUnionOf)
	 */
	@Override
	public void visit(OWLObjectUnionOf arg0) {
		// TODO Auto-generated method stub
		writer.print("OWLObjectUnionOf");
	}

	/* We assume that we deal with a normalized axioms, i.e. they are in NNF and structural transformation tool place.
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
		}
		//
		else if (operand instanceof OWLObjectHasSelf) {
			//TODO: test this case!
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
	public void visit(OWLObjectAllValuesFrom arg0) {
		//
		OWLObjectProperty property = arg0.getProperty().asOWLObjectProperty();
		
		OWLClassExpression  fillerClass = arg0.getFiller();
		
		String propertyName = mapper.getPredicateName(property);
		//String className = mapper.getPredicateName(fillerClass);
		writer.print(propertyName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		String var1 = var.currentVar();
		String var2 = var.nextVariable();
		writer.print(var1);
		writer.print(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.print(var2);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.CONJUNCTION);
		
		// distinguish -A or A of filler
		if (fillerClass instanceof OWLObjectComplementOf) {
			OWLClass owlClass = ((OWLObjectComplementOf)fillerClass).getOperand().asOWLClass();
			String predicateName = mapper.getPredicateName(owlClass);
			
			//writer.print(ASP2CoreSymbols.NAF + " ");
			writer.print(predicateName);
			writer.print(ASP2CoreSymbols.BRACKET_OPEN);
			writer.print(var2);
			writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		} else {
			assert fillerClass instanceof OWLClass;
			String predicateName = mapper.getPredicateName(fillerClass.asOWLClass());
			
			writer.print(ASP2CoreSymbols.NAF + " ");
			writer.print(predicateName);
			writer.print(ASP2CoreSymbols.BRACKET_OPEN);
			writer.print(var.currentVar());
			writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
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

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectMinCardinality)
	 */
	@Override
	public void visit(OWLObjectMinCardinality minCardinality) {
		OWLClassExpression filler = minCardinality.getFiller();
		OWLObjectPropertyExpression property = minCardinality.getProperty();
		
		assert filler instanceof OWLClass;
		assert property instanceof OWLObjectProperty;
		
		String fillerName = mapper.getPredicateName(filler.asOWLClass());
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
		writer.print("} < " + minCardinality.getCardinality());
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectExactCardinality)
	 */
	@Override
	public void visit(OWLObjectExactCardinality exactCardinality) {
		OWLClassExpression filler = exactCardinality.getFiller();
		OWLObjectPropertyExpression property = exactCardinality.getProperty();
		
		assert filler instanceof OWLClass;
		assert property instanceof OWLObjectProperty;
		
		String fillerName = mapper.getPredicateName(filler.asOWLClass());
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
		writer.print("} != " + exactCardinality.getCardinality());
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectMaxCardinality)
	 */
	@Override
	public void visit(OWLObjectMaxCardinality maxCardinality) {
		OWLClassExpression filler = maxCardinality.getFiller();
		OWLObjectPropertyExpression property = maxCardinality.getProperty();
		
		assert filler instanceof OWLClass;
		assert property instanceof OWLObjectProperty;
		
		String fillerName = mapper.getPredicateName(filler.asOWLClass());
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
		writer.print("} > " + maxCardinality.getCardinality());
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectHasSelf)
	 */
	@Override
	public void visit(OWLObjectHasSelf arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectOneOf)
	 */
	@Override
	public void visit(OWLObjectOneOf arg0) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

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

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLAsymmetricObjectPropertyAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLReflexiveObjectPropertyAxiom arg0) {
		// TODO Auto-generated method stub

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

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom)
	 */
	@Override
	public void visit(OWLDisjointObjectPropertiesAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom)
	 */
	@Override
	public void visit(OWLObjectPropertyRangeAxiom arg0) {
		
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
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom)
	 */
	@Override
	public void visit(OWLSubObjectPropertyOfAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLDisjointUnionAxiom)
	 */
	@Override
	public void visit(OWLDisjointUnionAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLSymmetricObjectPropertyAxiom arg0) {
		// TODO Auto-generated method stub

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

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLIrreflexiveObjectPropertyAxiom arg0) {
		// TODO Auto-generated method stub

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

}
