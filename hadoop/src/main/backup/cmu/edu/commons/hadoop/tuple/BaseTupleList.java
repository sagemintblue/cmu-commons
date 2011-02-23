package cmu.edu.commons.hadoop.tuple;

import java.util.List;

import org.apache.hadoop.io.Writable;

public interface BaseTupleList<E extends Writable> extends BaseTuple<E>,
		List<E> {}
