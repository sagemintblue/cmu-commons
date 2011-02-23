package edu.cmu.commons.mtj.test;

import java.io.File;
import java.io.FileOutputStream;

import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.cmu.commons.mtj.io.MatrixWriter;
import edu.cmu.commons.mtj.io.TextMatrixWriter;
import edu.cmu.commons.mtj.util.MatrixFileConverter;
import edu.cmu.commons.util.app.Application;

public class MatrixFileConverterTest {

	private static File inputFile = null;

	@BeforeClass
	public static void createMatrixFile() throws Exception {
		inputFile = File.createTempFile("matrix-", ".dat");
		FlexCompRowMatrix matrix = new FlexCompRowMatrix(2, 2);
		matrix.set(0, 1, 1.0);
		matrix.set(1, 0, 1.0);
		MatrixWriter writer = new TextMatrixWriter(new FileOutputStream(
				inputFile));
		writer.writeMatrix(matrix);
		writer.close();
	}

	public File convertTextRowToBinaryRow() throws Exception {
		File outputFile = File.createTempFile("matrix-", ".dat");
		Application app = new MatrixFileConverter();
		app.setArguments(new String[] { "TEXT_COMPROW", inputFile.toString(),
				"BINARY_COMPROW", outputFile.toString() });
		app.run();
		return outputFile;
	}

	public File convertTextRowToBinaryCol() throws Exception {
		File outputFile = File.createTempFile("matrix-", ".dat");
		Application app = new MatrixFileConverter();
		app.setArguments(new String[] { "TEXT_COMPROW", inputFile.toString(),
				"BINARY_COMPCOL", outputFile.toString() });
		app.run();
		return outputFile;
	}

	public File convertBinaryRowToTextRow() throws Exception {
		File intputFile = convertTextRowToBinaryRow();
		File outputFile = File.createTempFile("matrix-", ".dat");
		Application app = new MatrixFileConverter();
		app.setArguments(new String[] { "BINARY_COMPROW",
				intputFile.toString(), "TEXT_COMPCOL", outputFile.toString() });
		app.run();
		return outputFile;
	}

	@Test
	public void testConvertTextRowToBinaryRow() throws Exception {
		convertTextRowToBinaryRow();
	}

	@Test
	public void testConvertTextRowToBinaryCol() throws Exception {
		convertTextRowToBinaryCol();
	}

	@Test
	public void testConvertBinaryRowToTextRow() throws Exception {
		convertBinaryRowToTextRow();
	}
}
