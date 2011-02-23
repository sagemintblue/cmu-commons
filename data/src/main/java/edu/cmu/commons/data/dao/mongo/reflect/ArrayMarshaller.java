package edu.cmu.commons.data.dao.mongo.reflect;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author hazen
 * @param <E> Simple element type.
 */
class ArrayMarshaller<E> implements ContainerMarshaller<E[], Object> {
	private final CollectionMarshaller<E> marshaller;

	public ArrayMarshaller(CollectionMarshaller<E> marshaller) {
		this.marshaller = marshaller;
	}

	@Override
	public Object marshal(E[] value, Object marshalledValue) {
		return marshaller.marshal(Arrays.asList(value), marshalledValue);
	}

	@Override
	public E[] unmarshal(Object marshalledValue, E[] value) {
		Collection<E> collection =
				marshaller.unmarshal(marshalledValue,
						new ArrayList<E>(Arrays.asList(value)));
		@SuppressWarnings("unchecked")
		E[] array =
				(E[]) Array
						.newInstance(marshaller.getElementClass(), collection.size());
		return collection.toArray(array);
	}
}
