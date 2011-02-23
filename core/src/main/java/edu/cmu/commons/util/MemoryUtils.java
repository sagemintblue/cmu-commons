package edu.cmu.commons.util;

/**
 * Tracks memory usage by the JVM.
 * @author hazen
 */
public class MemoryUtils {
	/**
	 * @return percent memory use.
	 */
	public static double getUsage() {
		Runtime runtime = Runtime.getRuntime();
		long maxB = runtime.maxMemory();
		long allocB = runtime.totalMemory();
		long usedB = allocB - runtime.freeMemory();
		return (((double) usedB) / maxB) * 100;
	}

	/**
	 * Constant for calculating megabytes from bytes.
	 */
	static final long megaBytes = 1 << 20;

	/**
	 * @return a formatted message containing memory usage statistics appropriate
	 * for debug logging.
	 */
	public static String getStatus() {
		Runtime runtime = Runtime.getRuntime();
		long maxB = runtime.maxMemory();
		long allocB = runtime.totalMemory();
		long usedB = allocB - runtime.freeMemory();
		long usedPercent = usedB * 100 / maxB;
		long allocPercent = allocB * 100 / maxB;
		double maxMB = maxB / megaBytes;
		double allocMB = allocB / megaBytes;
		double usedMB = usedB / megaBytes;
		return String.format(
				"memory (MB) used: %.0f (%d%%) alloc: %.0f (%d%%) max: %.0f", usedMB,
				usedPercent, allocMB, allocPercent, maxMB);
	}
}
