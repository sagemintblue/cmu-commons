package edu.cmu.commons.data.io;

import java.io.OutputStream;

/**
 * Capable of writing data from an entity to an OutputStream.
 * @author hazen
 * @param <E>
 */
public interface Serializer<E> {
	public void serialize(E entity, OutputStream out) throws Exception;
}
