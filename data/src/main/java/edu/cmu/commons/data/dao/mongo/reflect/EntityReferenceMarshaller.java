package edu.cmu.commons.data.dao.mongo.reflect;

import edu.cmu.commons.data.dao.Dao;
import edu.cmu.commons.data.dao.mongo.Marshaller;

/**
 * @author hazen
 * @param <E> Entity type.
 * @param <I> Entity id type.
 */
class EntityReferenceMarshaller<E, I> implements Marshaller<E, Object> {
	private final PropertyInfo<I> idProperty;
	private final Dao<E, I> dao;

	public EntityReferenceMarshaller(PropertyInfo<I> idProperty, Dao<E, I> dao) {
		this.idProperty = idProperty;
		this.dao = dao;
	}

	@Override
	public Object marshal(E value, Object marshalledValue) {
		return idProperty.marshal(value, marshalledValue);
	}

	@Override
	public E unmarshal(Object marshalledValue, E value) {
		I id = idProperty.getMarshaller().unmarshal(marshalledValue, null);
		return dao.getReference(id);
	}
}
