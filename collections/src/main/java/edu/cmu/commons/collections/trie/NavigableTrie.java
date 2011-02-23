package edu.cmu.commons.collections.trie;

import java.util.List;
import java.util.NavigableMap;

/**
 * @author hazen
 * @param <E> Element type.
 * @param <V> Value type.
 */
public interface NavigableTrie<E, V> extends Trie<E, V>,
		NavigableMap<List<E>, V> {
	/*
	 * (non-Javadoc)
	 * @see edu.cmu.commons.trie.Trie#getRoot()
	 */
	public NavigableTrieNode<E, V> getRoot();
}