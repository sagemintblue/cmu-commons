package edu.cmu.commons.collections.trie;

/**
 * Trie implementation using HashTrieNodes internally.
 * @author hazen
 * @param <E> Element type.
 * @param <V> Value type.
 */
public class HashTrie<E, V> extends AbstractTrie<E, V> implements Trie<E, V> {
	private static final long serialVersionUID = 1L;

	private Integer defaultInitialCapacity;
	private Float defaultLoadFactor;

	public HashTrie() {
		super();
	}

	public HashTrie(int defaultInitialCapacity) {
		super();
		this.defaultInitialCapacity = defaultInitialCapacity;
	}

	public HashTrie(int defaultInitialCapacity, float defaultLoadFactor) {
		super();
		this.defaultInitialCapacity = defaultInitialCapacity;
		this.defaultLoadFactor = defaultLoadFactor;
	}

	@Override
	protected HashTrieNode<E, V> createNode() {
		if (defaultInitialCapacity != null) {
			if (defaultLoadFactor != null) {
				return new HashTrieNode<E, V>(defaultInitialCapacity, defaultLoadFactor);
			} else {
				return new HashTrieNode<E, V>(defaultInitialCapacity);
			}
		}
		return new HashTrieNode<E, V>();
	}
}
