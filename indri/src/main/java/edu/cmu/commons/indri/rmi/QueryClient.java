package edu.cmu.commons.indri.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cmu.commons.indri.api.QueryEnvironment;

/**
 * An RMI query client. Specify options on java command line as
 * <code>-Doption=value</code>:
 * <table>
 * <thead>
 * <th>Name</th>
 * <th>Default</th>
 * <th>Description</th> </thead> <tbody>
 * <tr>
 * <td>rmiregistry.host</td>
 * <td>localhost</td>
 * <td>Java RMI Registry host</td>
 * </tr>
 * <tr>
 * <td>rmiregistry.port</td>
 * <td>1099</td>
 * <td>Java RMI Registry port</td>
 * </tr>
 * </tbody>
 */
public class QueryClient extends edu.cmu.commons.indri.impl.QueryClient
{
	private static final Logger log = LoggerFactory.getLogger(QueryClient.class);

	public static void main(String[] args)
	{
		try {

			log.info("Parsing arguments");
			String rmiRegistryHost = System.getProperty("rmiregistry.host",
					"localhost");
			int rmiRegistryPort = Integer.parseInt(System.getProperty(
					"rmiregistry.port", "1099"));

			log.info("Initializing remote object");
			Registry registry = LocateRegistry.getRegistry(rmiRegistryHost,
					rmiRegistryPort);
			QueryEnvironment envStub = (QueryEnvironment) registry
					.lookup(QueryEnvironment.class.getName());

			processInput(envStub);

		} catch (Exception e) {
			log.error("Fatal exception encountered", e);
			System.exit(1);
		}
	}
}
