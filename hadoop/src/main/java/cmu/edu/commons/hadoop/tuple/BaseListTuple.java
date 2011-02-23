package cmu.edu.commons.hadoop.tuple;

import java.util.List;

import org.apache.hadoop.io.Writable;

public interface BaseListTuple<E extends Writable> extends BaseTuple<E>,
		List<E> {}
