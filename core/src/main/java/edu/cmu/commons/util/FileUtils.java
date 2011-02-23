package edu.cmu.commons.util;

import java.io.File;
import java.io.IOException;

/**
 * Collection of file utilities.
 * @author hazen
 */
public class FileUtils {
	/**
	 * @param prefix
	 * @return File referencing new temporary directory.
	 * @throws IOException
	 */
	public static File createTempDirectory(String prefix) throws IOException {
		final File temp =
				File.createTempFile(prefix, Long.toString(System.nanoTime()));
		if (!temp.delete()) throw new IOException("Failed to delete temp file '"
				+ temp.getAbsolutePath() + "'");
		if (!temp.mkdir()) throw new IOException(
				"Failed to create temp directory '" + temp.getAbsolutePath() + "'");
		return (temp);
	}

	/**
	 * Deletes each file if it exists and has zero length.
	 * @param files
	 */
	public static void deleteIfEmpty(File... files) {
		for (File file : files)
			if (file.exists() && file.length() == 0) file.delete();
	}

	/**
	 * Recursively deletes the given path.
	 * @param path
	 * @throws Exception
	 */
	public static void delete(File path) {
		if (path.isDirectory()) for (File child : path.listFiles())
			delete(child);
		path.delete();
	}
}
