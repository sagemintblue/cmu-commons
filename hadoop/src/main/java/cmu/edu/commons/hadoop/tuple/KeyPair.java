package cmu.edu.commons.hadoop.tuple;

import org.apache.hadoop.io.WritableComparable;

import cmu.edu.commons.hadoop.tuple.KeyPair;
import cmu.edu.commons.hadoop.tuple.KeyTuple;

public class KeyPair< //
E1 extends WritableComparable<?>, //
E2 extends WritableComparable<?>> //
		extends BaseTuple2Impl<WritableComparable<?>, E1, E2> //
		implements KeyTuple {
	private static final long serialVersionUID = 1L;

	public KeyPair() {
		super();
	}

	public KeyPair(E1 e1, E2 e2) {
		super(e1, e2);
	}

	@Override
	public int compareTo(KeyTuple o) {
		return TupleUtils.compare(this, o);
	}
}
