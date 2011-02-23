package edu.cmu.commons.mojos.exec;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class BashExecutor extends ShellExecutor {

	public static final String SHELL = "bash";

	public void createStartScript() throws Exception {
		PrintWriter writer = new PrintWriter(scriptFile);

		// do no use println when creating script as cygwin bash will die on '\r'
		// line-feed chars.
		writer.print("#!/bin/bash\n\n");
		writer.print("LOGFILE=\"" + logFile.getCanonicalPath() + "\"\n");
		writer.print("PIDFILE=\"" + pidFile.getCanonicalPath() + "\"\n\n");

		writer.print("if [ \"$1\" == \"stop\" ]; then\n");
		writer.print("  kill `cat $PIDFILE`\n");
		writer.print("  exit\n");
		writer.print("fi\n\n");

		writer.print("BIN=\"" + command + "\"\n");
		writer.print("declare -a ARGS=(");
		for (String arg : arguments)
			writer.print(arg + " ");
		writer.print(")\n\n");

		// cygwin bash can't execute windows paths, but seems to manage fine when
		// they're present elsewhere on the command line. fix the path to BIN if
		// we're running under cygwin.
		writer.print("if [ `uname -o` == \"Cygwin\" ]; then "
				+ " BIN=`cygpath -u \"$BIN\"`; fi\n\n");

		// execute BIN, redirecting output to LOGFILE and background. so long as
		// startup is successful, save process id to PIDFILE and exit.
		writer.print("if [ \"$1\" == \"start\" ]; then\n");
		writer.print("  shift\n");
		writer.print("  \"$BIN\" \"${ARGS[@]}\" \"$@\" >\"$LOGFILE\" 2>&1 &\n");
		writer.print("  RV=\"$?\"; PID=\"$!\"\n");
		writer.print("  if [ \"$RV\" -ne 0 ]; then exit \"$RV\"; fi\n");
		writer.print("  echo \"$PID\" >\"$PIDFILE\"\n");
		writer.print("  exit\n");
		writer.print("fi\n\n");

		// execute BIN without redirecting output
		writer.print("exec \"$BIN\" \"${ARGS[@]}\" \"$@\"\n");
		writer.close();
	}

	public void executeStartScript(boolean fork) throws Exception {
		ProcessBuilder pb = new ProcessBuilder(SHELL);
		pb.directory(workingDir);
		Process p = pb.start();
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(p
				.getOutputStream()));
		writer.print("LOGFILE=\"" + logFile.getCanonicalPath() + "\"\n");
		writer.print("BIN=\"" + scriptFile.getCanonicalPath() + "\"\n");
		writer.print("if [ `uname -o` == \"Cygwin\" ]; then "
				+ " BIN=`cygpath -u \"$BIN\"`; fi\n");
		writer.print("\"$BIN\"" + (fork ? " start" : " >\"$LOGFILE\" 2>&1") + "\n");
		writer.print("exit\n");
		writer.close();
		p.getOutputStream().close();
		p.getErrorStream().close();
		int rv = p.waitFor();
		if (rv != 0) throw new Exception("Shell exited with non-zero code " + rv);
	}

}
