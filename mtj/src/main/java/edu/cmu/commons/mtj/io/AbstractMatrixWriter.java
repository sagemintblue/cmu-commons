package edu.cmu.commons.mtj.io;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.VectorEntry;
import no.uib.cipr.matrix.sparse.FlexCompColMatrix;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;

/**
 * Base class for MatrixWriter implementations.
 */
public abstract class AbstractMatrixWriter implements MatrixWriter {

	protected abstract void write(String s) throws Exception;

	protected abstract void writeInt(int n) throws Exception;

	protected abstract void writeDouble(double n) throws Exception;

	protected void writeln() throws Exception {}

	protected abstract void flush() throws Exception;

	@Override
	public void writeMatrix(Matrix matrix) throws Exception {
		write(matrix.getClass().getName());
		writeln();
		if (matrix instanceof DenseMatrix) writeDenseMatrix((DenseMatrix) matrix);
		else if (matrix instanceof FlexCompRowMatrix) writeFlexCompRowMatrix((FlexCompRowMatrix) matrix);
		else if (matrix instanceof FlexCompColMatrix) writeFlexCompColMatrix((FlexCompColMatrix) matrix);
		else throw new UnsupportedOperationException("Unsupported matrix class '"
				+ matrix.getClass() + "'");
		flush();
	}

	private void writeDenseMatrix(DenseMatrix matrix) throws Exception {
		int numRows = matrix.numRows();
		int numCols = matrix.numColumns();
		writeInt(numRows);
		writeInt(numCols);
		writeln();
		// data array is column-major
		for (double d : matrix.getData()) {
			writeDouble(d);
			writeln();
		}
	}

	private void writeFlexCompRowMatrix(FlexCompRowMatrix matrix)
			throws Exception {
		int numRows = matrix.numRows();
		int numCols = matrix.numColumns();
		int numEntries = 0;
		for (int i = 0; i < numRows; ++i)
			numEntries += matrix.getRow(i).getUsed();
		writeInt(numRows);
		writeInt(numCols);
		writeInt(numEntries);
		writeln();
		for (int i = 0; i < numRows; ++i)
			for (VectorEntry e : matrix.getRow(i)) {
				writeInt(i);
				writeInt(e.index());
				writeDouble(e.get());
				writeln();
			}
	}

	private void writeFlexCompColMatrix(FlexCompColMatrix matrix)
			throws Exception {
		int numRows = matrix.numRows();
		int numCols = matrix.numColumns();
		int numEntries = 0;
		for (int j = 0; j < numCols; ++j)
			numEntries += matrix.getColumn(j).getUsed();
		writeInt(numRows);
		writeInt(numCols);
		writeInt(numEntries);
		writeln();
		for (int j = 0; j < numCols; ++j)
			for (VectorEntry e : matrix.getColumn(j)) {
				writeInt(e.index());
				writeInt(j);
				writeDouble(e.get());
				writeln();
			}
	}

}
