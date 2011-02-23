package edu.cmu.commons.data.dao.jpa;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import edu.cmu.commons.data.dao.Dao;

/**
 * Dao implementation which delegates to an EntityManager.
 * @author hazen
 * @param <E>
 * @param <I>
 */
public abstract class JPADao<E, I> implements Dao<E, I> {
	@Inject
	protected final EntityManager entityManager = null;
	protected final Class<E> entityClass;

	public JPADao(Class<E> entityClass) {
		super();
		this.entityClass = entityClass;
	}

	@Override
	public boolean isOpen() {
		return entityManager.isOpen();
	}

	@Override
	public void close() {
		entityManager.close();
	}

	@Override
	@Transactional
	public void flush() {
		entityManager.flush();
	}

	@Override
	@Transactional
	public long count() {
		return entityManager.createQuery( //
				"SELECT COUNT(e) FROM " + entityClass.getName() + " e" //
				, Long.class) //
				.getSingleResult();
	}

	@Override
	@Transactional
	public void persist(E entity) {
		entityManager.persist(entity);
	}

	@Override
	public E find(I id) {
		return entityManager.find(entityClass, id);
	}

	@Override
	public E getReference(I id) {
		return entityManager.getReference(entityClass, id);
	}

	@Override
	@Transactional
	public void refresh(E entity) {
		entityManager.refresh(entity);
	}

	@Override
	@Transactional
	public E merge(E entity) {
		return entityManager.merge(entity);
	}

	@Override
	@Transactional
	public void remove(E entity) {
		entityManager.remove(entity);
	}

	/**
	 * Delegates to {@link #removeAllOneByOne()}. Override and delegate to
	 * {@link #removeAllBulkDelete()} if possible.
	 */
	@Override
	@Transactional
	public void removeAll() {
		removeAllOneByOne();
	}

	/**
	 * Work around for {@link #removeAll()} for those Entity types containing
	 * ElementCollection fields.
	 */
	@Transactional
	protected void removeAllOneByOne() {
		int count = 0;
		for (E entity : this) {
			if (++count % 100 == 0) {
				entityManager.flush();
				entityManager.clear();
			}
			remove(entity);
		}
	}

	/**
	 * This DOES NOT WORK when entity class contains an ElementCollection and no
	 * CASCADE ON DELETE action has been specified on the CollectionTable's
	 * foreign key constraint. Hibernate's DDL generation isn't smart enough to do
	 * this (as of 3.6.0.RC2), so it must be done manually if you want to use this
	 * method. Otherwise, you should use {@link #removeAllOneByOne()}.
	 * @see <a
	 * href="http://opensource.atlassian.com/projects/hibernate/browse/HHH-5529">HHH-5529</a>
	 * @see <a
	 * href="http://opensource.atlassian.com/projects/hibernate/browse/HHH-5528">HHH-5528</a>
	 * @see <a
	 * href="http://opensource.atlassian.com/projects/hibernate/browse/HHH-695">HHH-695</a>
	 */
	@Transactional
	protected void removeAllBulkDelete() {
		entityManager.createQuery("DELETE FROM " + entityClass.getName())
				.executeUpdate();
	}

	@Override
	@Transactional
	public Iterator<E> iterator() {
		return new JPAEntityIterator<E>(entityClass, entityManager, 100);
	}

	/**
	 * Syntactic sugar for creation of JPQL {@code "SELECT e FROM <E> e ..."}
	 * queries. Parameter placeholders within query must correspond to integer
	 * indices (1-based) of specified parameter values. For instance,
	 * {@code find("WHERE e.first = ?1 AND e.last = ?1", "Jo")} selects all
	 * {@code E} instances whose {@code first} and {@code last} properties are
	 * equal to the String {@code "Jo"}.
	 * @param querySuffix suffix to append to {@code "SELECT e FROM <E> e "}.
	 * @param parameters zero or more values to assign to named parameters within
	 * {@code querySuffix}.
	 * @return JPQL select where query.
	 */
	protected TypedQuery<E> select(String querySuffix, Object... parameters) {
		TypedQuery<E> q =
				entityManager.createQuery("SELECT e FROM " + entityClass.getName()
						+ " e " + querySuffix, entityClass);
		for (int i = 0; i < parameters.length; ++i)
			q.setParameter(Integer.toString(i + 1), parameters[i]);
		return q;
	}

	/**
	 * Syntactic sugar for creation of JPQL {@code "FROM <E> WHERE ..."} queries.
	 * Parameter placeholders within query must correspond to integer indices
	 * (1-based) of specified parameter values. For instance,
	 * {@code find("first = ?1 AND last = ?1", "Jo")} selects all {@code E}
	 * instances whose {@code first} and {@code last} properties are equal to the
	 * String {@code "Jo"}.
	 * @param querySuffix suffix to append to {@code "FROM <E> WHERE "}.
	 * @param parameters zero or more values to assign to named parameters within
	 * {@code querySuffix}.
	 * @return JPQL select where query.
	 */
	protected TypedQuery<E> selectWhere(String querySuffix, Object... parameters) {
		TypedQuery<E> q =
				entityManager.createQuery("FROM " + entityClass.getName() + " WHERE "
						+ querySuffix, entityClass);
		for (int i = 0; i < parameters.length; ++i)
			q.setParameter(Integer.toString(i + 1), parameters[i]);
		return q;
	}

	/**
	 * Executes query returned by a call to {@link #select(String, Object...)}.
	 * @param querySuffix suffix to append to {@code "SELECT e FROM <E> e "}.
	 * @param parameters zero or more values to assign to named parameters within
	 * {@code querySuffix}.
	 * @return all matching entities of type {@code E}.
	 */
	protected List<E> find(String querySuffix, Object... parameters) {
		return select(querySuffix, parameters).getResultList();
	}

	/**
	 * Executes query returned by a call to {@link #select(String, Object...)}.
	 * @param querySuffix suffix to append to {@code "SELECT e FROM <E> e "}.
	 * @param parameters zero or more values to assign to named parameters within
	 * {@code querySuffix}.
	 * @return the single matching entity of type {@code E}, or {@code null} if no
	 * such entity exists.
	 */
	protected E get(String querySuffix, Object... parameters) {
		try {
			return select(querySuffix, parameters).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Executes query returned by a call to
	 * {@link #selectWhere(String, Object...)}.
	 * @param querySuffix suffix to append to {@code "SELECT FROM <E> WHERE "}.
	 * @param parameters zero or more values to assign to named parameters within
	 * {@code querySuffix}.
	 * @return all matching entities of type {@code E}.
	 */
	protected List<E> findWhere(String querySuffix, Object... parameters) {
		return selectWhere(querySuffix, parameters).getResultList();
	}

	/**
	 * Executes query returned by a call to
	 * {@link #selectWhere(String, Object...)}.
	 * @param querySuffix suffix to append to {@code "SELECT e FROM <E> e "}.
	 * @param parameters zero or more values to assign to named parameters within
	 * {@code querySuffix}.
	 * @return the single matching entity of type {@code E}, or {@code null} if no
	 * such entity exists.
	 */
	protected E getWhere(String querySuffix, Object... parameters) {
		try {
			return selectWhere(querySuffix, parameters).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
