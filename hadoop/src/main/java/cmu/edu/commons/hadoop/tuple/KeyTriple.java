package cmu.edu.commons.hadoop.tuple;

import org.apache.hadoop.io.WritableComparable;


public class KeyTriple< //
E1 extends WritableComparable<?>, //
E2 extends WritableComparable<?>, //
E3 extends WritableComparable<?>> //
		extends BaseTuple3Impl<WritableComparable<?>, E1, E2, E3> //
		implements KeyTuple {
	private static final long serialVersionUID = 1L;

	public KeyTriple() {
		super();
	}

	public KeyTriple(E1 e1, E2 e2, E3 e3) {
		super(e1, e2, e3);
	}

	@Override
	public int compareTo(KeyTuple o) {
		return TupleUtils.compare(this, o);
	}
}
