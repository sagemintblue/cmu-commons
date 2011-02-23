package cmu.edu.commons.hadoop.tuple;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.hadoop.io.Writable;

import cmu.edu.commons.hadoop.tuple.schema.TupleSchema;

public interface BaseTuple<E extends Writable> extends Serializable, Writable {
	public TupleSchema<E> getSchema();

	public void setSchema(TupleSchema<E> schema);

	public E get(int index);

	public E set(int index, E element);

	public int size();

	public boolean isEmpty();

	public int indexOf(Object o);

	public int lastIndexOf(Object o);

	public boolean contains(Object o);

	public boolean containsAll(Collection<?> c);

	public Object[] toArray();

	public <A> A[] toArray(A[] a);

	/**
	 * Fills the tuple with <code>null</code>s; Tuple's size does not change.
	 * @see java.util.List#clear()
	 */
	public void clear();

	/**
	 * @return Iterator over unmodifiable view of tuple.
	 * @see java.util.List#iterator()
	 */
	public Iterator<E> iterator();

	/**
	 * @return ListIterator over unmodifiable view of tuple.
	 * @see java.util.List#iterator()
	 */
	public ListIterator<E> listIterator();

	/**
	 * @return ListIterator over unmodifiable view of tuple.
	 * @see java.util.List#iterator()
	 */
	public ListIterator<E> listIterator(int index);

	/**
	 * @return List is unmodifiable.
	 * @see java.util.List#iterator()
	 */
	public List<E> subList(int fromIndex, int toIndex);
}
