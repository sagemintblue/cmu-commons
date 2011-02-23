package edu.cmu.commons.mtj.io;

import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.sparse.SparseVector;

/**
 * Base class for VectorReader implementations.
 */
public abstract class AbstractVectorReader implements VectorReader {

	protected abstract String read() throws Exception;

	protected abstract int readInt() throws Exception;

	protected abstract double readDouble() throws Exception;

	@Override
	public Vector readVector() throws Exception {
		String clsName = read();
		Class<?> cls = (Class<?>) Class.forName(clsName);
		if (cls.equals(DenseVector.class)) return readDenseVector();
		if (cls.equals(SparseVector.class)) return readSparseVector();
		throw new UnsupportedOperationException("Unsupported vector class '" + cls
				+ "'");
	}

	private DenseVector readDenseVector() throws Exception {
		int size = readInt();
		DenseVector vector = new DenseVector(size);
		for (int i = 0; i < size; ++i)
			vector.set(i, readDouble());
		return vector;
	}

	private SparseVector readSparseVector() throws Exception {
		int size = readInt();
		int nz = readInt();
		SparseVector vector = new SparseVector(size, nz);
		for (int e = 0; e < nz; ++e)
			vector.set(readInt(), readDouble());
		return vector;
	}
}
