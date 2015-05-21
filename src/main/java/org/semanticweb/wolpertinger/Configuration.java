/**
 * 
 */
package org.semanticweb.wolpertinger;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.reasoner.FreshEntityPolicy;
import org.semanticweb.owlapi.reasoner.IndividualNodeSetPolicy;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.ReasonerProgressMonitor;

/**
 * @author Lukas Schweizers
 *
 */
public class Configuration implements OWLReasonerConfiguration, Cloneable,
		Serializable {

	private static final long serialVersionUID = -2516044809777955981L;
	
	private Set<IRI> conceptsToProjectOn;
	
	public Configuration() {
		this.conceptsToProjectOn = new HashSet<IRI>();
	}
	
	public Configuration(Set<IRI> conceptsToProjectOn) {
		this.conceptsToProjectOn = conceptsToProjectOn;
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration#getFreshEntityPolicy()
	 */
	@Override
	public FreshEntityPolicy getFreshEntityPolicy() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration#getIndividualNodeSetPolicy()
	 */
	@Override
	public IndividualNodeSetPolicy getIndividualNodeSetPolicy() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration#getProgressMonitor()
	 */
	@Override
	public ReasonerProgressMonitor getProgressMonitor() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration#getTimeOut()
	 */
	@Override
	public long getTimeOut() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * @return A set of IRIs corresponding to those classes/concepts which are of
	 * particular interest, and the solver therefore can be asked to dedicate solving with respect to those.
	 */
	public Set<IRI> getConceptNamesToProjectOn() {
		return conceptsToProjectOn;
	}

	public void setConceptsToProjectOn(Set<IRI> conceptsToProjectOn) {
		this.conceptsToProjectOn = conceptsToProjectOn;
	}
	
}
