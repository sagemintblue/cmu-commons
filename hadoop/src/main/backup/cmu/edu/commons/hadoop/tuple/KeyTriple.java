package cmu.edu.commons.hadoop.tuple;

import org.apache.hadoop.io.WritableComparable;

public interface KeyTriple< //
E1 extends WritableComparable<?>, //
E2 extends WritableComparable<?>, //
E3 extends WritableComparable<?>> //
		extends KeyPair<E1, E2>, //
		BaseTuple3<WritableComparable<?>, E1, E2, E3> {}
