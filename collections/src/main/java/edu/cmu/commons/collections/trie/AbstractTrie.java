package edu.cmu.commons.collections.trie;

import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import edu.cmu.commons.collections.trie.TrieIterator.Visitor;

/**
 * Base Trie implementation. Does not support <code>null</code> (or empty) key
 * sequences, but does support <code>null</code> values.
 * <p>
 * This implementation is tailored to applications where the values of a given
 * Trie are expected to be sparse. In other words, it is expected that many
 * paths within the Trie will not be associated with a value. To avoid the
 * memory overhead of an explicit (and often <code>null</code>) value field at
 * each TrieNode, values are supported via creation of dynamic TrieNode
 * {@link Proxy} instances which make use of {@link TrieNodeValueDecorator}s.
 * @author hazen
 * @param <E> Element type.
 * @param <V> Value type.
 */
abstract class AbstractTrie<E, V> implements Trie<E, V> {
	private static final long serialVersionUID = 1L;

	/**
	 * Root of the tree.
	 */
	private TrieNode<E, V> root;

	/**
	 * Interface of nodes. Used to create node proxies of the appropriate type.
	 */
	private Class<?> nodeType;

	/**
	 * Default ctor.
	 */
	public AbstractTrie() {
		root = createNode();
		nodeType =
				root instanceof NavigableTrieNode ? NavigableTrieNode.class
						: TrieNode.class;
	}

	/**
	 * @return a new TrieNode of the appropriate type.
	 */
	protected abstract TrieNode<E, V> createNode();

	@Override
	public TrieNode<E, V> getRoot() {
		return root;
	}

	@Override
	public boolean isEmpty() {
		return root.isEmpty();
	}

	@Override
	public int size() {
		return countValues(root);
	}

	/**
	 * @param node
	 * @return number of non-<code>null</code> values encoded by the given node
	 * and its children.
	 */
	protected int countValues(TrieNode<E, V> node) {
		int values = 0;
		if (node.hasValue()) ++values;
		for (TrieNode<E, V> child : node.values())
			values += countValues(child);
		return values;
	}

	@Override
	public void clear() {
		root.clear();
	}

	@Override
	public V put(List<E> key, V value) {
		if (key == null || key.isEmpty()) throw new IllegalArgumentException(
				"Null or empty key is unsupported");
		TrieNode<E, V> parent = null;
		TrieNode<E, V> node = root;
		for (E element : key) {
			TrieNode<E, V> child = node.get(element);
			if (child == null) {
				child = createNode();
				node.put(element, child);
			}
			parent = node;
			node = child;
		}
		if (!node.hasValue()) node =
				addValue(parent, key.get(key.size() - 1), node);
		return node.putValue(value);
	}

	@SuppressWarnings("unchecked")
	protected TrieNode<E, V> addValue(TrieNode<E, V> parent, E element,
			TrieNode<E, V> node) {
		assert !node.hasValue();
		TrieNode<E, V> valuable =
				(TrieNode<E, V>) Proxy.newProxyInstance(
						ClassLoader.getSystemClassLoader(), new Class<?>[] { nodeType },
						new TrieNodeValueDecorator<E, V>(node));
		parent.put(element, valuable);
		return valuable;
	}

	@SuppressWarnings("unchecked")
	protected TrieNode<E, V> removeValue(TrieNode<E, V> parent, E element,
			TrieNode<E, V> node) {
		assert Proxy.isProxyClass(node.getClass());
		TrieNodeInvocationHandler<E, V> handler =
				(TrieNodeInvocationHandler<E, V>) Proxy.getInvocationHandler(node);
		TrieNode<E, V> valueless = handler.getDelegate();
		parent.put(element, valueless);
		return valueless;
	}

	@Override
	public void putAll(Map<? extends List<E>, ? extends V> m) {
		for (Map.Entry<? extends List<E>, ? extends V> entry : m.entrySet())
			put(entry.getKey(), entry.getValue());
	}

	@Override
	public void merge(Trie<? extends E, ? extends V> trie) {
		merge(null, null, root, trie.getRoot());
	}

	/**
	 * Merges source into target. New child nodes are created as needed.
	 * @param source
	 * @param target
	 */
	protected <E2 extends E, V2 extends V> void merge(TrieNode<E, V> parent,
			E element, TrieNode<E, V> target, TrieNode<E2, V2> source) {
		if (source.hasValue()) {
			if (!target.hasValue()) target = addValue(parent, element, target);
			target.setValue(source.getValue());
		}
		for (Map.Entry<E2, TrieNode<E2, V2>> entry : source.entrySet()) {
			E sourceElement = entry.getKey();
			TrieNode<E2, V2> sourceChild = entry.getValue();
			TrieNode<E, V> targetChild = target.get(sourceElement);
			if (targetChild == null) {
				targetChild = createNode();
				target.put(sourceElement, targetChild);
			}
			merge(target, sourceElement, targetChild, sourceChild);
		}
	}

	@Override
	public TrieNode<E, V> getNode(List<E> key) {
		if (key == null || key.isEmpty()) throw new IllegalArgumentException(
				"Null or empty key is unsupported");
		TrieNode<E, V> node = root;
		for (E e : key) {
			if (node == null) return null;
			node = node.get(e);
		}
		return node;
	}

	@Override
	public List<TrieNode<E, V>> getPath(List<E> key) {
		if (key == null || key.isEmpty()) throw new IllegalArgumentException(
				"Null or empty key is unsupported");
		List<TrieNode<E, V>> path = new LinkedList<TrieNode<E, V>>();
		TrieNode<E, V> node = root;
		for (E e : key) {
			if (node == null) return null;
			path.add(node);
			node = node.get(e);
		}
		path.add(node);
		return path;
	}

	@Override
	@SuppressWarnings("unchecked")
	public V get(Object key) {
		TrieNode<E, V> node = getNode((List<E>) key);
		if (node == null || !node.hasValue()) return null;
		return node.getValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public V remove(Object o) {
		List<E> key = (List<E>) o;
		List<TrieNode<E, V>> path = getPath(key);
		if (path == null) return null;
		TrieNode<E, V> node = path.get(path.size() - 1);
		V rv = null;
		if (node.hasValue()) {
			rv = node.getValue();
			node =
					removeValue(path.get(path.size() - 2), key.get(key.size() - 1), node);
			path.set(path.size() - 1, node);
		}
		prune(key, path);
		return rv;
	}

	/**
	 * Prunes as much of the given path as is possible.
	 * @param key
	 * @param path
	 */
	protected void prune(List<E> key, List<TrieNode<E, V>> path) {
		// test for empty key/path
		if (key.isEmpty() || path.isEmpty()) return;

		// get iterators
		ListIterator<E> keyItr = key.listIterator(key.size());
		ListIterator<TrieNode<E, V>> pathItr = path.listIterator(path.size());

		// get terminal node
		TrieNode<E, V> node = pathItr.previous();

		// can't remove node because...
		if (node.hasValue() // it has a value
				|| !node.isEmpty() // it has children
		) return;

		// find node closest to root which we can prune
		E element = null;
		TrieNode<E, V> parent = null;
		while (keyItr.hasPrevious() && pathItr.hasPrevious()) {
			element = keyItr.previous();
			parent = pathItr.previous();
			if (parent.hasValue() || parent.size() > 1) break;
		}

		// remove node
		parent.remove(element);
	}

	@Override
	public boolean containsKey(Object key) {
		return get(key) != null;
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO implement this
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<List<E>> keySet() {
		// TODO implement this
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<V> values() {
		// TODO implement this
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Entry<List<E>, V>> entrySet() {
		// TODO implement this
		throw new UnsupportedOperationException();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((root == null) ? 0 : root.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		AbstractTrie<?, ?> other = (AbstractTrie<?, ?>) obj;
		if (root == null) {
			if (other.root != null) return false;
		} else if (!root.equals(other.root)) return false;
		return true;
	}

	@Override
	public TrieIterator<E, V> trieIterator() {
		return root.trieIterator();
	}

	@Override
	public TrieIterator<E, V> trieIterator(Visitor<E, V> visitor) {
		return root.trieIterator(visitor);
	}
}
