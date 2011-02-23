package cmu.edu.commons.hadoop.tuple;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.DataInputBuffer;
import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.WritableComparable;

import cmu.edu.commons.hadoop.tuple.schema.TupleSchema;
import cmu.edu.commons.hadoop.tuple.schema.TupleSchemaFactory;

public interface KeyTuple extends BaseTuple<WritableComparable<?>>,
		WritableComparable<KeyTuple> {
	public static class Comparator extends Configured implements
			RawComparator<KeyTuple> {
		private DataInputBuffer buffer;
		private KeyTuple key1;
		private KeyTuple key2;

		public Comparator() {
			buffer = new DataInputBuffer();
		}

		@Override
		public void setConf(Configuration conf) {
			super.setConf(conf);
			key1 = new KeyListTuple();
			key2 = new KeyListTuple();
			TupleSchema<WritableComparable<?>> schema =
					TupleSchemaFactory.get(conf).getReduceKeyTupleSchema();
			key1.setSchema(schema);
			key2.setSchema(schema);
		}

		@Override
		public int compare(byte[] buf1, int start1, int len1, byte[] buf2,
				int start2, int len2) {
			try {
				buffer.reset(buf1, start1, len1);
				key1.readFields(buffer);
				buffer.reset(buf2, start2, len2);
				key2.readFields(buffer);
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (NullPointerException e) {
				throw new IllegalStateException("Comparator has not been configured", e);
			}
			return compare(key1, key2);
		}

		@Override
		public int compare(KeyTuple o1, KeyTuple o2) {
			return o1.compareTo(o2);
		}
	}
}
