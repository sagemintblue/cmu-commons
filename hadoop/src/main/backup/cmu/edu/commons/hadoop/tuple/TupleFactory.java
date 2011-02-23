package cmu.edu.commons.hadoop.tuple;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import cmu.edu.commons.hadoop.tuple.impl.KeyPairImpl;
import cmu.edu.commons.hadoop.tuple.impl.KeyTripleImpl;
import cmu.edu.commons.hadoop.tuple.impl.KeyTupleListImpl;
import cmu.edu.commons.hadoop.tuple.impl.PairImpl;
import cmu.edu.commons.hadoop.tuple.impl.TripleImpl;
import cmu.edu.commons.hadoop.tuple.impl.TupleListImpl;

public class TupleFactory {

	private TupleFactory() {
	// hide default ctor
	}

	public static < //
	E1 extends Writable, //
	E2 extends Writable> //
	Pair<E1, E2> pair(E1 e1, E2 e2) {
		return new PairImpl<E1, E2>(e1, e2);
	}

	public static < //
	E1 extends Writable, //
	E2 extends Writable, //
	E3 extends Writable> //
	Triple<E1, E2, E3> triple(E1 e1, E2 e2, E3 e3) {
		return new TripleImpl<E1, E2, E3>(e1, e2, e3);
	}

	public static <E extends Writable> TupleList tuple(E... elements) {
		return new TupleListImpl(elements);
	}

	public static < //
	E1 extends WritableComparable<?>, //
	E2 extends WritableComparable<?>> //
	KeyPair<E1, E2> keyPair(E1 e1, E2 e2) {
		return new KeyPairImpl<E1, E2>(e1, e2);
	}

	public static < //
	E1 extends WritableComparable<?>, //
	E2 extends WritableComparable<?>, //
	E3 extends WritableComparable<?>> //
	KeyTriple<E1, E2, E3> keyTriple(E1 e1, E2 e2, E3 e3) {
		return new KeyTripleImpl<E1, E2, E3>(e1, e2, e3);
	}

	public static <E extends WritableComparable<?>> KeyTupleListImpl keyTuple(
			E... elements) {
		return new KeyTupleListImpl(elements);
	}
}
