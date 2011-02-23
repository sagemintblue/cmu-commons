package edu.cmu.commons.hibernate;

import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic Hibernate SessionFactory access via singleton pattern.
 */
public class HibernateUtil {

	private static final Logger log = LoggerFactory
			.getLogger(HibernateUtil.class);
	private static SessionFactory sessionFactory = null;

	public static void close() {
		if (sessionFactory != null)
			sessionFactory.close();
	}

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {

				// attempt to load extra hibernate properties from file
				// specified by system property
				String propertiesPath = System
						.getProperty("hibernate.configuration");
				if (propertiesPath != null) {

					// promote properties from hibernate properties file to
					// system properties so hibernate initialization will see
					// them
					log.info("Loading Hibernate configuration from '"
							+ propertiesPath + "'");
					Properties properties = new Properties();
					properties.load(new FileInputStream(propertiesPath));
					for (Map.Entry<?, ?> entry : properties.entrySet()) {
						String key = (String) entry.getKey();
						String value = System.getProperty(key);
						if (value == null) {
							value = (String) entry.getValue();
							System.setProperty(key, value);
						}
						log.debug(key + "=" + value);
					}
				}

				// create hibernate configuration
				Configuration config = new Configuration().configure();

				// build session factory from configuration
				sessionFactory = config.buildSessionFactory();

			} catch (Throwable e) {
				throw new ExceptionInInitializerError(e);
			}
		}
		return sessionFactory;
	}
}
