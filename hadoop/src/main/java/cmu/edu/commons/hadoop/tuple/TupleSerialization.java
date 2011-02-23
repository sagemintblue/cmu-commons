package cmu.edu.commons.hadoop.tuple;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.serializer.Deserializer;
import org.apache.hadoop.io.serializer.Serialization;
import org.apache.hadoop.io.serializer.Serializer;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.ReflectionUtils;

import cmu.edu.commons.hadoop.tuple.schema.TupleSchema;
import cmu.edu.commons.hadoop.tuple.schema.TupleSchemaFactory;

public class TupleSerialization extends Configured implements
		Serialization<BaseTuple<?>> {

	public static final String IO_SERIALIZATIONS_PROPERTY = "io.serializations";

	public static void add(Job job) {
		List<String> serializations =
				new ArrayList<String>(Arrays.asList(TupleSerialization.class.getName()));
		serializations.addAll(Arrays.asList(job.getConfiguration().getStrings(
				IO_SERIALIZATIONS_PROPERTY)));
		job.getConfiguration().setStrings(IO_SERIALIZATIONS_PROPERTY,
				(String[]) serializations.toArray(new String[serializations.size()]));
	}

	public class TupleSerializer implements Serializer<BaseTuple<?>> {

		private DataOutputStream out;

		@Override
		public void open(OutputStream out) throws IOException {
			if (out instanceof DataOutputStream) {
				this.out = (DataOutputStream) out;
			} else {
				this.out = new DataOutputStream(out);
			}
		}

		@Override
		public void close() throws IOException {
			out.close();
		}

		@Override
		public void serialize(BaseTuple<?> writable) throws IOException {
			writable.write(out);
		}
	}

	public class BaseDeserializer implements
			Deserializer<BaseTuple<? extends Writable>> {

		private Class<? extends BaseTuple<? extends Writable>> tupleClass;

		private DataInputStream in;

		public BaseDeserializer(Class<? extends BaseTuple<? extends Writable>> cls) {
			this.tupleClass = cls;
		}

		@Override
		public void open(InputStream in) throws IOException {
			if (in instanceof DataInputStream) {
				this.in = (DataInputStream) in;
			} else {
				this.in = new DataInputStream(in);
			}
		}

		@Override
		public void close() throws IOException {
			in.close();
		}

		@Override
		public BaseTuple<?> deserialize(BaseTuple<?> tuple) throws IOException {
			if (tuple == null) tuple = newInstance();
			tuple.readFields(in);
			return tuple;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		protected BaseTuple<?> newInstance() {
			Configuration conf = getConf();
			BaseTuple<? extends Writable> tuple =
					ReflectionUtils.newInstance(tupleClass, conf);
			TupleSchema schema = TupleSchemaFactory.get(conf).getTupleSchema(tuple);
			tuple.setSchema(schema);
			return tuple;
		}
	}

	@Override
	public boolean accept(Class<?> cls) {
		return BaseTuple.class.isAssignableFrom(cls);
	}

	@Override
	public Serializer<BaseTuple<?>> getSerializer(Class<BaseTuple<?>> cls) {
		return new TupleSerializer();
	}

	@Override
	public Deserializer<BaseTuple<?>> getDeserializer(Class<BaseTuple<?>> cls) {
		return new BaseDeserializer(cls);
	}
}
