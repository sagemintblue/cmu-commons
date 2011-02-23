package edu.cmu.commons.data.io;

/**
 * Couples Serializer and Deserializer interfaces.
 * @author hazen
 * @param <E>
 */
public interface Serialization<E> extends Serializer<E>, Deserializer<E> {}
