package cmu.edu.commons.hadoop.tuple.schema;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MapContext;
import org.apache.hadoop.mapreduce.ReduceContext;
import org.apache.hadoop.util.ReflectionUtils;

import cmu.edu.commons.hadoop.tuple.BaseTuple;
import cmu.edu.commons.hadoop.tuple.KeyTuple;

public abstract class TupleSchemaFactory {

	private static final String FACTORY_CLASSNAME_PROPERTY =
			TupleSchemaFactory.class.getName() + ".factoryClassName";

	public static void set(Job job,
			Class<? extends TupleSchemaFactory> factoryClass) {
		job.getConfiguration().set(FACTORY_CLASSNAME_PROPERTY,
				factoryClass.getName());
	}

	/**
	 * In case the configured TupleSchemaFactory implementation is stateful, we
	 * maintain separate instances per Thread.
	 */
	private static ThreadLocal<TupleSchemaFactory> FACTORY =
			new ThreadLocal<TupleSchemaFactory>();

	public static TupleSchemaFactory get(Configuration conf) {
		TupleSchemaFactory factory = FACTORY.get();
		if (factory == null) {
			Class<? extends TupleSchemaFactory> factoryClass =
					conf.getClass(FACTORY_CLASSNAME_PROPERTY, null,
							TupleSchemaFactory.class);
			if (factoryClass == null) throw new IllegalStateException(
					"TupleSchemaFactory implementation is undefined");
			factory = ReflectionUtils.newInstance(factoryClass, conf);
			FACTORY.set(factory);
		}
		return factory;
	}

	public static boolean isMap() {
		for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
			String className = element.getClassName();
			if (MapContext.class.getName().equals(className)) return true;
			if (ReduceContext.class.getName().equals(className)) return false;
		}
		throw new IllegalStateException(
				"Failed to identify map/reduce context from call stack");
	}

	public TupleSchema<? extends Writable> getTupleSchema(
			BaseTuple<? extends Writable> tuple) {
		if (isMap()) {
			if (tuple instanceof KeyTuple) return getMapKeyTupleSchema();
			return getMapValueTupleSchema();
		}
		if (tuple instanceof KeyTuple) return getReduceKeyTupleSchema();
		return getReduceValueTupleSchema();
	}

	public TupleSchema<WritableComparable<?>> getMapKeyTupleSchema() {
		throw new UnsupportedOperationException("No map key tuple schema defined");
	}

	public TupleSchema<Writable> getMapValueTupleSchema() {
		throw new UnsupportedOperationException("No map value tuple schema defined");
	}

	public TupleSchema<WritableComparable<?>> getReduceKeyTupleSchema() {
		return getMapKeyTupleSchema();
	}

	public TupleSchema<Writable> getReduceValueTupleSchema() {
		return getMapValueTupleSchema();
	}
}
