package edu.cmu.commons.data.dao.mongo.reflect;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

import edu.cmu.commons.data.dao.mongo.Marshaller;

/**
 * Marshaller supporting {@code Map<K, V>} types, where both {@code K} and
 * {@code V} instances are supported by other marshallers.
 * @author hazen
 * @param <K> Map key type.
 * @param <V> Map value type.
 */
class MapKeyValueMarshaller<K, V> extends MapMarshaller<K, V> {
	private static final String KEY_KEY = "key";
	private static final String VALUE_KEY = "value";
	private final Marshaller<K, Object> keyMarshaller;
	private final Marshaller<V, Object> valueMarshaller;

	public MapKeyValueMarshaller(Class<K> keyClass, Class<V> valueClass,
			Class<? extends Map<K, V>> mapClass, Marshaller<K, Object> keyMarshaller,
			Marshaller<V, Object> valueMarshaller) {
		super(keyClass, valueClass, mapClass);
		this.keyMarshaller = keyMarshaller;
		this.valueMarshaller = valueMarshaller;
	}

	@Override
	public Object marshal(Map<K, V> map, Object marshalledObject) {
		Map<Object, Object> marshalledMap =
				marshalledObject == null ? new LinkedHashMap<Object, Object>()
						: unpackMap(marshalledObject);
		for (Entry<K, V> entry : map.entrySet()) {
			Object marshalledKey = keyMarshaller.marshal(entry.getKey(), null);
			Object marshalledValue =
					valueMarshaller.marshal(entry.getValue(),
							marshalledMap.get(marshalledKey));
			marshalledMap.put(marshalledKey, marshalledValue);
		}
		return packMap(marshalledMap);
	}

	@Override
	public Map<K, V> unmarshal(Object marshalledObject, Map<K, V> map) {
		Map<Object, Object> marshalledMap = unpackMap(marshalledObject);
		if (map == null) {
			try {
				map = getMapClass().newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Entry<Object, Object> marshalledEntry : marshalledMap.entrySet()) {
			K key = keyMarshaller.unmarshal(marshalledEntry.getKey(), null);
			V value =
					valueMarshaller.unmarshal(marshalledEntry.getValue(), map.get(key));
			map.put(key, value);
		}
		return map;
	}

	private DBObject packMap(Map<Object, Object> marshalledMap) {
		BasicDBList list = new BasicDBList();
		for (Entry<Object, Object> entry : marshalledMap.entrySet()) {
			Map<String, Object> element = new TreeMap<String, Object>();
			element.put(KEY_KEY, entry.getKey());
			element.put(VALUE_KEY, entry.getValue());
			list.add(element);
		}
		return list;
	}

	private Map<Object, Object> unpackMap(Object marshalledValue) {
		if (!(marshalledValue instanceof BasicDBList)) throw new IllegalArgumentException(
				"Marshalled map has type '" + marshalledValue.getClass().getName()
						+ "' but expecting type '" + BasicDBList.class.getName() + "'");
		BasicDBList list = (BasicDBList) marshalledValue;
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		for (Object element : list) {
			if (!(element instanceof DBObject)) throw new IllegalArgumentException(
					"Element of marshalled map has type '" + element.getClass().getName()
							+ "' but expecting type '" + DBObject.class.getName() + "'");
			DBObject entry = (DBObject) element;
			Object key = entry.get(KEY_KEY);
			Object value = entry.get(VALUE_KEY);
			map.put(key, value);
		}
		return map;
	}
}
