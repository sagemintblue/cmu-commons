package edu.cmu.commons.util.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A basic base class for cmdline utility applications.
 * @author hazen
 */
public abstract class Application {
	private static final Logger log = LoggerFactory.getLogger(Application.class);

	/**
	 * Name of the system property used to specify the Application class to load
	 * and run within {@link #main(String[])}.
	 */
	public static final String APPLICATION_CLASSNAME_PROPERTY = "application";

	/**
	 * Copy of command line arguments passed to {@link #main(String[])}.
	 */
	private String[] arguments;

	/**
	 * @return command arguments.
	 */
	public String[] getArguments() {
		return arguments;
	}

	/**
	 * @param arguments command arguments.
	 */
	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}

	/**
	 * Extension point for derived Application classes.
	 * @return a status value to pass to {@link System#exit(int)}. Zero signals
	 * success.
	 * @throws UsageException on any error deemed by the application to be a usage
	 * (user) error.
	 * @throws Exception on any other error.
	 */
	public abstract int run() throws UsageException, Exception;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int status = 0;
		try {
			String appClassName = System.getProperty(APPLICATION_CLASSNAME_PROPERTY);
			if (appClassName == null) throw new UsageException(
					"Missing required system property '" + APPLICATION_CLASSNAME_PROPERTY
							+ "'");
			Class<?> appClass = Class.forName(appClassName);
			Application app = (Application) appClass.newInstance();
			app.setArguments(args);
			status = app.run();
		} catch (UsageException e) {
			log.error(e.getMessage());
			status = 1;
		} catch (Exception e) {
			log.error("Fatal exception encountered", e);
			status = 1;
		}
		System.exit(status);
	}
}
