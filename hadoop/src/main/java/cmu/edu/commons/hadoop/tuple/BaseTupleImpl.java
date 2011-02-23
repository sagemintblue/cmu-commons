package cmu.edu.commons.hadoop.tuple;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.hadoop.io.Writable;

import cmu.edu.commons.hadoop.tuple.schema.TupleSchema;

public abstract class BaseTupleImpl<E extends Writable> implements BaseTuple<E> {
	private static final long serialVersionUID = 1L;

	protected List<E> elements;

	private transient TupleSchema<E> schema;

	public BaseTupleImpl() {
		elements = new ArrayList<E>();
	}

	public BaseTupleImpl(E... elements) {
		this();
		this.elements.addAll(Arrays.asList(elements));
	}

	@Override
	public TupleSchema<E> getSchema() {
		return schema;
	}

	@Override
	public void setSchema(TupleSchema<E> schema) {
		this.schema = schema;
		if (schema != null) schema.initialize(this);
	}

	@Override
	public int hashCode() {
		return elements.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return elements.equals(o);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		if (!elements.isEmpty()) {
			Iterator<E> itr = elements.iterator();
			sb.append(itr.next());
			while (itr.hasNext())
				sb.append(", ").append(itr.next());
		}
		return sb.append("]").toString();
	}

	@Override
	public E get(int index) {
		return elements.get(index);
	}

	@Override
	public E set(int index, E element) {
		return elements.set(index, element);
	}

	@Override
	public int size() {
		return elements.size();
	}

	@Override
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	@Override
	public int indexOf(Object o) {
		return elements.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return elements.lastIndexOf(o);
	}

	@Override
	public boolean contains(Object o) {
		return elements.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return elements.containsAll(c);
	}

	@Override
	public Object[] toArray() {
		return elements.toArray();
	}

	@Override
	public <A> A[] toArray(A[] a) {
		return elements.toArray(a);
	}

	@Override
	public void clear() {
		Collections.fill(elements, null);
	}

	@Override
	public Iterator<E> iterator() {
		return Collections.unmodifiableList(elements).iterator();
	}

	@Override
	public ListIterator<E> listIterator() {
		return Collections.unmodifiableList(elements).listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return Collections.unmodifiableList(elements).listIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return Collections.unmodifiableList(elements).subList(fromIndex, toIndex);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		for (Writable element : elements) {
			if (element == null) throw new IllegalStateException(
					"Cannot serialize null element");
			element.write(out);
		}
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		if (schema != null) {
			readFieldsWithSchema(in, schema);
			return;
		}
		for (Writable element : elements)
			element.readFields(in);
	}

	protected int readFieldsWithSchema(DataInput in, TupleSchema<E> schema)
			throws IOException {
		int i = 0;
		for (; schema.initialize(this, i); ++i) {
			Writable element = elements.get(i);
			if (element == null) throw new IllegalStateException("Element " + i
					+ " is null");
			element.readFields(in);
		}
		return i;
	}
}
