package cmu.edu.commons.hadoop.tuple.impl;

import org.apache.hadoop.io.Writable;

import cmu.edu.commons.hadoop.tuple.Triple;

public class TripleImpl< //
E1 extends Writable, //
E2 extends Writable, //
E3 extends Writable> //
		extends BaseTuple3Impl<Writable, E1, E2, E3> //
		implements Triple<E1, E2, E3> {
	private static final long serialVersionUID = 1L;

	public TripleImpl() {
		super();
	}

	public TripleImpl(E1 e1, E2 e2, E3 e3) {
		super(e1, e2, e3);
	}
}
