package cmu.edu.commons.hadoop.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparator;

public class FloatWritable implements NumberWritable {
	private org.apache.hadoop.io.FloatWritable delegate;

	public float get() {
		return delegate.get();
	}

	public void set(float value) {
		this.delegate.set(value);
	}

	@Override
	public Number getNumber() {
		return delegate.get();
	}

	@Override
	public void setNumber(Number number) {
		this.delegate.set(number.floatValue());
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public boolean equals(Object o) {
		return delegate.equals(o);
	}

	@Override
	public int compareTo(NumberWritable o) {
		if (this == o) return 0;
		if (o == null) return 1;
		float n = o.getNumber().floatValue();
		if (delegate.get() < n) return -1;
		if (delegate.get() > n) return 1;
		return 0;
	}

	public String toString() {
		return delegate.toString();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		delegate.write(out);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		delegate.readFields(in);
	}

	static {
		WritableComparator.define(FloatWritable.class,
				new org.apache.hadoop.io.FloatWritable.Comparator());
	}
}