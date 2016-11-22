package org.semanticweb.wolpertinger.clingo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.lang3.ArrayUtils;
//import org.apache.log4j.Logger;

/**
 * This class provides simple access to the clingo solver.
 *
 * @author Lukas Schweizer
 * Technische Universität Dresden, International Center for Computational Logic
 */
public class ClingoSolver implements Solver {

	//private static Logger log = Logger.getLogger(ClingoSolver.class);

	private String[] clingoArguments;
	private String	 clingoExecPath;

	public ClingoSolver(String _clingoExecPath, String[] _arguments) {
		this.clingoArguments = _arguments;
		this.clingoExecPath  = _clingoExecPath;
	}

	/**
	 * Perform a "one-shot" solving.
	 *
	 * @param _program
	 * @param _answers
	 * @return
	 * @throws SolvingException
	 */
	public Collection<String> solve(String _program, int _answers) throws SolvingException {
		String[] command = new String[clingoArguments.length + 3];
		command[0] = clingoExecPath;
		for (int i=0,j=1; i<clingoArguments.length; i++,j++){
			command[j] = clingoArguments[i];
		}
		command[command.length-2] = _program;
		command[command.length-1] = String.format("%d", _answers);

		ProcessBuilder pb = new ProcessBuilder(command);
		pb.redirectErrorStream(false);

		//log.debug(String.format("Build Process Builder using the command:\n %s", ArrayUtils.toString(command)));
		try {
			Process clingo = pb.start();
			BufferedReader clingoReader = new BufferedReader(new InputStreamReader(clingo.getInputStream()));

			String answerset;
			LinkedList<String> answersets = new LinkedList<String>();
			while ((answerset = clingoReader.readLine()) != null) {

				if (!answerset.equalsIgnoreCase("SATISFIABLE") && !answerset.equalsIgnoreCase("UNSATISFIABLE")) {
					answersets.add(answerset);
				}
			}
			return answersets;
		}
		catch (IOException e) {
			throw new SolvingException("IO problems in piped communication with clingo.");
		}
	}

	protected void printConfigStdout() {
		System.out.println(Arrays.toString(clingoArguments));
		System.out.println(clingoExecPath);
	}

//	public static void main(String[] args) {
//		ClingoSolver solver = SolverFactory.INSTANCE.createClingoSolver();
//		//solver.printConfigStdout();
//
////		File program = new File("3col.lp");
//
//		try {
//			Collection<String> colorings = solver.solve("3col.lp", 0);
//
//			for (String coloring : colorings) {
//				System.out.println(coloring);
//			}
//		} catch (SolvingException se) {
//
//		}
//	}

}
