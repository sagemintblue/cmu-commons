package cmu.edu.commons.hadoop.tuple.impl;

import java.io.DataInput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

import cmu.edu.commons.hadoop.tuple.BaseTuple2;
import cmu.edu.commons.hadoop.tuple.schema.TupleSchema;

public abstract class BaseTuple2Impl< //
E extends Writable, //
E1 extends E, //
E2 extends E> //
		extends BaseTupleImpl<E> //
		implements BaseTuple2<E, E1, E2> {
	private static final long serialVersionUID = 1L;

	protected BaseTuple2Impl(E... elements) {
		super(elements);
	}

	@SuppressWarnings("unchecked")
	public BaseTuple2Impl(E1 e1, E2 e2) {
		super(e1, e2);
	}

	public BaseTuple2Impl() {
		this(null, null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public E1 get1() {
		return (E1) elements.get(0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public E1 set1(E1 e1) {
		return (E1) elements.set(0, e1);
	}

	@Override
	public E1 getFirst() {
		return get1();
	}

	@Override
	public E1 setFirst(E1 e1) {
		return set1(e1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public E2 get2() {
		return (E2) elements.get(1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public E2 set2(E2 e2) {
		return (E2) elements.set(1, e2);
	}

	@Override
	public E2 getSecond() {
		return get2();
	}

	@Override
	public E2 setSecond(E2 e2) {
		return set2(e2);
	}

	@Override
	protected int readFieldsWithSchema(DataInput in, TupleSchema<E> schema)
			throws IOException {
		int size = super.readFieldsWithSchema(in, schema);
		if (size != elements.size()) throw new IllegalStateException("Expected "
				+ elements.size() + " elements but deserialized only " + size);
		return size;
	}
}
