package edu.cmu.commons.data.dao.mongo.reflect;

import java.util.Iterator;

import edu.cmu.commons.data.dao.mongo.Marshaller;

/**
 * @author hazen
 * @param <AE> A element type.
 * @param <A> A type.
 */
class IterableMarshaller<AE, A extends Iterable<AE>> implements
		ContainerMarshaller<A, Iterable<Object>> {
	private final Marshaller<AE, Object> elementMarshaller;

	public IterableMarshaller(Marshaller<AE, Object> elementMarshaller) {
		this.elementMarshaller = elementMarshaller;
	}

	@Override
	public Iterable<Object> marshal(final A source, Iterable<Object> destination) {
		return new Iterable<Object>() {
			@Override
			public Iterator<Object> iterator() {
				final Iterator<AE> itr = source.iterator();

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
	public A unmarshal(Iterable<Object> source, A destination) {
		// TODO Auto-generated method stub
		return null;
	}
}
