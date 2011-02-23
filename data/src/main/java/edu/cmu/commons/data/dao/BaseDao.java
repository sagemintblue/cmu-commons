package edu.cmu.commons.data.dao;


/**
 * Support class for Dao implementations.
 * @author hazen
 * @param <E> Entity type.
 * @param <I> Entity identifier type.
 */
public abstract class BaseDao<E, I> implements Dao<E, I> {
	private final Class<E> entityClass;
	private final Class<I> idClass;
	private final ProxyFactory<E, I> proxyFactory;

	public BaseDao(Class<E> entityClass, Class<I> idClass) {
		super();
		this.entityClass = entityClass;
		this.idClass = idClass;
		this.proxyFactory = new ProxyFactory<E, I>(entityClass, idClass, this);
	}

	public Class<E> getEntityClass() {
		return entityClass;
	}

	public Class<I> getIdClass() {
		return idClass;
	}

	public ProxyFactory<E, I> getProxyFactory() {
		return proxyFactory;
	}

	@Override
	public E getReference(I id) {
		return proxyFactory.getReference(id);
	}
}
