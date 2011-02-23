package edu.cmu.commons.collections;

import java.util.List;

/**
 * This class iteratively calculates k-variations (or "permutations") of the
 * elements of a List. A k-variation of a List L is simply a permuted sublist of
 * L of length k. For example, if you have a list such as <code>(a b)</code>,
 * all 2-variations of this list would include <code>(a b), (b a)</code>.
 * 
 * See Xavier Noria's Algorithm::Combinatorics Perl module for original
 * implementation.
 * 
 * @author Xavier Noria
 * @author Andy Schlaikjer
 */
public class ListVariationIterator<E> extends ListCombinationIterator<E> {
	private boolean[] used;

	public ListVariationIterator(List<E> data, int k) {
		super(data, k);

		// an array of booleans to keep track of which indices are in use
		used = new boolean[data.size()];
		for (int i = 0; i < used.length; ++i)
			used[i] = false;
	}

	protected void update_indices() {

		if (data.size() == tuple.length) {
			next_permutation();
			return;
		}

		int max_n = data.size() - 1;

		// for all tuple indices i, last to first
		int i = tuple.length;
		while (--i >= 0) {
			int n = tuple[i];
			used[n] = false;

			// find first unused data index above n
			while (++n <= max_n) {
				if (used[n]) continue;

				// if we get here we necessarily exit the subroutine, so forget about
				// the outer while and for loops

				tuple[i] = n;
				used[n] = true;

				// for all tuple indices above i
				while (++i < tuple.length) {

					// find first unused data index
					n = -1;
					while (++n <= max_n) {
						if (used[n]) continue;
						tuple[i] = n;
						used[n] = true;
						break;
					}
				}

				// done
				return;
			}
		}

		// all permutations have been returned
		has_next = false;
	}

	private void next_permutation() {
		int max_n = tuple.length - 1;

		// Find the element a(j) behind the longest decreasing tail
		int j;
		for (j = max_n - 1; j >= 0 && tuple[j] > tuple[j + 1]; --j);
		if (j < 0) {
			has_next = false;
			return;
		}

		// Find the rightmost element a(h) greater than a(j) and swap them
		int aj = tuple[j];
		int h;
		for (h = max_n; aj > tuple[h]; --h);
		swap(tuple, j, h);

		// Reverse the tail a(j+1)...a(max_n)
		for (++j, h = max_n; j < h; ++j, --h)
			swap(tuple, j, h);
	}

	private void swap(int[] tuple, int a, int b) {
		int n = tuple[a];
		tuple[a] = tuple[b];
		tuple[b] = n;
	}
}
