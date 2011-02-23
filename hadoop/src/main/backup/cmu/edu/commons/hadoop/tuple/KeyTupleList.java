package cmu.edu.commons.hadoop.tuple;

import org.apache.hadoop.io.WritableComparable;

public interface KeyTupleList extends KeyTuple,
		BaseTupleList<WritableComparable<?>> {}
