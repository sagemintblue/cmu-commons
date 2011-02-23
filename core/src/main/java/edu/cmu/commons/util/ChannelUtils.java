package edu.cmu.commons.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Adapted from <a href=
 * "http://thomaswabner.wordpress.com/2007/10/09/fast-stream-copy-using-javanio-channels/"
 * >Fast stream copy using Java NIO channels</a>
 * 
 * @author hazen
 */
public final class ChannelUtils {
	/**
	 * Copies bytes from src to dest.
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void fastChannelCopy(final ReadableByteChannel src,
			final WritableByteChannel dest) throws IOException {
		final ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
		while (src.read(buffer) != -1) {
			// prepare the buffer to be drained
			buffer.flip();
			// write to the channel, may block
			dest.write(buffer);
			// If partial transfer, shift remainder down
			// If buffer is empty, same as doing clear()
			buffer.compact();
		}
		// EOF will leave buffer in fill state
		buffer.flip();
		// make sure the buffer is fully drained.
		while (buffer.hasRemaining()) {
			dest.write(buffer);
		}
	}
}