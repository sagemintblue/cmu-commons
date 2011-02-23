package edu.cmu.commons.util.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses and organizes command line arguments.
 * @author hazen
 */
public class CommandLineArguments implements Serializable {
	private static final long serialVersionUID = 1L;
	protected static final Pattern namedArgumentPattern = Pattern
			.compile("^--?([^=]+)(?:=(.+))?$");

	protected Map<String, List<String>> namedArguments = null;
	protected List<String> positionalArguments = null;

	/**
	 * Constructs an empty instance of this class. You must then initialize it via
	 * the {@link #initialize(String[])} method.
	 */
	public CommandLineArguments() {}

	/**
	 * Constructs an instance of this class and immediately parses the given
	 * arguments.
	 * @param args
	 */
	public CommandLineArguments(String[] args) {
		initialize(args);
	}

	/**
	 * Parses the set of String arguments, initializing the internal named and
	 * positional argument data structures.
	 * @param args an array of command line argument Strings.
	 */
	public void initialize(String[] args) {
		positionalArguments = new ArrayList<String>();
		namedArguments = new HashMap<String, List<String>>();

		// parse the arguments
		String name = null;
		String value = null;
		for (String argument : args) {
			value = argument;
			Matcher matcher = namedArgumentPattern.matcher(argument);
			if (matcher.matches()) {
				// found named argument
				name = matcher.group(1);
				if (matcher.groupCount() > 1) {
					// value is attached
					value = matcher.group(2);
				} else {
					// value is detached
					value = null;
				}
			}
			if (name != null) {
				// found named argument just now or previously
				if (value != null) {
					// value is present either attached or stand alone
					List<String> arguments = namedArguments.get(name);
					if (arguments == null) {
						arguments = new ArrayList<String>();
						namedArguments.put(name, arguments);
					}
					arguments.add(value);
					name = null;
				}
			} else {
				if (value != null) positionalArguments.add(value);
			}
		}
	}

	/**
	 * Merges the given Properties object with this object's map of named
	 * arguments.
	 * @param properties a Properties object to merge with this object's named
	 * arguments.
	 */
	public void merge(Properties properties) {
		for (String name : properties.stringPropertyNames()) {
			List<String> values = namedArguments.get(name);
			if (values == null) {
				values = new ArrayList<String>();
				namedArguments.put(name, values);
			}
			values.add(properties.getProperty(name));
		}
	}

	/**
	 * Merges the given properties map with this object's map of named arguments.
	 * @param properties a properties map to merge with this object's named
	 * arguments.
	 */
	public void merge(Map<String, List<String>> properties) {
		for (Map.Entry<String, List<String>> entry : properties.entrySet()) {
			List<String> values = namedArguments.get(entry.getKey());
			if (values != null) {
				values.addAll(entry.getValue());
			} else {
				namedArguments.put(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * @return reference to the internal named arguments map.
	 */
	public Map<String, List<String>> getNamedArguments() {
		return namedArguments;
	}

	/**
	 * @param name an argument name.
	 * @return list of all arguments associated with <code>name</code>, or null if
	 * none exist.
	 */
	public List<String> getNamedArguments(String name) {
		return namedArguments.get(name);
	}

	/**
	 * @param name an argument name.
	 * @return the first argument value associated with <code>name</code>, or null
	 * if none exist.
	 */
	public String getNamedArgument(String name) {
		List<String> arguments = namedArguments.get(name);
		if (arguments == null || arguments.isEmpty()) return null;
		return arguments.get(0);
	}

	/**
	 * @param name an argument name.
	 * @param defaultValue a default value.
	 * @return the first argument value associated with <code>name</code>, or
	 * <code>defaultValue</code> if none exist.
	 */
	public String getNamedArgument(String name, String defaultValue) {
		String argument = getNamedArgument(name);
		if (argument == null) return defaultValue;
		return argument;
	}

	/**
	 * @param namedArguments
	 */
	public void setNamedArguments(Map<String, List<String>> namedArguments) {
		this.namedArguments = namedArguments;
	}

	/**
	 * @return reference to internal positional arguments list.
	 */
	public List<String> getPositionalArguments() {
		return positionalArguments;
	}

	/**
	 * @param index zero-based index of the positional argument to return.
	 * @return the positional argument at the given index or <code>null</code> if
	 * no argument is defined at that index.
	 */
	public String getPositionalArgument(int index) {
		if (index < 0 || index >= positionalArguments.size()) return null;
		return positionalArguments.get(index);
	}

	/**
	 * @param index zero-based index of the positional argument to return.
	 * @param defaultValue
	 * @return the positional argument at the given index or
	 * <code>defaultValue</code> if no argument is defined at that index.
	 */
	public String getPositionalArgument(int index, String defaultValue) {
		String argument = getPositionalArgument(index);
		if (argument == null) return defaultValue;
		return argument;
	}

	/**
	 * @param positionalArguments
	 */
	public void setPositionalArguments(List<String> positionalArguments) {
		this.positionalArguments = positionalArguments;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Arguments[named=").append(namedArguments)
				.append(", positional=").append(positionalArguments).append("]");
		return sb.toString();
	}
}
