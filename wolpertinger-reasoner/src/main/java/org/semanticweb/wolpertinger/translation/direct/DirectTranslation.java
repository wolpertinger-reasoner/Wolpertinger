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
package org.semanticweb.wolpertinger.translation.direct;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
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
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
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
import org.semanticweb.wolpertinger.structural.OWLAxioms;
import org.semanticweb.wolpertinger.structural.OWLNormalization;
import org.semanticweb.wolpertinger.translation.OWLOntologyTranslator;
import org.semanticweb.wolpertinger.translation.SignatureMapper;
import org.semanticweb.wolpertinger.translation.naive.ASP2CoreSignatureMapper;
import org.semanticweb.wolpertinger.translation.naive.ASP2CoreSymbols;

import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

/**
 * Experimental Translation of an OWL ontology into an answer set program.
 * 
 * @author Lukas Schweizer
 *
 */
public class DirectTranslation implements OWLOntologyTranslator {
	
	private Configuration configuration;
	private PrintWriter writer;
	
	private SignatureMapper mapper;
	
	public DirectTranslation(Configuration configuration, PrintWriter writer) {
		this.configuration = configuration;
		this.writer = writer;
		
		this.mapper = new ASP2CoreSignatureMapper();
	}
	
	/**
	 * Creates an instance with default configuration, writing to System.out 
	 */
	public DirectTranslation() {
		this(new Configuration(), new PrintWriter(System.out));
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

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom)
	 */
	@Override
	public void visit(OWLNegativeObjectPropertyAssertionAxiom arg0) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom)
	 */
	@Override
	public void visit(OWLObjectPropertyAssertionAxiom arg0) {
		// TODO Auto-generated method stub

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

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLClassAssertionAxiom)
	 */
	@Override
	public void visit(OWLClassAssertionAxiom arg0) {
		// TODO Auto-generated method stub

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

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLClass)
	 */
	@Override
	public void visit(OWLClass arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectIntersectionOf)
	 */
	@Override
	public void visit(OWLObjectIntersectionOf arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectUnionOf)
	 */
	@Override
	public void visit(OWLObjectUnionOf arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectComplementOf)
	 */
	@Override
	public void visit(OWLObjectComplementOf arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom)
	 */
	@Override
	public void visit(OWLObjectSomeValuesFrom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectAllValuesFrom)
	 */
	@Override
	public void visit(OWLObjectAllValuesFrom arg0) {
		// TODO Auto-generated method stub

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
	public void visit(OWLObjectMinCardinality arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectExactCardinality)
	 */
	@Override
	public void visit(OWLObjectExactCardinality arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectMaxCardinality)
	 */
	@Override
	public void visit(OWLObjectMaxCardinality arg0) {
		// TODO Auto-generated method stub

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
	 * @see org.semanticweb.owlapi.model.OWLPropertyExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectProperty)
	 */
	@Override
	public void visit(OWLObjectProperty arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLPropertyExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectInverseOf)
	 */
	@Override
	public void visit(OWLObjectInverseOf arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLPropertyExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLDataProperty)
	 */
	@Override
	public void visit(OWLDataProperty arg0) {
		// TODO Auto-generated method stub

	}
	
	// ----------------------
	// BEGIN MEMBER methods
	// ----------------------
	
	private HashMap<OWLClassExpression, OWLClass> auxiliaryMappings = new HashMap<OWLClassExpression, OWLClass>();
	private static final String INTERNAL_IRI_PREFIX = "http://www.semanticweb.org/wolpertinger/internal";
	
	private OWLClass getAuxClass(OWLClassExpression complexExpression) {
		if (auxiliaryMappings.containsKey(complexExpression))
			return auxiliaryMappings.get(complexExpression);
		OWLClass auxClass = new OWLClassImpl(IRI.create(INTERNAL_IRI_PREFIX + "#aux" + complexExpression.hashCode()));
		auxiliaryMappings.put(complexExpression, auxClass);
		return auxClass;
	}
	/**
	 * 
	 * @param inclusion
	 */
	private void translateInclusion(OWLClassExpression[] inclusion) {
		
		LinkedList<OWLClassExpression> bodyConcepts = new LinkedList<OWLClassExpression>();
		LinkedList<OWLClassExpression> headConcepts = new LinkedList<OWLClassExpression>();
		
		// we separate body and head literals first
		for (OWLClassExpression disjunct : inclusion) {
			if (disjunct instanceof  OWLObjectComplementOf) {
				bodyConcepts.add(((OWLObjectComplementOf) disjunct).getOperand());
			}
			else {
				headConcepts.add(disjunct);
			}
		}
		
		boolean isFirst=true;
		for (OWLClassExpression headConcept : headConcepts) {
			if (!isFirst) {
				writer.print(ASP2CoreSymbols.DISJUNCTION);
			}
			
			if (headConcept instanceof OWLClass) {
				String conceptName = mapper.getPredicateName(headConcept.asOWLClass());
				writer.print(conceptName);
				writer.print(ASP2CoreSymbols.BRACKET_OPEN);
				writer.print("X");
				writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
			}
			// in the direct encoding, we can be more generous on introducing auxiliary 
			// concepts (predicates)
			else if (headConcept instanceof OWLObjectSomeValuesFrom) {
				
			}
			else if (headConcept instanceof OWLObjectMaxCardinality) {
				
			}
			else if (headConcept instanceof OWLObjectMinCardinality) {
				
			}
			else if (headConcept instanceof OWLObjectAllValuesFrom) {
				if (headConcepts.size() == 1) {
					String roleName = mapper.getPredicateName(((OWLObjectAllValuesFrom)headConcept).getProperty().asOWLObjectProperty());
					OWLClassExpression filler = ((OWLObjectAllValuesFrom)headConcept).getFiller();
					
					assert filler instanceof OWLClass;
					String fillerName = mapper.getPredicateName(filler.asOWLClass());
					
					writer.print(roleName);
					writer.print(ASP2CoreSymbols.BRACKET_OPEN);
					writer.print("X");
					writer.print(ASP2CoreSymbols.ARG_SEPERATOR);
					writer.print("Y");
					writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
					writer.print(ASP2CoreSymbols.CONJUNCTION);
					writer.print(ASP2CoreSymbols.NAF + " ");
					writer.print(fillerName);
					writer.print(ASP2CoreSymbols.BRACKET_OPEN);
					writer.print("Y");
					writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
					
				}
				OWLClass auxClass = getAuxClass(headConcept);
				
				// we need to introduce the additional constraint
			}
			isFirst = false;
		}
		writer.print(ASP2CoreSymbols.IMPLICATION);
		
		isFirst = true;
		for (OWLClassExpression bodyConcept : bodyConcepts) {
			//here we should have only named concepts
			assert bodyConcept instanceof OWLClass;
			
			String conceptName = mapper.getPredicateName(bodyConcept.asOWLClass());
			
			if (!isFirst) {
				writer.print(ASP2CoreSymbols.CONJUNCTION);
			}
			
			writer.print(conceptName);
			writer.print(ASP2CoreSymbols.BRACKET_OPEN);
			writer.print("X");
			writer.print(ASP2CoreSymbols.BRACKET_CLOSE);
			
			isFirst = false;
		}
		writer.print(ASP2CoreSymbols.EOR);
	}
	
	private void translateOntology(OWLAxioms normalizedOntology) {
		
		for (OWLClassExpression[] inclusion : normalizedOntology.m_conceptInclusions) {
			translateInclusion(inclusion);
			writer.println();
		}
		
		writer.flush();
	}
	
	private OWLAxioms loadOntology(OWLOntology rootOntology) {
		OWLAxioms axioms = new OWLAxioms();
		
		Collection<OWLOntology> importClosure = rootOntology.getImportsClosure();
		OWLNormalization normalization = new OWLNormalization(rootOntology.getOWLOntologyManager().getOWLDataFactory(), axioms, 0);
		
		for (OWLOntology ontology : importClosure) {
			normalization.processOntology(ontology);
		}
		
		return axioms;
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.wolpertinger.translation.OWLOntologyTranslator#translateOntology(org.semanticweb.wolpertinger.structural.OWLAxioms, org.semanticweb.wolpertinger.Configuration, java.io.PrintWriter)
	 */
	@Override
	public void translateOntology(OWLOntology rootOntology) {
		translateOntology(loadOntology(rootOntology));
	}

}