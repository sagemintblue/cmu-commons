package edu.cmu.commons.compare;

import java.util.Comparator;

import edu.cmu.commons.collections.Pair;

/**
 * Facilitates comparing pairs with one another whose key and value types are
 * both comparable.
 * @param <First>
 * @param <Second>
 */
public class PairComparator<First extends Comparable<? super First>, Second extends Comparable<? super Second>>
		implements Comparator<Pair<First, Second>> {
	@Override
	public int compare(Pair<First, Second> p1, Pair<First, Second> p2) {
		if (p1 == p2) return 0;
		if (p1 == null) return -1;
		if (p2 == null) return 1;
		if (p1.getFirst() == null) {
			if (p2.getFirst() != null) return -1;
		} else {
			int c = p1.getFirst().compareTo(p2.getFirst());
			if (c != 0) return c;
		}
		if (p1.getSecond() == null && p2.getSecond() != null) return -1;
		return p1.getSecond().compareTo(p2.getSecond());
	}
}