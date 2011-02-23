package edu.cmu.commons.mtj.io;

import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.VectorEntry;
import no.uib.cipr.matrix.sparse.SparseVector;

/**
 * Base class for VectorWriter implementations.
 */
public abstract class AbstractVectorWriter implements VectorWriter {

	protected abstract void write(String s) throws Exception;

	protected abstract void writeInt(int v) throws Exception;

	protected abstract void writeDouble(double v) throws Exception;

	protected void writeln() throws Exception {};

	protected abstract void flush() throws Exception;

	@Override
	public void writeVector(Vector vector) throws Exception {
		write(vector.getClass().getName());
		writeln();
		if (vector instanceof DenseVector) writeDenseVector((DenseVector) vector);
		else if (vector instanceof SparseVector) writeSparseVector((SparseVector) vector);
		else throw new UnsupportedOperationException("Unsupported vector class '"
				+ vector.getClass() + "'");
		flush();
	}

	private void writeDenseVector(DenseVector vector) throws Exception {
		writeInt(vector.size());
		writeln();
		for (int i = 0; i < vector.size(); ++i) {
			writeDouble(vector.get(i));
			writeln();
		}
	}

	private void writeSparseVector(SparseVector vector) throws Exception {
		writeInt(vector.size());
		writeInt(vector.getUsed());
		writeln();
		for (VectorEntry entry : vector) {
			writeInt(entry.index());
			writeDouble(entry.get());
			writeln();
		}
	}
}
