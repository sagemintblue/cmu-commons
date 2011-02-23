package edu.cmu.commons.mtj.io;

import java.io.IOException;

import no.uib.cipr.matrix.Matrix;

/**
 * Writes a compressed row or column (sparse) matrix to an output stream.
 */
public interface MatrixWriter {
	/**
	 * Writes the given matrix to the output stream in a format best suited to the
	 * implementation of the matrix.
	 * @param matrix the matrix to write to the output stream.
	 * @throws Exception
	 */
	public void writeMatrix(Matrix matrix) throws Exception;

	/**
	 * Closes the underlying output stream.
	 * @throws IOException
	 */
	public void close() throws IOException;
}
