package edu.cmu.commons.data.dao.mongo.reflect;

import java.util.Map;

/**
 * Base class for {@link ContainerMarshaller}s supporting {@code Map<K, V>}
 * types.
 * @author hazen
 * @param <K> Map key type.
 * @param <V> Map value type.
 */
abstract class MapMarshaller<K, V> implements
		ContainerMarshaller<Map<K, V>, Object> {
	private final Class<K> keyClass;
	private final Class<V> valueClass;
	private final Class<? extends Map<K, V>> mapClass;

	public MapMarshaller(Class<K> keyClass, Class<V> valueClass,
			Class<? extends Map<K, V>> mapClass) {
		super();
		this.keyClass = keyClass;
		this.valueClass = valueClass;
		this.mapClass = mapClass;
	}

	public Class<K> getKeyClass() {
		return keyClass;
	}

	public Class<V> getValueClass() {
		return valueClass;
	}

	public Class<? extends Map<K, V>> getMapClass() {
		return mapClass;
	}
}
