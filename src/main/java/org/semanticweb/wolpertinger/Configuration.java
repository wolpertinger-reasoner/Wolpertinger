/**
 * 
 */
package org.semanticweb.wolpertinger;

import java.io.Serializable;

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

}
