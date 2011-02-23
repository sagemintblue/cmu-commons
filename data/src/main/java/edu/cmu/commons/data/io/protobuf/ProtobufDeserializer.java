package edu.cmu.commons.data.io.protobuf;

import java.io.InputStream;

import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;

import edu.cmu.commons.data.io.Deserializer;

/**
 * @author hazen
 * @param <E>
 */
public class ProtobufDeserializer<E extends Message> implements Deserializer<E> {
	@Override
	@SuppressWarnings("unchecked")
	public E deserialize(InputStream in, E entity) throws Exception {
		Builder builder = entity.newBuilderForType();
		if (!builder.mergeDelimitedFrom(in)) return null;
		return (E) builder.build();
	}
}
