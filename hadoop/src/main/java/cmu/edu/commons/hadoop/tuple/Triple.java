package cmu.edu.commons.hadoop.tuple;

import org.apache.hadoop.io.Writable;


public class Triple< //
E1 extends Writable, //
E2 extends Writable, //
E3 extends Writable> //
		extends BaseTuple3Impl<Writable, E1, E2, E3> //
		implements Tuple {
	private static final long serialVersionUID = 1L;

	public Triple() {
		super();
	}

	public Triple(E1 e1, E2 e2, E3 e3) {
		super(e1, e2, e3);
	}
}
