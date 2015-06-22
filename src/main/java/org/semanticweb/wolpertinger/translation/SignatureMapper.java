/**
 * 
 */
package org.semanticweb.wolpertinger.translation;

import java.util.HashMap;
import java.util.Map;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.wolpertinger.translation.naive.ASP2CoreSignatureMapper;

/**
 * 
 * @author Lukas Schweizer
 *
 */
public abstract class SignatureMapper {
	
	private Map<String, OWLClass> classNameMappings;
	private Map<String, OWLObjectProperty> propertyNameMappings;
	private Map<String, OWLNamedIndividual> individualNameMappings;
	
	protected SignatureMapper() {
		classNameMappings = new HashMap<String, OWLClass>();
		propertyNameMappings = new HashMap<String, OWLObjectProperty>();
		individualNameMappings = new HashMap<String, OWLNamedIndividual>();
	}
	
	public static SignatureMapper ASP2CoreMapping = new ASP2CoreSignatureMapper();
	
	protected String putPredicateMapping(String predicateName, OWLClass owlClass) {
		if (!classNameMappings.containsKey(predicateName))  
			classNameMappings.put(predicateName, owlClass);
		return predicateName;
	}
	
	protected String putPredicateMapping(String predicateName, OWLObjectProperty owlObjectProperty) {
		if (!propertyNameMappings.containsKey(predicateName))
			propertyNameMappings.put(predicateName, owlObjectProperty);
		return predicateName;
	}
	
	protected String putIndividualMapping(String constantName, OWLNamedIndividual owlindividual) {
		if (!individualNameMappings.containsKey(constantName))
			individualNameMappings.put(constantName, owlindividual);
		return constantName;
	}
	
	public abstract String getPredicateName(OWLClass owlClass);
	public abstract String getPredicateName(OWLObjectProperty owlObjectProperty);
	public abstract String getConstantName(OWLNamedIndividual owlIndividual);
	
	/**
	 * @param predicateName
	 * @return Returns the {@link OWLClass} the predicateName was mapped to.
	 */
	public OWLClass getOWLClass(String predicateName) {
		return classNameMappings.get(predicateName);
	}
	
	public OWLObjectProperty getOWLObjectProperty(String propertyName) {
		return propertyNameMappings.get(propertyName);
	}
	
	public OWLNamedIndividual getOWLIndividual(String individualName) {
		return individualNameMappings.get(individualName);
	}

}
