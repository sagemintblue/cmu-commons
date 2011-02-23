package edu.cmu.commons.indri.impl;

import java.io.Console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cmu.commons.indri.api.QueryEnvironment;
import edu.cmu.commons.indri.api.QueryRequest;
import edu.cmu.commons.indri.api.QueryResult;

/**
 * A basic client application for querying a local Indri index.
 */
public class QueryClient {
	private static final Logger log = LoggerFactory.getLogger(QueryClient.class);

	protected static void processInput(QueryEnvironment env) throws Exception {
		String query = null;
		Console console = System.console();
		while ((query = console.readLine("query> ")) != null) {
			QueryResult results = env.runQuery(new QueryRequest(query));
			System.out.println(results);
		}
	}

	public static void main(String[] args) {
		try {

			log.info("Parsing arguments");
			if (args.length < 1) throw new IllegalArgumentException(
					"Missing required argument <index>");
			String index = args[0];

			log.info("Initializing query environment");
			edu.cmu.commons.indri.impl.QueryEnvironment env =
					new edu.cmu.commons.indri.impl.QueryEnvironment();
			env.setIndexPath(index);
			env.initialize();
			processInput(env);

		} catch (Exception e) {
			log.error("Fatal exception encountered", e);
			System.exit(1);
		}
	}
}
