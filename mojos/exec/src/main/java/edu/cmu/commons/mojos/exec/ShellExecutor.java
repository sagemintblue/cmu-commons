package edu.cmu.commons.mojos.exec;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.logging.Log;

public abstract class ShellExecutor {

	protected Log log;
	protected String name;
	protected String command;
	protected List<String> arguments;
	protected File workingDir;
	protected File logFile;
	protected File pidFile;
	protected File scriptFile;

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public List<String> getArguments() {
		return arguments;
	}

	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}

	public File getWorkingDir() {
		return workingDir;
	}

	public void setWorkingDir(File workingDir) {
		this.workingDir = workingDir;
	}

	public File getLogFile() {
		return logFile;
	}

	public void setLogFile(File logFile) {
		this.logFile = logFile;
	}

	public File getPidFile() {
		return pidFile;
	}

	public void setPidFile(File pidFile) {
		this.pidFile = pidFile;
	}

	public File getScriptFile() {
		return scriptFile;
	}

	public void setScriptFile(File scriptFile) {
		this.scriptFile = scriptFile;
	}

	public abstract void createStartScript() throws Exception;

	protected abstract void executeStartScript(boolean fork) throws Exception;

}
