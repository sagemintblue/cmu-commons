package edu.cmu.commons.mtj.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * MatrixReader supporting binary format.
 */
public class BinaryMatrixReader extends AbstractMatrixReader {
	private DataInputStream is;

	/**
	 * @param is an InputStream from which to read Matrix data.
	 */
	public BinaryMatrixReader(InputStream is) {
		this.is = new DataInputStream(is);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.cmu.albatross.util.matrix.io.MatrixReader#close()
	 */
	@Override
	public void close() throws IOException {
		is.close();
	}

	@Override
	protected String read() throws Exception {
		return is.readUTF();
	}

	@Override
	protected double readDouble() throws Exception {
		return is.readDouble();
	}

	@Override
	protected int readInt() throws Exception {
		return is.readInt();
	}
}
