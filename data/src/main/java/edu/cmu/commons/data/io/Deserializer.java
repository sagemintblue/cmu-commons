package edu.cmu.commons.data.io;

import java.io.InputStream;

/**
 * Capable of populating an entity with data parsed from an InputStream.
 * @author hazen
 * @param <E>
 */
public interface Deserializer<E> {
	public E deserialize(InputStream in, E entity) throws Exception;
}
