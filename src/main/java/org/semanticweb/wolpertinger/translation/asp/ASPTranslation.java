/**
 * 
 */
package org.semanticweb.wolpertinger.translation.asp;

import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.wolpertinger.structural.OWLAxioms;

/**
 * @author lschweizer
 *
 */
public interface ASPTranslation extends OWLAxiomVisitor,
		OWLClassExpressionVisitor {

	public void translateOntology(OWLAxioms ontology);
	
}
