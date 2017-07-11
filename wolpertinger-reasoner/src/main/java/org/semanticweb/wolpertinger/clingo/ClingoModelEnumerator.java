/**
 *
 */
package org.semanticweb.wolpertinger.clingo;

import java.util.Collection;
//import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Given an {@link OWLOntology} this class can enumerate models of the ontology;
 * i.e. interpretations satisfying each axiom in the ontology.
 *
 * @author Lukas Schweizer</br>
 * Technische Universität Dresden, International Center for Computational Logic
 */
public class ClingoModelEnumerator {
	//private static Logger log = Logger.getLogger(OWLModelEnumerator.class);

	private String[] lpFilePath = null;

	public ClingoModelEnumerator(String[] _lpFilePath) {
		lpFilePath = _lpFilePath;
	}

	/**
	 * Ask for <b>all</b> models of the ontology.
	 * This call is equivalent to {@link OWLModelEnumerator#enumerateModels(0)}.
	 * @return A collection of all models of the ontology.
	 */
	public Collection<String> enumerateAllModels() {
		return enumerateModels(0);
	}

	/**
	 * Ask for a certain number of models of the ontology.
	 * </br></br>
	 * <b>Note:</b> there might not be as many models as requested. Therefore the _number argument is interpreted as a maximum.
	 *
	 * @param _number The number of models requested. For the value 0, all models are enumerated.
	 * @return A collection of {@link OWLOntology} objects, each representing an ABox determining a model. An empty collection is returned if no model
	 * exists; i.e. the logic program is unsatisfiable.
	 */
	public Collection<String> enumerateModels(int _number) {
		ClingoSolver clingo = SolverFactory.INSTANCE.createClingoSolver();

		try {
			Collection<String> answers = clingo.solve(lpFilePath, _number);
			return answers;
		} catch (SolvingException se) {
			//System.out.println("Error Solving : " + se.getMessage());
			//log.error("Something happend while enumerating models!", se);
			se.printStackTrace();
		}

		return null;
	}

}
