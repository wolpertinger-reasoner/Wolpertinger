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

	private String filter;
	private Set<IRI> conceptsToProjectOn;
	private Set<OWLNamedIndividual> domainIndividuals;
	private String aboxDirectory = ""; // the dirc to write the models to

	public Configuration() {
		this.conceptsToProjectOn = new HashSet<IRI>();
		this.domainIndividuals = null;
		this.filter = "";
	}

	public Configuration(Set<IRI> conceptsToProjectOn) {
		this.conceptsToProjectOn = conceptsToProjectOn;
		this.domainIndividuals = null;
		this.filter = "";
	}

	public Configuration(Set<IRI> conceptsToProjectOn, Set<OWLNamedIndividual> domainIndividuals) {
		this.conceptsToProjectOn = conceptsToProjectOn;
		this.domainIndividuals = domainIndividuals;
		this.filter = "";
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration#getFreshEntityPolicy()
	 */
	public FreshEntityPolicy getFreshEntityPolicy() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration#getIndividualNodeSetPolicy()
	 */
	public IndividualNodeSetPolicy getIndividualNodeSetPolicy() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration#getProgressMonitor()
	 */
	public ReasonerProgressMonitor getProgressMonitor() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration#getTimeOut()
	 */
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

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	public String getAboxDirectory() {
		return aboxDirectory;
	}
	
	public void setAboxDirectory(String directory) {
		this.aboxDirectory = directory;
	}

}
