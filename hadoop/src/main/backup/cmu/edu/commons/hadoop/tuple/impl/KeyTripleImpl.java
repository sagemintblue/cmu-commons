package cmu.edu.commons.hadoop.tuple.impl;

import org.apache.hadoop.io.WritableComparable;

import cmu.edu.commons.hadoop.tuple.KeyTriple;
import cmu.edu.commons.hadoop.tuple.KeyTuple;

public class KeyTripleImpl< //
E1 extends WritableComparable<?>, //
E2 extends WritableComparable<?>, //
E3 extends WritableComparable<?>> //
		extends BaseTuple3Impl<WritableComparable<?>, E1, E2, E3> //
		implements KeyTriple<E1, E2, E3> {
	private static final long serialVersionUID = 1L;

	public KeyTripleImpl() {
		super();
	}

	public KeyTripleImpl(E1 e1, E2 e2, E3 e3) {
		super(e1, e2, e3);
	}

	@Override
	public int compareTo(KeyTuple o) {
		return KeyTupleImpl.compare(this, o);
	}
}
