package edu.cmu.commons.data.io.jaxb;

import java.io.InputStream;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Simplifies deserialization of JAXB bound objects of type E from an XML feed.
 * @author hazen
 * @param <E>
 */
public class JAXBXMLEventReaderDeserializer<E> extends JAXBDeserializer<E> {
	private XMLInputFactory xmlif = XMLInputFactory.newInstance();
	private InputStream in;
	private XMLEventReader reader;
	private XMLEventReader filteringReader;

	/**
	 * @param entityClass JAXB bound entity class, labeled with
	 * {@link XmlRootElement}.
	 * @param unmarshaller the JAXB unmarshaller used to recover instances of
	 * entityClass from the input XML stream.
	 * @param in XML input stream.
	 * @param elementName name of XML elements in XML input stream which
	 * correspond to instances of entityClass.
	 * @param namespaceURI namespace URI associated with elementName. May be
	 * <code>null</code>, in which case namespace comparison is not performed.
	 * @throws XMLStreamException
	 */
	public JAXBXMLEventReaderDeserializer(Class<E> entityClass,
			Unmarshaller unmarshaller, InputStream in, final String elementName,
			final String namespaceURI) throws XMLStreamException {
		super(entityClass, unmarshaller);
		this.in = in;

		/*
		 * create two xml event readers, the first to parse entities from, the
		 * second to filter forward through the input stream till we find the next
		 * valid entity start point.
		 */
		reader = xmlif.createXMLEventReader(in);
		filteringReader = xmlif.createFilteredReader(reader, new EventFilter() {
			public boolean accept(XMLEvent event) {
				if (!event.isStartElement()) return false;
				StartElement se = event.asStartElement();
				QName name = se.getName();
				if (!name.getLocalPart().equals(elementName)) return false;
				if (namespaceURI == null) return true;
				String currentNamespaceURI = name.getNamespaceURI();
				if (currentNamespaceURI == null) return true;
				return currentNamespaceURI.equals(namespaceURI);
			}
		});
	}

	/**
	 * @param in input stream must match that given to ctor.
	 * @see edu.cmu.commons.data.io.jaxb.JAXBDeserializer#deserialize(java.io.InputStream,
	 * java.lang.Object)
	 */
	@Override
	public E deserialize(InputStream in, E entity) throws Exception {
		assert this.in == in;
		if (filteringReader.peek() == null) return null;
		return unmarshaller.unmarshal(reader, entityClass).getValue();
	}
}
