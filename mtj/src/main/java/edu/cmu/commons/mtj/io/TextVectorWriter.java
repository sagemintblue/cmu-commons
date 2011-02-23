package edu.cmu.commons.mtj.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.CharBuffer;

/**
 * VectorWriter supporting text format.
 */
public class TextVectorWriter extends AbstractVectorWriter {
	private PrintStream os;
	private CharBuffer buf;

	public TextVectorWriter(OutputStream os) {
		this.os = new PrintStream(os);
		this.buf = CharBuffer.allocate(1024);
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
		buf.append(s).append(" ");
	}

	@Override
	protected void writeDouble(double v) throws Exception {
		buf.append(Double.toString(v)).append(" ");
	}

	@Override
	protected void writeInt(int v) throws Exception {
		buf.append(Integer.toString(v)).append(" ");
	}

	@Override
	protected void writeln() throws Exception {
		buf.flip();
		buf.limit(buf.limit() - 1);
		os.println(buf);
		buf.clear();
	}
}
