/**
 * 
 */
package org.semanticweb.wolpertinger.translation;

import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLPropertyExpressionVisitor;

/**
 * @author Lukas Schweizer
 *
 */
public interface OWLOntologyTranslator extends OWLAxiomVisitor,
		OWLClassExpressionVisitor, OWLPropertyExpressionVisitor {

	// 
	public void translateOntology(OWLOntology rootOntology);
	
}
