package edu.cmu.commons.data.io.jaxb;

import java.io.InputStream;
import java.io.OutputStream;

import edu.cmu.commons.data.io.Serialization;

public class JAXBSerialization<E> implements Serialization<E> {
	private JAXBSerializer<E> serializer;
	private JAXBDeserializer<E> deserializer;

	public JAXBSerialization(JAXBSerializer<E> serializer,
			JAXBDeserializer<E> deserializer) {
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
