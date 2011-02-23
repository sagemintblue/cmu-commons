package edu.cmu.commons.data.io.jaxb;

import java.io.OutputStream;

import javax.xml.bind.Marshaller;

import edu.cmu.commons.data.io.Serializer;

public class JAXBSerializer<E> implements Serializer<E> {
	private Marshaller marshaller;

	public JAXBSerializer(Marshaller marshaller) {
		super();
		this.marshaller = marshaller;
	}

	@Override
	public void serialize(E entity, OutputStream out) throws Exception {
		marshaller.marshal(entity, out);
	}
}
