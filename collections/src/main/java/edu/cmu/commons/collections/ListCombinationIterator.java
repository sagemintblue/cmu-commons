package edu.cmu.commons.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class iteratively calculates k-combinations of the elements of a List. A
 * k-combination of a List L is simply a sublist of L of length k whose order
 * follows the lexicographic ordering of the original list. For example, if you
 * have a list such as <code>(a b c)</code>, all 2-combinations of this list
 * would include <code>(a b), (a c), (b c)</code>.
 * 
 * See Xavier Noria's Algorithm::Combinatorics Perl module for original
 * implementation.
 * 
 * @author Xavier Noria
 * @author Andy Schlaikjer
 */
public class ListCombinationIterator<E> implements Iterator<List<E>> {
	protected ArrayList<E> data;
	protected int[] tuple;
	protected boolean has_next;

	public ListCombinationIterator(List<E> data, int k) {
		if (k < 0) throw new IllegalArgumentException("Argument k (" + k
				+ ") must be positive integer");
		if (k > data.size()) throw new IllegalArgumentException("Argument k (" + k
				+ ") must be less than or equal to data list size (" + data.size()
				+ ")");

		// copy input List to ArrayList to ensure fast random access to members
		// of data
		this.data = new ArrayList<E>(data);

		// an array of indices into data list which we initialize to identity
		// mapping
		tuple = new int[k];
		for (int i = 0; i < tuple.length; i++)
			tuple[i] = i;

		// base case flags
		has_next = true;
	}

	public boolean hasNext() {
		return has_next;
	}

	public List<E> next() throws NoSuchElementException {
		if (!has_next) throw new NoSuchElementException();

		// handle zero-length tuple case
		if (tuple.length == 0) {
			has_next = false;
			return Collections.emptyList();
		}

		// create k-tuple from current indices
		List<E> list = new ArrayList<E>(tuple.length);
		for (int i = 0; i < tuple.length; i++)
			list.add(data.get(tuple[i]));

		// update the indices
		update_indices();

		return list;
	}

	protected void update_indices() {
		int offset = data.size() - tuple.length;
		int i = tuple.length;
		while (--i >= 0) {
			int n = tuple[i];
			if (n < i + offset) {
				tuple[i] = ++n;
				int j = i;
				while (++j < tuple.length)
					tuple[j] = ++n;
				return;
			}
		}
		has_next = false;
	}

	public void remove() throws UnsupportedOperationException,
			IllegalStateException {
		throw new UnsupportedOperationException();
	}
}
