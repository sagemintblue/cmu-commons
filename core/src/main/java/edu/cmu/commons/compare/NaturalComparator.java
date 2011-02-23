package edu.cmu.commons.compare;

import java.util.Comparator;

/**
 * A Comparator which relies on the Comparable implementation of the target
 * type.
 * @param <T>
 */
public class NaturalComparator<T extends Comparable<? super T>> implements
		Comparator<T> {
	@Override
	public int compare(T o1, T o2) {
		if (o1 == o2) return 0;
		if (o1 == null) return -1;
		return o1.compareTo(o2);
	}
}
