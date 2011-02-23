package edu.cmu.commons.mtj.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * MatrixWriter supporting binary format.
 */
public class BinaryMatrixWriter extends AbstractMatrixWriter {
	private DataOutputStream os;

	/**
	 * @param os the OutputStream to write to.
	 */
	public BinaryMatrixWriter(OutputStream os) {
		this.os = new DataOutputStream(os);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.cmu.albatross.util.matrix.io.MatrixWriter#close()
	 */
	@Override
	public void close() throws IOException {
		os.close();
	}

	@Override
	protected void write(String s) throws Exception {
		os.writeUTF(s);
	}

	@Override
	protected void writeInt(int n) throws Exception {
		os.writeInt(n);
	}

	@Override
	protected void writeDouble(double n) throws Exception {
		os.writeDouble(n);
	}

	@Override
	protected void flush() throws Exception {
		os.flush();
	}
}
