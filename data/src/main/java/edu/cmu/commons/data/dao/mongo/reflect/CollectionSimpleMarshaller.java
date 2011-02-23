package edu.cmu.commons.data.dao.mongo.reflect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Marshaller supporting {@code Collection<E>} types, where {@code E} is a
 * simple type.
 * @author hazen
 * @param <E> Simple element type.
 */
class CollectionSimpleMarshaller<E> extends CollectionMarshaller<E> {
	public static final Set<Class<?>> SIMPLE_COLLECTION_TYPES =
			new HashSet<Class<?>>(Arrays.<Class<?>> asList(Collection.class,
					List.class, ArrayList.class));

	public CollectionSimpleMarshaller(Class<E> elementClass,
			Class<? extends Collection<E>> collectionClass) {
		super(elementClass, collectionClass);
	}

	@Override
	public Object marshal(Collection<E> value, Object marshalledValue) {
		if (marshalledValue != null) {
			if (!(marshalledValue instanceof Collection)) throw new IllegalArgumentException(
					"Marshalled value of type '" + marshalledValue.getClass().getName()
							+ "' is not instance of '" + Collection.class.getName() + "'");
			@SuppressWarnings("unchecked")
			Collection<Object> marshalledCollection =
					(Collection<Object>) marshalledValue;
			for (E element : value)
				marshalledCollection.add(element);
			return marshalledValue;
		}
		return value;
	}

	@Override
	public Collection<E> unmarshal(Object marshalledValue, Collection<E> value) {
		if (value != null) {
			Iterable<?> marshalledIterable = (Iterable<?>) marshalledValue;
			for (Object element : marshalledIterable)
				value.add(getElementClass().cast(element));
			return value;
		}
		return getCollectionClass().cast(marshalledValue);
	}
}
