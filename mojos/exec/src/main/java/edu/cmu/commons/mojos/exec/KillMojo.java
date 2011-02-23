package edu.cmu.commons.mojos.exec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Goal which attempts to signal a running process via the "kill" command.
 * @author Andy Schlaikjer <hazen@cs.cmu.edu>
 * @goal kill
 */
public class KillMojo extends AbstractExecMojo {

	private static final String COMMAND_KILL = "kill";
	private static final String COMMAND_TASKKILL = "TASKKILL";

	/**
	 * Signal to send process. Not used if value of command parameter is not equal
	 * to "kill".
	 * @parameter default-value="1"
	 */
	private String signal;

	/**
	 * If true, an exception will be thrown on any error. Otherwise, a warning
	 * will be logged.
	 * @parameter default-value="false"
	 */
	private boolean failOnError;

	private File command;

	@Override
	protected File getCommand() {
		if (command == null) {
			if (shell.equals("cmd")) command = new File(COMMAND_TASKKILL);
			else command = new File(COMMAND_KILL);
		}
		return command;
	}

	@Override
	protected void initializeParameters() throws MojoExecutionException {
		super.initializeParameters();
		try {
			// create temp file for script
			String suffix = "";
			if (shell == CmdExecutor.SHELL) suffix = ".bat";
			scriptFile = File.createTempFile("maven-exec-plugin-kill-", suffix);
			logFile = File.createTempFile("maven-exec-plugin-kill-", ".log");

			// never fork the kill script
			fork = false;
		} catch (IOException e) {
			throw new MojoExecutionException("Failed to override parameters", e);
		}
	}

	@Override
	protected void initializeArguments(List<String> arguments)
			throws MojoExecutionException {
		// test for existence of pid file
		if (!pidFile.exists()) {
			String message = "Pid file '" + pidFile.getAbsolutePath() + "' not found";
			if (failOnError) throw new MojoExecutionException(message);
			else getLog().warn(message);
			return;
		}

		// read pid from file
		getLog().debug("Reading pid file");
		int pid = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(pidFile));
			String line = reader.readLine();
			Scanner scanner = new Scanner(line);
			pid = scanner.nextInt();
		} catch (Exception e) {
			throw new MojoExecutionException("Failed to read pid from file '"
					+ pidFile.getAbsolutePath() + "'", e);
		}

		// validate pid
		if (pid < 1) error("Invalid process id '" + pid + "'");

		// add arguments
		if (shell.equals(BashExecutor.SHELL)) {
			arguments.add("-" + signal); // bash supports signal arg
		} else if (shell.equals(CmdExecutor.SHELL)) {
			arguments.add("/PID"); // TASKKILL requires extra arg
		}
		arguments.add(Integer.toString(pid));
	}

	private void error(String message) throws MojoExecutionException {
		if (failOnError) throw new MojoExecutionException(message);
		getLog().warn(message);
	}
}
