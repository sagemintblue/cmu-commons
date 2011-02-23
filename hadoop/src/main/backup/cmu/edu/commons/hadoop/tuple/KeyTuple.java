package cmu.edu.commons.hadoop.tuple;

import org.apache.hadoop.io.WritableComparable;

public interface KeyTuple extends BaseTuple<WritableComparable<?>>,
		Comparable<KeyTuple> {}
