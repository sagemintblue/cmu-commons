package edu.cmu.commons.data.dao.mongo.reflect;

import java.util.Collection;

/**
 * Base class for {@link ContainerMarshaller}s supporting {@link Collection}
 * types.
 * @author hazen
 * @param <E> Element type.
 */
abstract class CollectionMarshaller<E> implements
		ContainerMarshaller<Collection<E>, Object> {
	private final Class<E> elementClass;
	private final Class<? extends Collection<E>> collectionClass;

	public CollectionMarshaller(Class<E> elementClass,
			Class<? extends Collection<E>> collectionClass) {
		super();
		this.elementClass = elementClass;
		this.collectionClass = collectionClass;
	}

	public Class<E> getElementClass() {
		return elementClass;
	}

	public Class<? extends Collection<E>> getCollectionClass() {
		return collectionClass;
	}
}
