package cmu.edu.commons.hadoop.tuple;

import org.apache.hadoop.io.WritableComparable;


public class KeyListTuple
		extends BaseListTupleImpl<WritableComparable<?>> //
		implements KeyTuple {
	private static final long serialVersionUID = 1L;

	public KeyListTuple() {
		super();
	}

	public KeyListTuple(WritableComparable<?>... elements) {
		super(elements);
	}

	@Override
	public int compareTo(KeyTuple o) {
		return TupleUtils.compare(this, o);
	}
}
