package cmu.edu.commons.hadoop.tuple;

import org.apache.hadoop.io.WritableComparable;

import cmu.edu.commons.hadoop.tuple.schema.TupleSchema;

public interface KeyTupleSchema extends TupleSchema<WritableComparable<?>> {}
