package cmu.edu.commons.hadoop.tuple.impl;

import org.apache.hadoop.io.WritableComparable;

import cmu.edu.commons.hadoop.tuple.KeyPair;
import cmu.edu.commons.hadoop.tuple.KeyTuple;

public class KeyPairImpl< //
E1 extends WritableComparable<?>, //
E2 extends WritableComparable<?>> //
		extends BaseTuple2Impl<WritableComparable<?>, E1, E2> //
		implements KeyPair<E1, E2> {
	private static final long serialVersionUID = 1L;

	public KeyPairImpl() {
		super();
	}

	public KeyPairImpl(E1 e1, E2 e2) {
		super(e1, e2);
	}

	@Override
	public int compareTo(KeyTuple o) {
		return KeyTupleImpl.compare(this, o);
	}
}
