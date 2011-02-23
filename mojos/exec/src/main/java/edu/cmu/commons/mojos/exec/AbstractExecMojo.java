package edu.cmu.commons.mojos.exec;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.Os;

/**
 * Goal which executes a command as a background process via a shell.
 * @author Andy Schlaikjer <hazen@cs.cmu.edu>
 * @requiresDependencyResolution test
 */
public abstract class AbstractExecMojo extends AbstractMojo {
	/**
	 * The enclosing project.
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	protected MavenProject project;

	/**
	 * One of "bash", "cmd", or "auto". When set to "auto", the shell used to
	 * invoke the command will be determined by the OS family.
	 * @parameter default-value="auto"
	 */
	protected String shell;

	/**
	 * Name of task. Used for default script, log and pid file names.
	 * @parameter
	 * @required
	 */
	protected String name;

	/**
	 * Command arguments. If any of the arguments match the value of the
	 * classpathArgumentName parameter, then they are replaced by the classpath
	 * whose scope is specified by the classpathScope parameter.
	 * @parameter
	 */
	protected List<Object> args;

	/**
	 * @parameter default-value="@CLASSPATH@"
	 */
	protected String classpathArgumentName;

	/**
	 * The directory in which to execute shell.
	 * @parameter default-value="${basedir}"
	 * @required
	 */
	protected File workingDir;

	/**
	 * The file in which to create the start script invoked by the shell. Defaults
	 * to ${project.build.directory}/bin/${name}. On Windows the default script
	 * name will also include a ".bat" suffix.
	 * @parameter
	 */
	protected File scriptFile;

	/**
	 * File to which all standard and error output of process will be directed.
	 * @parameter
	 */
	protected File logFile;

	/**
	 * File where process id will be written.
	 * @parameter
	 */
	protected File pidFile;

	/**
	 * Dependency scope to use when constructing classpath.
	 * @parameter default-value="runtime"
	 * @required
	 */
	protected String classpathScope;

	/**
	 * If true, the start script is created but not executed.
	 * @parameter default-value="false"
	 */
	protected Boolean createScriptOnly;

	/**
	 * If true, the start script is forked and runs independently of Maven.
	 * Otherwise, Maven will wait for the command to terminate before continuing.
	 * @parameter default-value="false"
	 */
	protected Boolean fork;

	/**
	 * @return path to command to exec.
	 */
	protected abstract File getCommand();

	private String classpath = null;

	protected final String getClasspath() throws MojoExecutionException {
		if (classpath != null) return classpath;
		getLog().debug("Calculating classpath with scope '" + classpathScope + "'");
		StringBuilder sb = new StringBuilder();
		String pathSeparator = System.getProperty("path.separator");
		List<?> classpathElements = null;
		try {
			if (classpathScope.equals("runtime")) {
				classpathElements = project.getRuntimeClasspathElements();
			} else if (classpathScope.equals("compile")) {
				classpathElements = project.getCompileClasspathElements();
			} else if (classpathScope.equals("test")) {
				classpathElements = project.getTestClasspathElements();
			} else if (classpathScope.equals("system")) {
				classpathElements = project.getSystemClasspathElements();
			} else {
				throw new Exception("Unrecognized scope '" + classpathScope + "'");
			}
		} catch (Exception e) {
			throw new MojoExecutionException("Failed to generate classpath", e);
		}
		for (Object element : classpathElements) {
			String path = element.toString();
			getLog().debug("Adding path '" + path + "'");
			if (sb.length() > 0) sb.append(pathSeparator);
			sb.append(path);
		}
		classpath = sb.toString();
		return classpath;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.maven.plugin.AbstractMojo#execute()
	 */
	public void execute() throws MojoExecutionException {
		initializeParameters();
		createRuntimePaths();
		ShellExecutor executor = createShellExecutor();
		createStartScript(executor);
		if (createScriptOnly) return;
		executeStartScript(executor);
	}

	protected void initializeParameters() throws MojoExecutionException {
		if (shell.equals("auto")) {
			if (Os.isFamily(Os.FAMILY_UNIX)) {
				shell = BashExecutor.SHELL;
			} else if (Os.isFamily(Os.FAMILY_WINDOWS)) {
				shell = CmdExecutor.SHELL;
			} else {
				getLog().warn(
						"OS family not recognized."
								+ " Defaulting to bash for command execution.");
				shell = BashExecutor.SHELL;
			}
		}
		String buildDir = project.getBuild().getDirectory();
		if (scriptFile == null) {
			String suffix = "";
			if (shell == CmdExecutor.SHELL) suffix = ".bat";
			scriptFile = new File(buildDir, "bin/" + name + suffix);
		}
		if (logFile == null) logFile = new File(buildDir, "logs/" + name + ".log");
		if (pidFile == null) pidFile = new File(buildDir, "run/" + name + ".pid");

		// resolve relative paths against working directory
		if (!scriptFile.isAbsolute()) scriptFile =
				new File(workingDir, scriptFile.getPath());
		if (!logFile.isAbsolute()) logFile =
				new File(workingDir, logFile.getPath());
		if (!pidFile.isAbsolute()) pidFile =
				new File(workingDir, pidFile.getPath());
	}

	protected void createRuntimePaths() {
		getLog().debug("Creating runtime paths");
		for (File file : new File[] { scriptFile, logFile, pidFile }) {
			File parentDir = file.getParentFile();
			if (!parentDir.exists()) if (parentDir.mkdirs()) getLog().debug(
					"Created directory '" + parentDir + "'");
		}
	}

	protected ShellExecutor createShellExecutor() throws MojoExecutionException {
		getLog().debug("Creating shell executor");
		ShellExecutor executor = null;
		if (shell.equals(BashExecutor.SHELL)) {
			executor = new BashExecutor();
		} else if (shell.equals(CmdExecutor.SHELL)) {
			executor = new CmdExecutor();
		} else {
			throw new MojoExecutionException("Invalid shell '" + shell + "'");
		}
		executor.setLog(getLog());
		executor.setName(name);
		executor.setCommand(getCommand().getPath());
		List<String> arguments = new ArrayList<String>();
		initializeArguments(arguments);
		executor.setArguments(arguments);
		executor.setWorkingDir(workingDir);
		executor.setScriptFile(scriptFile);
		executor.setLogFile(logFile);
		executor.setPidFile(pidFile);
		return executor;
	}

	protected void initializeArguments(List<String> arguments)
			throws MojoExecutionException {
		if (args == null) return;
		for (Object element : args) {
			if (element == null) continue;
			String arg = element.toString();
			if (arg.equals(classpathArgumentName)) arg = getClasspath();
			getLog().debug("Adding arg " + arg);
			arguments.add(arg);
		}
	}

	protected void createStartScript(ShellExecutor executor)
			throws MojoExecutionException {
		try {
			getLog().debug("Creating start script");
			executor.createStartScript();
			executor.getScriptFile().setExecutable(true);
			getLog().debug(
					"Start script '" + scriptFile.getCanonicalPath() + "' created");
		} catch (Exception e) {
			throw new MojoExecutionException("Failed to create start script '"
					+ scriptFile + "'", e);
		}
	}

	protected void executeStartScript(ShellExecutor executor)
			throws MojoExecutionException {
		try {
			getLog().debug("Executing start script");
			executor.executeStartScript(fork);
		} catch (Exception e) {
			throw new MojoExecutionException("Failed to execute start script '"
					+ scriptFile.getAbsolutePath() + "'", e);
		}
	}
}
