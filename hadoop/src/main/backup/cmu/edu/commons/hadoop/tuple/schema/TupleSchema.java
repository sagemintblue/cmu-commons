package cmu.edu.commons.hadoop.tuple.schema;

import org.apache.hadoop.io.Writable;

import cmu.edu.commons.hadoop.tuple.BaseTuple;

public interface TupleSchema<E extends Writable> {
	public void initialize(BaseTuple<E> tuple);

	public boolean initialize(BaseTuple<E> tuple, int i);
}
