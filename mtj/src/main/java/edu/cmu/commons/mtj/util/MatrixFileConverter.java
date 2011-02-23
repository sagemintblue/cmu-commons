package edu.cmu.commons.mtj.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.sparse.FlexCompColMatrix;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cmu.commons.mtj.io.BinaryMatrixReader;
import edu.cmu.commons.mtj.io.BinaryMatrixWriter;
import edu.cmu.commons.mtj.io.MatrixReader;
import edu.cmu.commons.mtj.io.MatrixWriter;
import edu.cmu.commons.mtj.io.TextMatrixReader;
import edu.cmu.commons.mtj.io.TextMatrixWriter;
import edu.cmu.commons.util.app.Application;
import edu.cmu.commons.util.app.UsageException;

/**
 * Utility for manipulating matrix files.
 */
public class MatrixFileConverter extends Application {

	public static enum Format {
		BINARY_DENSE, BINARY_COMPROW, BINARY_COMPCOL, TEXT_DENSE, TEXT_COMPROW, TEXT_COMPCOL;

		static final Set<Format> binaryFormats = new HashSet<Format>(Arrays
				.asList(new Format[] { BINARY_DENSE, BINARY_COMPROW,
						BINARY_COMPCOL }));

		static final Set<Format> textFormats = new HashSet<Format>(
				Arrays.asList(new Format[] { TEXT_DENSE, TEXT_COMPROW,
						TEXT_COMPCOL }));

		static final Set<Format> rowFormats = new HashSet<Format>(Arrays
				.asList(new Format[] { BINARY_COMPROW, TEXT_COMPROW }));

		static final Set<Format> columnFormats = new HashSet<Format>(Arrays
				.asList(new Format[] { BINARY_COMPCOL, TEXT_COMPCOL }));

	}

	private static final Logger log = LoggerFactory
			.getLogger(MatrixFileConverter.class);

	@Override
	public int run() throws UsageException, Exception {

		String[] args = getArguments();
		if (args.length < 1)
			throw new UsageException(
					"Required argument 'inputFormat' is undefined");
		Format inputFormat = Format.valueOf(args[0]);
		if (args.length < 2)
			throw new UsageException(
					"Required argument 'inputFile' is undefined");
		File inputFile = new File(args[1]);
		if (args.length < 3)
			throw new UsageException(
					"Required argument 'outputFormat' is undefined");
		Format outputFormat = Format.valueOf(args[2]);
		if (args.length < 4)
			throw new UsageException(
					"Required argument 'outputFile' is undefined");
		File outputFile = new File(args[3]);

		if (inputFormat == outputFormat)
			throw new UsageException(
					"Arguments 'inputFormat' and 'outputFormat' are equal");

		InputStream is = new FileInputStream(inputFile);
		OutputStream os = new FileOutputStream(outputFile);

		MatrixReader reader = null;
		if (Format.binaryFormats.contains(inputFormat)) {
			reader = new BinaryMatrixReader(is);
		} else {
			reader = new TextMatrixReader(is);
		}

		MatrixWriter writer = null;
		if (Format.binaryFormats.contains(outputFormat)) {
			writer = new BinaryMatrixWriter(os);
		} else {
			writer = new TextMatrixWriter(os);
		}

		log.info("Reading matrix in " + inputFormat + " format from file '"
				+ inputFile + "'");

		Matrix matrix = reader.readMatrix();
		reader.close();

		log.info("Matrix loaded");

		boolean inputIsRowMajor = Format.rowFormats.contains(inputFormat);
		boolean outputIsRowMajor = Format.rowFormats.contains(outputFormat);
		if (inputIsRowMajor != outputIsRowMajor) {
			if (inputIsRowMajor) {
				log.info("Converting matrix from row- to column-major");
				matrix = new FlexCompColMatrix(matrix);
			} else {
				log.info("Converting matrix from column- to row-major");
				matrix = new FlexCompRowMatrix(matrix);
			}
		}

		log.info("Writing matrix in " + outputFormat + " format to file '"
				+ outputFile + "'");

		writer.writeMatrix(matrix);
		writer.close();

		log.info("Matrix saved");

		return 0;
	}
}
