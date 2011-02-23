package edu.cmu.commons.mtj.util;

import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
import no.uib.cipr.matrix.sparse.SparseVector;

/**
 * A collection of matrix utility routines.
 */
public class MatrixUtils {
	/**
	 * Normalizes each row of the given matrix independently to sum to one.
	 * @param matrix
	 */
	public static void rowNormalize(FlexCompRowMatrix matrix) {
		int rows = matrix.numRows();
		for (int i = 0; i < rows; ++i) {
			SparseVector row = matrix.getRow(i);
			if (row.getUsed() == 0) continue;
			row.scale(1.0 / row.norm(Vector.Norm.One));
		}
	}
}
