package org.semanticweb.wolpertinger.clingo;

import java.io.File;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
//import org.apache.logging.log4j.Logger;
import org.apache.commons.configuration2.builder.fluent.Parameters;

/**
 * Factory class for ClingoSolver.
 *
 * @author Lukas Schweizer
 * Technische Universität Dresden, International Center for Computational Logic
 */
public final class SolverFactory {

	//private static Logger log = Logger.getLogger(SolverFactory.class);
	private static String PROPERTY_FILE = String.format("%s%s%s","etc", File.separator, "clingo.conf");

	private String[] clingoParameters;
	private String clingoExec;

	// Just a Test
	static final class OsUtils
	{
	   private static String OS = null;
	   public static String getOsName()
	   {
	      if(OS == null) { OS = System.getProperty("os.name").toLowerCase(); }
	      return OS;
	   }
	   public static boolean isWindows()
	   {
	      return getOsName().startsWith("windows");
	   }

	   public static boolean isUnix() {
		   return getOsName().indexOf("nux") >= 0 || getOsName().indexOf("mac") >= 0;
	   }
	}

	private SolverFactory() {
		Configuration configuration = null;

		try {
			String baseDir = System.getProperty("user.dir");
			FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
				    new FileBasedConfigurationBuilder<PropertiesConfiguration>(PropertiesConfiguration.class)
				    .configure(new Parameters().properties()
				        .setFileName(String.format("%s%s%s", baseDir, File.separator, PROPERTY_FILE)));
			configuration = builder.getConfiguration();
		} catch (Exception ce) {
			//log.error("There went something wrong while reading the clingo configuration. Using Default configruation.", ce);
		}

		if (configuration!=null) {
			clingoParameters = getClingoParameters(configuration);
			clingoExec = configuration.getString("clingo.exec", "clingo.exe");
		} else {
			//defaults
			if (OsUtils.isUnix())
				clingoExec = "clingo";
			else if (OsUtils.isWindows())
				clingoExec = "clingo.exe";

			clingoParameters = new String[] {"--warn=no-atom-undefined", "--quiet=0,2,2", "--verbose=0", "--project"};
		}
	}

	private String[] getClingoParameters(Configuration _configuration) {
		String warnValue = _configuration.getString("clingo.clasp.warn","no-atom-undefined");
		String warnOption = String.format("--warn=%s", warnValue);

		String quietValue = _configuration.getString("clingo.clasp.quiet", "0,2,2");
		String quietOption = String.format("--quiet=%s", quietValue);

		Integer verboseValue = _configuration.getInteger("clingo.clasp.verbose", 0);
		String verboseOption = String.format("--verbose=%s", verboseValue);

		return new String[] {warnOption, quietOption, verboseOption};
	}

	public final static SolverFactory INSTANCE = new SolverFactory();

	/**
	 *
	 * @param _name
	 * @return
	 */
	public ClingoSolver createClingoSolver(){
		return new ClingoSolver(clingoExec, clingoParameters);
	}

	public ClingoSolver createClingoBraveSolver() {
		String[] param = new String[4];
		for (int ii = 0; ii < clingoParameters.length; ii++) {
			param[ii] = clingoParameters[ii];
		}
		param[3] = "--enum-mode=brave";
		return new ClingoSolver(clingoExec, param);
	}
	
	public ClingoSolver createClingoCautiousSolver() {
		String[] param = new String[4];
		for (int ii = 0; ii < clingoParameters.length; ii++) {
			param[ii] = clingoParameters[ii];
		}
		param[3] = "--enum-mode=cautious";
		return new ClingoSolver(clingoExec, param);
	}
}
