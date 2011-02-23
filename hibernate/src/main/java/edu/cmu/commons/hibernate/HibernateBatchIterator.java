package edu.cmu.commons.hibernate;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;

/**
 * Iterator which handles exhaustive iteration over results of a Hibernate Query
 * containing a "limit :limit offset :offset" clause, allowing batch retrieval
 * of result sets.
 * @param <T>
 */
public class HibernateBatchIterator<T> implements Iterator<T> {
	/**
	 * the appropriate HQL limit clause to use in Query instances passed to the
	 * constructor of this class.
	 */
	public static final String HQL_LIMIT_CLAUSE = " limit :limit offset :offset ";

	public static enum QueryType {
		HQL, SQL;
	}

	private SessionFactory sessionFactory;
	private String queryString;
	private QueryType queryType;
	private int batchSize;
	private int offset;
	private boolean exhausted = false;
	private List<T> segment;
	private Iterator<T> segmentIterator;

	/**
	 * @param sessionFactory
	 * @param queryString the query whose results we'll iterate over.
	 * @param queryType
	 * @param batchSize the number of results we'll retrieve (and iterate over)
	 * with a single query execution.
	 */
	public HibernateBatchIterator(SessionFactory sessionFactory,
			String queryString, QueryType queryType, int batchSize) {
		this.sessionFactory = sessionFactory;
		this.queryString = queryString;
		this.queryType = queryType;
		this.batchSize = batchSize;
		getNextSegment();
	}

	@SuppressWarnings("unchecked")
	private void getNextSegment() {
		StatelessSession session = null;
		session = sessionFactory.openStatelessSession();
		Query query = null;
		if (queryType == QueryType.HQL) {
			query = session.createQuery(queryString);
		} else {
			query = session.createSQLQuery(queryString);
		}
		query.setInteger("limit", batchSize);
		query.setInteger("offset", offset);
		segment = query.list();
		session.close();
		segmentIterator = segment.iterator();
		offset += batchSize;
		if (segment.size() < batchSize) exhausted = true;
	}

	@Override
	public boolean hasNext() {
		if (segmentIterator.hasNext()) return true;
		if (exhausted) return false;
		getNextSegment();
		return segmentIterator.hasNext();
	}

	@Override
	public T next() {
		if (segmentIterator.hasNext()) return segmentIterator.next();
		if (exhausted) throw new NoSuchElementException();
		getNextSegment();
		return segmentIterator.next();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
