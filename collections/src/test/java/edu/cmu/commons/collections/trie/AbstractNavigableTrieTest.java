package edu.cmu.commons.collections.trie;

import java.util.List;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.junit.Test;

import edu.cmu.commons.collections.CharacterList;
import edu.cmu.commons.collections.trie.NavigableTrie;

/**
 * @author hazen
 */
public abstract class AbstractNavigableTrieTest<T extends NavigableTrie<Character, Integer>>
		extends AbstractTrieTest<T> {
	public abstract T createTrie();

	@Test
	public void firstEntry() {
		T trie = createTrie();
		List<Character> key1 = new CharacterList("abc");
		List<Character> key2 = new CharacterList("abd");
		trie.put(key1, 1);
		trie.put(key2, 2);
		Entry<List<Character>, Integer> entry = trie.firstEntry();
		Assert.assertNotNull(entry);
		Assert.assertEquals(key1, entry.getKey());
		Assert.assertEquals(1, (int) entry.getValue());
	}

	@Test
	public void lastEntry() {
		T trie = createTrie();
		List<Character> key1 = new CharacterList("abc");
		List<Character> key2 = new CharacterList("abd");
		trie.put(key1, 1);
		trie.put(key2, 2);
		Entry<List<Character>, Integer> entry = trie.lastEntry();
		Assert.assertNotNull(entry);
		Assert.assertEquals(key2, entry.getKey());
		Assert.assertEquals(2, (int) entry.getValue());
	}

	@Test
	public void pollFirstEntry() {
		T trie = createTrie();
		List<Character> key1 = new CharacterList("abc");
		List<Character> key2 = new CharacterList("abd");
		trie.put(key1, 1);
		trie.put(key2, 2);
		Entry<List<Character>, Integer> entry = trie.pollFirstEntry();
		Assert.assertNotNull(entry);
		Assert.assertEquals(key1, entry.getKey());
		Assert.assertEquals(1, (int) entry.getValue());
		Assert.assertEquals(1, trie.size());
	}

	@Test
	public void pollLastEntry() {
		T trie = createTrie();
		List<Character> key1 = new CharacterList("abc");
		List<Character> key2 = new CharacterList("abd");
		trie.put(key1, 1);
		trie.put(key2, 2);
		Entry<List<Character>, Integer> entry = trie.pollLastEntry();
		Assert.assertNotNull(entry);
		Assert.assertEquals(key2, entry.getKey());
		Assert.assertEquals(2, (int) entry.getValue());
		Assert.assertEquals(1, trie.size());
	}

	@Test
	public void floorEntry() {
		T trie = createTrie();
		List<Character> key = new CharacterList("2");
		trie.put(key, 1);
		Entry<List<Character>, Integer> entry = trie
				.floorEntry(new CharacterList("1"));
		Assert.assertNull(entry);
		entry = trie.floorEntry(new CharacterList("11"));
		Assert.assertNull(entry);
		entry = trie.floorEntry(new CharacterList("2"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key, entry.getKey());
		Assert.assertEquals(1, (int) entry.getValue());
		entry = trie.floorEntry(new CharacterList("22"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key, entry.getKey());
		Assert.assertEquals(1, (int) entry.getValue());
		entry = trie.floorEntry(new CharacterList("3"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key, entry.getKey());
		Assert.assertEquals(1, (int) entry.getValue());
	}

	@Test
	public void floorEntry2() {
		T trie = createTrie();
		List<Character> key = new CharacterList("22");
		trie.put(key, 1);
		Entry<List<Character>, Integer> entry = trie
				.floorEntry(new CharacterList("1"));
		Assert.assertNull(entry);
		entry = trie.floorEntry(new CharacterList("2"));
		Assert.assertNull(entry);
		entry = trie.floorEntry(new CharacterList("21"));
		Assert.assertNull(entry);
		entry = trie.floorEntry(new CharacterList("22"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key, entry.getKey());
		Assert.assertEquals(1, (int) entry.getValue());
		entry = trie.floorEntry(new CharacterList("23"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key, entry.getKey());
		Assert.assertEquals(1, (int) entry.getValue());
		entry = trie.floorEntry(new CharacterList("221"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key, entry.getKey());
		Assert.assertEquals(1, (int) entry.getValue());
		entry = trie.floorEntry(new CharacterList("3"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key, entry.getKey());
		Assert.assertEquals(1, (int) entry.getValue());
	}

	@Test
	public void floorEntry3() {
		T trie = createTrie();
		List<Character> key1 = new CharacterList("22");
		List<Character> key2 = new CharacterList("222");
		trie.put(key1, 1);
		trie.put(key2, 2);
		Entry<List<Character>, Integer> entry = trie
				.floorEntry(new CharacterList("1"));
		Assert.assertNull(entry);
		entry = trie.floorEntry(new CharacterList("2"));
		Assert.assertNull(entry);
		entry = trie.floorEntry(new CharacterList("21"));
		Assert.assertNull(entry);
		entry = trie.floorEntry(new CharacterList("22"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key1, entry.getKey());
		Assert.assertEquals(1, (int) entry.getValue());
		entry = trie.floorEntry(new CharacterList("221"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key1, entry.getKey());
		Assert.assertEquals(1, (int) entry.getValue());
		entry = trie.floorEntry(new CharacterList("222"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key2, entry.getKey());
		Assert.assertEquals(2, (int) entry.getValue());
		entry = trie.floorEntry(new CharacterList("2221"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key2, entry.getKey());
		Assert.assertEquals(2, (int) entry.getValue());
		entry = trie.floorEntry(new CharacterList("223"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key2, entry.getKey());
		Assert.assertEquals(2, (int) entry.getValue());
		entry = trie.floorEntry(new CharacterList("23"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key2, entry.getKey());
		Assert.assertEquals(2, (int) entry.getValue());
		entry = trie.floorEntry(new CharacterList("3"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key2, entry.getKey());
		Assert.assertEquals(2, (int) entry.getValue());
	}

	@Test
	public void ceilingEntry() {
		T trie = createTrie();
		List<Character> key = new CharacterList("2");
		trie.put(key, 1);
		Entry<List<Character>, Integer> entry = trie
				.ceilingEntry(new CharacterList("1"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key, entry.getKey());
		Assert.assertEquals(1, (int) entry.getValue());
		entry = trie.ceilingEntry(new CharacterList("11"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key, entry.getKey());
		Assert.assertEquals(1, (int) entry.getValue());
		entry = trie.ceilingEntry(new CharacterList("2"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key, entry.getKey());
		Assert.assertEquals(1, (int) entry.getValue());
		entry = trie.ceilingEntry(new CharacterList("22"));
		Assert.assertNull(entry);
		entry = trie.ceilingEntry(new CharacterList("3"));
		Assert.assertNull(entry);
	}

	@Test
	public void ceilingEntry2() {
		T trie = createTrie();
		List<Character> key = new CharacterList("22");
		trie.put(key, 1);
		Entry<List<Character>, Integer> entry = trie
				.ceilingEntry(new CharacterList("1"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key, entry.getKey());
		Assert.assertEquals(1, (int) entry.getValue());
		entry = trie.ceilingEntry(new CharacterList("2"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key, entry.getKey());
		Assert.assertEquals(1, (int) entry.getValue());
		entry = trie.ceilingEntry(new CharacterList("21"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key, entry.getKey());
		Assert.assertEquals(1, (int) entry.getValue());
		entry = trie.ceilingEntry(new CharacterList("22"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key, entry.getKey());
		Assert.assertEquals(1, (int) entry.getValue());
		entry = trie.ceilingEntry(new CharacterList("23"));
		Assert.assertNull(entry);
		entry = trie.ceilingEntry(new CharacterList("221"));
		Assert.assertNull(entry);
		entry = trie.ceilingEntry(new CharacterList("3"));
		Assert.assertNull(entry);
	}

	@Test
	public void ceilingEntry3() {
		T trie = createTrie();
		List<Character> key1 = new CharacterList("22");
		List<Character> key2 = new CharacterList("222");
		trie.put(key1, 1);
		trie.put(key2, 2);
		Entry<List<Character>, Integer> entry = trie
				.ceilingEntry(new CharacterList("21"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key1, entry.getKey());
		Assert.assertEquals(1, (int) entry.getValue());
		entry = trie.ceilingEntry(new CharacterList("22"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key1, entry.getKey());
		Assert.assertEquals(1, (int) entry.getValue());
		entry = trie.ceilingEntry(new CharacterList("221"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key2, entry.getKey());
		Assert.assertEquals(2, (int) entry.getValue());
		entry = trie.ceilingEntry(new CharacterList("222"));
		Assert.assertNotNull(entry);
		Assert.assertEquals(key2, entry.getKey());
		Assert.assertEquals(2, (int) entry.getValue());
		entry = trie.ceilingEntry(new CharacterList("2221"));
		Assert.assertNull(entry);
		entry = trie.ceilingEntry(new CharacterList("223"));
		Assert.assertNull(entry);
		entry = trie.ceilingEntry(new CharacterList("23"));
		Assert.assertNull(entry);
		entry = trie.ceilingEntry(new CharacterList("3"));
		Assert.assertNull(entry);
	}
}
