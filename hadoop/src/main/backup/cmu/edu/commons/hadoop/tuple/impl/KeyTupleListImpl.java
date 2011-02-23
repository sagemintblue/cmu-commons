package cmu.edu.commons.hadoop.tuple.impl;

import org.apache.hadoop.io.WritableComparable;

import cmu.edu.commons.hadoop.tuple.KeyTuple;
import cmu.edu.commons.hadoop.tuple.KeyTupleList;

public class KeyTupleListImpl extends BaseTupleListImpl<WritableComparable<?>>
		implements KeyTupleList {
	private static final long serialVersionUID = 1L;

	public KeyTupleListImpl() {
		super();
	}

	public KeyTupleListImpl(WritableComparable<?>... elements) {
		super(elements);
	}

	@Override
	public int compareTo(KeyTuple o) {
		return KeyTupleImpl.compare(this, o);
	}
}
