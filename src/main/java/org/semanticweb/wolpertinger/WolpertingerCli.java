/* 
 */
package org.semanticweb.wolpertinger;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.io.File;
import java.io.PrintWriter;
import java.net.URI;
import java.text.BreakIterator;
import java.util.Collection;
import java.util.LinkedList;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.wolpertinger.translation.asp.NaiveTranslation;

/**
 * Command Line Interface for Wolpertinger.
 * 
 * @author Lukas Schweizer
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

    protected interface Action {
        void run(Wolpertinger wolpert, StatusOutput status, PrintWriter output, boolean ignoreOntologyPrefixes);
    }
    
    // Display normalized ontology only.
    static protected class OnlyNormalizeAction implements Action {

		@Override
		public void run(Wolpertinger wolpert, StatusOutput status, PrintWriter output, boolean ignoreOntologyPrefixes) {
			wolpert.outputNormalizedOntology(output);
			output.flush();
		}
    	
    }
    
    static protected class NaiveTranslationAction implements Action {

		@Override
		public void run(Wolpertinger wolpert, StatusOutput status, PrintWriter output, boolean ignoreOntologyPrefixes) {
			wolpert.translateOntologyToASP(null, output);
		}
    	
    }

	@SuppressWarnings("serial")
	protected static class UsageException extends IllegalArgumentException {
		public UsageException(String inMessage) {
			super(inMessage);
		}
	}

	protected static final String usageString = "";

	protected static final String groupActions = "Actions",
			groupMisc = "Miscellaneous";

	protected static final Option[] options = new Option[] {
			// misc options
			new Option('h', "help", groupMisc, "display this help and exit"),
			new Option('V', "version", groupMisc, "display Wolpertinger's built version and exit"),
			new Option('v', "verbose", groupMisc, true, "AMOUNT", "increase verbosity by AMOUNT levels (default 1)"),
			// actions
			new Option('N', "normalize", groupActions, "normalize the input ontology (structural transformation), optionally writing it back to file (via --output)"),
			new Option('T', "translate", groupActions, true, "TARGET", "translate the ontology to TARGET language, optionally writing it back to file (via --output)")
	};

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Getopt getopt = new Getopt("", args, Option.formatOptionsString(options), Option.createLongOpts(options));

			URI base;
			try {
				base = new URI("file", System.getProperty("user.dir") + "/", null);
			} catch (java.net.URISyntaxException e) {
				throw new RuntimeException("unable to create default IRI base");
			}
			
			Collection<IRI> ontologies = new LinkedList<IRI>();
			Collection<Action> actions = new LinkedList<Action>();
			int option;
			int verbosity=1;
			while ((option = getopt.getopt()) != -1) {
				switch (option) {
				// misc
				case 'h':
					System.out.println(usageString);
					System.out.println(Option.formatOptionHelp(options));
					System.exit(0);
					break;
					
				case 'v': 
				{
                    String arg=getopt.getOptarg();
                    if (arg==null) {
                        verbosity+=1;
                    }
                    else
                        try {
                            verbosity+=Integer.parseInt(arg,10);
                        }
                        catch (NumberFormatException e) {
                            throw new UsageException("argument to --verbose must be a number");
                        }
				}
                break;
			
				
				// actions
				case 'N': {				
					actions.add(new OnlyNormalizeAction());
				}
				break;
				
				case 'T': {
					String arg = getopt.getOptarg();
					Action action;
					if (arg == null) {
						action = new NaiveTranslationAction();
					}
					else {
						if (arg.toLowerCase().equals("naive")) {
							action = new NaiveTranslationAction();
						}
						else
							throw new UsageException("Unknown value for TARGET argument");
					}
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
			for (int i = getopt.getOptind(); i < args.length; ++i) {
				try {
					ontologies.add(IRI.create(base.resolve(args[i])));
				} catch (IllegalArgumentException e) {
					throw new UsageException(args[i]
							+ " is not a valid ontology name");
				}
			}
			StatusOutput status = new StatusOutput(verbosity);
			for (IRI ont : ontologies) {
				status.log(2,"Processing "+ont.toString());
                status.log(2,String.valueOf(actions.size())+" actions");
                try {
                    long startTime=System.currentTimeMillis();
                    OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
                    if (ont.isAbsolute()) {
                    	
                        URI uri=URI.create(ont.getStart());
                        String scheme = uri.getScheme();
                        if (scheme!=null && scheme.equalsIgnoreCase("file")) {
                            File file=new File(URI.create(ont.getStart()));
                            if (file.isDirectory()) {
                                OWLOntologyIRIMapper mapper=new AutoIRIMapper(file, false);
                                ontologyManager.addIRIMapper(mapper);
                            }
                        }
                    }
                    OWLOntology ontology=ontologyManager.loadOntology(ont);
                    long parseTime=System.currentTimeMillis()-startTime;
                    status.log(2,"Ontology parsed in "+String.valueOf(parseTime)+" msec.");
                    startTime=System.currentTimeMillis();
                    Wolpertinger hermit = new Wolpertinger(new Configuration(),ontology);
                   // Prefixes prefixes=hermit.getPrefixes();
//                    if (defaultPrefix!=null) {
//                        try {
//                            prefixes.declareDefaultPrefix(defaultPrefix);
//                        }
//                        catch (IllegalArgumentException e) {
//                            status.log(2,"Default prefix "+defaultPrefix+" could not be registered because there is already a registered default prefix. ");
//                        }
//                    }
//                    for (String prefixName : prefixMappings.keySet()) {
//                        try {
//                            prefixes.declarePrefix(prefixName, prefixMappings.get(prefixName));
//                        }
//                        catch (IllegalArgumentException e) {
//                            status.log(2,"Prefixname "+prefixName+" could not be set to "+prefixMappings.get(prefixName)+" because there is already a registered prefix name for the IRI. ");
//                        }
//                    }
                    long loadTime=System.currentTimeMillis()-startTime;
                    status.log(2,"Reasoner created in "+String.valueOf(loadTime)+" msec.");
                    for (Action action : actions) {
                        status.log(2,"Doing action...");
                        startTime=System.currentTimeMillis();
                        action.run(hermit,status,new PrintWriter(System.out),true);
                        long actionTime=System.currentTimeMillis()-startTime;
                        status.log(2,"...action completed in "+String.valueOf(actionTime)+" msec.");
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
			System.err.println("try 'wolperting --help' for more information.");
		}
	}

}

enum Arg {
	NONE, OPTIONAL, REQUIRED
}

/**
 * Represents a single CL Option.
 * 
 * @author Lukas Schweizer
 * @Credits to
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
