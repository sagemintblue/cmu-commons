package edu.cmu.commons.collections;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import junit.framework.Assert;

import org.junit.Test;

import edu.cmu.commons.collections.CharacterList;

public class CharacterListTest {
	@Test
	public void basicTest() {
		List<Character> list = new CharacterList("abc");
		Assert.assertEquals(3, list.size());
		Assert.assertEquals('a', (char) list.get(0));
		Assert.assertEquals('b', (char) list.get(1));
		Assert.assertEquals('c', (char) list.get(2));
	}

	@Test
	public void iteratorTest() {
		List<Character> list = new CharacterList("abc");
		char[] chars = new char[] { 'a', 'b', 'c' };
		int i = 0;
		for (Character e : list) {
			char c = chars[i++];
			Assert.assertEquals(c, (char) e);
		}
	}

	@Test
	public void listIteratorTest() {
		List<Character> list = new CharacterList("abc");
		ListIterator<Character> itr = list.listIterator();
		Assert.assertTrue(itr.hasNext());
		Assert.assertFalse(itr.hasPrevious());
		Assert.assertEquals(0, itr.nextIndex());
		Assert.assertEquals(-1, itr.previousIndex());
		Character c = itr.next();
		Assert.assertNotNull(c);
		Assert.assertEquals('a', (char) c);
		Assert.assertTrue(itr.hasNext());
		Assert.assertTrue(itr.hasPrevious());
		Assert.assertEquals(1, itr.nextIndex());
		Assert.assertEquals(0, itr.previousIndex());
		c = itr.next();
		Assert.assertNotNull(c);
		Assert.assertEquals('b', (char) c);
		Assert.assertTrue(itr.hasNext());
		Assert.assertTrue(itr.hasPrevious());
		Assert.assertEquals(2, itr.nextIndex());
		Assert.assertEquals(1, itr.previousIndex());
		c = itr.next();
		Assert.assertNotNull(c);
		Assert.assertEquals('c', (char) c);
		Assert.assertFalse(itr.hasNext());
		Assert.assertTrue(itr.hasPrevious());
		Assert.assertEquals(3, itr.nextIndex());
		Assert.assertEquals(2, itr.previousIndex());
		c = itr.previous();
		Assert.assertNotNull(c);
		Assert.assertEquals('c', (char) c);
		Assert.assertTrue(itr.hasNext());
		Assert.assertTrue(itr.hasPrevious());
		Assert.assertEquals(2, itr.nextIndex());
		Assert.assertEquals(1, itr.previousIndex());
	}

	@Test
	public void listIteratorTest2() {
		List<Character> list = new CharacterList("abc");
		ListIterator<Character> itr = list.listIterator(1);
		Assert.assertTrue(itr.hasNext());
		Assert.assertTrue(itr.hasPrevious());
		Assert.assertEquals(1, itr.nextIndex());
		Assert.assertEquals(0, itr.previousIndex());
		Character c = itr.next();
		Assert.assertNotNull(c);
		Assert.assertEquals('b', (char) c);
		Assert.assertTrue(itr.hasNext());
		Assert.assertTrue(itr.hasPrevious());
		Assert.assertEquals(2, itr.nextIndex());
		Assert.assertEquals(1, itr.previousIndex());
		c = itr.next();
		Assert.assertNotNull(c);
		Assert.assertEquals('c', (char) c);
		Assert.assertFalse(itr.hasNext());
		Assert.assertTrue(itr.hasPrevious());
		Assert.assertEquals(3, itr.nextIndex());
		Assert.assertEquals(2, itr.previousIndex());
		c = itr.previous();
		Assert.assertNotNull(c);
		Assert.assertEquals('c', (char) c);
		Assert.assertTrue(itr.hasNext());
		Assert.assertTrue(itr.hasPrevious());
		Assert.assertEquals(2, itr.nextIndex());
		Assert.assertEquals(1, itr.previousIndex());
	}

	@Test
	public void iteratorRemoveTest() {
		List<Character> list = new CharacterList("abc");
		Iterator<Character> itr = list.iterator();
		itr.next();
		itr.remove();
		Assert.assertEquals("[b, c]", list.toString());
	}

	@Test
	public void iteratorInsertTest() {
		List<Character> list = new CharacterList("bc");
		ListIterator<Character> itr = list.listIterator();
		itr.next();
		itr.add('a');
		Assert.assertEquals("[a, b, c]", list.toString());
	}
}
