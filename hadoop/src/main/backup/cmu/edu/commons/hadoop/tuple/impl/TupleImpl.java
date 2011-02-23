package cmu.edu.commons.hadoop.tuple.impl;

import org.apache.hadoop.io.Writable;

import cmu.edu.commons.hadoop.tuple.Tuple;

public abstract class TupleImpl extends BaseTupleImpl<Writable> implements
		Tuple {
	private static final long serialVersionUID = 1L;

	public TupleImpl() {
		super();
	}

	public TupleImpl(Writable... elements) {
		super(elements);
	}
}
