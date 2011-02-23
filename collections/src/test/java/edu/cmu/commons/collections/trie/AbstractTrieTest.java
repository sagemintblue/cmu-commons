package edu.cmu.commons.collections.trie;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import edu.cmu.commons.collections.CharacterList;
import edu.cmu.commons.collections.trie.Trie;
import edu.cmu.commons.collections.trie.TrieNode;

/**
 * @author hazen
 */
public abstract class AbstractTrieTest<T extends Trie<Character, Integer>> {
	public abstract T createTrie();

	@Test
	public void create() {
		T trie = createTrie();
		Assert.assertNotNull(trie);
		Assert.assertTrue(trie.isEmpty());
		Assert.assertEquals(0, trie.size());
		Assert.assertNotNull(trie.getRoot());
	}

	@Test
	public void put() {
		T trie = createTrie();
		List<Character> key = new CharacterList("abc");
		trie.put(key, 1);
		Assert.assertFalse(trie.isEmpty());
		Assert.assertEquals(1, trie.size());
		TrieNode<Character, Integer> node = trie.getRoot();
		Assert.assertNotNull(node);
		TrieNode<Character, Integer> child = node.get('a');
		Assert.assertNotNull(child);
		node = child;
		child = node.get('b');
		Assert.assertNotNull(child);
		node = child;
		child = node.get('c');
		Assert.assertNotNull(child);
		Assert.assertNotNull(child.getValue());
	}

	@Test
	public void put2Overlapping() {
		T trie = createTrie();
		List<Character> key1 = new CharacterList("ab");
		List<Character> key2 = new CharacterList("abc");
		trie.put(key1, 1);
		trie.put(key2, 2);
		Assert.assertEquals(1, (int) trie.get(key1));
		Assert.assertEquals(2, (int) trie.get(key2));
		TrieNode<Character, Integer> node = trie.getRoot();
		Assert.assertNotNull(node);
		TrieNode<Character, Integer> child = node.get('a');
		Assert.assertNotNull(child);
		node = child;
		child = node.get('b');
		Assert.assertNotNull(child);
		node = child;
		Assert.assertNotNull(node.getValue());
		Assert.assertEquals(1, node.size());
		Assert.assertTrue(node.containsKey('c'));
	}

	@Test
	public void put2() {
		T trie = createTrie();
		List<Character> key1 = new CharacterList("abc");
		List<Character> key2 = new CharacterList("abd");
		trie.put(key1, 1);
		trie.put(key2, 2);
		Assert.assertEquals(1, (int) trie.get(key1));
		Assert.assertEquals(2, (int) trie.get(key2));
		TrieNode<Character, Integer> node = trie.getRoot();
		Assert.assertNotNull(node);
		TrieNode<Character, Integer> child = node.get('a');
		Assert.assertNotNull(child);
		node = child;
		child = node.get('b');
		Assert.assertNotNull(child);
		node = child;
		Assert.assertEquals(2, node.size());
		Assert.assertTrue(node.containsKey('c'));
		Assert.assertTrue(node.containsKey('d'));
	}

	@Test
	public void get() {
		T trie = createTrie();
		List<Character> key = new CharacterList("abc");
		trie.put(key, 1);
		Integer value = trie.get(key);
		Assert.assertEquals(1, (int) value);
	}

	@Test
	public void remove() {
		T trie = createTrie();
		List<Character> key = new CharacterList("abc");
		trie.put(key, 1);
		Integer value = trie.remove(key);
		Assert.assertNotNull(value);
		Assert.assertEquals(1, (int) value);
		Assert.assertTrue(trie.isEmpty());
	}

	@Test
	public void remove2() {
		T trie = createTrie();
		List<Character> key1 = new CharacterList("abc");
		List<Character> key2 = new CharacterList("abd");
		trie.put(key1, 1);
		trie.put(key2, 2);
		Assert.assertEquals(2, (int) trie.remove(key2));
		Assert.assertFalse(trie.isEmpty());
		Assert.assertEquals(1, trie.size());
		Assert.assertEquals(1, (int) trie.get(key1));
	}

	@Test
	public void merge() {
		T trie1 = createTrie();
		T trie2 = createTrie();
		List<Character> key1 = new CharacterList("abc");
		List<Character> key2 = new CharacterList("abd");
		trie1.put(key1, 1);
		trie2.put(key2, 2);
		trie1.merge(trie2);
		Assert.assertEquals(1, (int) trie1.get(key1));
		Assert.assertEquals(2, (int) trie1.get(key2));
		TrieNode<Character, Integer> node = trie1.getRoot();
		Assert.assertNotNull(node);
		TrieNode<Character, Integer> child = node.get('a');
		Assert.assertNotNull(child);
		node = child;
		child = node.get('b');
		Assert.assertNotNull(child);
		node = child;
		Assert.assertEquals(2, node.size());
		Assert.assertTrue(node.containsKey('c'));
		Assert.assertTrue(node.containsKey('d'));
	}
}
