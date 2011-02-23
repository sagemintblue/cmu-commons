package edu.cmu.commons.data.dao.mongo.jap;

import java.util.HashMap;
import java.util.Map;

import edu.cmu.commons.data.dao.mongo.EntityMarshaller;
import edu.cmu.commons.data.dao.mongo.EntityMarshallerFactory;

public class JAPMarshallerFactory implements EntityMarshallerFactory {
	private static final EntityMarshallerFactory instance =
			new JAPMarshallerFactory();

	public static EntityMarshallerFactory getInstance() {
		return instance;
	}

	private final Map<Class<?>, EntityMarshaller<?>> entityMarshallerCache =
			new HashMap<Class<?>, EntityMarshaller<?>>();

	private JAPMarshallerFactory() {}

	@Override
	@SuppressWarnings("unchecked")
	public <E> EntityMarshaller<E> getEntityMarshaller(Class<E> entityClass) {
		return (EntityMarshaller<E>) entityMarshallerCache.get(entityClass);
	}

	public <E> void registerEntityMarshaller(Class<E> entityClass,
			EntityMarshaller<E> entityMarshaller) {
		entityMarshallerCache.put(entityClass, entityMarshaller);
	}
}
