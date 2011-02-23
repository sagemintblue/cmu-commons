package edu.cmu.commons.data.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Dao which multiplexes over a collection of Dao instances.
 * 
 * @author hazen
 * @param <E>
 * @param <I>
 */
public class CompositeDao<E, I> implements Dao<E, I> {
	private final List<Dao<E, I>> daos;
	private final Dao<E, I> primaryDao;

	public CompositeDao(List<Dao<E, I>> daos) {
		super();
		if (daos.isEmpty())
			throw new IllegalArgumentException("Daos list is empty");
		this.daos = new ArrayList<Dao<E, I>>(daos);
		this.primaryDao = this.daos.get(0);
	}

	@Override
	public boolean isOpen() {
		for (Dao<E, I> dao : daos)
			if (dao.isOpen())
				return true;
		return false;
	}

	@Override
	public void close() {
		for (Dao<E, I> dao : daos)
			dao.close();
	}

	@Override
	public void flush() {
		for (Dao<E, I> dao : daos)
			dao.flush();
	}

	@Override
	public long count() {
		return primaryDao.count();
	}

	@Override
	public void persist(E entity) {
		for (Dao<E, I> dao : daos)
			dao.persist(entity);
	}

	@Override
	public E find(I id) {
		return primaryDao.find(id);
	}

	@Override
	public E getReference(I id) {
		return primaryDao.getReference(id);
	}

	@Override
	public void refresh(E entity) {
		primaryDao.refresh(entity);
	}

	@Override
	public E merge(E entity) {
		return primaryDao.merge(entity);
	}

	@Override
	public void remove(E entity) {
		for (Dao<E, I> dao : daos)
			dao.remove(entity);
	}

	@Override
	public void removeAll() {
		for (Dao<E, I> dao : daos)
			dao.removeAll();
	}

	@Override
	public Iterator<E> iterator() {
		// TODO
		throw new UnsupportedOperationException();
	}
}
