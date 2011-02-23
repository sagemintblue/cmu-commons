package edu.cmu.commons.data.dao.mongo;

import com.mongodb.DBObject;

/**
 * Supports conversion of data between instances of a custom Java type and data
 * structures supported by Mongo.
 * @author hazen
 * @param <E> Entity type.
 */
public interface EntityMarshaller<E> extends Marshaller<E, DBObject> {}
