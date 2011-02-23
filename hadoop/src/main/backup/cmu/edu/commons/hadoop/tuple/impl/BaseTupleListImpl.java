package cmu.edu.commons.hadoop.tuple.impl;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.hadoop.io.Writable;

import cmu.edu.commons.hadoop.tuple.BaseTupleList;
import cmu.edu.commons.hadoop.tuple.schema.TupleSchema;

public abstract class BaseTupleListImpl<E extends Writable> extends
		BaseTupleImpl<E> implements BaseTupleList<E> {
	private static final long serialVersionUID = 1L;

	public BaseTupleListImpl() {
		super();
	}

	public BaseTupleListImpl(E... elements) {
		super(elements);
	}

	@Override
	public void clear() {
		elements.clear();
	}

	@Override
	public Iterator<E> iterator() {
		return elements.iterator();
	}

	@Override
	public ListIterator<E> listIterator() {
		return elements.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return elements.listIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return elements.subList(fromIndex, toIndex);
	}

	@Override
	public boolean add(E element) {
		return elements.add(element);

	}

	@Override
	public void add(int index, E element) {
		elements.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return elements.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return elements.addAll(index, c);
	}

	@Override
	public E remove(int index) {
		return elements.remove(index);
	}

	@Override
	public boolean remove(Object o) {
		return elements.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return elements.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return elements.retainAll(c);
	}

	@Override
	protected int readFieldsWithSchema(DataInput in, TupleSchema<E> schema)
			throws IOException {
		int size = super.readFieldsWithSchema(in, schema);
		if (size < elements.size()) elements =
				new ArrayList<E>(elements.subList(0, size));
		return size;
	}
}
