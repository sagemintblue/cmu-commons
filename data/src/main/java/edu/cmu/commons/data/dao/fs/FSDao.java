package edu.cmu.commons.data.dao.fs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import edu.cmu.commons.data.dao.BaseDao;
import edu.cmu.commons.data.io.Serialization;

/**
 * FileSystem Dao implementation. Each entity is serialized independently
 * (serialization is pluggable) and stored in a separate file in a directory
 * hierarchy which reflects the entity type <code>E</code>'s class name.
 * Filenames are formed from the string-ified version of the entity id.
 * Therefore, the entity identifier type <code>I</code> must support
 * {@link Object#toString()} in a way which preserves uniqueness, and fulfills
 * any naming constraints of the underlying filesystem.
 * @author hazen
 * @param <E> Entity type.
 * @param <I> Entity unique identifier type.
 */
public abstract class FSDao<E, I> extends BaseDao<E, I> {
	private final File dataPath;
	private final File entityPath;
	private final Serialization<E> serialization;

	public FSDao(File dataPath, Class<E> entityClass, Class<I> idClass,
			Serialization<E> serialization) {
		super(entityClass, idClass);
		this.dataPath = dataPath;
		this.entityPath =
				new File(this.dataPath, entityClass.getName().replaceAll("\\.", "/"));
		this.serialization = serialization;
	}

	/**
	 * @param entity
	 * @return the unique entity id.
	 */
	protected abstract I getId(E entity);

	/**
	 * Uses default ctor to construct new uninitialized entity instance. Override
	 * to specialize construction of new entity instances.
	 * @return a new entity instance.
	 * @throws Exception
	 */
	protected E createEntity() throws Exception {
		return getEntityClass().newInstance();
	}

	private File getEntityFileById(I id) {
		return new File(entityPath, id.toString());
	}

	private File getEntityFile(E entity) {
		return getEntityFileById(getId(entity));
	}

	private void write(E entity, File entityFile) throws Exception {
		OutputStream out =
				new BufferedOutputStream(new FileOutputStream(entityFile));
		serialization.serialize(entity, out);
		out.close();
	}

	private void read(File entityFile, E entity) throws Exception {
		InputStream in = new BufferedInputStream(new FileInputStream(entityFile));
		serialization.deserialize(in, entity);
		in.close();
	}

	@Override
	public void flush() {
		// nothing to flush
		/* TODO implement write-behind caching strategy to avoid immediate write on
		 * persist */
	}

	@Override
	public boolean isOpen() {
		// always open so long as underlying file system is available
		return true;
	}

	@Override
	public void close() {
		// nothing to close
	}

	@Override
	public long count() {
		// number of files within path
		return entityPath.list().length;
	}

	@Override
	public void refresh(E entity) {
		File entityFile = getEntityFile(entity);
		if (!entityFile.exists()) throw new EntityNotFoundException();
		try {
			read(entityFile, entity);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void persist(E entity) {
		File entityFile = getEntityFile(entity);
		if (entityFile.exists()) throw new EntityExistsException();
		try {
			write(entity, entityFile);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public E find(I id) {
		File entityFile = getEntityFileById(id);
		E entity;
		try {
			entity = createEntity();
			read(entityFile, entity);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return entity;
	}

	@Override
	public E merge(E entity) {
		File entityFile = getEntityFile(entity);
		try {
			if (entityFile.exists()) {
				read(entityFile, entity);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return entity;
	}

	@Override
	public void remove(E entity) {
		File entityFile = getEntityFile(entity);
		if (entityFile.exists()) entityFile.delete();
	}
}
