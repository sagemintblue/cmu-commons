package cmu.edu.commons.hadoop.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparator;

public class ShortWritable implements NumberWritable {
	private short value;

	public short get() {
		return value;
	}

	public void set(short value) {
		this.value = value;
	}

	@Override
	public Number getNumber() {
		return value;
	}

	@Override
	public void setNumber(Number number) {
		this.value = number.shortValue();
	}

	@Override
	public int compareTo(NumberWritable o) {
		if (this == o) return 0;
		if (o == null) return 1;
		short n = o.getNumber().shortValue();
		if (value < n) return -1;
		if (value > n) return 1;
		return 0;
	}

	public String toString() {
		return Short.toString(value);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeShort(value);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		value = in.readShort();
	}

	public static class Comparator extends WritableComparator {
		public Comparator() {
			super(ShortWritable.class);
		}

		@Override
		public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
			
			return 0;
		}
	}

	static {
		WritableComparator.define(ShortWritable.class, new Comparator());
	}
}