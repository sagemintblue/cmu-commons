package edu.cmu.commons.mtj.io;

import java.io.IOException;

import no.uib.cipr.matrix.Vector;

public interface VectorReader {
	/**
	 * @return Vector read from input stream.
	 * @throws Exception
	 */
	public Vector readVector() throws Exception;

	/**
	 * Closes the underlying input stream.
	 * @throws IOException
	 */
	public void close() throws IOException;
}
