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
package org.semanticweb.wolpertinger.translation.debug;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
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
import org.semanticweb.wolpertinger.structural.OWLAxioms.ComplexObjectPropertyInclusion;
import org.semanticweb.wolpertinger.structural.OWLNormalizationWithTracer;
import org.semanticweb.wolpertinger.translation.OWLOntologyTranslator;
import org.semanticweb.wolpertinger.translation.SignatureMapper;
import org.semanticweb.wolpertinger.translation.naive.ASP2CoreSymbols;
import org.semanticweb.wolpertinger.translation.naive.NaiveTranslation;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import uk.ac.manchester.cs.owl.owlapi.OWLAsymmetricObjectPropertyAxiomImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLDisjointObjectPropertiesAxiomImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLIrreflexiveObjectPropertyAxiomImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectComplementOfImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectMinCardinalityImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLReflexiveObjectPropertyAxiomImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLSubPropertyChainAxiomImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLSymmetricObjectPropertyAxiomImpl;

/**
 * Implementation of the debug translation, for encoding a normalized
 * OWL DL ontology into an answer set program. Each answer of the program
 * corresponds to a justification of the ontology inconsistency.
 * <p>
 *
 * </p>
 *
 * @see OWLAxiomVisitor
 * @see OWLClassExpressionVisitor
 *
 * @author Lukas Schweizer
 * @author Satyadharma Tirtarasa
 *
 */
public class DebugTranslation implements OWLOntologyTranslator {
	//private Configuration configuration;
	private PrintWriter writer;
	private SignatureMapper mapper;
	private VariableIssuer var;

	private Set<OWLClass> auxClasses;

	// inclusions resutling from, e.g. resolving nominals
	private Collection<OWLClassExpression[]> newInclusions;
	private Configuration configuration;

	private int nConstraints;
	private int nIndividuals;
	private boolean debugFlag;

	// storing information which cardinality auxiliary predicates should be computed
	private HashMap<OWLClass, HashSet<OWLObjectProperty>> nraSet;
	private HashMap<OWLClass, HashSet<OWLObjectProperty>> nraComplementSet;

	private OWLNormalizationWithTracer normalization;
	
	public DebugTranslation(Configuration configuration, PrintWriter writer, boolean debugFlag, OWLNormalizationWithTracer normalization) {
		// TODO: based on config parameter instantiate the name mappers (nice,std)
		this.mapper = SignatureMapper.ASP2CoreMapping;
		this.newInclusions = new LinkedList<OWLClassExpression[]>();
		this.auxClasses = new HashSet<OWLClass>();
		this.configuration = configuration;
		this.writer = writer;
		this.nConstraints = 0;
		this.debugFlag = debugFlag;
		this.nraSet = new HashMap<OWLClass, HashSet<OWLObjectProperty>>();
		this.nraComplementSet = new HashMap<OWLClass, HashSet<OWLObjectProperty>>();
		this.normalization = normalization;
		var = new VariableIssuer();
	}

	/**
	 * Load the root ontology and all imports and apply normalization.
	 */
	private OWLAxioms loadOntology(OWLOntology rootOntology) {
		OWLAxioms axioms = new OWLAxioms();

		Collection<OWLOntology> importClosure = rootOntology.getImportsClosure();
		if(configuration.getDomainIndividuals() == null) {
			configuration.setDomainIndividuals(rootOntology.getIndividualsInSignature(true));
		}

		normalization = new OWLNormalizationWithTracer(rootOntology.getOWLOntologyManager().getOWLDataFactory(), axioms, 0, configuration.getDomainIndividuals());

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
	
	public void translateOntology(OWLOntology rootOntology) {
		OWLAxioms normalizedOntology = loadOntology(rootOntology);
	}
	
	public void translateOntologyAxioms(OWLAxioms normalizedOntology) {
		generateAxiomsTranslation(normalizedOntology);
		writer.flush();
	}
	
	public Set<OWLAxiom> retranslateSolution(String solution) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * We have the OWLOntology (ies) now (normalized) in our internal data model representation.
	 *
	 * @param normalizedOntology
	 */
	public void translateOntology(OWLAxioms normalizedOntology) {
		clearState();
		nIndividuals = normalizedOntology.m_namedIndividuals.size();
		generateAxiomsTranslation(normalizedOntology);
		generateInterpretationGuessing(normalizedOntology);
		generateShowStatements(normalizedOntology);
		generatePreference();
		writer.flush();
	}
	
	private void generateAxiomsTranslation(OWLAxioms normalizedOntology) {
		// thing assertions for all named individuals
		writer.println();
		writer.println("% Individual Assertions");
		writer.println();
		for (OWLNamedIndividual individual : normalizedOntology.m_namedIndividuals) {
			// TODO: avoid adding assertions to owl:Thing when there is already real owl:Thing assertions
			assertThing(individual);
			writer.println();
			var.reset();
		}
		
		// ABox axioms
		writer.println();
		writer.println("% ABox Axiom");
		writer.println();
		for (OWLIndividualAxiom assertion : normalizedOntology.m_facts) {
			assertion.accept(this);
			writer.println();
			var.reset();
		}
		
		// TBox axioms
		writer.println();
		writer.println("% TBox Axiom");
		writer.println();
		HashMap<OWLClassExpression[], Integer> counterHash = new HashMap<OWLClassExpression[], Integer> ();
		for (OWLClassExpression[] inclusion : normalizedOntology.m_conceptInclusions) {
			boolean hasDataTypeClassExpression = false;
			// skip axiom with data type
			for (int ii = 0; ii < inclusion.length; ii++) {
				OWLClassExpression c = inclusion[ii];
				if (c instanceof OWLDataSomeValuesFrom || 
					c instanceof OWLDataAllValuesFrom ||
					c instanceof OWLDataMaxCardinality) {
					hasDataTypeClassExpression = true;
					continue;
				}
			}
			if (hasDataTypeClassExpression) continue;
			int axiomNumber = 0;
			OWLClassExpression[] originalAxiom = normalization.finalTracer.get(inclusion);

			if (counterHash.keySet().contains(originalAxiom)) {
				axiomNumber = counterHash.get(originalAxiom);
			} else {
				counterHash.put(originalAxiom, nConstraints);
				axiomNumber = nConstraints++;
			}
			translateInclusion(inclusion, axiomNumber);
			var.reset();
		}
		
		// RBox
		for (OWLObjectPropertyExpression objectPropertyExp : normalizedOntology.m_complexObjectPropertyExpressions) {
			// TODO
		}

		for	(ComplexObjectPropertyInclusion complexObjPropertyInclusion : normalizedOntology.m_complexObjectPropertyInclusions) {
			LinkedList<OWLObjectPropertyExpression> subPropertyList = new LinkedList<OWLObjectPropertyExpression> ();
			for (OWLObjectPropertyExpression e : complexObjPropertyInclusion.m_subObjectProperties) {
				subPropertyList.add(e.getObjectPropertiesInSignature().iterator().next());
			}
			OWLSubPropertyChainAxiomImpl prop = new OWLSubPropertyChainAxiomImpl(subPropertyList, complexObjPropertyInclusion.m_superObjectProperty, new LinkedList<OWLAnnotation>());
			prop.accept(this);
			var.reset();
			writer.println();
		}

		for(OWLObjectPropertyExpression objPropertyExp : normalizedOntology.m_asymmetricObjectProperties) {
			OWLAsymmetricObjectPropertyAxiomImpl asyProp = new OWLAsymmetricObjectPropertyAxiomImpl(objPropertyExp, new LinkedList<OWLAnnotation>());
			asyProp.accept(this);
			var.reset();
			writer.println();
		}

		for (OWLObjectPropertyExpression objPropertyExp : normalizedOntology.m_irreflexiveObjectProperties) {
			OWLIrreflexiveObjectPropertyAxiomImpl irrProp = new OWLIrreflexiveObjectPropertyAxiomImpl(objPropertyExp, new LinkedList<OWLAnnotation>());
			irrProp.accept(this);
			var.reset();
			writer.println();
		}

		for (OWLObjectPropertyExpression objPropertyExp : normalizedOntology.m_reflexiveObjectProperties) {
			OWLReflexiveObjectPropertyAxiomImpl refProp = new OWLReflexiveObjectPropertyAxiomImpl(objPropertyExp, new LinkedList<OWLAnnotation>());
			refProp.accept(this);
			var.reset();
			writer.println();
		}

		for (OWLObjectPropertyExpression[] properties : normalizedOntology.m_disjointObjectProperties) {
			HashSet<OWLObjectPropertyExpression> props = new HashSet<OWLObjectPropertyExpression>();
			for (OWLObjectPropertyExpression property : properties) {
				props.add(property);
			}
			OWLDisjointObjectPropertiesAxiomImpl disProp = new OWLDisjointObjectPropertiesAxiomImpl(props, new LinkedList<OWLAnnotation>());
			disProp.accept(this);
			var.reset();
			writer.println();
		}

		for (OWLObjectPropertyExpression[] objectProperty : normalizedOntology.m_simpleObjectPropertyInclusions) {
			if(objectProperty[0].getInverseProperty().equals(objectProperty[1])) {
				OWLSymmetricObjectPropertyAxiomImpl prop = new OWLSymmetricObjectPropertyAxiomImpl(objectProperty[0], new LinkedList<OWLAnnotation>());
				prop.accept(this);
			} else {
				LinkedList<OWLObjectPropertyExpression> subProperty = new LinkedList<OWLObjectPropertyExpression> ();
				subProperty.add(objectProperty[0]);
				OWLSubPropertyChainAxiomImpl prop = new OWLSubPropertyChainAxiomImpl(subProperty, objectProperty[1], new LinkedList<OWLAnnotation>());
				prop.accept(this);
			}
			var.reset();
			writer.println();
		}

		// translate remaining new inclusions, mainly dealing with auxiliary classes
		for (OWLClassExpression[] inclusion : newInclusions) {
			translateInclusion(inclusion, nConstraints++);
			var.reset();
		}
	}
	
	private void generateInterpretationGuessing(OWLAxioms normalizedOntology) {
		// add assertions of nominal guard classes
		for (OWLNamedIndividual individual : nominalGuards.keySet()) {
			OWLClass guard = nominalGuards.get(individual);
			String guardName = mapper.getPredicateName(guard);

			if (configuration.getDomainIndividuals().contains(individual)) {
				writer.write(guardName);
				writer.write(ASP2CoreSymbols.BRACKET_OPEN);
				writer.write(mapper.getConstantName(individual));
				writer.write(ASP2CoreSymbols.BRACKET_CLOSE);
				writer.write(ASP2CoreSymbols.EOR);
				writer.println();
			} else {
				String thing = "thing";

				// 1 {guard_i_x(X):thing(X) } 1.
				writer.write("1 {");
				writer.write(guardName);
				writer.write(ASP2CoreSymbols.BRACKET_OPEN);
				writer.write(var.currentVar);
				writer.write(ASP2CoreSymbols.BRACKET_CLOSE);
				writer.write(ASP2CoreSymbols.CONDITION);
				writer.write(thing);
				writer.write(ASP2CoreSymbols.BRACKET_OPEN);
				writer.write(var.currentVar);
				writer.write(ASP2CoreSymbols.BRACKET_CLOSE);
				writer.write("} 1");
				writer.write(ASP2CoreSymbols.EOR);
				writer.println();
			}
			var.reset();
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
			if (isOneOfAuxiliaryClass(owlClass)) {

			} else {
				createExtensionGuess(owlClass);
				var.reset();
				writer.println();
			}
		}
		//}

		// Inconsistencywriter.println();
		writer.println();
		writer.println("% Inconsistency Axiom");
		writer.println();
		for (OWLClass owlClass : normalizedOntology.m_classes) {
			createIconsClass(owlClass);
			var.reset();
			writer.println();
		}
		for (OWLClass owlClass : auxClasses) {
			createIconsClass(owlClass);
			var.reset();
			writer.println();
		}

		// Everything follows
		writer.println();
		writer.println("% Everything Follows Axiom");
		writer.println();
		for (OWLClass owlClass : normalizedOntology.m_classes) {
			createIconsImpactClass(owlClass);
			var.reset();
			writer.println();
		}

		for (OWLClass owlClass : auxClasses) {
			createIconsImpactClass(owlClass);
			var.reset();
			writer.println();
		}

		// NRA
		writer.println();
		writer.println("% Negation Axiom");
		writer.println();
		Collection<OWLClass> allClasses = new LinkedList<OWLClass> ();
		allClasses.addAll(normalizedOntology.m_classes);
		allClasses.addAll(auxClasses);
		for (OWLClass owlClass : allClasses) {
			boolean needLineBreak = false;
			for (OWLObjectProperty owlProperty : normalizedOntology.m_objectProperties) {
				if(nraSet.containsKey(owlClass) && nraSet.get(owlClass).contains(owlProperty)) {
					createPropertyNegation(owlClass, owlProperty);
					needLineBreak = true;
				}
				var.reset();
				if(nraComplementSet.containsKey(owlClass) && nraComplementSet.get(owlClass).contains(owlProperty)) {
					createNegatedPropertyNegation(owlClass, owlProperty);
					needLineBreak = true;
				}
				var.reset();
				if (needLineBreak) {
				}
			}
		}

		// Property Guess
		writer.println();
		writer.println("% Guess Property Axiom");
		writer.println();

		for (OWLObjectProperty property : normalizedOntology.m_objectProperties) {
			createExtensionGuess(property);
			var.reset();
			writer.println();
		}

		// Property Inconsistency
		writer.println();
		writer.println("% Property Inconsistency Axiom");
		writer.println();
		for (OWLObjectProperty property : normalizedOntology.m_objectProperties) {
			createIconsProperty(property);
			var.reset();
			writer.println();
		}

		// Everything Follows Property
		writer.println();
		writer.println("% Everything Follows Property Axiom");
		writer.println();
		for (OWLObjectProperty property : normalizedOntology.m_objectProperties) {
			createIconsImpactProperty(property);
			var.reset();
			writer.println();
		}

		// SWRL Rules
		// for (DisjunctiveRule rule : normalizedOntology.m_rules) {
		//     writer.println(rule);
		// }

		// add #show p/n. statements if required
		for (IRI conceptIRI : configuration.getConceptNamesToProjectOn()) {
			String conceptName = mapper.getPredicateName(new OWLClassImpl(conceptIRI));

			writer.write("#show " + conceptName + "/1.");
			writer.println();
		}
	}
	
	private void generatePreference() {
		writer.println();
		writer.println("% Auxiliary Part");
		writer.println("");

		if (!debugFlag) {
			for (int ii = 0; ii < nConstraints; ii++) {
				writer.write(String.format("activated(%d).\n", ii));
			}

			writer.write(":- icons.\n");
		} else {
			writer.write(":- not icons.\n");
			writer.write(String.format("{activated(X) : X=0..%d}.\n", nConstraints - 1));
			writer.write("#optimize(p).\n");
			writer.write("#preference(p, subset) {\n");
	        writer.write(String.format("    activated(C) : C=0..%d\n", nConstraints - 1));
			writer.write("}.\n");
		}
	}
	
	private void generateShowStatements(OWLAxioms normalizedOntology) {
		if (!debugFlag) {
			// show statement
			for (OWLClass owlClass : normalizedOntology.m_classes) {
				createShowStatement(owlClass);
				writer.println();
			}

			for (OWLObjectProperty property : normalizedOntology.m_objectProperties) {
				createShowStatement(property);
				writer.println();
			}
		} else {
			writer.println("#show activated/1.");
		}
	}
	private void translateInclusion(OWLClassExpression[] inclusion, int index) {
		writer.print("icons " + ASP2CoreSymbols.IMPLICATION);
		if (debugFlag) {
			writer.write(String.format(" activated(%d), ", index));
		}
		boolean isFirst=true;
		for (OWLClassExpression classExp : inclusion) {
			if (!isFirst) {
				writer.print(ASP2CoreSymbols.CONJUNCTION);
			}
			//writer.print("----" + classExp + "----");
			classExp.accept(this);
			var.reset();
			isFirst=false;
		}

		String currentVar = var.currentVar();
		writer.print(ASP2CoreSymbols.CONJUNCTION);
		writer.print("thing");
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.EOR);
		writer.println();
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

		writer.print(ASP2CoreSymbols.CONJUNCTION + " ");

		writer.print("not_");
		writer.print(className);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(var.currentVar());
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);

		///////////////////////////////////////////////

		writer.print(" " + ASP2CoreSymbols.IMPLICATION + " ");

		writer.print(owlThing);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(var.currentVar());
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.EOR);
	}

	private void createIconsClass(OWLClass owlClass) {
		String className = mapper.getPredicateName(owlClass);

		writer.print("icons " + ASP2CoreSymbols.IMPLICATION + " ");

		writer.print(className);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(var.currentVar());
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);

		writer.print(ASP2CoreSymbols.CONJUNCTION + " ");

		writer.print("not_");
		writer.print(className);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(var.currentVar());
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.EOR);
	}

	private void createIconsImpactClass(OWLClass owlClass) {
		String className = mapper.getPredicateName(owlClass);
		String negClassName = "not_" + className;

		writer.print(className);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(var.currentVar());
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.IMPLICATION + " thing(X), icons");
		writer.print(ASP2CoreSymbols.EOR);

		writer.println();

		writer.print(negClassName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(var.currentVar());
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.IMPLICATION + " thing(X), icons");
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

		String currentVar = var.currentVar();
		String nextVar = var.nextVariable();

		// r(X,Y) :- not -r(X,Y), thing(X), thing(Y).
		writer.print(propertyName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);

		writer.print(ASP2CoreSymbols.CONJUNCTION + " ");

		writer.print("not_");
		writer.print(propertyName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);

		writer.print(" " + ASP2CoreSymbols.IMPLICATION + " ");

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
	}

	private void createPropertyNegation(OWLClass owlClass, OWLObjectProperty owlProperty) {
		String propertyName = mapper.getPredicateName(owlProperty);
		String negPropertyName = "not_" + propertyName;

		String currentVar = var.currentVar();
		String nextVar = var.nextVariable();

		String className = mapper.getPredicateName(owlClass);
		String negClassName = "not_" + className;

		String classPredicateName = propertyName + "_" + className;
		String negClassPredicateName = "not_" + classPredicateName;

		writer.print(negClassPredicateName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(",");
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(" :- ");
		writer.print(propertyName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(",");
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(", ");
		writer.print(negClassName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.EOR);
		writer.println();

		writer.print(negClassPredicateName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(",");
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(" :- ");
		writer.print(negPropertyName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(",");
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.EOR);
		writer.println();
	}

	private void createNegatedPropertyNegation(OWLClass owlClass, OWLObjectProperty owlProperty) {
		String propertyName = mapper.getPredicateName(owlProperty);
		String negPropertyName = "not_" + propertyName;

		String currentVar = var.currentVar();
		String nextVar = var.nextVariable();

		String className = mapper.getPredicateName(owlClass);

		String classPredicateName = propertyName + "_not_" + className;
		String negClassPredicateName = "not_" + classPredicateName;

		writer.print(negClassPredicateName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(",");
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(" :- ");
		writer.print(propertyName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(",");
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(", ");
		writer.print(className);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.EOR);
		writer.println();

		writer.print(negClassPredicateName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(",");
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(" :- ");
		writer.print(negPropertyName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(",");
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.EOR);
		writer.println();
	}

//	private void createEverythingFollowsPropertyNegation(OWLClass owlClass, OWLObjectProperty owlProperty) {
//		String owlThing = "thing";
//		String propertyName = mapper.getPredicateName(owlProperty);
//		String negPropertyName = "not_" + propertyName;
//
//		String currentVar = var.currentVar();
//		String nextVar = var.nextVariable();
//
//		String className = mapper.getPredicateName(owlClass);
//		String negClassName = "not_" + className;
//
//		String classPredicateName = propertyName + "_" + className;
//		String negClassPredicateName = "not_" + classPredicateName;
//
//		writer.print(negClassPredicateName);
//		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
//		writer.print(currentVar);
//		writer.print(",");
//		writer.print(nextVar);
//		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
//		writer.print(" :- icons, ");
//		writer.print("thing");
//		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
//		writer.print(currentVar);
//		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
//		writer.print(ASP2CoreSymbols.CONJUNCTION);
//		writer.print("thing");
//		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
//		writer.print(nextVar);
//		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
//		writer.print(ASP2CoreSymbols.EOR);
//	}

	private void createIconsProperty(OWLObjectProperty property) {
		String propertyName = mapper.getPredicateName(property);

		String currentVar = var.currentVar();
		String nextVar = var.nextVariable();

		writer.print("icons " + ASP2CoreSymbols.IMPLICATION + " ");

		writer.print(propertyName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.CONJUNCTION);

		writer.print("not_");
		writer.print(propertyName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);

		writer.print(ASP2CoreSymbols.EOR);
	}

	private void createIconsImpactProperty(OWLObjectProperty property) {
		String propertyName = mapper.getPredicateName(property);

		String currentVar = var.currentVar();
		String nextVar = var.nextVariable();

		writer.print(propertyName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);

		writer.print(" " + ASP2CoreSymbols.IMPLICATION + " icons, ");

		writer.print("thing");
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.CONJUNCTION);
		writer.print("thing");
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.EOR);
		writer.println();

		writer.print("not_");
		writer.print(propertyName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(" " + ASP2CoreSymbols.IMPLICATION + " icons, ");
		writer.print("thing");
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(currentVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.CONJUNCTION);
		writer.print("thing");
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(nextVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.print(ASP2CoreSymbols.EOR);
	}

	private void createShowStatement(OWLClass owlClass) {
		String className = mapper.getPredicateName(owlClass);

		writer.write("#show " + className + "/1.");
	}

	private void createShowStatement(OWLObjectProperty property) {
		String propertyName = mapper.getPredicateName(property);

		writer.write("#show " + propertyName + "/2.");
	}

	// TODO NAFF TRANSLATION?
	private void createComplexInclusion(ComplexObjectPropertyInclusion complexObjPropertyInclusion) {
		LinkedList<String> chainPredicateNameList = new LinkedList<String> ();
		for (OWLObjectPropertyExpression e : complexObjPropertyInclusion.m_subObjectProperties) {
			chainPredicateNameList.add(mapper.getPredicateName(e.getObjectPropertiesInSignature().iterator().next()));
		}
		String superPropertyName = (mapper.getPredicateName(complexObjPropertyInclusion.m_superObjectProperty.getNamedProperty()));

		int counter = 1;
		writer.print(":-");
		for (String subPropertyName : chainPredicateNameList) {
			writer.print(String.format("%s(X%d,X%d),", subPropertyName, counter, ++counter));
		}
		writer.print(String.format("not %s(X%d,X%d).", superPropertyName, 1, counter));
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
	
	public void visit(OWLAnnotationAssertionAxiom arg0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAnnotationAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom)
	 */
	
	public void visit(OWLSubAnnotationPropertyOfAxiom arg0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAnnotationAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom)
	 */
	
	public void visit(OWLAnnotationPropertyDomainAxiom arg0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAnnotationAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom)
	 */
	
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

	private boolean isOneOfAuxiliaryClass(OWLClass owlClass) {
		if (Prefixes.isInternalIRI(owlClass.getIRI().toString())) {
			String iriString = owlClass.getIRI().toString();
			boolean isOneOf = iriString.substring(iriString.lastIndexOf(":") + 1, iriString.lastIndexOf("#")).equals("nnq");
			return isOneOf;
		} else {
			return false;
		}
	}

	/**
	 * According to the naive translation:<br/>
	 * <br/>
	 * <code>naive(A) := not A(X)</code>
	 *
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLClass)
	 */
	
	public void visit(OWLClass owlClass) {
		String predicateName = mapper.getPredicateName(owlClass);

		writer.print(ASP2CoreSymbols.NAF + "_");
		writer.print(predicateName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(var.currentVar());
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);

		if (isAuxiliaryClass(owlClass)) auxClasses.add(owlClass);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectIntersectionOf)
	 */
	
	public void visit(OWLObjectIntersectionOf arg0) {
		// should not occur since axioms are normalized
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectUnionOf)
	 */
	
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
			OWLObjectHasSelf owlHasSelf = (OWLObjectHasSelf) operand;
			OWLObjectProperty property = owlHasSelf.getProperty().asOWLObjectProperty();
			String propertyName = mapper.getPredicateName(property);
			String cVar = var.currentVar();

			// r(X,X)
			writer.print(propertyName);
			writer.print(ASP2CoreSymbols.BRACKET_OPEN);
			writer.print(cVar);
			writer.print(ASP2CoreSymbols.ARG_SEPERATOR);
			writer.print(cVar);
			writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
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
	
	public void visit(OWLObjectSomeValuesFrom objExistential) {
		// we require normalized axioms, therefore we can do the following
		OWLObjectPropertyExpression property = objExistential.getProperty();
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
	
	public void visit(OWLObjectAllValuesFrom allValFrom) {
		OWLObjectProperty property;
		boolean isInverseOf = false;

		if(allValFrom.getProperty() instanceof OWLObjectInverseOf) {
			isInverseOf = true;
			property = ((OWLObjectInverseOf) allValFrom.getProperty()).getInverse().asOWLObjectProperty();
		} else {
			property = allValFrom.getProperty().asOWLObjectProperty();
		}

		OWLClassExpression filler = allValFrom.getFiller();
		String propertyName = mapper.getPredicateName(property);

		//String className = mapper.getPredicateName(fillerClass);
		
		String r1Var = var.currentVar();
		String r2Var = var.nextVariable();
		String cVar = var.currentVar();
		
		if (isInverseOf) {
			String temp = r1Var;
			r1Var = r2Var;
			r2Var = temp;
		}

		// r(X,Y),
		writer.print(propertyName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(r1Var);
		writer.print(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.print(r2Var);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);

		// distinguish -A or A
		// complex fillers are not possible anymore at this stage
		if (filler instanceof OWLObjectComplementOf) {
			OWLClassExpression expr = ((OWLObjectComplementOf)filler).getOperand();
			if (expr instanceof OWLObjectOneOf) {
				OWLObjectOneOf oneOf = (OWLObjectOneOf) expr;
				OWLClass auxOneOf = getOneOfAuxiliaryClass(oneOf);

				String auxOneOfName = mapper.getPredicateName(auxOneOf);
				writer.print(ASP2CoreSymbols.CONJUNCTION);
				writer.write(auxOneOfName);
				writer.write(ASP2CoreSymbols.BRACKET_OPEN);
				writer.write(cVar);
				writer.write(ASP2CoreSymbols.BRACKET_CLOSE);
			} else {
				assert !expr.isAnonymous();

				OWLClass owlClass = expr.asOWLClass();
				String predicateName = mapper.getPredicateName(owlClass);

				// A(X)
				writer.print(ASP2CoreSymbols.CONJUNCTION);
				writer.print(predicateName);
				writer.print(ASP2CoreSymbols.BRACKET_OPEN);
				writer.print(cVar);
				writer.print(ASP2CoreSymbols.BRACKET_CLOSE);

				if (isAuxiliaryClass(owlClass)) auxClasses.add(owlClass);
			}
		}
		else if (filler instanceof OWLObjectOneOf) {
			OWLObjectOneOf oneOf = (OWLObjectOneOf) filler;
			OWLClass auxOneOf = getOneOfAuxiliaryClass(oneOf);

			String auxOneOfName = mapper.getPredicateName(auxOneOf);
			writer.print(ASP2CoreSymbols.CONJUNCTION);
			writer.print(ASP2CoreSymbols.NAF + "_");
			writer.write(auxOneOfName);
			writer.write(ASP2CoreSymbols.BRACKET_OPEN);
			writer.write(cVar);
			writer.write(ASP2CoreSymbols.BRACKET_CLOSE);
		}
		else if (filler.isOWLNothing()) {
			// do nothing
		}
		else {
			assert filler instanceof OWLClass;
			String predicateName = mapper.getPredicateName(filler.asOWLClass());

			// not A(X).
			writer.print(ASP2CoreSymbols.CONJUNCTION);
			writer.print(ASP2CoreSymbols.NAF + "_");
			writer.print(predicateName);
			writer.print(ASP2CoreSymbols.BRACKET_OPEN);
			writer.print(cVar);
			writer.print(ASP2CoreSymbols.BRACKET_CLOSE);

			if (isAuxiliaryClass(filler.asOWLClass()))
				auxClasses.add(filler.asOWLClass());
		}
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectHasValue)
	 */
	
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

		OWLClass auxOneOf = new OWLClassImpl(IRI.create(INTERNAL_IRI_PREFIX + "#oneOfaux" + (oneOfAuxClasses.size()+1)));
		OWLClassExpression[] inclusion = new OWLClassExpression[2];

		inclusion[0] = new OWLObjectComplementOfImpl(auxOneOf);
		inclusion[1] = objectOneOf;

		//translateInclusion(inclusion);
		newInclusions.add(inclusion);

		// add to the set of class which needs to be guessed
		// auxClasses.add(auxOneOf);
		oneOfAuxClasses.put(objectOneOf, auxOneOf);
		return auxOneOf;
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectMinCardinality)
	 */
	
	public void visit(OWLObjectMinCardinality minCardinality) {
		visitCardinalityRestriction(minCardinality);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectExactCardinality)
	 */
	
	public void visit(OWLObjectExactCardinality exactCardinality) {
		visitCardinalityRestriction(exactCardinality);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectMaxCardinality)
	 */
	
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

		boolean isComplement = false;
		boolean isInverseOf = false;
		
		if(property instanceof OWLObjectInverseOf) {
			isInverseOf = true;
			property = ((OWLObjectInverseOf) property).getInverse();
		} else {

		}
		
		if (filler instanceof OWLObjectComplementOf){
			isComplement = true;
			OWLClassExpression classExpr = ((OWLObjectComplementOf) filler).getOperand();
			fillerName = mapper.getPredicateName(classExpr.asOWLClass());

			if(comperator.equals("<"))
				addComplementNRA(classExpr.asOWLClass(),property.asOWLObjectProperty());
			if (isAuxiliaryClass(classExpr.asOWLClass()))
				auxClasses.add(classExpr.asOWLClass());
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

			if(comperator.equals("<"))
				addNRA(filler.asOWLClass(),property.asOWLObjectProperty());
			if (isAuxiliaryClass(filler.asOWLClass()))
				auxClasses.add(filler.asOWLClass());
		}

		assert property instanceof OWLObjectProperty;
		
		String propertyName = mapper.getPredicateName(property.asOWLObjectProperty());
		
		String currentVar = var.currentVar();
		String nextVar = var.nextVariable();
		String classVar = nextVar;

		if (isInverseOf) {
			String temp = currentVar;
			currentVar = nextVar;
			nextVar = temp;
		}
		
		if (comperator.equals(">")) {
			writer.print("#count{");
			writer.print(classVar + "," + propertyName + ":");
			writer.print(propertyName);
			writer.print(ASP2CoreSymbols.BRACKET_OPEN);
			writer.print(currentVar);
			writer.print(ASP2CoreSymbols.ARG_SEPERATOR);
			writer.print(nextVar);
			writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
			writer.print(ASP2CoreSymbols.CONJUNCTION);
			writer.print(fillerName);
			writer.print(ASP2CoreSymbols.BRACKET_OPEN);
			writer.print(classVar);
			writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
			writer.print("}" + comperator + cardinalityRestriction.getCardinality());
		} else {
			writer.print("#count{");
			writer.print(classVar + "," + propertyName + ":");
			writer.print("not_");
			writer.print(propertyName);
			writer.print("_");
			if (isComplement) {
				writer.print("not_");
			}
			writer.print(fillerName);
			writer.print(ASP2CoreSymbols.BRACKET_OPEN);
			writer.print(currentVar);
			writer.print(",");
			writer.print(nextVar);
			writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
			writer.print("}" + ">" + (nIndividuals - cardinalityRestriction.getCardinality()));
		}
		var.reset();
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectHasSelf)
	 */
	
	public void visit(OWLObjectHasSelf owlHasSelf) {
		OWLObjectProperty property = owlHasSelf.getProperty().asOWLObjectProperty();
		String propertyName = mapper.getPredicateName(property);
		String cVar = var.currentVar();

		// not r(X,X)
		writer.print("not_");
		writer.print(propertyName);
		writer.print(ASP2CoreSymbols.BRACKET_OPEN);
		writer.print(cVar);
		writer.print(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.print(cVar);
		writer.print(ASP2CoreSymbols.BRACKET_CLOSE);

	}

	/**
	 *
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectOneOf)
	 */
	
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
	
	public void visit(OWLDataSomeValuesFrom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLDataAllValuesFrom)
	 */
	
	public void visit(OWLDataAllValuesFrom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLDataHasValue)
	 */
	
	public void visit(OWLDataHasValue arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLDataMinCardinality)
	 */
	
	public void visit(OWLDataMinCardinality arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLDataExactCardinality)
	 */
	
	public void visit(OWLDataExactCardinality arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLDataMaxCardinality)
	 */
	
	public void visit(OWLDataMaxCardinality arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLDeclarationAxiom)
	 */
	
	public void visit(OWLDeclarationAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLSubClassOfAxiom)
	 */
	
	public void visit(OWLSubClassOfAxiom arg0) {
		throw new IllegalStateException("At this stage OWLSubClassOfAxioms should have been normalized and replaced by OWLObjectUnionOf");
	}

	/**
	 *
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom)
	 */
	
	public void visit(OWLNegativeObjectPropertyAssertionAxiom negObjPropertyAssertion) {
		//assert !negObjPropertyAssertion.getProperty().isAnonymous();

		OWLObjectProperty property = negObjPropertyAssertion.getProperty().asOWLObjectProperty();
		String propertyName = mapper.getPredicateName(property);

		OWLNamedIndividual subject = negObjPropertyAssertion.getSubject().asOWLNamedIndividual();
		OWLNamedIndividual object = negObjPropertyAssertion.getObject().asOWLNamedIndividual();

		String subjectName = mapper.getConstantName(subject);
		String objectName = mapper.getConstantName(object);

		writer.print("not_");
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
	
	public void visit(OWLAsymmetricObjectPropertyAxiom asymetricProperty) {
		OWLObjectPropertyExpression property = asymetricProperty.getProperty();

		String propertyName = mapper.getPredicateName(property.getNamedProperty());
		String cVar = var.currentVar();
		String nVar = var.nextVariable();

		writer.write("icons " + ASP2CoreSymbols.IMPLICATION);
		if (debugFlag) {
			writer.write(String.format(" activated(%d), ", nConstraints++));
		} 
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
	
	public void visit(OWLReflexiveObjectPropertyAxiom refPropertyAxiom) {
		OWLObjectPropertyExpression property = refPropertyAxiom.getProperty();
		String propertyName = mapper.getPredicateName(property.getNamedProperty());
		String cVar = var.currentVar();

		writer.write("icons " + ASP2CoreSymbols.IMPLICATION);
		writer.write(String.format(" activated(%d), ", nConstraints++));
		writer.write(ASP2CoreSymbols.NAF);
		writer.write("_");
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
	
	public void visit(OWLDisjointClassesAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom)
	 */
	
	public void visit(OWLDataPropertyDomainAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom)
	 */
	
	public void visit(OWLObjectPropertyDomainAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom)
	 */
	
	public void visit(OWLEquivalentObjectPropertiesAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom)
	 */
	
	public void visit(OWLNegativeDataPropertyAssertionAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom)
	 */
	
	public void visit(OWLDifferentIndividualsAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom)
	 */
	
	public void visit(OWLDisjointDataPropertiesAxiom arg0) {
		// TODO Auto-generated method stub
	}

	/**
	 * Disjoint Properties are translated into constraints:</br>
	 * <code>:- r(X,Y), s(X,Y), ...</code>
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom)
	 */
	
	public void visit(OWLDisjointObjectPropertiesAxiom disProperties) {
		writer.write("icons " + ASP2CoreSymbols.IMPLICATION);
		if (debugFlag) {
			writer.write(String.format(" activated(%d), ", nConstraints++));
		}
		String cVar = var.currentVar();
		String nVar = var.nextVariable();

		boolean isFirst=true;
		for (OWLObjectPropertyExpression property : disProperties.getProperties()) {
			String propertyName = mapper.getPredicateName(property.getNamedProperty());

			if (!isFirst) {
				writer.write(ASP2CoreSymbols.CONJUNCTION);

			}
			isFirst = false;
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
	
	public void visit(OWLObjectPropertyRangeAxiom arg0) {
		throw new IllegalStateException("OWLObjectRangeAxiom should have been normalized (rewritten) already.");
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom)
	 */
	
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
		
		if (debugFlag) {
			writer.print(ASP2CoreSymbols.SPACE);
			writer.write(ASP2CoreSymbols.IMPLICATION);
			writer.write(String.format(" activated(%d), ", nConstraints++));
		}
		writer.write(ASP2CoreSymbols.EOR);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom)
	 */
	
	public void visit(OWLFunctionalObjectPropertyAxiom arg0) {
		throw new IllegalStateException("OWLFunctionalObjectPropertyAxiom should have been normalized (rewritten) already.");
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom)
	 */
	
	public void visit(OWLSubObjectPropertyOfAxiom arg0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLDisjointUnionAxiom)
	 */
	
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
	
	public void visit(OWLSymmetricObjectPropertyAxiom symmetricProperty) {
		writer.write("icons " + ASP2CoreSymbols.IMPLICATION);

		OWLObjectPropertyExpression property = symmetricProperty.getProperty();

		String propertyName = mapper.getPredicateName(property.getNamedProperty());
		String cVar = var.currentVar();
		String nVar = var.nextVariable();

		if (debugFlag) {
			writer.write(String.format(" activated(%d), ", nConstraints++));
		}
		writer.write(propertyName);
		writer.write(ASP2CoreSymbols.BRACKET_OPEN);
		writer.write(cVar);
		writer.write(ASP2CoreSymbols.ARG_SEPERATOR);
		writer.write(nVar);
		writer.write(ASP2CoreSymbols.BRACKET_CLOSE);
		writer.write(ASP2CoreSymbols.CONJUNCTION);
		writer.write(ASP2CoreSymbols.NAF + "_");
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
	
	public void visit(OWLDataPropertyRangeAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom)
	 */
	
	public void visit(OWLFunctionalDataPropertyAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom)
	 */
	
	public void visit(OWLEquivalentDataPropertiesAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLClassAssertionAxiom)
	 */
	
	public void visit(OWLClassAssertionAxiom classAssertion) {
		OWLClassExpression classExpression = classAssertion.getClassExpression();
		OWLNamedIndividual individual = classAssertion.getIndividual().asOWLNamedIndividual();

		writer.print("icons");
		writer.print(ASP2CoreSymbols.IMPLICATION);
		if (debugFlag) {
			writer.write(String.format("activated(%d),", nConstraints++));
		}
		if (classExpression instanceof OWLObjectComplementOf) {
			OWLClass owlClass = classAssertion.getClassExpression().getComplementNNF().asOWLClass();
			writer.print(mapper.getPredicateName(owlClass));
			writer.print(ASP2CoreSymbols.BRACKET_OPEN);
			writer.print(mapper.getConstantName(individual));
			writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
		} else {
			OWLClass owlClass = classExpression.asOWLClass();
			OWLClass guard = getNominalGuard(individual);

			if (isOneOfAuxiliaryClass(owlClass)) {
				writer.print(mapper.getPredicateName(owlClass));
				writer.print(ASP2CoreSymbols.BRACKET_OPEN);
				writer.print(var.currentVar());
				writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
				writer.print(ASP2CoreSymbols.IMPLICATION);
				writer.print(mapper.getPredicateName(guard));
				writer.print(ASP2CoreSymbols.BRACKET_OPEN);
				writer.print(var.currentVar());
				writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
			} else {
				// A(a).
				writer.print("not_");
				writer.print(mapper.getPredicateName(owlClass));
				writer.print(ASP2CoreSymbols.BRACKET_OPEN);
				writer.print(mapper.getConstantName(individual));
				writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
			}
		}
		writer.write(ASP2CoreSymbols.EOR);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom)
	 */
	
	public void visit(OWLEquivalentClassesAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom)
	 */
	
	public void visit(OWLDataPropertyAssertionAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom)
	 */
	
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
	
	public void visit(OWLIrreflexiveObjectPropertyAxiom irrPropertyAxiom) {
		OWLObjectPropertyExpression property = irrPropertyAxiom.getProperty();
		String propertyName = mapper.getPredicateName(property.getNamedProperty());
		String cVar = var.currentVar();
		//String nVar = var.nextVariable();

		writer.write("icons " + ASP2CoreSymbols.IMPLICATION);
		if (debugFlag) {
			writer.write(String.format(" activated(%d), ", nConstraints++));
		}
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
	
	public void visit(OWLSubDataPropertyOfAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom)
	 */
	
	public void visit(OWLInverseFunctionalObjectPropertyAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLSameIndividualAxiom)
	 */
	
	public void visit(OWLSameIndividualAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom)
	 */
	
	public void visit(OWLSubPropertyChainOfAxiom arg0) {
		// TODO Auto-generated method stub
		String superPropertyName = null;
		OWLObjectPropertyExpression superPropertyExpression = arg0.getSuperProperty();

		int counter = 1;
		writer.print("icons :-");

		for (OWLObjectPropertyExpression subPropertyExpression : arg0.getPropertyChain()) {
			int firstCounter = counter;
			int secondCounter = ++counter;
			String subPropertyName = null;

			if (subPropertyExpression instanceof OWLObjectInverseOf) {
				int temp = firstCounter;
				firstCounter = secondCounter;
				secondCounter = temp;
				subPropertyName = mapper.getPredicateName(((OWLObjectInverseOf) subPropertyExpression).getInverse().asOWLObjectProperty());
			} else {
				subPropertyName = mapper.getPredicateName(subPropertyExpression.asOWLObjectProperty());
			}

			writer.print(String.format("%s(X%d,X%d),", subPropertyName, firstCounter, secondCounter));
		}
		if (superPropertyExpression instanceof OWLObjectInverseOf) {
			superPropertyName = mapper.getPredicateName(((OWLObjectInverseOf) superPropertyExpression).getInverse().asOWLObjectProperty());
			writer.print(String.format("not %s(X%d,X%d).", superPropertyName, counter, 1));
		} else {
			superPropertyName = mapper.getPredicateName(arg0.getSuperProperty().asOWLObjectProperty());
			writer.print(String.format("not %s(X%d,X%d).", superPropertyName, 1, counter));
		}
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom)
	 */
	
	public void visit(OWLInverseObjectPropertiesAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLHasKeyAxiom)
	 */
	
	public void visit(OWLHasKeyAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom)
	 */
	
	public void visit(OWLDatatypeDefinitionAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.SWRLRule)
	 */
	
	public void visit(SWRLRule arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	
	public void visit(OWLObjectProperty arg0) {
		// TODO Auto-generated method stub

	}

	
	public void visit(OWLObjectInverseOf arg0) {
		// TODO Auto-generated method stub

	}

	
	public void visit(OWLDataProperty arg0) {
		// TODO Auto-generated method stub

	}

	private void addNRA (OWLClass c, OWLObjectProperty p) {
		if (nraSet.containsKey(c)) {
			HashSet<OWLObjectProperty> set = nraSet.get(c);
			if (set.contains(p)) {
				return;
			} else {
				set.add(p);
			}
		}  else {
			HashSet<OWLObjectProperty> set = new HashSet<OWLObjectProperty> ();
			set.add(p);
			nraSet.put(c, set);
		}
	}

	private void addComplementNRA (OWLClass c, OWLObjectProperty p) {
		if (nraComplementSet.containsKey(c)) {
			HashSet<OWLObjectProperty> set = nraComplementSet.get(c);
			if (set.contains(p)) {
				return;
			} else {
				set.add(p);
			}
		}  else {
			HashSet<OWLObjectProperty> set = new HashSet<OWLObjectProperty> ();
			set.add(p);
			nraComplementSet.put(c, set);
		}
	}

	public void visit(OWLAnnotationProperty arg0) {
		// TODO Auto-generated method stub
		
	}
}