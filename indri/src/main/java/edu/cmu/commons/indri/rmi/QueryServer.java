package edu.cmu.commons.indri.rmi;

import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cmu.commons.indri.api.QueryEnvironment;

/**
 * An RMI query server. Either <code>indri.native.host</code> or
 * <code>index</code> must be specified. Specify options on java command line as
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
 * <tr>
 * <td>indri.native.host</td>
 * <td></td>
 * <td>native Indri daemon "host[:port]" string</td>
 * </tr>
 * <tr>
 * <td>index</td>
 * <td></td>
 * <td>path to local Indri index</td>
 * </tr>
 * <tr>
 * <td>port</td>
 * <td>0</td>
 * <td>the port which this server should bind to. Specify zero here to let the
 * server chose an available port automatically.</td>
 * </tr>
 * </tbody>
 */
public class QueryServer {
	private static final Logger log = LoggerFactory.getLogger(QueryServer.class);

	public static void main(String[] args) {
		try {

			log.info("Parsing arguments");
			String rmiRegistryHost =
					System.getProperty("rmiregistry.host", "localhost");
			int rmiRegistryPort =
					Integer.parseInt(System.getProperty("rmiregistry.port", "1099"));
			String index = System.getProperty("index");
			int port = Integer.parseInt(System.getProperty("port", "0"));

			log.info("Initializing query environment");
			edu.cmu.commons.indri.impl.QueryEnvironment env =
					new edu.cmu.commons.indri.impl.QueryEnvironment();
			env.setIndexPath(index);
			env.initialize();

			log.info("Registering remote object");
			QueryEnvironment stub =
					(QueryEnvironment) UnicastRemoteObject.exportObject(env, port);
			Registry registry =
					LocateRegistry.getRegistry(rmiRegistryHost, rmiRegistryPort);
			try {
				registry.unbind(QueryEnvironment.class.getName());
			} catch (NotBoundException e) {
				// ignore this error
			}
			registry.bind(QueryEnvironment.class.getName(), stub);
			log.info("Server ready");

		} catch (Exception e) {

			log.error("Fatal exception encountered", e);
			System.exit(1);

		}
	}
}
