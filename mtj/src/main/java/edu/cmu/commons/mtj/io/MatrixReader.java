package edu.cmu.commons.mtj.io;

import java.io.IOException;

import no.uib.cipr.matrix.Matrix;

/**
 * Reads a compressed row or column (sparse) matrix from an input stream.
 */
public interface MatrixReader {
	/**
	 * @return a Matrix whose implementation is dependent on the format of the
	 * data read from the input stream.
	 * @throws Exception
	 */
	public Matrix readMatrix() throws Exception;

	/**
	 * Closes the underlying data stream.
	 * @throws IOException
	 */
	public void close() throws IOException;
}
