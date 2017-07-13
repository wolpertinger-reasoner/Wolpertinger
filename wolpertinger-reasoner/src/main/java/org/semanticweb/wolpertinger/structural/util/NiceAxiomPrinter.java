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
package org.semanticweb.wolpertinger.structural.util;

import java.io.PrintWriter;

import org.semanticweb.owlapi.model.IRI;
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
import org.semanticweb.owlapi.model.OWLIndividual;
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
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
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
import org.semanticweb.wolpertinger.Prefixes;

/**
 * Print the axioms nicely.
 * 
 * @author Lukas Schweizer
 *
 */
public class NiceAxiomPrinter implements OWLAxiomVisitor, OWLClassExpressionVisitor {
	
	private PrintWriter writer;
	
	public NiceAxiomPrinter(PrintWriter writer) {
		this.writer = writer;
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAnnotationAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom)
	 */
	
	public void visit(OWLAnnotationAssertionAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAnnotationAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom)
	 */
	
	public void visit(OWLSubAnnotationPropertyOfAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAnnotationAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom)
	 */
	
	public void visit(OWLAnnotationPropertyDomainAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAnnotationAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom)
	 */
	
	public void visit(OWLAnnotationPropertyRangeAxiom arg0) {
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
	
	public void visit(OWLSubClassOfAxiom subClassAxiom) {
		OWLClassExpression subClass = subClassAxiom.getSubClass();
		OWLClassExpression supClass = subClassAxiom.getSuperClass();
		
		subClass.accept(this);
		supClass.accept(this);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom)
	 */
	
	public void visit(OWLNegativeObjectPropertyAssertionAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom)
	 */
	
	public void visit(OWLAsymmetricObjectPropertyAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom)
	 */
	
	public void visit(OWLReflexiveObjectPropertyAxiom arg0) {
		// TODO Auto-generated method stub

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

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom)
	 */
	
	public void visit(OWLDisjointObjectPropertiesAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom)
	 */
	
	public void visit(OWLObjectPropertyRangeAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom)
	 */
	
	public void visit(OWLObjectPropertyAssertionAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom)
	 */
	
	public void visit(OWLFunctionalObjectPropertyAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom)
	 */
	
	public void visit(OWLSubObjectPropertyOfAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLDisjointUnionAxiom)
	 */
	
	public void visit(OWLDisjointUnionAxiom arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom)
	 */
	
	public void visit(OWLSymmetricObjectPropertyAxiom arg0) {
		// TODO Auto-generated method stub

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

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLClassAssertionAxiom)
	 */
	
	public void visit(OWLClassAssertionAxiom classAssertion) {
		OWLIndividual individual = classAssertion.getIndividual();
		OWLClassExpression classExpression = classAssertion.getClassExpression();
		
		if (!classExpression.isAnonymous()) {
			OWLClass namedClass = classExpression.asOWLClass();
			
			writer.print(namedClass.getIRI().getFragment());
			writer.print("(");
			writer.print(IRI.create(individual.toStringID()).getFragment());
			writer.print(").\n");
		}
		else {
			
		}
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

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.model.OWLAxiomVisitor#visit(org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom)
	 */
	
	public void visit(OWLIrreflexiveObjectPropertyAxiom arg0) {
		// TODO Auto-generated method stub

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
	
	private boolean isAuxiliaryClass(OWLClass auxClass) {
		return Prefixes.isInternalIRI(auxClass.getIRI().toString());
	}

	
	public void visit(OWLClass arg0) {
		if (isAuxiliaryClass(arg0)) {
			//TODO invoke name mapper here
			writer.print("aux" + arg0.getIRI().toString().substring(arg0.getIRI().toString().lastIndexOf("#")+1, arg0.getIRI().toString().length()));
		}
		else {
			writer.print(arg0.getIRI().getFragment());
		}
	}

	
	public void visit(OWLObjectIntersectionOf arg0) {
		boolean isFirst=true;
		for (OWLClassExpression operand : arg0.getOperands()) {
			if (!isFirst)
				writer.write(" and ");
			operand.accept(this);
			isFirst = false;
		}
	}

	
	public void visit(OWLObjectUnionOf arg0) {
		boolean isFirst=true;
		for (OWLClassExpression operand : arg0.getOperands()) {
			if (!isFirst)
				writer.write(" or ");
			operand.accept(this);
			isFirst=false;
		}
	}

	
	public void visit(OWLObjectComplementOf arg0) {
		writer.print("-");
		arg0.getOperand().accept(this);
	}

	
	public void visit(OWLObjectSomeValuesFrom arg0) {
		writer.print("Exists ");
		writer.print(arg0.getProperty().getNamedProperty().getIRI().getFragment());
		writer.print(".");
		arg0.getFiller().accept(this);
	}

	
	public void visit(OWLObjectAllValuesFrom arg0) {
		writer.print("ForAll ");
		writer.print(arg0.getProperty().getNamedProperty().getIRI().getFragment());
		writer.print(".");
		arg0.getFiller().accept(this);
	}

	
	public void visit(OWLObjectHasValue arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void visit(OWLObjectMinCardinality arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void visit(OWLObjectExactCardinality arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void visit(OWLObjectMaxCardinality arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void visit(OWLObjectHasSelf arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void visit(OWLObjectOneOf arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void visit(OWLDataSomeValuesFrom arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void visit(OWLDataAllValuesFrom arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void visit(OWLDataHasValue arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void visit(OWLDataMinCardinality arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void visit(OWLDataExactCardinality arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void visit(OWLDataMaxCardinality arg0) {
		// TODO Auto-generated method stub
		
	}

}
