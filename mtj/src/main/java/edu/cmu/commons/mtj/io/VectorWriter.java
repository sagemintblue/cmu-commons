package edu.cmu.commons.mtj.io;

import java.io.IOException;

import no.uib.cipr.matrix.Vector;

public interface VectorWriter {
	/**
	 * Writes the vector to the output stream.
	 * @param vector
	 * @throws Exception
	 */
	public void writeVector(Vector vector) throws Exception;

	/**
	 * Closes the underlying output stream.
	 * @throws IOException
	 */
	public void close() throws IOException;
}
