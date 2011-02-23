package edu.cmu.commons.data.io.xstream;

import java.io.InputStream;
import java.io.OutputStream;

import edu.cmu.commons.data.io.Serialization;

public class XStreamSerialization<E> implements Serialization<E> {
	private XStreamSerializer<E> serializer;
	private XStreamDeserializer<E> deserializer;

	public XStreamSerialization(XStreamSerializer<E> serializer,
			XStreamDeserializer<E> deserializer) {
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
