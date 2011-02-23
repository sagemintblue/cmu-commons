package cmu.edu.commons.hadoop.io;

import org.apache.hadoop.io.WritableComparable;

/**
 * Base class for numeric Writable types, supporting size conversion.
 * @author hazen
 */
public interface NumberWritable extends WritableComparable<NumberWritable> {
	public Number getNumber();

	public void setNumber(Number number);
}
