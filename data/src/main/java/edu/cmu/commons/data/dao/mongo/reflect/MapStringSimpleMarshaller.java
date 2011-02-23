package edu.cmu.commons.data.dao.mongo.reflect;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Marshaller supporting {@code Map<String, V>} types, where {@code V} is a
 * simple type.
 * @author hazen
 * @param <V> Map simple value type.
 */
class MapStringSimpleMarshaller<V> extends MapMarshaller<String, V> {
	public static final Set<Class<?>> SIMPLE_MAP_TYPES = new HashSet<Class<?>>(
			Arrays.<Class<?>> asList(Map.class, HashMap.class, LinkedHashMap.class));

	public MapStringSimpleMarshaller(Class<V> valueClass,
			Class<? extends Map<String, V>> mapClass) {
		super(String.class, valueClass, mapClass);
		// TODO how to validate valueClass is simple type?
		// if (!SimpleMarshaller.SIMPLE_TYPES.contains(valueClass)) throw new
		// IllegalArgumentException(
		// "");
		if (!SIMPLE_MAP_TYPES.contains(mapClass)) throw new IllegalArgumentException(
				"Map type '" + mapClass.getName() + "' is unsupported");
	}

	@Override
	public Object marshal(Map<String, V> map, Object marshalledValue) {
		if (marshalledValue != null) {
			if (!(marshalledValue instanceof DBObject)) throw new IllegalArgumentException(
					"Marshalled value of type '" + marshalledValue.getClass().getName()
							+ "' is not instance of type '" + DBObject.class.getName() + "'");
			DBObject marshalledMap = (DBObject) marshalledValue;
			if (!marshalledMap.keySet().isEmpty()) {
				for (Entry<String, V> entry : map.entrySet()) {
					String key = entry.getKey();
					marshalledMap.put(key, entry.getValue());
				}
				return marshalledMap;
			}
		}
		return map;
	}

	@Override
	public Map<String, V> unmarshal(Object marshalledValue, Map<String, V> map) {
		if (!(marshalledValue instanceof BasicDBObject)) throw new IllegalArgumentException(
				"Marshalled value of type '" + marshalledValue.getClass().getName()
						+ "' is not instance of '" + BasicDBObject.class.getName() + "'");
		BasicDBObject marshalledMap = (BasicDBObject) marshalledValue;
		if (map != null) {
			for (String key : marshalledMap.keySet())
				map.put(key, getValueClass().cast(marshalledMap.get(key)));
			return map;
		}
		return getMapClass().cast(marshalledMap);
	}
}
