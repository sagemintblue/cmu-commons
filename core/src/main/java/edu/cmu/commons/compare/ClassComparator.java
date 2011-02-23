package edu.cmu.commons.compare;

import java.util.Comparator;

/**
 * A comparator capable of ordering two classes A and B based on assignability.
 * If A and B are the same Class instance, then they are considered equivalent.
 * Otherwise, if B is assignable to A, then A will be ordered before B.
 * Likewise, if A is assignable to B, then B will be ordered before A. If A and
 * B are not assignable in either direction, then they are considered
 * equivalent.
 * @author hazen
 * @see Class#isAssignableFrom(Class)
 */
public class ClassComparator implements Comparator<Class<?>> {
	@Override
	public int compare(Class<?> o1, Class<?> o2) {
		if (o1 == o2) return 0;
		if (o1 == null) return -1;
		if (o2 == null) return 1;
		if (o1.isAssignableFrom(o2)) return -1;
		if (o2.isAssignableFrom(o1)) return 1;
		return 0;
	}
}
