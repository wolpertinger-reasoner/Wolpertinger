package org.semanticweb.wolpertinger.protege;

import org.protege.editor.owl.model.inference.AbstractProtegeOWLReasonerInfo;
import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

public class ProtegeReasonerFactory extends AbstractProtegeOWLReasonerInfo {

	@Override
	public OWLReasonerFactory getReasonerFactory() {
		return org.semanticweb.wolpertinger.WolpertingerReasonerFactory.getInstance();
	}

	@Override
	public BufferingMode getRecommendedBuffering() {
		return BufferingMode.NON_BUFFERING;
	}

}
