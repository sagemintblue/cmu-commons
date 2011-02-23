package edu.cmu.commons.collections.trie;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import edu.cmu.commons.collections.trie.TrieIterator.Visitor;

/**
 * Base TrieNode implementation. Does not support value property. Derived types
 * (or Proxies) must support value property.
 * @author hazen
 * @param <E> Element type.
 * @param <V> Value type.
 * @param <N> Node type.
 * @param <M> Type of the Map used internally to keep track of child nodes.
 * @see HashTrieNode
 * @see TreeTrieNode
 * @see TrieNodeValueDecorator
 */
abstract class AbstractTrieNode<E, V, M extends Map<E, TrieNode<E, V>>>
		implements TrieNode<E, V> {
	private static final long serialVersionUID = 1L;

	/**
	 * Base implementation of TrieIterator.
	 * @author hazen
	 */
	private class TrieIteratorImpl implements TrieIterator<E, V> {
		private Visitor<E, V> visitor;
		private Iterator<Entry<E, TrieNode<E, V>>> iterator;
		private Deque<Iterator<Entry<E, TrieNode<E, V>>>> iterators =
				new LinkedList<Iterator<Entry<E, TrieNode<E, V>>>>();
		private boolean descended = false;
		private boolean childrenSkipped = false;
		private boolean siblingsSkipped = false;

		public TrieIteratorImpl(TrieNode<E, V> node) {
			this.iterator = node.entrySet().iterator();
		}

		public TrieIteratorImpl(TrieNode<E, V> node, Visitor<E, V> visitor) {
			this(node);
			this.visitor = visitor;
		}

		@Override
		public boolean hasNext() {
			if (iterator == null || !iterator.hasNext()) popIterator();
			return iterator != null && iterator.hasNext();
		}

		private void popIterator() {
			descended = false;
			iterator = iterators.poll();
			if (iterator != null && visitor != null) visitor.ascend();
		}

		private void pushIterator(Iterator<Entry<E, TrieNode<E, V>>> iterator) {
			iterators.push(this.iterator);
			this.iterator = iterator;
			descended = true;
			if (visitor != null) visitor.descend();
		}

		@Override
		public Entry<E, TrieNode<E, V>> next() {
			if (!hasNext()) throw new NoSuchElementException();
			descended = false;
			Entry<E, TrieNode<E, V>> entry = iterator.next();
			TrieNode<E, V> node = entry.getValue();
			if (visitor != null) visitor.visit(entry.getKey(), node);
			if (!node.isEmpty()) pushIterator(node.entrySet().iterator());
			childrenSkipped = false;
			siblingsSkipped = false;
			return entry;
		}

		@Override
		public void remove() {
			iterator.remove();
		}

		@Override
		public void skipChildren() {
			if (childrenSkipped) throw new IllegalStateException();
			childrenSkipped = true;
			if (descended) popIterator();
		}

		@Override
		public void skipSiblings() {
			if (siblingsSkipped) throw new IllegalStateException();
			siblingsSkipped = true;
			if (!childrenSkipped) skipChildren();
			popIterator();
		}
	}

	/**
	 * child nodes.
	 */
	protected M children;

	public AbstractTrieNode(M children) {
		this.children = children;
	}

	/**
	 * @return <code>false</code>, always.
	 * @see edu.cmu.commons.collections.trie.TrieNode#hasValue()
	 */
	@Override
	public boolean hasValue() {
		return false;
	}

	/**
	 * @throws UnsupportedOperationException
	 * @see edu.cmu.commons.collections.trie.TrieNode#getValue()
	 */
	@Override
	public V getValue() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 * @see edu.cmu.commons.collections.trie.TrieNode#setValue(Object)
	 */
	@Override
	public void setValue(V value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 * @see edu.cmu.commons.collections.trie.TrieNode#putValue(Object)
	 */
	@Override
	public V putValue(V value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result =
				prime * result
						+ (!hasValue() || getValue() == null ? 0 : getValue().hashCode());
		result = prime * result + ((children == null) ? 0 : children.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		AbstractTrieNode<?, ?, ?> other = (AbstractTrieNode<?, ?, ?>) obj;
		if (!hasValue()) {
			if (other.hasValue()) return false;
		} else {
			if (!other.hasValue()) return false;
			if (getValue() == null) {
				if (other.getValue() != null) return false;
			} else if (!getValue().equals(other.getValue())) return false;
		}
		if (children == null) {
			if (other.children != null) return false;
		} else if (!children.equals(other.children)) return false;
		return true;
	}

	@Override
	public void clear() {
		children.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return children.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return children.containsValue(value);
	}

	@Override
	public Set<Map.Entry<E, TrieNode<E, V>>> entrySet() {
		return children.entrySet();
	}

	@Override
	public TrieNode<E, V> get(Object key) {
		return children.get(key);
	}

	@Override
	public boolean isEmpty() {
		return children.isEmpty();
	}

	@Override
	public Set<E> keySet() {
		return children.keySet();
	}

	@Override
	public TrieNode<E, V> put(E key, TrieNode<E, V> value) {
		return children.put(key, value);
	}

	@Override
	public void putAll(Map<? extends E, ? extends TrieNode<E, V>> m) {
		children.putAll(m);
	}

	@Override
	public TrieNode<E, V> remove(Object key) {
		return children.remove(key);
	}

	@Override
	public int size() {
		return children.size();
	}

	@Override
	public Collection<TrieNode<E, V>> values() {
		return children.values();
	}

	@Override
	public TrieIterator<E, V> trieIterator() {
		return new TrieIteratorImpl(this);
	}

	@Override
	public TrieIterator<E, V> trieIterator(Visitor<E, V> visitor) {
		return new TrieIteratorImpl(this, visitor);
	}
}
