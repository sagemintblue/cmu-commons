package edu.cmu.commons.collections.trie;

import java.util.Comparator;
import java.util.TreeMap;

/**
 * NavigableTrieNode implementation using TreeMap internally.
 * @author hazen
 * @param <E>
 * @param <V>
 */
public class TreeTrieNode<E, V> extends
		AbstractNavigableTrieNode<E, V, TreeMap<E, TrieNode<E, V>>> implements
		TrieNode<E, V> {
	private static final long serialVersionUID = 1L;

	public TreeTrieNode() {
		super(new TreeMap<E, TrieNode<E, V>>());
	}

	public TreeTrieNode(Comparator<? super E> comparator) {
		super(new TreeMap<E, TrieNode<E, V>>(comparator));
	}
}
