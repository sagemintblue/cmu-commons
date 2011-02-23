package cmu.edu.commons.hadoop.tuple.impl;

import org.apache.hadoop.io.Writable;

import cmu.edu.commons.hadoop.tuple.Pair;

public class PairImpl< //
E1 extends Writable, //
E2 extends Writable> //
		extends BaseTuple2Impl<Writable, E1, E2> //
		implements Pair<E1, E2> {
	private static final long serialVersionUID = 1L;

	public PairImpl() {
		super();
	}

	public PairImpl(E1 e1, E2 e2) {
		super(e1, e2);
	}
}
