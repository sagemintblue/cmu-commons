package cmu.edu.commons.hadoop.tuple.impl;

import org.apache.hadoop.io.Writable;

import cmu.edu.commons.hadoop.tuple.TupleList;

public class TupleListImpl extends BaseTupleListImpl<Writable> implements
		TupleList {
	private static final long serialVersionUID = 1L;

	public TupleListImpl() {
		super();
	}

	public TupleListImpl(Writable... elements) {
		super(elements);
	}
}
