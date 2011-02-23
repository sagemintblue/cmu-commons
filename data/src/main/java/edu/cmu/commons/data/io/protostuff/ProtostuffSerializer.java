package edu.cmu.commons.data.io.protostuff;

import java.io.OutputStream;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;

import edu.cmu.commons.data.io.Serializer;

/**
 * @author hazen
 * @param <E>
 */
public class ProtostuffSerializer<E> implements Serializer<E> {
	private final Schema<E> schema;
	private final ThreadLocal<LinkedBuffer> linkedBuffer;

	public ProtostuffSerializer(Schema<E> schema,
			ThreadLocal<LinkedBuffer> linkedBuffer) {
		super();
		this.schema = schema;
		this.linkedBuffer = linkedBuffer;
	}

	@Override
	public void serialize(E entity, OutputStream out) throws Exception {
		ProtostuffIOUtil.writeDelimitedTo(out, entity, schema, linkedBuffer.get());
	}
}
