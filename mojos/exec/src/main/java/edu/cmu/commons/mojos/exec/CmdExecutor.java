package edu.cmu.commons.mojos.exec;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class CmdExecutor extends ShellExecutor {

	public static final String SHELL = "cmd";

	public void createStartScript() throws Exception {
		PrintWriter writer = new PrintWriter(scriptFile);
		writer.println("@ECHO OFF");
		writer.println();
		writer.println("SET PIDFILE=\"" + pidFile.getCanonicalPath() + "\"");

		// branch depending on value of first argument
		writer.println("IF \"%1\" == \"start\" (GOTO :start)");
		writer.println("IF \"%1\" == \"stop\" (GOTO :stop)");
		writer.println("goto :default");
		writer.println();

		// spawn self to run ":redirect" branch and record pid. only reasonable way
		// to determine pid is to search output of TASKLIST for process with
		// matching WINDOWTITLE string, which means we have to create a visible
		// window for the process. Unfortunately, using START "title" /B to create
		// the process without a window prohibits us from finding the process in the
		// output of TASKLIST. Execution of BIN directly here via START "title" CMD
		// /C %BIN% .. doesn't work as CMD /C %BIN% fails when BIN is wrapped in
		// quotes or has spaces (d'oh).
		writer.println(":start");
		writer.println("SET SCRIPT=%0");
		writer.println("SHIFT");
		writer.println("START \"" + name
				+ "\" /MIN %SCRIPT% !REDIRECT %1 %2 %3 %4 %5 %6 %7 %8 %9");
		writer.println("FOR /F \"tokens=2\" %%G IN"
				+ " ('TASKLIST /NH /FI \"WINDOWTITLE eq " + name
				+ "*\"') DO ECHO %%G >%PIDFILE%");
		writer.println("GOTO :eof");
		writer.println();

		// kill process with taskkill
		writer.println(":stop");
		writer.println("SET /P PID=<%PIDFILE%");
		writer.println("TASKKILL /PID %PID%");
		writer.println("GOTO :eof");
		writer.println();

		// define bin and args vars
		writer.println(":default");
		writer.println("SET BIN=\"" + command + "\"");
		writer.print("SET ARGS=");
		for (String arg : arguments)
			writer.print(arg + " ");
		writer.println();
		writer.println("IF \"%1\" == \"!REDIRECT\" (GOTO :redirect)");
		writer.println();

		// run the command without redirection. Unfortunately, entering ctrl-c after
		// execution of BIN will kill this script (CMD shell), but not the child
		// BIN process.
		writer.println(":exec");
		writer.println("%BIN% %ARGS% %1 %2 %3 %4 %5 %6 %7 %8 %9");
		writer.println("GOTO :eof");
		writer.println();

		// run the command, redirecting output to logfile
		writer.println(":redirect");
		writer.println("SHIFT");
		writer.println("SET LOGFILE=\"" + logFile.getCanonicalPath() + "\"");
		writer.println("%BIN% %ARGS% %1 %2 %3 %4 %5 %6 %7 %8 %9 >%LOGFILE% 2>&1");

		// finish
		writer.close();
	}

	public void executeStartScript(boolean fork) throws Exception {
		ProcessBuilder pb = new ProcessBuilder(SHELL);
		pb.directory(workingDir);
		Process p = pb.start();
		PrintWriter writer =
				new PrintWriter(new OutputStreamWriter(p.getOutputStream()));
		writer.println("@ECHO OFF");
		writer.println("SET LOGFILE=\"" + logFile.getCanonicalPath() + "\"");
		writer.println("\"" + scriptFile.getCanonicalPath() + "\""
				+ (fork ? " start" : " >%LOGFILE% 2>&1"));
		writer.println("EXIT");
		writer.close();
		p.getOutputStream().close();
		p.getErrorStream().close();
		int rv = p.waitFor();
		if (rv != 0) throw new Exception("Shell exited with non-zero code " + rv);
	}

}
