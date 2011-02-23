package edu.cmu.commons.collections.trie;

import java.util.NavigableMap;

/**
 * @author hazen
 * @param <E> Element type.
 * @param <V> Value type.
 */
public interface NavigableTrieNode<E, V> extends TrieNode<E, V>,
		NavigableMap<E, TrieNode<E, V>> {}
