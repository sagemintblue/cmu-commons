package edu.cmu.commons.data.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import edu.cmu.commons.data.dao.BaseDao;

/**
 * Base MongoDB Dao implementation.
 * @author hazen
 * @param <E>
 * @param <I>
 */
public abstract class MongoDao<E, I> extends BaseDao<E, I> {
	private DBCollection collection;
	private Marshaller<E, DBObject> marshaller;

	private MongoDao(Class<E> entityClass, Class<I> idClass, DB database,
			Marshaller<E, DBObject> marshaller) {
		super(entityClass, idClass);
		this.collection = database.getCollection(entityClass.getName());
		this.marshaller = marshaller;
	}

	@Override
	public boolean isOpen() {
		return collection != null;
	}

	@Override
	public void close() {
		collection = null;
	}

	protected void ensureOpen() {
		if (collection == null) throw new IllegalStateException(
				"Dao has been closed");
	}

	@Override
	public void flush() {
		// no-op, though this should be revisited
	}

	@Override
	public long count() {
		ensureOpen();
		return collection.count();
	}

	@Override
	public void persist(E entity) {
		ensureOpen();
		collection.insert(marshaller.marshal(entity, null));
	}

	protected E find(I id, E entity) {
		DBObject dbo = collection.findOne(id);
		if (dbo == null) return null;
		return marshaller.unmarshal(dbo, entity);
	}

	@Override
	public E find(I id) {
		ensureOpen();
		try {
			return find(id, getEntityClass().newInstance());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void refresh(E entity) {
		ensureOpen();
		find(getEntityId(entity), entity);
	}

	@Override
	public E merge(E entity) {
		ensureOpen();
		DBObject dbo = marshaller.marshal(entity, null);
		DBObject merged =
				collection.findAndModify(
						new BasicDBObjectBuilder().add("_id", getEntityId(entity)).get(),
						null, null, false, dbo, true, true);
		return marshaller.unmarshal(merged, entity);
	}

	@Override
	public void remove(E entity) {
		ensureOpen();
		// TODO how to interpret WriteResult
		collection.remove(new BasicDBObject("_id", getEntityId(entity)));
	}

	protected abstract I getEntityId(E entity);
}
