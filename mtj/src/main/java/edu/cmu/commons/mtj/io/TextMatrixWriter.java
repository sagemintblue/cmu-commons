package edu.cmu.commons.mtj.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.CharBuffer;

/**
 * MatrixWriter supporting text format.
 */
public class TextMatrixWriter extends AbstractMatrixWriter {
	private PrintStream os;
	private CharBuffer buf;

	/**
	 * @param os the OutputStream to write to.
	 */
	public TextMatrixWriter(OutputStream os) {
		this.os = new PrintStream(os);
		this.buf = CharBuffer.allocate(1024);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.cmu.albatross.util.matrix.io.MatrixWriter#close()
	 */
	public void close() throws IOException {
		os.close();
	}

	@Override
	protected void write(String s) throws Exception {
		buf.append(s).append(" ");
	}

	@Override
	protected void writeInt(int n) throws Exception {
		buf.append(Integer.toString(n)).append(" ");
	}

	@Override
	protected void writeDouble(double n) throws Exception {
		buf.append(Double.toString(n)).append(" ");
	}

	@Override
	protected void writeln() throws Exception {
		// prepare buf for read
		buf.flip();
		// ignore trailing space char
		buf.limit(buf.limit() - 1);
		// print contents of buf to output stream
		os.println(buf);
		// reset buf
		buf.clear();
	}

	@Override
	protected void flush() throws Exception {
		os.flush();
	}
}
