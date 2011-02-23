package edu.cmu.commons.data.dao;

import javax.persistence.EntityManager;

/**
 * High-level contract for Data Access Objects. This interface intentionally
 * reflects {@link EntityManager}, but it assumes that a given Dao instance will
 * be responsible for persistence of instances of a single entity type alone,
 * and not all entity types supported by a JPA Persistence Unit.
 * 
 * @author hazen
 * @param <E> Entity type.
 * @param <I> Entity identifier type.
 */
public interface Dao<E, I> extends Iterable<E> {
	/**
	 * @return {@code true} if this Dao instance is "open", {@code false}
	 * otherwise.
	 * @see EntityManager#isOpen()
	 */
	public boolean isOpen();

	/**
	 * @see EntityManager#close()
	 */
	public void close();

	/**
	 * @return the number of instances of type E in the persistent store.
	 */
	public long count();

	/**
	 * @param entity
	 * @see EntityManager#persist(Object)
	 */
	public void persist(E entity);

	/**
	 * @param id
	 * @see EntityManager#find(Class, Object)
	 */
	public E find(I id);

	/**
	 * @param id
	 * @see EntityManager#getReference(Class, Object)
	 */
	public E getReference(I id);

	/**
	 * @param entity
	 * @see EntityManager#refresh(Object)
	 */
	public void refresh(E entity);

	/**
	 * @param entity
	 * @see EntityManager#merge(Object)
	 */
	public E merge(E entity);

	/**
	 * @param entity
	 * @see EntityManager#remove(Object)
	 */
	public void remove(E entity);

	/**
	 * Removes all entities of type {@code E} from persistent store.
	 */
	public void removeAll();

	/**
	 * @see EntityManager#flush()
	 */
	public void flush();
}
