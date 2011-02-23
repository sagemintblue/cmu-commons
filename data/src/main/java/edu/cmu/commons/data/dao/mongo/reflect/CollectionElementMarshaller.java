package edu.cmu.commons.data.dao.mongo.reflect;

import java.util.Collection;
import java.util.Iterator;

import edu.cmu.commons.data.dao.mongo.Marshaller;

/**
 * Marshaller supporting {@code Collection<E>} types, where {@code E} instances
 * are supported by another marshaller.
 * @author hazen
 * @param <E> Element type.
 */
class CollectionElementMarshaller<E> extends CollectionMarshaller<E> {
	private final Marshaller<E, Object> elementMarshaller;

	public CollectionElementMarshaller(Class<E> elementClass,
			Class<? extends Collection<E>> collectionClass,
			Marshaller<E, Object> elementMarshaller) {
		super(elementClass, collectionClass);
		this.elementMarshaller = elementMarshaller;
	}

	@Override
	public Object marshal(final Collection<E> value, Object marshalledValue) {
		if (marshalledValue != null) {
			if (!(marshalledValue instanceof Collection)) throw new IllegalArgumentException(
					"Marshalled value of type '" + marshalledValue.getClass().getName()
							+ "' is not instance of '" + Collection.class.getName() + "'");
			@SuppressWarnings("unchecked")
			Collection<Object> marshalledCollection =
					(Collection<Object>) marshalledValue;
			for (E element : value)
				marshalledCollection.add(elementMarshaller.marshal(element, null));
			return marshalledValue;
		}
		return new Iterable<Object>() {
			@Override
			public Iterator<Object> iterator() {
				final Iterator<E> itr = value.iterator();

				return new Iterator<Object>() {
					@Override
					public boolean hasNext() {
						return itr.hasNext();
					}

					@Override
					public Object next() {
						return elementMarshaller.marshal(itr.next(), null);
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	@Override
	public Collection<E> unmarshal(Object marshalledValue, Collection<E> value) {
		if (!(marshalledValue instanceof Iterable)) throw new IllegalArgumentException(
				"Marshalled value of type '" + marshalledValue.getClass().getName()
						+ "' is not instance of '" + Iterable.class.getName() + "'");
		if (value == null) {
			try {
				value = getCollectionClass().newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		Iterable<?> marshalledIterable = (Iterable<?>) marshalledValue;
		for (Object marshalledElement : marshalledIterable)
			value.add(elementMarshaller.unmarshal(marshalledElement, null));
		return value;
	}
}
