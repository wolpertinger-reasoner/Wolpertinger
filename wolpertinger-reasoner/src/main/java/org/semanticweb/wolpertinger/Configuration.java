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
package org.semanticweb.wolpertinger;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
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
	private Set<OWLNamedIndividual> domainIndividuals;

	public Configuration() {
		this.conceptsToProjectOn = new HashSet<IRI>();
		this.domainIndividuals = null;
	}

	public Configuration(Set<IRI> conceptsToProjectOn) {
		this.conceptsToProjectOn = conceptsToProjectOn;
		this.domainIndividuals = null;
	}

	public Configuration(Set<IRI> conceptsToProjectOn, Set<OWLNamedIndividual> domainIndividuals) {
		this.conceptsToProjectOn = conceptsToProjectOn;
		this.domainIndividuals = domainIndividuals;
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

	public Set<OWLNamedIndividual> getDomainIndividuals() {
		return domainIndividuals;
	}

	public void setDomainIndividuals(Set<OWLNamedIndividual> domainIndividuals) {
		this.domainIndividuals = domainIndividuals;
	}

}
