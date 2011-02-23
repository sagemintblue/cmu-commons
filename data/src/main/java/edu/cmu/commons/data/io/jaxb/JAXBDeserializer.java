package edu.cmu.commons.data.io.jaxb;

import java.io.InputStream;

import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import edu.cmu.commons.data.io.Deserializer;

public class JAXBDeserializer<E> implements Deserializer<E> {
	protected Class<E> entityClass;
	protected Unmarshaller unmarshaller;

	public JAXBDeserializer(Class<E> entityClass, Unmarshaller unmarshaller) {
		super();
		this.entityClass = entityClass;
		this.unmarshaller = unmarshaller;
	}

	@Override
	public E deserialize(InputStream in, E entity) throws Exception {
		return unmarshaller.unmarshal(new StreamSource(in), entityClass).getValue();
	}
}
