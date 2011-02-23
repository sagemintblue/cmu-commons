package edu.cmu.commons.util.app;

/**
 * An exception which signals an application usage (user) error.
 * @author hazen
 * @see Application
 */
public class UsageException extends Exception {

	private static final long serialVersionUID = 1L;

	public UsageException() {}

	public UsageException(String message) {
		super(message);
	}

	public UsageException(Throwable cause) {
		super(cause);
	}

	public UsageException(String message, Throwable cause) {
		super(message, cause);
	}

}
