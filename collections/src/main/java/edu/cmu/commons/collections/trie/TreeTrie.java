package edu.cmu.commons.collections.trie;

import java.util.Comparator;

/**
 * Trie implementation using TreeTrieNodes internally.
 * @author hazen
 * @param <E> Element type.
 * @param <V> Value type.
 */
public class TreeTrie<E, V> extends AbstractNavigableTrie<E, V> {
	private static final long serialVersionUID = 1L;

	private Comparator<? super E> comparator;

	public TreeTrie() {
		super();
	}

	public TreeTrie(Comparator<? super E> comparator) {
		super();
		this.comparator = comparator;
	}

	@Override
	protected TreeTrieNode<E, V> createNode() {
		if (comparator != null) return new TreeTrieNode<E, V>(comparator);
		return new TreeTrieNode<E, V>();
	}
}
