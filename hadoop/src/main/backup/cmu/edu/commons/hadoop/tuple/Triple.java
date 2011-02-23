package cmu.edu.commons.hadoop.tuple;

import org.apache.hadoop.io.Writable;

public interface Triple< //
E1 extends Writable, //
E2 extends Writable, //
E3 extends Writable> //
		extends Pair<E1, E2>, //
		BaseTuple3<Writable, E1, E2, E3> {}
