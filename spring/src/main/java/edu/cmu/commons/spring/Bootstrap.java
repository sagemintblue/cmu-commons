package edu.cmu.commons.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.cmu.commons.util.app.Application;
import edu.cmu.commons.util.app.UsageException;

/**
 * Bootstrap class for executing an application configured via Spring.
 * <p>
 * The name of the application context xml file to load is formed using the
 * value of the "context" system property (i.e. the system property whose name
 * matches the value of the constant <code>CONTEXT_PROPERTY_NAME</code>):
 * "/META-INF/${context}.xml".
 * <p>
 * A bootstrap PropertyPlaceholderConfigurer is registered with the created
 * context such that any "@{name}" placeholders within Spring configuration may
 * be resolved against system properties, even when referenced by other
 * BeanFactoryPostProcessors. Any "@{name}" placeholders not matched by system
 * properties will be ignored.
 * <p>
 * The last Application bean, if any, defined within the loaded Spring
 * configuration will be further configured with the command line arguments
 * passed to {@link #main(String[])} and then executed via its
 * {@link Application#run()} method.
 */
public class Bootstrap {
	private static Logger log = LoggerFactory.getLogger(Bootstrap.class);
	public static final String CONTEXT_PROPERTY_NAME = "context";
	public static final String CONTEXT_PROPERTY_DEFAULT = "context";

	public static void main(String[] args) {
		int status = 0;
		try {

			// get the name of the application context xml file to load
			String context = System.getProperty(CONTEXT_PROPERTY_NAME);
			if (context == null) context = CONTEXT_PROPERTY_DEFAULT;

			// create specified application context, but do not refresh
			ClassPathXmlApplicationContext applicationContext =
					new ClassPathXmlApplicationContext(new String[] {
						"/META-INF/" + context + ".xml"
					}, false);

			// add a property placeholder configurer capable of resolving names
			// of system properties, even when used within the property values
			// of other BeanFactoryPostProcessors or their dependencies.
			PropertyPlaceholderConfigurer bootstrapPropertyPlaceholderConfigurer =
					new PropertyPlaceholderConfigurer();
			bootstrapPropertyPlaceholderConfigurer
					.setIgnoreUnresolvablePlaceholders(true);
			bootstrapPropertyPlaceholderConfigurer.setPlaceholderPrefix("@{");
			applicationContext
					.addBeanFactoryPostProcessor(bootstrapPropertyPlaceholderConfigurer);

			// now refresh context
			applicationContext.refresh();

			// give container a chance to shutdown gracefully
			applicationContext.registerShutdownHook();

			// find all Application beans and execute the last defined
			String[] appBeanNames =
					applicationContext.getBeanNamesForType(Application.class);
			if (appBeanNames.length > 0) {
				String appBeanName = appBeanNames[appBeanNames.length - 1];
				log.info("Running application bean '" + appBeanName + "'");
				Application app = (Application) applicationContext.getBean(appBeanName);
				app.setArguments(args);
				status = app.run();
			}
		} catch (UsageException e) {
			log.error(e.getMessage());
			status = 1;
		} catch (Exception e) {
			log.error("Fatal exception encountered", e);
			status = 1;
		}
		if (status != 0) System.exit(status);
	}

}
