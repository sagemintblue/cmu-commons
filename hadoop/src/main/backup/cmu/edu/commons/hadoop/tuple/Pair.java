package cmu.edu.commons.hadoop.tuple;

import org.apache.hadoop.io.Writable;

public interface Pair< //
E1 extends Writable, //
E2 extends Writable> //
		extends Tuple, //
		BaseTuple2<Writable, E1, E2> {}
