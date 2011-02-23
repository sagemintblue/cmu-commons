package edu.cmu.commons.data.io.protostuff;

import java.io.InputStream;

import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;

import edu.cmu.commons.data.io.Deserializer;

/**
 * @author hazen
 * @param <E>
 */
public class ProtostuffDeserializer<E> implements Deserializer<E> {
	private final Schema<E> schema;

	public ProtostuffDeserializer(Schema<E> schema) {
		super();
		this.schema = schema;
	}

	@Override
	public E deserialize(InputStream in, E entity) throws Exception {
		ProtostuffIOUtil.mergeDelimitedFrom(in, entity, schema);
		return entity;
	}
}
