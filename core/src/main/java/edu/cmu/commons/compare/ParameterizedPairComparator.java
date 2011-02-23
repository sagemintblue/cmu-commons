package edu.cmu.commons.compare;

import java.util.Comparator;

import edu.cmu.commons.collections.Pair;

/**
 * Facilitates comparing pairs with one another using custom Comparators for
 * First and Second types.
 * @param <First>
 * @param <Second>
 */
public class ParameterizedPairComparator<First, Second> implements
		Comparator<Pair<First, Second>> {
	private Comparator<First> firstComparator;
	private Comparator<Second> secondComparator;

	/**
	 * @param firstComparator if null, Pair first values are not used for
	 * comparison.
	 * @param secondComparator if null, Pair second values are not used for
	 * comparison.
	 */
	public ParameterizedPairComparator(Comparator<First> firstComparator,
			Comparator<Second> secondComparator) {
		super();
		this.firstComparator = firstComparator;
		this.secondComparator = secondComparator;
	}

	@Override
	public int compare(Pair<First, Second> p1, Pair<First, Second> p2) {
		if (p1 == p2) return 0;
		if (p1 == null) return -1;
		if (p2 == null) return 1;
		if (firstComparator != null) {
			int c = firstComparator.compare(p1.getFirst(), p2.getFirst());
			if (c != 0) return c;
		}
		if (secondComparator != null) return secondComparator.compare(
				p1.getSecond(), p2.getSecond());
		return 0;
	}
}
