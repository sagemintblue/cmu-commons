package cmu.edu.commons.hadoop.tuple;

import org.apache.hadoop.io.Writable;


public class Pair< //
E1 extends Writable, //
E2 extends Writable> //
		extends BaseTuple2Impl<Writable, E1, E2> //
		implements Tuple {
	private static final long serialVersionUID = 1L;

	public Pair() {
		super();
	}

	public Pair(E1 e1, E2 e2) {
		super(e1, e2);
	}
}
