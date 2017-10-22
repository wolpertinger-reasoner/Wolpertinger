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
package org.semanticweb.wolpertinger.cli;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.text.BreakIterator;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.wolpertinger.Configuration;
import org.semanticweb.wolpertinger.Wolpertinger;

/**
 * Command Line Interface for Wolpertinger.
 *
 * @author Lukas Schweizer
 * @author Satyadharma Tirtarasa
 */
public class WolpertingerCli {

    protected static class StatusOutput {
        protected int level;
        public StatusOutput(int inLevel) {
            level=inLevel;
        }
        static public final int ALWAYS=0;
        static public final int STATUS=1;
        static public final int DETAIL=2;
        static public final int DEBUG=3;
        public void log(int inLevel,String message) {
            if (inLevel<=level)
                System.err.println(message);
        }
    }

    protected static final String versionString;
    static {
        String version=WolpertingerCli.class.getPackage().getImplementationVersion();
        if (version == null) {
        	version = "<no version set>";
        } else {
        	version = version + ".0.0";
        }
        versionString=version;
    }

    protected interface TranslationAction {
    	void run(Wolpertinger wolpertinger, Configuration configuration, StatusOutput status, PrintWriter output);
    }

    static protected class DebugTranslationAction implements TranslationAction {
    	boolean debugFlag;

    	public DebugTranslationAction(boolean debugFlag) {
    		super();
    		this.debugFlag = debugFlag;
    	}

		public void run(Wolpertinger wolpertinger, Configuration configuration, StatusOutput status, PrintWriter output) {
			wolpertinger.naffTranslate(new PrintWriter(System.out), debugFlag);
		}
    }

    static protected class NaiveTranslationAction implements TranslationAction {
		public void run(Wolpertinger wolpertinger, Configuration configuration, StatusOutput status, PrintWriter output) {
			wolpertinger.naiveTranslate(new PrintWriter(System.out));
		}
    }

    static protected class DirectTranslationAction implements TranslationAction {
		public void run(Wolpertinger wolpertinger, Configuration configuration, StatusOutput status, PrintWriter output) {
			//DirectTranslation translation = new DirectTranslation(configuration, output);
		}
    }

    static protected class EntailmentCheckingAction implements TranslationAction {
    	private OWLOntology owlOntology;

    	public EntailmentCheckingAction(OWLOntology owlOntology) {
    		super();
    		this.owlOntology = owlOntology;
    	}

		public void run(Wolpertinger wolpertinger, Configuration configuration, StatusOutput status, PrintWriter output) {
			Set<OWLAxiom> axioms = owlOntology.getAxioms();
			System.out.println("Is entailed? : " + wolpertinger.isEntailed(axioms));
		}
    }

    static protected class JustificationAction implements TranslationAction {
    	public JustificationAction() {
    		super();
    	}

		public void run(Wolpertinger wolpertinger, Configuration configuration, StatusOutput status, PrintWriter output) {
			wolpertinger.naffTranslate(new PrintWriter(System.out), true);
		}
    }

    static protected class ConsistencyAction implements TranslationAction {
    	public ConsistencyAction() {
    		super();
    	}

		public void run(Wolpertinger wolpertinger, Configuration configuration, StatusOutput status, PrintWriter output) {
			if (wolpertinger.isConsistent()) {
				output.println("Input ontologies are consistent");
			} else {
				output.println("Input ontologies are inconsistent");
			}
			output.flush();
		}
    }

    static protected class ModelEnumerationAction implements TranslationAction {
    	int number = 0;

    	public ModelEnumerationAction(int number) {
    		super();
    		this.number = number;
    	}

		public void run(Wolpertinger wolpertinger, Configuration configuration, StatusOutput status, PrintWriter output) {
			Collection<String> models = wolpertinger.enumerateModels(number);
			
			if (!configuration.getAboxDirectory().isEmpty()) {
				//String modelFilePattern = "model%d.owl";
				String targetDir = configuration.getAboxDirectory();
				
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

				Collection<Set<OWLAxiom>> aboxes = wolpertinger.enumerateModelsAsOWLAxioms(this.number);
				
				int n = 0;
				for (Set<OWLAxiom> abox : aboxes) {
					if (targetDir.endsWith(File.separator))
						targetDir = targetDir.substring(0, targetDir.length()-1);
					
					IRI fileIri = IRI.create(new File(String.format("%s%s%s%d.%s", targetDir, File.separator, "model", n, "owl")).toURI());
				
					// write to file
					try {
						//OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(String.format(configuration.getAboxDirectory() + modelFilePattern, n))));
						OWLOntology owlABox = manager.createOntology(abox);
						manager.setOntologyDocumentIRI(owlABox, fileIri);
						manager.saveOntology(owlABox);
					} catch (OWLOntologyCreationException e) {
						System.err.println("Could not create ABox Ontology!");
					} catch (OWLOntologyStorageException e) {
						e.printStackTrace();
					} 
					n += 1;
				}
			}
			else {
				if (number == 0) {
					output.printf("Found " + models.size() + " models (requested ALL): \n");
				} else {
					output.printf("Found " + models.size() + " models (requested %d): \n", number);
				}
				for (String model : models) {
					output.println(model);
				}
				output.flush();
			}
		}
    }

    static protected class AxiomatizationAction implements TranslationAction {
    	File file;
    	public AxiomatizationAction(File axiomatizedOntology) {
    		super();
    		this.file = axiomatizedOntology;
    	}

		public void run(Wolpertinger wolpertinger, Configuration configuration, StatusOutput status, PrintWriter output) {
			wolpertinger.axiomatizeFDSemantics(file);
		}
	}

    static protected class SubconceptsActions implements TranslationAction {
    	final String conceptName;
    	final boolean direct;

    	public SubconceptsActions(String conceptName, boolean direct) {
    		super();
    		this.conceptName = conceptName;
    		this.direct = direct;
    	}

		public void run(Wolpertinger wolpertinger, Configuration configuration, StatusOutput status, PrintWriter output) {
			OWLClass owlClass = wolpertinger.getClass(conceptName);
			NodeSet<OWLClass> subconcepts = wolpertinger.getSubClasses(owlClass, direct);
			for (OWLClass cl : subconcepts.getFlattened()) {
				output.println(cl);
			}
			output.flush();
		}
	}

    static protected class SuperconceptsActions implements TranslationAction {
    	final String conceptName;
    	final boolean direct;

    	public SuperconceptsActions(String conceptName, boolean direct) {
    		super();
    		this.conceptName = conceptName;
    		this.direct = direct;
    	}

		public void run(Wolpertinger wolpertinger, Configuration configuration, StatusOutput status, PrintWriter output) {
            OWLClass owlClass = wolpertinger.getClass(conceptName);
			NodeSet<OWLClass> subconcepts = wolpertinger.getSuperClasses(owlClass, direct);
			for (OWLClass cl : subconcepts.getFlattened()) {
				output.println(cl);
			}
			output.flush();
		}
	}

    static protected class EquivalentConceptsActions implements TranslationAction {
    	final String conceptName;
    	final boolean direct;

    	public EquivalentConceptsActions(String conceptName, boolean direct) {
    		super();
    		this.conceptName = conceptName;
    		this.direct = direct;
    	}

		public void run(Wolpertinger wolpertinger, Configuration configuration, StatusOutput status, PrintWriter output) {
            OWLClass owlClass = wolpertinger.getClass(conceptName);
			Node<OWLClass> equivalentConcepts = wolpertinger.getEquivalentClasses(owlClass);
			for (OWLClass cl : equivalentConcepts.getEntities()) {
				output.println(cl);
			}					
			output.flush();
		}
	}
    
    static protected class ConceptInstancesAction implements TranslationAction {
    	final String conceptName;
    	final boolean direct;

    	public ConceptInstancesAction(String conceptName, boolean direct) {
    		super();
    		this.conceptName = conceptName;
    		this.direct = direct;
    	}

		public void run(Wolpertinger wolpertinger, Configuration configuration, StatusOutput status, PrintWriter output) {
			OWLClass owlClass = wolpertinger.getClass(conceptName);
			NodeSet<OWLNamedIndividual> instances = wolpertinger.getInstances(owlClass, direct);
			for (OWLNamedIndividual ind : instances.getFlattened()) {
				output.println(ind);
			}
			output.flush();
		}
	}
    
    static protected class IndividualTypesAction implements TranslationAction {
    	final String individualName;
    	final boolean direct;

    	public IndividualTypesAction(String individualName, boolean direct) {
    		super();
    		this.individualName = individualName;
    		this.direct = direct;
    	}

		public void run(Wolpertinger wolpertinger, Configuration configuration, StatusOutput status, PrintWriter output) {
            OWLNamedIndividual owlIndividual=OWLManager.createOWLOntologyManager().getOWLDataFactory().getOWLNamedIndividual(IRI.create(individualName));
			NodeSet<OWLClass> types = wolpertinger.getTypes(owlIndividual, direct);
			for (OWLClass cl : types.getFlattened()) {
				output.println(cl);
			}
			output.flush();
		}
	}
    
	@SuppressWarnings("serial")
	protected static class UsageException extends IllegalArgumentException {
		public UsageException(String inMessage) {
			super(inMessage);
		}
	}

	protected static final String usageString = "java -jar wolpertinger.jar [OPTIONS]... IRI...";

	protected static final String 	groupActions = "Actions",
									groupMisc = "Miscellaneous",
									groupDebug = "Debugging",
									groupOptimize = "Optimization",
									groupUtility = "Utility Functions";

	protected static final Option[] options = new Option[] {
			// misc options
			new Option('h', "help", groupMisc, "display this help and exit"),
			new Option('V', "version", groupMisc, "display Wolpertinger's built version and exit"),

			// optimization options
			new Option('p', "project", groupOptimize,true, "IRI1,..,IRI2", "project on concept "),
			// debug options
			new Option('v', "verbose", groupDebug, true, "AMOUNT", "increase verbosity by AMOUNT levels (default 1)"),
			// actions
			//new Option('N', "normalize", groupActions, "normalize the input ontology (structural transformation), optionally writing it back to file (via --output)"),
			new Option('T', "translate", groupActions, true, "TARGET", "translate the ontology to TARGET language, optionally writing it back to file (via --output); supported values are 'naive' and 'naff'"),
			new Option('O', "output", groupActions, true, "FILE", "output non-debug informations to FILE"),
			new Option('e', "entail", groupActions, true, "FILE", "check whether ontology FILE is entailed by input ontology"),
			new Option('D', "domain", groupActions, true, "FILE", "get fixed domain from FILE. if this option is not provided, the domain is the implicit set of individuals in the input ontology"),
			new Option('d', "direct", groupActions, "apply direct sub/superclasses for next query"),
			new Option('f', "filter", groupActions, true, "MODE", "filter what predicates are to be shown in the model; supported values are 'positive' and 'negative'"),
			new Option('m', "model", groupActions, true, "NUMBER", "enumerate NUMBER many models; NUMBER=0 means asking for ALL models"),
			new Option('A', "abox", groupActions, true, "DIRECTORY", "write models as proper assertions in TTL syntax to DIRECTORY"),
			new Option('c', "consistent", groupActions, "ask whether input ontology(-ies) is consistent"),
			new Option('j', "justification", groupActions, "ask for an inconsistency justification"),
			new Option('s', "subs", groupActions, true, "CLASS", "output classes subsumed by CLASS"),
			new Option('S', "supers", groupActions, true, "CLASS", "output classes subsuming by CLASS"),
			new Option('E', "equi", groupActions, true, "CLASS", "output classes equivalent to CLASS"),
			new Option('i', "instances", groupActions, true, "CLASS", "output instances of the CLASS"),
			new Option('t', "types", groupActions, true, "INDIVIDUAL", "output types of the INDIVIDUAL"),
			new Option('a', "axiomatize", groupUtility, true, "FILE", "For the ontology given, generate axioms that axiomatize the fixed-domain semantics and write the axiomatized ontolgy to FILE."),
	};

	public static void main(String[] args) {
		try {
			Configuration configuration = new Configuration();
			Getopt getopt = new Getopt("", args, Option.formatOptionsString(options), Option.createLongOpts(options));
			
			// by default print to System.out
			PrintWriter output = new PrintWriter(System.out);
			boolean direct = false;

			URI base;
			try {
				base = new URI("file", System.getProperty("user.dir") + "/", null);
			} catch (java.net.URISyntaxException e) {
				throw new RuntimeException("unable to create default IRI base");
			}

			Collection<IRI> ontologies = new LinkedList<IRI>();
			Collection<TranslationAction> actions = new LinkedList<TranslationAction>();

			OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();

			int option;
			int verbosity=1;
			//int debug = 1;

			while ((option = getopt.getopt()) != -1) {
				switch (option) {
				// misc
				case 'h': {
					System.out.println(usageString);
					System.out.println();
					System.out.println("For example, to get the ASP program of naive translation of example.owl :");
					System.out.println("java -jar Wolpertinger.jar --translate=naive example.owl");
					System.out.println(Option.formatOptionHelp(options));
					System.exit(0);
				}
				break;

				case 'v': {
					String arg = getopt.getOptarg();
					if (arg == null) {
						verbosity += 1;
					} else
						try {
							verbosity += Integer.parseInt(arg, 10);
						} catch (NumberFormatException e) {
							throw new UsageException(
									"argument to --verbose must be a number");
						}
				}
				break;
				case 'V': {
					System.out.println("Wolpertinger Version : " + versionString);
				}
				break;
				case 'p': {
					String arg = getopt.getOptarg();
					HashSet<IRI> iris = new HashSet<IRI>();
					for (String sIRI : arg.split(",")) {
						iris.add(IRI.create(sIRI));
					}
					configuration.setConceptsToProjectOn(iris);
				}
				break;
				case 'f': {
					String arg = getopt.getOptarg();
					configuration.setFilter(arg);
				}
				break;
				case 'd': {
					direct = true;
				}
				break;
				// ACTIONS
				
				// ABox
				case 'A': {
					String arg = getopt.getOptarg();
					
					configuration.setAboxDirectory(arg);
				}
				break;

				// translate
				case 'T': {
					String arg = getopt.getOptarg();
					TranslationAction action= null;

					if (arg.toLowerCase().equals("naive")) {
						action = new NaiveTranslationAction();
					} else if (arg.toLowerCase().equals("direct")) {
						action = new DirectTranslationAction();
					} else if (arg.toLowerCase().equals("naff")) {
						action = new DebugTranslationAction(false);
					} else {
						throw new UsageException("Unknown value for TARGET argument");
					}
					actions.add(action);
				}
				break;

				//ontology entailment
				case 'e': {
					String arg = getopt.getOptarg();
					IRI domainIRI = null;
					try {
						domainIRI = IRI.create(base.resolve(arg));
						OWLOntologyManager domainOntologyManager = OWLManager.createOWLOntologyManager();
						OWLOntology checkedOntology = domainOntologyManager.loadOntology(domainIRI);
						TranslationAction action = null;
						action = new EntailmentCheckingAction(checkedOntology);
						actions.add(action);
					} catch (IllegalArgumentException e) {
						throw new UsageException(arg + " is not a valid ontology name");
					} catch (OWLOntologyCreationException e) {
						throw new UsageException("Failed to load ontology");
					}
				}
				break;

				//output
				case 'O': {
					String arg = getopt.getOptarg();

					if (arg == null) {
						throw new UsageException("Empty value for argument --output");
					}
					else {
						File fOut = new File(arg);

						try {
							output = new PrintWriter(new FileWriter(fOut, false));
						}
						catch (IOException e) {
							throw new UsageException("Cannot open file: " + fOut.getAbsolutePath());
						}
					}
				}
				break;

				//domain file
				case 'D': {
					String arg = getopt.getOptarg();
					IRI domainIRI = null;
					try {
						domainIRI = IRI.create(base.resolve(arg));
						OWLOntologyManager domainOntologyManager = OWLManager.createOWLOntologyManager();
						OWLOntology domainOntology = domainOntologyManager.loadOntology(domainIRI);
						Set<OWLNamedIndividual> domainIndividuals = domainOntology.getIndividualsInSignature(true);
						configuration.setDomainIndividuals(domainIndividuals);
					} catch (IllegalArgumentException e) {
						throw new UsageException(arg + " is not a valid ontology name");
					} catch (OWLOntologyCreationException e) {
						throw new UsageException("Failed to load ontology");
					}
				}
				break;

				//consistency
				case 'c': {
					TranslationAction action = new ConsistencyAction();
					actions.add(action);
				}
				break;

				//enumerate models
				case 'm': {
					String arg = getopt.getOptarg();
					int number = Integer.parseInt(arg);
					TranslationAction action = new ModelEnumerationAction(number);
					actions.add(action);
				}
				break;
				//justification
				case 'j': {
					TranslationAction action = new JustificationAction();
					actions.add(action);
				}
				break;
				case 'a': {
					String arg = getopt.getOptarg();
					File axiomatizedOntology;
					if (arg == null) {
						throw new UsageException("Empty value for argument --axiomatize");
					}
					else {
						axiomatizedOntology = new File(arg);
						TranslationAction action = new AxiomatizationAction(axiomatizedOntology);
						actions.add(action);
					}
				}
				break;
				case 's': {
					String arg = getopt.getOptarg();
					TranslationAction action = new SubconceptsActions(arg, direct);
					actions.add(action);
				}
				break;
				case 'S': {
					String arg = getopt.getOptarg();
					TranslationAction action = new SuperconceptsActions(arg, direct);
					actions.add(action);
				}
				break;
				case 'E': {
					String arg = getopt.getOptarg();
					TranslationAction action = new EquivalentConceptsActions(arg, direct);
					actions.add(action);
				}
				break;
				case 't': {
					String arg = getopt.getOptarg();
					TranslationAction action = new IndividualTypesAction(arg, direct);
					actions.add(action);
				}
				break;
				case 'i': {
					String arg = getopt.getOptarg();
					TranslationAction action = new ConceptInstancesAction(arg, direct);
					actions.add(action);
				}
				break;
				default:
					if (getopt.getOptopt() != 0) {
						throw new UsageException("invalid option -- " + (char) getopt.getOptopt());
					}
					throw new UsageException("invalid option");
				} // END switch options
			} // END while options loop
			if(args.length == 0) {
				System.out.println("No input ontologies given.");
				System.out.println("Usage : " + usageString);
				System.out.println("Try -h or --help for more information");
			}

			for (int i = getopt.getOptind(); i < args.length; ++i) {
				try {
					ontologies.add(IRI.create(base.resolve(args[i])));
				} catch (IllegalArgumentException e) {
					throw new UsageException(args[i] + " is not a valid ontology name");
				}
			}
			StatusOutput status = new StatusOutput(verbosity);
			for (IRI iriOntology : ontologies) {
				status.log(2,"Processing "+iriOntology.toString());
                status.log(2,String.valueOf(actions.size())+" actions");

                try {
                    long startTime=System.currentTimeMillis();

                    if (iriOntology.isAbsolute()) {
                        URI uri = URI.create(iriOntology.getNamespace());
                        String scheme = uri.getScheme();
                        if (scheme != null && scheme.equalsIgnoreCase("file")) {
                            File file = new File(URI.create(iriOntology.getNamespace()));
                            if (file.isDirectory()) {
                                OWLOntologyIRIMapper mapper = new AutoIRIMapper(file, false);
                                ontologyManager.addIRIMapper(mapper);
                            }
                        }
                    }

                    OWLOntology ontology=ontologyManager.loadOntology(iriOntology);

                    long parseTime = System.currentTimeMillis()-startTime;
                    status.log(2,"Ontology parsed in " + String.valueOf(parseTime) + " msec.");
                    startTime = System.currentTimeMillis();
                    Wolpertinger wolpertinger = new Wolpertinger(configuration, ontology);

                    long loadTime = System.currentTimeMillis() - startTime;
                    status.log(2, "Reasoner created in " + String.valueOf(loadTime) + " msec.");

                    for (TranslationAction action : actions) {
                        status.log(2, "Doing action...");
                        startTime = System.currentTimeMillis();
                        action.run(wolpertinger, configuration, status, output);
                        long actionTime = System.currentTimeMillis() - startTime;
                        status.log(2, "...action completed in " + String.valueOf(actionTime) + " msec.");
                    }
                } catch (OWLException e) {
                	System.err.println(e.getMessage());
                	e.printStackTrace(System.err);
                }
			}
		} // END try
		catch (UsageException e) {
			System.err.println(e.getMessage());
			System.err.println(usageString);
			System.err.println("try '--help' for more information.");
		}
	}

}

enum Arg {
	NONE, OPTIONAL, REQUIRED
}

/**
 * Represents a single CL Option.
 */
class Option {
	protected int optChar;
	protected String longStr;
	protected String group;
	protected Arg arg;
	protected String metavar;
	protected String help;

	public Option(int inChar, String inLong, String inGroup, String inHelp) {
		optChar = inChar;
		longStr = inLong;
		group = inGroup;
		arg = Arg.NONE;
		help = inHelp;
	}

	public Option(int inChar, String inLong, String inGroup,
			boolean argRequired, String inMetavar, String inHelp) {
		optChar = inChar;
		longStr = inLong;
		group = inGroup;
		arg = (argRequired ? Arg.REQUIRED : Arg.OPTIONAL);
		metavar = inMetavar;
		help = inHelp;
	}

	public static LongOpt[] createLongOpts(Option[] opts) {
		LongOpt[] out = new LongOpt[opts.length];
		for (int i = 0; i < opts.length; ++i) {
			out[i] = new LongOpt(
					opts[i].longStr,
					(opts[i].arg == Arg.NONE ? LongOpt.NO_ARGUMENT
							: opts[i].arg == Arg.OPTIONAL ? LongOpt.OPTIONAL_ARGUMENT
									: LongOpt.REQUIRED_ARGUMENT), null,
					opts[i].optChar);
		}
		return out;
	}

	public String getLongOptExampleStr() {
		if (longStr == null || longStr.equals(""))
			return "";
		return new String("--"
				+ longStr
				+ (arg == Arg.NONE ? "" : arg == Arg.OPTIONAL ? "[=" + metavar
						+ "]" : "=" + metavar));
	}

	public static String formatOptionHelp(Option[] opts) {
		StringBuffer out = new StringBuffer();
		int fieldWidth = 0;
		for (Option o : opts) {
			int curWidth = o.getLongOptExampleStr().length();
			if (curWidth > fieldWidth)
				fieldWidth = curWidth;
		}
		String curGroup = null;
		for (Option o : opts) {
			if (o.group != curGroup) {
				curGroup = o.group;
				out.append(System.getProperty("line.separator"));
				if (o.group != null) {
					out.append(curGroup + ":");
					out.append(System.getProperty("line.separator"));
				}
			}
			if (o.optChar < 256) {
				out.append("  -");
				out.appendCodePoint(o.optChar);
				if (o.longStr != null && o.longStr != "") {
					out.append(", ");
				} else {
					out.append("  ");
				}
			} else {
				out.append("      ");
			}
			int fieldLeft = fieldWidth + 1;
			if (o.longStr != null && o.longStr != "") {
				String s = o.getLongOptExampleStr();
				out.append(s);
				fieldLeft -= s.length();
			}
			for (; fieldLeft > 0; --fieldLeft)
				out.append(' ');
			out.append(breakLines(o.help, 80, 6 + fieldWidth + 1));
			out.append(System.getProperty("line.separator"));
		}
		return out.toString();
	}

	public static String formatOptionsString(Option[] opts) {
		StringBuffer out = new StringBuffer();
		for (Option o : opts) {
			if (o.optChar < 256) {
				out.appendCodePoint(o.optChar);
				switch (o.arg) {
				case REQUIRED:
					out.append(":");
					break;
				case OPTIONAL:
					out.append("::");
					break;
				case NONE:
					break;
				}
			}
		}
		return out.toString();
	}

	protected static String breakLines(String str, int lineWidth, int indent) {
		StringBuffer out = new StringBuffer();
		BreakIterator i = BreakIterator.getLineInstance();
		i.setText(str);
		int curPos = 0;
		int curLinePos = indent;
		int next = i.first();
		while (next != BreakIterator.DONE) {
			String curSpan = str.substring(curPos, next);
			if (curLinePos + curSpan.length() > lineWidth) {
				out.append(System.getProperty("line.separator"));
				for (int j = 0; j < indent; ++j)
					out.append(" ");
				curLinePos = indent;
			}
			out.append(curSpan);
			curLinePos += curSpan.length();
			curPos = next;
			next = i.next();
		}
		return out.toString();
	}
}
