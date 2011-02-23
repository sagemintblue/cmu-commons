package edu.cmu.commons.util.sh;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Utility class for executing external commands within a Bash shell. This is
 * useful when you'd like to take advantage of the shell's IO redirection
 * facilities, e.g. to append standard error or output to a file.
 * @author hazen
 */
public class ShellCommand {
	protected ShellCommand() {};

	/**
	 * Executes a command through a shell instance. This call blocks until the
	 * child shell process terminates.
	 * @param shell a shell with which to exec the command (e.g. "bash").
	 * @param cmd the command to pass to shell for execution.
	 * @return zero on success.
	 * @throws IOException if execution of bash process fails.
	 * @throws InterruptedException if interrupted while waiting for command to
	 * terminate.
	 * @throws NonZeroExitStatusException if command returns non-zero exit status.
	 */
	public static int execute(String shell, String cmd) throws IOException,
			InterruptedException, NonZeroExitStatusException {
		Process process = Runtime.getRuntime().exec(shell);
		BufferedWriter writer =
				new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
		process.getInputStream().close();
		process.getErrorStream().close();
		writer.append(cmd).append("\n");
		writer.flush();
		writer.close();
		int rv = process.waitFor();
		if (rv != 0) throw new NonZeroExitStatusException(rv);
		return rv;
	}

	/**
	 * Convenience method. Equivalent to <code>execute("bash", String cmd)</code>.
	 * @see #execute(String, String)
	 */
	public static int execute(String cmd) throws IOException,
			InterruptedException, NonZeroExitStatusException {
		return execute("bash", cmd);
	}

	/**
	 * Executes a command through a shell instance, buffering all standard output
	 * and returning buffer content as a CharSequence on shell termination.
	 * @param shell a shell with which to exec the command (e.g. "bash").
	 * @param cmd the command to pass to bash instance for execution.
	 * @param maxChars the maximum number of output characters to buffer.
	 * @return a CharSequence containing buffered standard output from executed
	 * command.
	 * @throws IOException if execution of bash process fails, or input buffer is
	 * overrun.
	 * @throws InterruptedException if interrupted while waiting for command to
	 * terminate.
	 * @throws NonZeroExitStatusException if command returns non-zero exit status.
	 */
	public static CharSequence executeQuery(String shell, String cmd,
			long maxChars) throws IOException, InterruptedException,
			NonZeroExitStatusException {
		Process process = Runtime.getRuntime().exec(shell);
		BufferedWriter writer =
				new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
		BufferedReader reader =
				new BufferedReader(new InputStreamReader(process.getInputStream()));
		process.getErrorStream().close();
		writer.append(cmd).append("\n");
		writer.flush();
		writer.close();
		StringBuilder buffer = new StringBuilder();
		char[] cbuf = new char[4096];
		int charsRead = 0;
		long totalCharsRead = 0;
		while ((charsRead = reader.read(cbuf, 0, 4096)) != -1) {
			totalCharsRead += charsRead;
			if (totalCharsRead > maxChars) throw new IOException(
					"Maximum buffer size exceeded");
			buffer.append(cbuf, 0, charsRead);
		}
		reader.close();
		int rv = process.waitFor();
		if (rv != 0) throw new NonZeroExitStatusException(rv);
		return buffer;
	}

	/**
	 * Convenience method. Equivalent to
	 * <code>executeQuery("bash", String cmd, long maxChars)</code>
	 * @see #executeQuery(String, String, long)
	 */
	public static CharSequence executeQuery(String cmd, long maxChars)
			throws IOException, InterruptedException, NonZeroExitStatusException {
		return executeQuery("bash", cmd, maxChars);
	}

	/**
	 * Convenience method. Equivalent to
	 * <code>executeQuery("bash", String cmd, 5000000)</code>.
	 * @see #executeQuery(String, String, long)
	 */
	public static CharSequence executeQuery(String cmd) throws IOException,
			InterruptedException, NonZeroExitStatusException {
		return executeQuery(cmd, 5000000);
	}
}
