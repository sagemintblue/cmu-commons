package edu.cmu.commons.data.io.xstream;

import java.io.OutputStream;

import com.thoughtworks.xstream.XStream;

import edu.cmu.commons.data.io.Serializer;

public class XStreamSerializer<E> implements Serializer<E> {
	private XStream xstream;

	@Override
	public void serialize(E entity, OutputStream out) throws Exception {
		xstream.toXML(entity, out);
	}
}
