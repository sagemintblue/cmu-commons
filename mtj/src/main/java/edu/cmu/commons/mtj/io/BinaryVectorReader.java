package edu.cmu.commons.mtj.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * VectorReader supporting binary format.
 */
public class BinaryVectorReader extends AbstractVectorReader {
	private DataInputStream is;

	/**
	 * @param is an InputStream from which to read Matrix data.
	 */
	public BinaryVectorReader(InputStream is) {
		this.is = new DataInputStream(is);
	}

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
