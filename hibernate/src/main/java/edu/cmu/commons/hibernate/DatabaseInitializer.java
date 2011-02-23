package edu.cmu.commons.hibernate;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility bean aiding database initialization.
 */
public class DatabaseInitializer {
	private static final Logger log =
			LoggerFactory.getLogger(DatabaseInitializer.class);

	@Resource
	private SessionFactory sessionFactory;
	@Resource
	private Configuration configuration;
	private boolean dropDatabaseSchema = true;
	private String[] preCreateSQLStatements;
	private String[] postCreateSQLStatements;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public boolean isDropDatabaseSchema() {
		return dropDatabaseSchema;
	}

	public void setDropDatabaseSchema(boolean dropDatabaseSchema) {
		this.dropDatabaseSchema = dropDatabaseSchema;
	}

	public String[] getPreCreateSQLStatements() {
		return preCreateSQLStatements;
	}

	public void setPreCreateSQLStatements(String[] preCreateSQLStatements) {
		this.preCreateSQLStatements = preCreateSQLStatements;
	}

	public String[] getPostCreateSQLStatements() {
		return postCreateSQLStatements;
	}

	public void setPostCreateSQLStatements(String[] postCreateSQLStatements) {
		this.postCreateSQLStatements = postCreateSQLStatements;
	}

	/**
	 * Performs the following series of operations:
	 * <ul>
	 * <li>{@link #dropDatabaseSchema()}, if dropDatabaseSchema is defined.</li>
	 * <li>Executes preCreateSQLStatements, if defined.</li>
	 * <li>Executes schema create script.</li>
	 * <li>Executes postCreateSQLStatements, if defined.</li>
	 * </ul>
	 * @throws Exception
	 */
	public void init() throws Exception {
		if (dropDatabaseSchema) dropDatabaseSchema();
		if (preCreateSQLStatements != null) {
			log.info("Executing pre-create SQL statements");
			executeSQLStatements(preCreateSQLStatements);
		}
		createDatabaseSchema();
		if (postCreateSQLStatements != null) {
			log.info("Executing post-create SQL statements");
			executeSQLStatements(postCreateSQLStatements);
		}
	}

	/**
	 * Execute schema drop script, determined by the Configuration object used for
	 * creating the SessionFactory.
	 */
	protected void dropDatabaseSchema() {
		log.info("Dropping database schema for Hibernate SessionFactory");
		Dialect dialect = Dialect.getDialect(getConfiguration().getProperties());
		String[] sql = getConfiguration().generateDropSchemaScript(dialect);
		executeSQLStatements(sql);
	}

	/**
	 * Execute schema creation script, determined by the Configuration object used
	 * for creating the SessionFactory.
	 */
	protected void createDatabaseSchema() {
		log.info("Creating database schema for Hibernate SessionFactory");
		Dialect dialect = Dialect.getDialect(getConfiguration().getProperties());
		String[] sql = getConfiguration().generateSchemaCreationScript(dialect);
		executeSQLStatements(sql);
	}

	/**
	 * Executes the list of sql statements using the SessionFactory.
	 * @param sql the statements to execute.
	 */
	protected void executeSQLStatements(final String[] sql) {
		StatelessSession session = getSessionFactory().openStatelessSession();
		for (String statement : sql) {
			try {
				session.createSQLQuery(statement).executeUpdate();
			} catch (Exception e) {
				// ignore all exceptions
			}
		}
		session.close();
	}
}
