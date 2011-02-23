package edu.cmu.commons.mojos.exec;

import java.io.File;

/**
 * Goal which executes a command as a background process via a shell.
 * @author Andy Schlaikjer <hazen@cs.cmu.edu>
 * @goal exec
 * @requiresDependencyResolution test
 */
public class ExecMojo extends AbstractExecMojo {
	/**
	 * Command to execute.
	 * @parameter
	 * @required
	 */
	protected File command;

	protected File getCommand() {
		return command;
	}
}
