package edu.cmu.commons.mtj.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * VectorWriter supporting binary format.
 */
public class BinaryVectorWriter extends AbstractVectorWriter {
	private DataOutputStream os;

	/**
	 * @param os the OutputStream to write to.
	 */
	public BinaryVectorWriter(OutputStream os) {
		this.os = new DataOutputStream(os);
	}

	@Override
	public void close() throws IOException {
		os.close();
	}

	@Override
	protected void flush() throws Exception {
		os.flush();
	}

	@Override
	protected void write(String s) throws Exception {
		os.writeUTF(s);
	}

	@Override
	protected void writeDouble(double v) throws Exception {
		os.writeDouble(v);
	}

	@Override
	protected void writeInt(int v) throws Exception {
		os.writeInt(v);
	}
}
