package edu.cmu.commons.data.dao.jpa;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 * @author hazen
 * @param <E>
 *            Entity type.
 */
public class JPAEntityIterator<E> implements Iterator<E> {
	private final EntityManager entityManager;
	private final int batchSize;
	private int offset;
	private final TypedQuery<E> query;
	private List<E> results;
	private Iterator<E> itr;
	private E prev;

	public JPAEntityIterator(Class<E> entityClass, EntityManager entityManager,
			int batchSize, int offset) {
		super();
		this.entityManager = entityManager;
		this.batchSize = batchSize;
		this.offset = offset;
		CriteriaBuilder b = entityManager.getCriteriaBuilder();
		CriteriaQuery<E> c = b.createQuery(entityClass);
		c.from(entityClass);
		this.query = entityManager.createQuery(c).setMaxResults(batchSize);
		getNext(offset);
	}

	public JPAEntityIterator(Class<E> entityClass, EntityManager entityManager,
			int batchSize) {
		this(entityClass, entityManager, batchSize, 0);
	}

	private void getNext(int offset) {
		itr = null;
		query.setFirstResult(offset);
		results = query.getResultList();
		if (results == null || results.isEmpty())
			return;
		itr = results.iterator();
	}

	@Override
	public boolean hasNext() {
		return itr != null && itr.hasNext();
	}

	@Override
	public E next() {
		if (!hasNext())
			throw new NoSuchElementException();
		prev = itr.next();
		if (!itr.hasNext())
			getNext(offset += batchSize);
		return prev;
	}

	@Override
	public void remove() {
		entityManager.remove(prev);
	}
}
