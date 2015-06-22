/**
 * 
 */
package org.semanticweb.wolpertinger.translation;

import java.io.PrintWriter;

import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLPropertyExpressionVisitor;
import org.semanticweb.wolpertinger.Configuration;
import org.semanticweb.wolpertinger.structural.OWLAxioms;

/**
 * @author Lukas Schweizer
 *
 */
public interface OWLOntologyTranslator extends OWLAxiomVisitor,
		OWLClassExpressionVisitor, OWLPropertyExpressionVisitor {

	// 
	public void translateOntology(OWLAxioms ontology, Configuration configuration, PrintWriter writer);
	
}
