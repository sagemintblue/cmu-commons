package edu.cmu.commons.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import edu.cmu.commons.collections.ListCombinationIterator;

public class ListCombinationIteratorTest {

	private static final List<String> strings = new ArrayList<String>(Arrays
			.asList(new String[] { "a", "b", "c" }));

	public <E> void printCombinations(List<E> list, int k) {
		Iterator<List<E>> listCombinationIterator = new ListCombinationIterator<E>(
				list, k);
		int i = 0;
		while (listCombinationIterator.hasNext()) {
			List<E> combination = listCombinationIterator.next();
			System.err.print(++i + ".");
			for (E element : combination)
				System.err.print(" " + element.toString());
			System.err.println();
		}
	}

	@Test
	public void testOne() {
		printCombinations(strings, 1);
	}

	@Test
	public void testTwo() {
		printCombinations(strings, 2);
	}

	@Test
	public void testThree() {
		printCombinations(strings, 3);
	}

}
