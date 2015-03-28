package org.semanticweb.wolpertinger.translation.asp;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.wolpertinger.Prefixes;

public class ASP2CoreSignatureMapper extends SignatureMapper {

	private boolean isAuxiliaryClass(OWLClass owlClass) {
		return Prefixes.isInternalIRI(owlClass.getIRI().toString());
	}
	
	@Override
	public String getPredicateName(OWLClass owlClass) {
		String predicateName;
		if (isAuxiliaryClass(owlClass)) {
			predicateName = "aux" + owlClass.getIRI().toString().substring(owlClass.getIRI().toString().lastIndexOf("#")+1, owlClass.getIRI().toString().length());
		}
		else {
			predicateName = owlClass.getIRI().getFragment().toLowerCase();
		}
		
		putPredicateMapping(predicateName, owlClass);
		return predicateName;
	}

	@Override
	public String getPredicateName(OWLObjectProperty owlObjectProperty) {
		String predicateName = owlObjectProperty.getIRI().getFragment().toLowerCase();
		putPredicateMapping(predicateName, owlObjectProperty);
		return predicateName;
	}

	@Override
	public String getConstantName(OWLNamedIndividual owlIndividual) {
		String individualName = owlIndividual.getIRI().getFragment().toLowerCase();
		putIndividualMapping(individualName, owlIndividual);
		return individualName;
	}

}
