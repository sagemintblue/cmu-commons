package edu.cmu.commons.data.dao.mongo.reflect;

import java.util.Map;
import java.util.Map.Entry;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import edu.cmu.commons.data.dao.mongo.Marshaller;

/**
 * Marshaller supporting {@code Map<String, V>} types, where {@code V} instances
 * are supported by another marshaller.
 * @author hazen
 * @param <V> Map value type.
 */
class MapStringValueMarshaller<V> extends MapMarshaller<String, V> {
	private final Marshaller<V, Object> valueMarshaller;

	public MapStringValueMarshaller(Class<String> keyClass, Class<V> valueClass,
			Class<? extends Map<String, V>> mapClass,
			Marshaller<V, Object> valueMarshaller) {
		super(keyClass, valueClass, mapClass);
		this.valueMarshaller = valueMarshaller;
	}

	@Override
	public Object marshal(Map<String, V> map, Object marshalledValue) {
		if (marshalledValue == null) marshalledValue = new BasicDBObject();
		if (!(marshalledValue instanceof DBObject)) throw new IllegalArgumentException(
				"Marshalled value of type '" + marshalledValue.getClass().getName()
						+ "' is not instance of type '" + DBObject.class.getName() + "'");
		DBObject marshalledMap = (DBObject) marshalledValue;
		for (Entry<String, V> entry : map.entrySet()) {
			String key = entry.getKey();
			marshalledMap.put(key,
					valueMarshaller.marshal(entry.getValue(), marshalledMap.get(key)));
		}
		return marshalledMap;
	}

	@Override
	public Map<String, V> unmarshal(Object marshalledValue, Map<String, V> map) {
		if (!(marshalledValue instanceof DBObject)) throw new IllegalArgumentException(
				"Marshalled value of type '" + marshalledValue.getClass().getName()
						+ "' is not instance of type '" + DBObject.class.getName() + "'");
		DBObject marshalledMap = (DBObject) marshalledValue;
		if (map == null) {
			try {
				map = getMapClass().newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (String key : marshalledMap.keySet()) {
			V value = valueMarshaller.unmarshal(marshalledMap.get(key), map.get(key));
			map.put(key, value);
		}
		return map;
	}
}
