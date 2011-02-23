package edu.cmu.commons.collections.trie;

import java.util.Map;

import edu.cmu.commons.collections.trie.TrieIterator.Visitor;

/**
 * Basic contract for nodes used to build Tries.
 * @author hazen
 * @param <E> Element type.
 * @param <V> Value type.
 */
public interface TrieNode<E, V> extends Map<E, TrieNode<E, V>> {
	/**
	 * @return {@code true} if this node has some value (possibly {@code null}),
	 * {@code false} otherwise.
	 */
	public boolean hasValue();

	/**
	 * @return the value associated with this node.
	 */
	public V getValue();

	/**
	 * @param value the value to associate with this node.
	 */
	public void setValue(V value);

	/**
	 * Sets the value of this node and returns any existing value.
	 * @param value
	 * @return existing value.
	 */
	public V putValue(V value);

	/**
	 * @return a TrieIterator.
	 */
	public TrieIterator<E, V> trieIterator();

	/**
	 * @param visitor
	 * @return a TrieIterator.
	 */
	public TrieIterator<E, V> trieIterator(Visitor<E, V> visitor);
}
