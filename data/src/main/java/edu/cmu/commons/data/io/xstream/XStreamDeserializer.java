package edu.cmu.commons.data.io.xstream;

import java.io.InputStream;

import com.thoughtworks.xstream.XStream;

import edu.cmu.commons.data.io.Deserializer;

public class XStreamDeserializer<E> implements Deserializer<E> {
	private XStream xstream;

	public XStreamDeserializer(XStream xstream) {
		super();
		this.xstream = xstream;
	}

	@Override
	public E deserialize(InputStream in, E entity) throws Exception {
		xstream.fromXML(in, entity);
		return entity;
	}
}
