package edu.cmu.commons.collections.trie;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedMap;

/**
 * Base NavigableTrie implementation.
 * 
 * @author hazen
 * @param <E>
 *            Element type.
 * @param <V>
 *            Value type.
 */
abstract class AbstractNavigableTrie<E, V> extends AbstractTrie<E, V> implements
		NavigableTrie<E, V> {
	private static final long serialVersionUID = 1L;

	private class EntryImpl implements Entry<List<E>, V> {
		private List<E> key;
		private V value;

		public EntryImpl(List<E> key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public List<E> getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			return put(key, value);
		}
	}

	private interface NavigableTrieNodeAccessor<E, V> {
		public Entry<E, TrieNode<E, V>> get(NavigableTrieNode<E, V> node);
	}

	private class FirstEntryAccessor implements NavigableTrieNodeAccessor<E, V> {
		@Override
		public Entry<E, TrieNode<E, V>> get(NavigableTrieNode<E, V> node) {
			return node.firstEntry();
		}
	}

	private class LastEntryAccessor implements NavigableTrieNodeAccessor<E, V> {
		@Override
		public Entry<E, TrieNode<E, V>> get(NavigableTrieNode<E, V> node) {
			return node.lastEntry();
		}
	}

	private interface NavigableTrieNodeKeyAccessor<E, V> {
		public Entry<E, TrieNode<E, V>> getInclusive(
				NavigableTrieNode<E, V> node, E key);

		public Entry<E, TrieNode<E, V>> getExclusive(
				NavigableTrieNode<E, V> node, E key);

		public Entry<E, TrieNode<E, V>> descend(NavigableTrieNode<E, V> node);

		public boolean isNextClosestDescendantOfExactMatch();
	}

	private class FloorEntryAccessor implements
			NavigableTrieNodeKeyAccessor<E, V> {
		@Override
		public Entry<E, TrieNode<E, V>> getInclusive(
				NavigableTrieNode<E, V> node, E key) {
			return node.floorEntry(key);
		}

		@Override
		public Entry<E, TrieNode<E, V>> getExclusive(
				NavigableTrieNode<E, V> node, E key) {
			return node.lowerEntry(key);
		}

		@Override
		public Entry<E, TrieNode<E, V>> descend(NavigableTrieNode<E, V> node) {
			return node.lastEntry();
		}

		@Override
		public boolean isNextClosestDescendantOfExactMatch() {
			return false;
		}
	}

	private class CeilingEntryAccessor implements
			NavigableTrieNodeKeyAccessor<E, V> {
		@Override
		public Entry<E, TrieNode<E, V>> getInclusive(
				NavigableTrieNode<E, V> node, E key) {
			return node.ceilingEntry(key);
		}

		@Override
		public Entry<E, TrieNode<E, V>> getExclusive(
				NavigableTrieNode<E, V> node, E key) {
			return node.higherEntry(key);
		}

		@Override
		public Entry<E, TrieNode<E, V>> descend(NavigableTrieNode<E, V> node) {
			return node.firstEntry();
		}

		@Override
		public boolean isNextClosestDescendantOfExactMatch() {
			return true;
		}
	}

	public AbstractNavigableTrie() {
		super();
	}

	@Override
	protected abstract NavigableTrieNode<E, V> createNode();

	@Override
	public NavigableTrieNode<E, V> getRoot() {
		return (NavigableTrieNode<E, V>) super.getRoot();
	}

	private Entry<List<E>, V> getEntry(NavigableTrieNodeAccessor<E, V> accessor) {
		if (isEmpty())
			return null;
		List<E> key = new ArrayList<E>();
		NavigableTrieNode<E, V> node = getRoot();
		while (node != null) {
			Entry<E, TrieNode<E, V>> entry = accessor.get(node);
			if (entry == null)
				break;
			key.add(entry.getKey());
			node = (NavigableTrieNode<E, V>) entry.getValue();
			if (node.hasValue())
				break;
		}
		assert node.hasValue();
		return new EntryImpl(key, node.getValue());
	}

	private Entry<List<E>, V> pollEntry(NavigableTrieNodeAccessor<E, V> accessor) {
		if (isEmpty())
			return null;
		List<E> key = new ArrayList<E>();
		List<TrieNode<E, V>> path = new ArrayList<TrieNode<E, V>>();
		NavigableTrieNode<E, V> node = getRoot();
		path.add(node);
		while (node != null) {
			Entry<E, TrieNode<E, V>> entry = accessor.get(node);
			if (entry == null)
				break;
			key.add(entry.getKey());
			node = (NavigableTrieNode<E, V>) entry.getValue();
			path.add(node);
			if (node.hasValue())
				break;
		}
		assert path.size() > 1;
		assert node.hasValue();
		V value = node.getValue();
		node = (NavigableTrieNode<E, V>) removeValue(path.get(path.size() - 2),
				key.get(key.size() - 1), node);
		path.set(path.size() - 1, node);
		prune(key, path);
		return new EntryImpl(key, value);
	}

	private Entry<List<E>, V> getKeyEntryInclusive(List<E> key,
			NavigableTrieNodeKeyAccessor<E, V> accessor) {
		if (isEmpty())
			return null;
		if (key == null || key.isEmpty())
			return null;

		ListIterator<E> keyItr = key.listIterator();
		NavigableTrieNode<E, V> node = getRoot();
		LinkedList<E> keyPath = new LinkedList<E>();
		LinkedList<NavigableTrieNode<E, V>> nodePath = new LinkedList<NavigableTrieNode<E, V>>();
		nodePath.push(node);

		// attempt to find node associated with target key, keeping track of
		// match path in case backtracking is necessary to find next closest
		// entry
		E keyElement = null;
		while (keyItr.hasNext()) {
			keyElement = keyItr.next();
			Entry<E, TrieNode<E, V>> entry = accessor.getInclusive(node,
					keyElement);
			if (entry == null) {
				// no "closest" node exists at this point in trie; we must
				// backtrack to find next closest node
				node = null;
				break;
			}

			// match found; update paths
			E element = entry.getKey();
			keyPath.push(element);
			node = (NavigableTrieNode<E, V>) entry.getValue();
			nodePath.push(node);

			if (!element.equals(keyElement)) {
				// match is not exact-- next closest is descendant; query
				// accessor for behavior
				if (accessor.isNextClosestDescendantOfExactMatch()) {
					// descend until first node with value is found
					while (!node.hasValue() && !node.isEmpty()) {
						entry = accessor.descend(node);
						keyPath.push(entry.getKey());
						node = (NavigableTrieNode<E, V>) entry.getValue();
					}
				} else {
					// descend to leaf
					while (!node.isEmpty()) {
						entry = accessor.descend(node);
						keyPath.push(entry.getKey());
						node = (NavigableTrieNode<E, V>) entry.getValue();
					}
				}
				assert node.hasValue();
				return new EntryImpl(keyPath, node.getValue());
			}
		}

		if (node != null) {
			// exact match succeeded; test for node value
			if (node.hasValue()) {
				// node has value; return entry
				return new EntryImpl(keyPath, node.getValue());
			}

			// node has no value; query accessor for behavior
			if (accessor.isNextClosestDescendantOfExactMatch()) {
				// next closest key may be found below exact match node; descend
				while (!node.hasValue() && !node.isEmpty()) {
					Entry<E, TrieNode<E, V>> entry = accessor.descend(node);
					keyPath.push(entry.getKey());
					node = (NavigableTrieNode<E, V>) entry.getValue();
				}
				assert node.hasValue();
				return new EntryImpl(keyPath, node.getValue());
			}

			// must backtrack to find next closest entry
		}

		// match failed; backtrack to find next closest entry
		keyItr.previous();
		node = nodePath.poll();
		if (node.hasValue() && !accessor.isNextClosestDescendantOfExactMatch()) {
			// node has value and next closest node may lie on partial match
			// path; return entry
			return new EntryImpl(keyPath, node.getValue());
		}
		keyPath.poll();
		Entry<E, TrieNode<E, V>> entry = null;
		while (keyItr.hasPrevious() && !nodePath.isEmpty()) {
			keyElement = keyItr.previous();
			node = nodePath.pop();
			if (node.hasValue()
					&& !accessor.isNextClosestDescendantOfExactMatch()) {
				// node has value and next closest node may lie on partial match
				// path; return entry
				return new EntryImpl(keyPath, node.getValue());
			}
			entry = accessor.getExclusive(node, keyElement);
			if (entry != null) {
				keyPath.push(entry.getKey());
				break;
			}
			keyPath.poll();
		}

		if (entry == null) {
			// no valid entry found; return null
			return null;
		}

		// found node in path which leads to next closest entry; query accessor
		// for behavior
		node = (NavigableTrieNode<E, V>) entry.getValue();
		if (accessor.isNextClosestDescendantOfExactMatch()) {
			while (!node.hasValue() && !node.isEmpty()) {
				entry = accessor.descend(node);
				keyPath.push(entry.getKey());
				node = (NavigableTrieNode<E, V>) entry.getValue();
			}
		} else {
			while (!node.isEmpty()) {
				entry = accessor.descend(node);
				keyPath.push(entry.getKey());
				node = (NavigableTrieNode<E, V>) entry.getValue();
			}
		}
		assert node.hasValue();
		return new EntryImpl(keyPath, node.getValue());
	}

	private Entry<List<E>, V> getKeyEntryExclusive(List<E> key,
			NavigableTrieNodeKeyAccessor<E, V> accessor) {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public Entry<List<E>, V> firstEntry() {
		return getEntry(new FirstEntryAccessor());
	}

	@Override
	public Entry<List<E>, V> lastEntry() {
		return getEntry(new LastEntryAccessor());
	}

	@Override
	public List<E> firstKey() {
		Entry<List<E>, V> entry = firstEntry();
		if (entry == null)
			return null;
		return entry.getKey();
	}

	@Override
	public List<E> lastKey() {
		Entry<List<E>, V> entry = lastEntry();
		if (entry == null)
			return null;
		return entry.getKey();
	}

	@Override
	public Entry<List<E>, V> pollFirstEntry() {
		return pollEntry(new FirstEntryAccessor());
	}

	@Override
	public Entry<List<E>, V> pollLastEntry() {
		return pollEntry(new LastEntryAccessor());
	}

	@Override
	public Entry<List<E>, V> floorEntry(List<E> key) {
		return getKeyEntryInclusive(key, new FloorEntryAccessor());
	}

	@Override
	public Entry<List<E>, V> ceilingEntry(List<E> key) {
		return getKeyEntryInclusive(key, new CeilingEntryAccessor());
	}

	@Override
	public List<E> floorKey(List<E> key) {
		Entry<List<E>, V> entry = getKeyEntryInclusive(key,
				new FloorEntryAccessor());
		if (entry == null)
			return null;
		return entry.getKey();
	}

	@Override
	public List<E> ceilingKey(List<E> key) {
		Entry<List<E>, V> entry = getKeyEntryInclusive(key,
				new CeilingEntryAccessor());
		if (entry == null)
			return null;
		return entry.getKey();
	}

	@Override
	public Entry<List<E>, V> lowerEntry(List<E> key) {
		return getKeyEntryExclusive(key, new FloorEntryAccessor());
	}

	@Override
	public Entry<List<E>, V> higherEntry(List<E> key) {
		return getKeyEntryExclusive(key, new CeilingEntryAccessor());
	}

	@Override
	public List<E> lowerKey(List<E> key) {
		Entry<List<E>, V> entry = lowerEntry(key);
		if (entry == null)
			return null;
		return entry.getKey();
	}

	@Override
	public List<E> higherKey(List<E> key) {
		Entry<List<E>, V> entry = higherEntry(key);
		if (entry == null)
			return null;
		return entry.getKey();
	}

	@Override
	public SortedMap<List<E>, V> headMap(List<E> toKey) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public NavigableMap<List<E>, V> headMap(List<E> toKey, boolean inclusive) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public SortedMap<List<E>, V> tailMap(List<E> fromKey) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public NavigableMap<List<E>, V> tailMap(List<E> fromKey, boolean inclusive) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public SortedMap<List<E>, V> subMap(List<E> fromKey, List<E> toKey) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public NavigableMap<List<E>, V> subMap(List<E> fromKey,
			boolean fromInclusive, List<E> toKey, boolean toInclusive) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public NavigableMap<List<E>, V> descendingMap() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public NavigableSet<List<E>> descendingKeySet() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public NavigableSet<List<E>> navigableKeySet() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public Comparator<? super List<E>> comparator() {
		return new Comparator<List<E>>() {
			private Comparator<? super E> elementComparator = getRoot()
					.comparator();

			@Override
			public int compare(List<E> o1, List<E> o2) {
				if (o1 == null) {
					if (o2 != null)
						return -1;
				} else {
					if (o2 == null)
						return 1;
					Iterator<E> itr1 = o1.iterator();
					Iterator<E> itr2 = o2.iterator();
					while (itr1.hasNext()) {
						E e1 = itr1.next();
						if (!itr2.hasNext())
							return 1;
						E e2 = itr2.next();
						int c = elementComparator.compare(e1, e2);
						if (c != 0)
							return c;
					}
					if (itr2.hasNext())
						return -1;
				}
				return 0;
			}
		};
	}
}
