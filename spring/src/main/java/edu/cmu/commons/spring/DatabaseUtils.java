package edu.cmu.commons.spring;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

/**
 * Database utilities.
 */
public class DatabaseUtils {
	private static final Logger log =
			LoggerFactory.getLogger(DatabaseUtils.class);

	@Resource
	private LocalSessionFactoryBean localSessionFactoryBean;

	/**
	 * Drops then creates database schema using LocalSessionFactoryBean.
	 * @throws Exception
	 */
	public void recreateDatabaseSchema() throws Exception {
		log.info("Recreating database schema");
		localSessionFactoryBean.dropDatabaseSchema();
		localSessionFactoryBean.createDatabaseSchema();
	}
}
