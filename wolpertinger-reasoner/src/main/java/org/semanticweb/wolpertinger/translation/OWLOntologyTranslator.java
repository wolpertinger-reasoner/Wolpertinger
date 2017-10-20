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
package org.semanticweb.wolpertinger.translation;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLPropertyExpressionVisitor;

/**
 * Interface for rewriting an ontology to a target formalism 
 * in which the reasoning is carried out.
 * 
 * I.e. rewrite into an answer set program.
 * 
 * @author Lukas Schweizer
 *
 */
public interface OWLOntologyTranslator extends OWLAxiomVisitor,
		OWLClassExpressionVisitor, OWLPropertyExpressionVisitor {

	/**
	 *  
	 * @param rootOntology
	 */
	public void translateOntology(OWLOntology rootOntology);
	
	/**
	 * 
	 * @param solution
	 * @return
	 */
	public Set<OWLIndividualAxiom> retranslateSolution(String solution);
	
}
