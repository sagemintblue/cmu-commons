package edu.cmu.commons.data.io.protobuf;

import java.io.InputStream;
import java.io.OutputStream;

import com.google.protobuf.Message;

import edu.cmu.commons.data.io.Serialization;

/**
 * @author hazen
 * @param <E>
 */
public class ProtobufSerialization<E extends Message> implements
		Serialization<E> {
	private final ProtobufSerializer<E> serializer;
	private final ProtobufDeserializer<E> deserializer;

	public ProtobufSerialization(ProtobufSerializer<E> serializer,
			ProtobufDeserializer<E> deserializer) {
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
