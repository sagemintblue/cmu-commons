package edu.cmu.commons.data.dao.mongo;

/**
 * Supports conversion of data between two representations.
 * @author hazen
 * @param <A> Unmarshalled type.
 * @param <B> Marshalled type.
 */
public interface Marshaller<A, B> {
	/**
	 * Marshals data from {@code value} into {@code marshalledValue}, or possibly
	 * a new {@code B} instance. Only non-{@code null} property values from
	 * {@code value} are used to update {@code marshalledValue}. This implies that
	 * if {@code marshalledValue} is not {@code null}, a "merge" operation is
	 * performed, pushing non-{@code null} data from {@code value} into
	 * {@code marshalledValue}, but otherwise leaving any existing data in
	 * {@code marshalledValue} alone.
	 * @param value
	 * @param marshalledValue
	 * @return {@code marshalledValue} (or a new {@code B} instance) containing
	 * data from {@code value} .
	 */
	public B marshal(A value, B marshalledValue);

	/**
	 * Symmetric conversion of data from instance of type {@code B} to instance of
	 * type {@code A}.
	 * @param marshalledValue
	 * @param value
	 * @return {@code value} (or a new {@code A} instance) containing data from
	 * {@code marshalledValue} .
	 * @see #marshal(Object, Object)
	 */
	public A unmarshal(B marshalledValue, A value);
}
