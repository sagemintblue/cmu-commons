package edu.cmu.commons.util.sh;

/**
 * @author hazen
 */
public class NonZeroExitStatusException extends Exception {
	private static final long serialVersionUID = 1L;

	private int exitStatus;

	public NonZeroExitStatusException() {
		super();
	}

	public NonZeroExitStatusException(int exitStatus) {
		this("Command exited with status " + exitStatus);
		this.exitStatus = exitStatus;
	}

	public NonZeroExitStatusException(String message) {
		super(message);
	}

	public NonZeroExitStatusException(String message, int exitStatus) {
		super(message);
		this.exitStatus = exitStatus;
	}

	public NonZeroExitStatusException(Throwable cause) {
		super(cause);
	}

	public NonZeroExitStatusException(String message, Throwable cause) {
		super(message, cause);
	}

	public int getExitStatus() {
		return exitStatus;
	}
}
