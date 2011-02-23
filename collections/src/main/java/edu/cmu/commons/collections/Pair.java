package edu.cmu.commons.collections;

import java.io.Serializable;

/**
 * Simple pair.
 * @param <First> type of the first object in this pair.
 * @param <Second> type of the second object in this pair.
 */
public class Pair<First, Second> implements Serializable {
	private static final long serialVersionUID = 1L;

	private First first;
	private Second second;

	public Pair(First first, Second second) {
		this.first = first;
		this.second = second;
	}

	public Pair() {
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
		if (!(obj instanceof Pair<?, ?>)) return false;
		Pair<?, ?> other = (Pair<?, ?>) obj;
		if (getFirst() == null) {
			if (other.getFirst() != null) return false;
		} else if (!getFirst().equals(other.getFirst())) return false;
		if (getSecond() == null) {
			if (other.getSecond() != null) return false;
		} else if (!getSecond().equals(other.getSecond())) return false;
		return true;
	}
}
