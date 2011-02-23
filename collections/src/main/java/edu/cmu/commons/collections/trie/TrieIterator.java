package edu.cmu.commons.collections.trie;

import java.util.Iterator;
import java.util.Map.Entry;

/**
 * A depth-first iterator for {@link Trie}s.
 * @author hazen
 * @param <E>
 * @param <V>
 */
public interface TrieIterator<E, V> extends Iterator<Entry<E, TrieNode<E, V>>> {
	/**
	 * A class which encapsulates various call-backs which may occur during
	 * iteration over a Trie.
	 * @author hazen
	 * @param <E>
	 * @param <V>
	 */
	public static interface Visitor<E, V> {
		/**
		 * Handler called when TrieIterator reaches given node.
		 * @param element
		 * @param node
		 */
		public void visit(E element, TrieNode<E, V> node);

		/**
		 * Handler called when TrieIterator begins processing a parent node's
		 * children.
		 */
		public void descend();

		/**
		 * Handler called when TrieIterator finishes processing a parent node's
		 * children.
		 */
		public void ascend();
	}

	/**
	 * @return <code>true</code> if a subsequent call to {@link #next()} will
	 * return a valid value, <code>false</code> otherwise.
	 */
	@Override
	public boolean hasNext();

	/**
	 * @return the next (depth-first) entry within the Trie being iterated over.
	 */
	@Override
	public Entry<E, TrieNode<E, V>> next();

	/**
	 * Causes the current node's children to be skipped over during iteration. A
	 * subsequent call to {@link #next()} will return the next sibling (if any) of
	 * the current node within the Trie. This method, along with
	 * {@link #skipSiblings()}, may only be called once after a call to
	 * {@link #next()}. Subsequent calls will cause an
	 * {@link IllegalStateException} to be thrown.
	 */
	public void skipChildren();

	/**
	 * Causes the current node's children and remaining sibling nodes to be
	 * skipped over during iteration. A subsequent call to {@link #next()} will
	 * return the first node of the next branch (if any) within the Trie. This
	 * method, along with {@link #skipChildren()}, may only be called once after a
	 * call to {@link #next()}. Subsequent calls will cause an
	 * {@link IllegalStateException} to be thrown.
	 */
	public void skipSiblings();
}
