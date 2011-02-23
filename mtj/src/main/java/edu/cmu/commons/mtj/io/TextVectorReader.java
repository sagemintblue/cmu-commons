package edu.cmu.commons.mtj.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * VectorReader supporting text format.
 */
public class TextVectorReader extends AbstractVectorReader {

	private Scanner is;

	public TextVectorReader(InputStream is) {
		this.is = new Scanner(is);
	}

	@Override
	public void close() throws IOException {
		is.close();
	}

	@Override
	protected String read() throws Exception {
		return is.next();
	}

	@Override
	protected double readDouble() throws Exception {
		return is.nextDouble();
	}

	@Override
	protected int readInt() throws Exception {
		return is.nextInt();
	}
}
