package edu.cmu.commons.collections.trie;

import java.util.HashMap;

/**
 * TrieNode implementation using HashMap internally.
 * @author hazen
 * @param <E>
 * @param <V>
 */
public class HashTrieNode<E, V> extends
		AbstractTrieNode<E, V, HashMap<E, TrieNode<E, V>>> implements
		TrieNode<E, V> {
	private static final long serialVersionUID = 1L;

	public HashTrieNode() {
		super(new HashMap<E, TrieNode<E, V>>());
	}

	public HashTrieNode(int initialCapacity) {
		super(new HashMap<E, TrieNode<E, V>>(initialCapacity));
	}

	public HashTrieNode(int initialCapacity, float loadFactor) {
		super(new HashMap<E, TrieNode<E, V>>(initialCapacity, loadFactor));
	}
}
