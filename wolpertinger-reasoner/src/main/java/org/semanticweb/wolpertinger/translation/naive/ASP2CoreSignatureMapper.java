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

	private boolean isOneOfAuxiliaryClass(OWLClass owlClass) {
		if (Prefixes.isInternalIRI(owlClass.getIRI().toString())) {
			String iriString = owlClass.getIRI().toString();
			boolean isOneOf = iriString.substring(iriString.lastIndexOf(":") + 1, iriString.lastIndexOf("#")).equals("nnq");
			return isOneOf;
		} else {
			return false;
		}
	}

	@Override
	public String getPredicateName(OWLClass owlClass) {
		String predicateName;
		if (isOneOfAuxiliaryClass(owlClass)) {
			predicateName = "oneofaux" + owlClass.getIRI().toString().substring(owlClass.getIRI().toString().lastIndexOf("#")+1, owlClass.getIRI().toString().length());
		} else if (isAuxiliaryClass(owlClass)) {
			predicateName = "aux" + owlClass.getIRI().toString().substring(owlClass.getIRI().toString().lastIndexOf("#")+1, owlClass.getIRI().toString().length());
		}
		else {
			predicateName = owlClass.getIRI().getFragment().toLowerCase().replace('-', '_').replace('.', '_');
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
