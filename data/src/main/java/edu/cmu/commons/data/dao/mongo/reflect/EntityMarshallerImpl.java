package edu.cmu.commons.data.dao.mongo.reflect;

import java.util.Collection;

import edu.cmu.commons.data.dao.mongo.EntityMarshaller;

/**
 * @author hazen
 * @param <E> Entity type.
 * @param <I> Entity id type.
 */
class EntityMarshallerImpl<E> extends CompositeMarshaller<E> implements
		EntityMarshaller<E> {
	public EntityMarshallerImpl(Class<E> valueClass,
			Collection<PropertyInfo<?>> properties) {
		super(valueClass, properties);
	}
}
