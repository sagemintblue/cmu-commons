package edu.cmu.commons.mtj.io;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.sparse.FlexCompColMatrix;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
import no.uib.cipr.matrix.sparse.SparseVector;

/**
 * Base class for MatrixReader implementations.
 */
public abstract class AbstractMatrixReader implements MatrixReader {

	protected abstract String read() throws Exception;

	protected abstract int readInt() throws Exception;

	protected abstract double readDouble() throws Exception;

	@Override
	public Matrix readMatrix() throws Exception {
		String clsName = read();
		Class<?> cls = Class.forName(clsName);
		if (cls.equals(DenseMatrix.class)) return readDenseMatrix();
		if (cls.equals(FlexCompRowMatrix.class)) return readFlexCompRowMatrix();
		if (cls.equals(FlexCompColMatrix.class)) return readFlexCompColMatrix();
		throw new UnsupportedOperationException("Unsupported matrix class '" + cls
				+ "'");
	}

	private DenseMatrix readDenseMatrix() throws Exception {
		int numRows = readInt();
		int numColumns = readInt();
		DenseMatrix matrix = new DenseMatrix(numRows, numColumns);
		// data is column-major
		for (int j = 0; j < numColumns; ++j)
			for (int i = 0; i < numRows; ++i)
				matrix.set(i, j, readDouble());
		return matrix;
	}

	private FlexCompRowMatrix readFlexCompRowMatrix() throws Exception {
		int numRows = readInt();
		int numColumns = readInt();
		int numEntries = readInt();
		FlexCompRowMatrix matrix = new FlexCompRowMatrix(numRows, numColumns);
		// entries sorted by row (asc), column (asc)
		int lastRowIndex = -1;
		SparseVector row = null;
		for (int e = 0; e < numEntries; ++e) {
			int r = readInt();
			int c = readInt();
			double value = readDouble();
			if (r != lastRowIndex) {
				if (row != null) matrix.setRow(lastRowIndex, row);
				row = new SparseVector(numColumns);
			}
			row.set(c, value);
			lastRowIndex = r;
		}
		if (row != null) matrix.setRow(lastRowIndex, row);
		return matrix;
	}

	private FlexCompColMatrix readFlexCompColMatrix() throws Exception {
		int numRows = readInt();
		int numColumns = readInt();
		int numEntries = readInt();
		FlexCompColMatrix matrix = new FlexCompColMatrix(numRows, numColumns);
		// entries sorted by column (asc), row (asc)
		int lastColIndex = -1;
		SparseVector col = null;
		for (int e = 0; e < numEntries; ++e) {
			int r = readInt();
			int c = readInt();
			double value = readDouble();
			if (c != lastColIndex) {
				if (col != null) matrix.setColumn(lastColIndex, col);
				col = new SparseVector(numColumns);
			}
			col.set(c, value);
			lastColIndex = r;
		}
		if (col != null) matrix.setColumn(lastColIndex, col);
		return matrix;
	}
}
