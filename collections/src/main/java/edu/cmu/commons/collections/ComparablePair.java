package edu.cmu.commons.collections;

import java.io.Serializable;

/**
 * Templated pair whose types are required to be comparable.
 * @param <First> type of the first object in this pair.
 * @param <Second> type of the second object in this pair.
 */
public class ComparablePair<First extends Comparable<? extends First>, Second extends Comparable<? extends Second>>
		implements Serializable, Comparable<ComparablePair<First, Second>> {
	private static final long serialVersionUID = 1L;

	private First first;
	private Second second;

	public ComparablePair(First first, Second second) {
		this.first = first;
		this.second = second;
	}

	public ComparablePair() {
		this(null, null);
	}

	public First getFirst() {
		return first;
	}

	public void setFirst(First first) {
		this.first = first;
	}

	public Second getSecond() {
		return second;
	}

	public void setSecond(Second second) {
		this.second = second;
	}

	@Override
	public String toString() {
		return "(" + getFirst() + ", " + getSecond() + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result =
				prime * result + ((getFirst() == null) ? 0 : getFirst().hashCode());
		result =
				prime * result + ((getSecond() == null) ? 0 : getSecond().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof ComparablePair<?, ?>)) return false;
		ComparablePair<?, ?> other = (ComparablePair<?, ?>) obj;
		if (getFirst() == null) {
			if (other.getFirst() != null) return false;
		} else if (!getFirst().equals(other.getFirst())) return false;
		if (getSecond() == null) {
			if (other.getSecond() != null) return false;
		} else if (!getSecond().equals(other.getSecond())) return false;
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public int compareTo(ComparablePair<First, Second> other) {
		if (this == other) return 0;
		if (other == null) return 1;
		if (getFirst() == null) {
			if (other.getFirst() != null) return -1;
		} else {
			int rv = ((Comparable<First>) getFirst()).compareTo(other.getFirst());
			if (rv != 0) return rv;
		}
		if (getSecond() == null && other.getSecond() != null) return -1;
		return ((Comparable<Second>) getSecond()).compareTo(other.getSecond());
	}
}
