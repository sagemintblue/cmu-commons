package edu.cmu.commons.collections.trie;

import java.lang.reflect.InvocationHandler;

/**
 * InvocationHandler used when proxying {@link TrieNode}s.
 * @author hazen
 * @param <E> Element type.
 * @param <V> Value type.
 */
public interface TrieNodeInvocationHandler<E, V> extends InvocationHandler {
	public TrieNode<E, V> getDelegate();
}
