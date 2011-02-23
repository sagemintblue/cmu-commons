package edu.cmu.commons.mtj.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * MatrixReader supporting text format.
 */
public class TextMatrixReader extends AbstractMatrixReader {
	private Scanner is;

	public TextMatrixReader(InputStream is) {
		this.is = new Scanner(is);
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
		return is.next();
	}

	@Override
	protected int readInt() throws Exception {
		return is.nextInt();
	}

	@Override
	protected double readDouble() throws Exception {
		return is.nextDouble();
	}

}
