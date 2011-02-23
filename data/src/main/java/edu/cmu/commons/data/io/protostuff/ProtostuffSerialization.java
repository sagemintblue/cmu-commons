package edu.cmu.commons.data.io.protostuff;

import java.io.InputStream;
import java.io.OutputStream;

import edu.cmu.commons.data.io.Serialization;

public class ProtostuffSerialization<E> implements Serialization<E> {
	private final ProtostuffSerializer<E> serializer;
	private final ProtostuffDeserializer<E> deserializer;

	public ProtostuffSerialization(ProtostuffSerializer<E> serializer,
			ProtostuffDeserializer<E> deserializer) {
		super();
		this.serializer = serializer;
		this.deserializer = deserializer;
	}

	@Override
	public void serialize(E entity, OutputStream out) throws Exception {
		serializer.serialize(entity, out);
	}

	@Override
	public E deserialize(InputStream in, E entity) throws Exception {
		return deserializer.deserialize(in, entity);
	}
}
