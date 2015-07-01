package org.semanticweb.wolpertinger.translation.naive;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.wolpertinger.Prefixes;
import org.semanticweb.wolpertinger.translation.SignatureMapper;

/**
 * 
 * @author Lukas Schweizer
 *
 */
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
		
		return putPredicateMapping(predicateName, owlClass);
	}

	@Override
	public String getPredicateName(OWLObjectProperty owlObjectProperty) {
		String predicateName = owlObjectProperty.getIRI().getFragment().toLowerCase();
		return putPredicateMapping(predicateName, owlObjectProperty);
	}

	/**
	 * For given {@link OWLIndividual}, a string representation is created induced by the 
	 * individual's IRI. Example: http://www.semanticweb.org/wolpertinger/ontologies/Sudoko#n11
	 * becomes <i>i_n11</i>.
	 */
	@Override
	public String getConstantName(OWLNamedIndividual owlIndividual) {
		String individualName = "i_" + owlIndividual.getIRI().getFragment().toLowerCase();
		return putIndividualMapping(individualName, owlIndividual);
	}

}
