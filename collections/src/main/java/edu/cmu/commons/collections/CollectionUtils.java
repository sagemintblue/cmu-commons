package edu.cmu.commons.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Basic utilities for Collections.
 * 
 * @author hazen
 */
public class CollectionUtils {
	private CollectionUtils() {}

	/**
	 * @param <T>
	 * @param elements
	 * @return a new {@code HashSet<T>} containing {@code elements}.
	 */
	public static <T> HashSet<T> hashSet(T... elements) {
		return new HashSet<T>(Arrays.asList(elements));
	}

	/**
	 * @param <T>
	 * @param elements
	 * @return a new {@code HashSet<T>} containing {@code elements}, or
	 * {@code null} if {@code elements} is empty.
	 */
	public static <T> HashSet<T> hashSetOrNull(T... elements) {
		if (elements.length == 0) return null;
		return hashSet(elements);
	}

	/**
	 * @param <T>
	 * @param elements
	 * @return a new {@code ArrayList<T>} containing {@code elements}.
	 */
	public static <T> ArrayList<T> arrayList(T... elements) {
		return new ArrayList<T>(Arrays.asList(elements));
	}

	/**
	 * @param <T>
	 * @param elements
	 * @return a new {@code ArrayList<T>} containing {@code elements}, or
	 * {@code null} if {@code elements} is empty.
	 */
	public static <T> ArrayList<T> arrayListOrNull(T... elements) {
		if (elements.length == 0) return null;
		return arrayList(elements);
	}

	/**
	 * @param <T>
	 * @param elements
	 * @return a mutable copy of {@code elements}.
	 */
	public static <T> T[] array(T... elements) {
		return Arrays.copyOf(elements, elements.length);
	}

	/**
	 * @param <T>
	 * @param elements
	 * @return a mutable copy of {@code elements}, or {@code null} if
	 * {@code elements} is empty.
	 */
	public static <T> T[] arrayOrNull(T... elements) {
		if (elements.length == 0) return null;
		return array(elements);
	}
}
