package edu.cmu.commons.data.dao.mongo.reflect;

import com.mongodb.DBObject;

import edu.cmu.commons.data.dao.mongo.Marshaller;

/**
 * @author hazen
 * @param <T> Hybrid composite-container type.
 */
class CompositeContainerMarshaller<T> implements Marshaller<T, DBObject> {
	private final CompositeMarshaller<T> compositeMarshaller;
	private final ContainerMarshaller<T, Object> containerMarshaller;
	private final String dataKey;

	public CompositeContainerMarshaller(
			CompositeMarshaller<T> compositeMarshaller,
			ContainerMarshaller<T, Object> containerMarshaller) {
		super();
		this.compositeMarshaller = compositeMarshaller;
		this.containerMarshaller = containerMarshaller;
		this.dataKey =
				containerMarshaller instanceof MapKeyValueMarshaller ? "entries" : "elements";
	}

	@Override
	public DBObject marshal(T value, DBObject marshalledValue) {
		marshalledValue = compositeMarshaller.marshal(value, marshalledValue);
		marshalledValue.put(dataKey, containerMarshaller.marshal(value, null));
		return marshalledValue;
	}

	@Override
	public T unmarshal(DBObject marshalledValue, T value) {
		value = compositeMarshaller.unmarshal(marshalledValue, value);
		value = containerMarshaller.unmarshal(marshalledValue.get(dataKey), value);
		return value;
	}
}
