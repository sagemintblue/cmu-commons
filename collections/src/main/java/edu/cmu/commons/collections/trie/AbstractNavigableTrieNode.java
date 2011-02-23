package edu.cmu.commons.collections.trie;

import java.util.Comparator;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedMap;

/**
 * Refines AbstractTrieNode to support NavigableTrieNode interface.
 * @author hazen
 * @param <E>
 * @param <V>
 * @param <M>
 */
abstract class AbstractNavigableTrieNode<E, V, M extends NavigableMap<E, TrieNode<E, V>>>
		extends AbstractTrieNode<E, V, M> implements NavigableTrieNode<E, V> {
	private static final long serialVersionUID = 1L;

	public AbstractNavigableTrieNode(M children) {
		super(children);
	}

	@Override
	public Entry<E, TrieNode<E, V>> ceilingEntry(E key) {
		return children.ceilingEntry(key);
	}

	@Override
	public E ceilingKey(E key) {
		return children.ceilingKey(key);
	}

	@Override
	public Comparator<? super E> comparator() {
		return children.comparator();
	}

	@Override
	public NavigableSet<E> descendingKeySet() {
		return children.descendingKeySet();
	}

	@Override
	public NavigableMap<E, TrieNode<E, V>> descendingMap() {
		return children.descendingMap();
	}

	@Override
	public Entry<E, TrieNode<E, V>> firstEntry() {
		return children.firstEntry();
	}

	@Override
	public E firstKey() {
		return children.firstKey();
	}

	@Override
	public Entry<E, TrieNode<E, V>> floorEntry(E key) {
		return children.floorEntry(key);
	}

	@Override
	public E floorKey(E key) {
		return children.floorKey(key);
	}

	@Override
	public NavigableMap<E, TrieNode<E, V>> headMap(E toKey, boolean inclusive) {
		return children.headMap(toKey, inclusive);
	}

	@Override
	public SortedMap<E, TrieNode<E, V>> headMap(E toKey) {
		return children.headMap(toKey);
	}

	@Override
	public Entry<E, TrieNode<E, V>> higherEntry(E key) {
		return children.higherEntry(key);
	}

	@Override
	public E higherKey(E key) {
		return children.higherKey(key);
	}

	@Override
	public Entry<E, TrieNode<E, V>> lastEntry() {
		return children.lastEntry();
	}

	@Override
	public E lastKey() {
		return children.lastKey();
	}

	@Override
	public Entry<E, TrieNode<E, V>> lowerEntry(E key) {
		return children.lowerEntry(key);
	}

	@Override
	public E lowerKey(E key) {
		return children.lowerKey(key);
	}

	@Override
	public NavigableSet<E> navigableKeySet() {
		return children.navigableKeySet();
	}

	@Override
	public Entry<E, TrieNode<E, V>> pollFirstEntry() {
		return children.pollFirstEntry();
	}

	@Override
	public Entry<E, TrieNode<E, V>> pollLastEntry() {
		return children.pollLastEntry();
	}

	@Override
	public NavigableMap<E, TrieNode<E, V>> subMap(E fromKey,
			boolean fromInclusive, E toKey, boolean toInclusive) {
		return children.subMap(fromKey, fromInclusive, toKey, toInclusive);
	}

	@Override
	public SortedMap<E, TrieNode<E, V>> subMap(E fromKey, E toKey) {
		return children.subMap(fromKey, toKey);
	}

	@Override
	public NavigableMap<E, TrieNode<E, V>> tailMap(E fromKey, boolean inclusive) {
		return children.tailMap(fromKey, inclusive);
	}

	@Override
	public SortedMap<E, TrieNode<E, V>> tailMap(E fromKey) {
		return children.tailMap(fromKey);
	}
}
