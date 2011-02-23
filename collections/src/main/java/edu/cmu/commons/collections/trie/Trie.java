package edu.cmu.commons.collections.trie;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import edu.cmu.commons.collections.trie.TrieIterator.Visitor;

/**
 * Trie extends <code>Map&lt;List&lt;E&gt;,V&gt;</code> where keys are
 * represented in a compact data structure (key sequences share common
 * prefixes). Implementations may choose to support, <code>null</code> (or
 * empty) key sequences and/or <code>null</code> values.
 * @author hazen
 * @param <E> Element type.
 * @param <V> Value type.
 */
public interface Trie<E, V> extends Map<List<E>, V>, Serializable {
	/**
	 * @return the root TrieNode of this Trie.
	 */
	public TrieNode<E, V> getRoot();

	/**
	 * @param key
	 * @return the TrieNode associated with the given key, or <code>null</code> if
	 * no such node exists. The presence of a node for a given key is not
	 * indicative of the presence of a value for that key; It could be that the
	 * node exists to support some longer key sequence.
	 */
	public TrieNode<E, V> getNode(List<E> key);

	/**
	 * @param key
	 * @return path to the TrieNode associated with the given key, or
	 * <code>null</code> if no such node exists. The presence of a path for a
	 * given key is not indicative of the presence of a value for that key; It
	 * could be that the node exists to support some longer key sequence.
	 */
	public List<TrieNode<E, V>> getPath(List<E> key);

	/**
	 * @return a depth-first iterator over nodes in this Trie.
	 */
	public TrieIterator<E, V> trieIterator();

	/**
	 * @param visitor a Visitor which receives callbacks during Trie iteration.
	 * @return a depth-first iterator over nodes in this Trie.
	 */
	public TrieIterator<E, V> trieIterator(Visitor<E, V> visitor);

	/**
	 * Merges <code>source</code> into this Trie. New nodes are created within
	 * this Trie as needed to encode all keys in <code>source</code>. For keys
	 * which are present in both Tries, the value present in <code>source</code>
	 * supersedes any existing value.
	 * @param source
	 */
	public void merge(Trie<? extends E, ? extends V> source);
}
