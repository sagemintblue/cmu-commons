package edu.cmu.commons.data.dao.mongo.reflect;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import edu.cmu.commons.data.dao.mongo.Marshaller;
import edu.cmu.commons.data.dao.mongo.MarshallerFactory;

/**
 * @author hazen
 * @param <T> Polymorphic type.
 */
class PolymorphicMarshaller<T> implements Marshaller<T, DBObject> {
	private static final String TYPE_KEY = "type";
	private final Class<T> baseClass;
	private final MarshallerFactory marshallerFactory;

	public PolymorphicMarshaller(Class<T> baseClass,
			MarshallerFactory marshallerFactory) {
		this.baseClass = baseClass;
		this.marshallerFactory = marshallerFactory;
	}

	@Override
	public DBObject marshal(T value, DBObject marshalledValue) {
		if (marshalledValue == null) marshalledValue = new BasicDBObject();

		// record value class in marshalled value
		Class<? extends T> valueClass = value.getClass().asSubclass(baseClass);
		marshalledValue.put(TYPE_KEY, valueClass.getName());

		// get marshaller associated with value class
		@SuppressWarnings("unchecked")
		Marshaller<T, Object> marshaller =
				(Marshaller<T, Object>) marshallerFactory.getMarshaller(valueClass);
		return (DBObject) marshaller.marshal(value, marshalledValue);
	}

	@Override
	public T unmarshal(DBObject marshalledValue, T value) {
		// determine value class from marshalled data
		Object typeValue = marshalledValue.get(TYPE_KEY);
		if (typeValue == null) throw new IllegalStateException("Source '"
				+ TYPE_KEY + "' value is undefined");
		String className = typeValue.toString();
		Class<?> cls;
		try {
			cls = Class.forName(className);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (!baseClass.isAssignableFrom(cls)) throw new IllegalStateException(
				"Class '" + className + "' specified by source '" + TYPE_KEY
						+ "' key is not assignable to class '" + baseClass.getName() + "'");
		Class<? extends T> valueClass = cls.asSubclass(baseClass);

		// construct new value instance if necessary
		if (value == null || !valueClass.isInstance(value)) {
			try {
				value = valueClass.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		// get marshaller associated with value class
		@SuppressWarnings("unchecked")
		Marshaller<T, Object> marshaller =
				(Marshaller<T, Object>) marshallerFactory.getMarshaller(valueClass);
		return marshaller.unmarshal(marshalledValue, value);
	}
}
