package edu.cmu.commons.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import edu.cmu.commons.collections.ListVariationIterator;

public class ListVariationIteratorTest {

	private static final List<String> strings = new ArrayList<String>(Arrays
			.asList(new String[] { "a", "b", "c" }));

	public <E> void printVariations(List<E> list, int k) {
		Iterator<List<E>> listVariationIterator = new ListVariationIterator<E>(
				list, k);
		int i = 0;
		while (listVariationIterator.hasNext()) {
			List<E> combination = listVariationIterator.next();
			System.err.print(++i + ".");
			for (E element : combination)
				System.err.print(" " + element.toString());
			System.err.println();
		}
	}

	@Test
	public void testOne() {
		printVariations(strings, 1);
	}

	@Test
	public void testTwo() {
		printVariations(strings, 2);
	}

	@Test
	public void testThree() {
		printVariations(strings, 3);
	}

}
