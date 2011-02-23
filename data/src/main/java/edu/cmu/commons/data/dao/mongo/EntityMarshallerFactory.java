package edu.cmu.commons.data.dao.mongo;

import com.mongodb.DBObject;

/**
 * A factory able to create/retrieve {@link EntityMarshaller} instances.
 * @author hazen
 */
public interface EntityMarshallerFactory {
	public <E> Marshaller<E, DBObject> getEntityMarshaller(Class<E> entityClass);
}
