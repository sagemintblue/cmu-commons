package edu.cmu.commons.mojos.exec;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.maven.model.Profile;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Goal which executes a background Java process via a shell.
 * @author Andy Schlaikjer <hazen@cs.cmu.edu>
 * @goal java
 * @requiresDependencyResolution test
 */
public class JavaMojo extends AbstractExecMojo {

	/**
	 * Path to Java executable.
	 * @parameter
	 * default-value="${java.home}${file.separator}bin${file.separator}java"
	 * @required
	 */
	private File java;

	/**
	 * Java virtual machine arguments.
	 * @parameter
	 */
	private List<Object> jvmargs;

	/**
	 * Target class to execute.
	 * @parameter alias="class"
	 * @required
	 */
	private String mainClass;

	/**
	 * When true, all Maven properties are passed to the child process as system
	 * properties.
	 * @parameter default-value="false"
	 */
	private boolean inheritProps;

	@Override
	protected File getCommand() {
		return java;
	}

	@Override
	protected void initializeArguments(List<String> arguments)
			throws MojoExecutionException {
		arguments.add("-cp");
		arguments.add("\"" + getClasspath() + "\"");
		migrateMavenProperties();
		if (jvmargs != null) for (Object element : jvmargs) {
			String arg = element.toString();
			getLog().debug("Adding jvmarg " + arg);
			arguments.add(arg);
		}
		arguments.add(mainClass);
		super.initializeArguments(arguments);
	}

	protected void migrateMavenProperties() {
		if (!inheritProps) return;
		getLog().debug("Migrating Maven properties to jvmargs");
		List<Properties> propertySets = new ArrayList<Properties>();
		for (Object element : project.getActiveProfiles()) {
			Profile profile = (Profile) element;
			Properties properties = profile.getProperties();
			getLog().debug(
					"Merging properties from profile '" + profile.getId() + "'");
			getLog().debug(properties.toString());
			propertySets.add(properties);
		}
		propertySets.add(project.getProperties());
		Collections.reverse(propertySets);
		Properties mergedProperties = new Properties();
		for (Properties properties : propertySets)
			mergedProperties.putAll(properties);
		for (Entry<?, ?> entry : mergedProperties.entrySet()) {
			String name = entry.getKey().toString();
			String value = entry.getValue().toString();
			String arg = "-D" + name + "=" + value;
			jvmargs.add(arg);
		}
	}
}
