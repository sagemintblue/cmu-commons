package edu.cmu.commons.data.dao.mongo.reflect;

import java.util.Collection;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import edu.cmu.commons.data.dao.mongo.Marshaller;

/**
 * @author hazen
 * @param <T> Composite type.
 */
class CompositeMarshaller<T> implements Marshaller<T, DBObject> {
	private final Class<T> compositeClass;
	private final Collection<PropertyInfo<?>> properties;

	public CompositeMarshaller(Class<T> compositeClass,
			Collection<PropertyInfo<?>> properties) {
		super();
		this.compositeClass = compositeClass;
		this.properties = properties;
	}

	@Override
	public DBObject marshal(T value, DBObject marshalledValue) {
		if (marshalledValue == null) marshalledValue = new BasicDBObject();
		for (PropertyInfo<?> property : properties)
			property.marshal(value, marshalledValue);
		return marshalledValue;
	}

	@Override
	public T unmarshal(DBObject marshalledValue, T value) {
		if (value == null) {
			try {
				value = compositeClass.newInstance();
			} catch (Exception e) {
				// TODO improve error message
				throw new RuntimeException(e);
			}
		}
		for (PropertyInfo<?> property : properties)
			property.unmarshal(marshalledValue, value);
		return value;
	}
}
