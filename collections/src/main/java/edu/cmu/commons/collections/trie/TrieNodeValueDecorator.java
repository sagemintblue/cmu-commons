package edu.cmu.commons.collections.trie;

import java.lang.reflect.Method;

/**
 * TrieNodeInvocationHandler which supports TrieNode value property.
 * @author hazen
 * @param <E>
 * @param <V>
 */
public class TrieNodeValueDecorator<E, V> implements
		TrieNodeInvocationHandler<E, V> {

	private TrieNode<E, V> delegate;
	private V value;

	public TrieNodeValueDecorator(TrieNode<E, V> delegate) {
		this.delegate = delegate;
	}

	public TrieNodeValueDecorator(TrieNode<E, V> delegate, V value) {
		this.delegate = delegate;
		this.value = value;
	}

	@Override
	public TrieNode<E, V> getDelegate() {
		return delegate;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		String name = method.getName();
		if (name.equals("hasValue")) return true;
		if (name.equals("getValue")) return value;
		if (name.equals("setValue")) {
			value = (V) args[0];
			return null;
		}
		if (name.equals("putValue")) {
			V rv = value;
			value = (V) args[0];
			return rv;
		}
		return method.invoke(getDelegate(), args);
	}
}
