package edu.cmu.commons.data.io.protobuf;

import java.io.OutputStream;

import com.google.protobuf.Message;

import edu.cmu.commons.data.io.Serializer;

/**
 * @author hazen
 * @param <E>
 */
public class ProtobufSerializer<E extends Message> implements Serializer<E> {
	@Override
	public void serialize(E entity, OutputStream out) throws Exception {
		entity.writeDelimitedTo(out);
	}
}
