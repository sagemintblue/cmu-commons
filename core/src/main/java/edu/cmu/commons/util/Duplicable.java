package edu.cmu.commons.util;

/**
 * Refinement of {@link Cloneable} interface which enforces existence, exception
 * specification, and return type of clone method.
 * 
 * @author hazen
 * @param <T>
 */
public interface Duplicable<T> extends Cloneable {
	public T clone();
}
