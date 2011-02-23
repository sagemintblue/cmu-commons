package cmu.edu.commons.hadoop.tuple;

import org.apache.hadoop.io.WritableComparable;

public interface KeyPair< //
E1 extends WritableComparable<?>, //
E2 extends WritableComparable<?>> //
		extends KeyTuple, //
		BaseTuple2<WritableComparable<?>, E1, E2> {}
