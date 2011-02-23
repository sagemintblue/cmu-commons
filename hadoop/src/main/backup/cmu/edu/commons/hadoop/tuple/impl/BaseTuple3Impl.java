package cmu.edu.commons.hadoop.tuple.impl;

import org.apache.hadoop.io.Writable;

import cmu.edu.commons.hadoop.tuple.BaseTuple3;

public abstract class BaseTuple3Impl< //
E extends Writable, //
E1 extends E, //
E2 extends E, //
E3 extends E> //
		extends BaseTuple2Impl<E, E1, E2> //
		implements BaseTuple3<E, E1, E2, E3> {
	private static final long serialVersionUID = 1L;

	protected BaseTuple3Impl(E... elements) {
		super(elements);
	}

	@SuppressWarnings("unchecked")
	public BaseTuple3Impl(E1 e1, E2 e2, E3 e3) {
		super(e1, e2, e3);
	}

	public BaseTuple3Impl() {
		this(null, null, null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public E3 get3() {
		return (E3) elements.get(2);
	}

	@Override
	@SuppressWarnings("unchecked")
	public E3 set3(E3 e3) {
		return (E3) elements.set(2, e3);
	}

	@Override
	public E3 getThird() {
		return get3();
	}

	@Override
	public E3 setThird(E3 e3) {
		return set3(e3);
	}
}
