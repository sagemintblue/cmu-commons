package edu.cmu.commons.data.dao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.persistence.EntityNotFoundException;
import javax.persistence.Id;


/**
 * Factory which creates {@link Proxy} instances for type E. When a client first
 * accesses a method of a returned Proxy which does not resemble
 * <code>@Id I get...()</code>, the Proxy's InvocationHandler will attempt to
 * fetch the real E instance from the persistent store using the provided
 * {@link Dao}.
 * @author hazen
 * @param <E> Entity type.
 * @param <I> Entity identifier type.
 */
public class ProxyFactory<E, I> {
	private final Class<E> entityClass;
	private final Class<I> idClass;
	private final Dao<E, I> dao;

	public ProxyFactory(Class<E> entityClass, Class<I> idClass, Dao<E, I> dao) {
		super();
		this.entityClass = entityClass;
		this.idClass = idClass;
		this.dao = dao;
	}

	@SuppressWarnings("unchecked")
	public E getReference(final I id) {
		if (id == null) throw new IllegalArgumentException("Argument 'id' is null");
		return (E) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
				new Class<?>[] { entityClass }, new InvocationHandler() {
					private E delegate;

					@Override
					public Object invoke(Object proxy, Method method, Object[] args)
							throws Throwable {
						if (method.getName().startsWith("get")
								&& method.isAnnotationPresent(Id.class)
								&& method.getReturnType().equals(idClass)) return id;
						if (delegate == null) delegate = dao.find(id);
						if (delegate == null) {
							// TODO support persistence of new (empty) entity here?
							throw new EntityNotFoundException();
						}
						return method.invoke(delegate, args);
					}
				});
	}
}
