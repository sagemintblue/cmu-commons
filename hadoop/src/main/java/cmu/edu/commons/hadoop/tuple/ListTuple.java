package cmu.edu.commons.hadoop.tuple;

import org.apache.hadoop.io.Writable;


public class ListTuple extends BaseListTupleImpl<Writable> implements Tuple {
	private static final long serialVersionUID = 1L;

	public ListTuple() {
		super();
	}

	public ListTuple(Writable... elements) {
		super(elements);
	}
}
