package org.semanticweb.wolpertinger;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.IllegalConfigurationException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

/**
 * 
 * @author Lukas Schweizer
 *
 */
public class WolpertingerReasonerFactory implements OWLReasonerFactory {

	private static WolpertingerReasonerFactory INSTANCE = new WolpertingerReasonerFactory();
	
	private WolpertingerReasonerFactory() {}
	
	public static OWLReasonerFactory getInstance() {
		return INSTANCE;
	}
	
	public OWLReasoner createNonBufferingReasoner(OWLOntology arg0) {
		return createReasoner(arg0);
	}

	public OWLReasoner createNonBufferingReasoner(OWLOntology arg0,
			OWLReasonerConfiguration arg1) throws IllegalConfigurationException {
		return createReasoner(arg0);
	}

	public OWLReasoner createReasoner(OWLOntology arg0) {
		return new Wolpertinger(arg0);
	}

	public OWLReasoner createReasoner(OWLOntology arg0,
			OWLReasonerConfiguration arg1) throws IllegalConfigurationException {
		return createReasoner(arg0);
	}

	public String getReasonerName() {
		// TODO Incorporate VersionNumber?
		return "Wolpertinger Fixed-Domain Reasoner";
	}

}
