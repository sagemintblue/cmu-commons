package edu.cmu.commons.data.test.jaxb;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cmu.commons.data.io.Deserializer;
import edu.cmu.commons.data.io.jaxb.JAXBXMLEventReaderDeserializer;

/**
 * Helper class for simple instrumentation of JAXB tests. Derived test classes
 * should include sibling resource files named "example.xml" and
 * "example-list.xml" containing a single element mapped to type T and a list of
 * elements mapped to type T respectively. These two filenames may be redefined
 * via {@link #setExampleFilename(String)} and
 * {@link #setExampleListFilename(String)}.
 * <p>
 * In addition, derived test classes may supply namespace URI to schema filename
 * mappings via the {@link #getSchemaFilenames()} map. This mapping is used by
 * the {@link #createSchemata()} test method when generating ".xsd" output
 * files. All such files are created beneath the "./target/generated-schemata"
 * directory, by default. This path may be set via
 * {@link #setSchemataOutputPath(String)}.
 * @author hazen
 * @param <T> Type labeled as {@link XmlRootElement} on which JAXB tests should
 * focus.
 */
public abstract class JAXBTest<T> {
	private static final Logger log = LoggerFactory.getLogger(JAXBTest.class);
	protected Class<T> elementClass;
	protected String elementName;
	protected String namespace;
	protected Class<?>[] supportingClasses;
	protected JAXBContext context;
	protected Unmarshaller unmarshaller;
	protected Marshaller marshaller;
	protected String schemataOutputPath = "./target/generated-schemata";
	protected Map<String, String> schemaFilenames = new HashMap<String, String>();
	protected String exampleFilename = "example.xml";
	protected String exampleListFilename = "example-list.xml";

	public JAXBTest(Class<T> elementClass, String elementName, String namespace,
			Class<?>... supportingClasses) throws JAXBException {
		this.elementClass = elementClass;
		this.elementName = elementName;
		this.namespace = namespace;
		this.supportingClasses = supportingClasses;
		Class<?>[] boundClasses = new Class<?>[] { elementClass };
		if (supportingClasses != null) {
			boundClasses = Arrays.copyOf(boundClasses, supportingClasses.length + 1);
			int i = 0;
			for (Class<?> cls : supportingClasses)
				boundClasses[++i] = cls;
		}
		context = JAXBContext.newInstance(boundClasses);
		unmarshaller = context.createUnmarshaller();
		marshaller = context.createMarshaller();
		marshaller.setProperty("jaxb.formatted.output", true);
	}

	public String getSchemataOutputPath() {
		return schemataOutputPath;
	}

	public void setSchemataOutputPath(String schemataOutputPath) {
		this.schemataOutputPath = schemataOutputPath;
	}

	public Map<String, String> getSchemaFilenames() {
		return schemaFilenames;
	}

	public void setSchemaFilenames(Map<String, String> schemaFilenames) {
		this.schemaFilenames = schemaFilenames;
	}

	public String getExampleFilename() {
		return exampleFilename;
	}

	public void setExampleFilename(String exampleFilename) {
		this.exampleFilename = exampleFilename;
	}

	public String getExampleListFilename() {
		return exampleListFilename;
	}

	public void setExampleListFilename(String exampleListFilename) {
		this.exampleListFilename = exampleListFilename;
	}

	public String serialize(T element) throws JAXBException {
		StringWriter writer = new StringWriter();
		marshaller.marshal(element, writer);
		return writer.toString();
	}

	public T deserialize() throws JAXBException {
		return deserialize(getClass().getResourceAsStream(exampleFilename));
	}

	public List<T> deserializeList() throws Exception {
		return deserializeList(getClass().getResourceAsStream(exampleListFilename));
	}

	public T deserialize(InputStream in) throws JAXBException {
		return unmarshaller.unmarshal(new StreamSource(in), elementClass)
				.getValue();
	}

	public List<T> deserializeList(InputStream in) throws Exception {
		Deserializer<T> deserializer =
				new JAXBXMLEventReaderDeserializer<T>(elementClass, unmarshaller, in,
						elementName, namespace);
		List<T> elements = new ArrayList<T>();
		T element = null;
		while ((element = deserializer.deserialize(in, element)) != null)
			elements.add(element);
		return elements;
	}

	@Test
	public void createSchemata() throws IOException {
		context.generateSchema(new SchemaOutputResolver() {
			@Override
			public Result createOutput(String namespace, String schemaName)
					throws IOException {
				String filename = schemaFilenames.get(namespace);
				if (filename == null) filename = schemaName;
				File schemaPath = new File("./target/generated-schemas");
				if (!schemaPath.exists()) schemaPath.mkdirs();
				return new StreamResult(new File(schemaPath, filename));
			}
		});
	}

	@Test
	public void deserializeTest() throws JAXBException {
		T element = deserialize();
		Assert.assertNotNull(element);
		log.debug(serialize(element));
	}

	@Test
	public void deserializeListTest() throws Exception {
		List<T> elements = deserializeList();
		Assert.assertNotNull(elements);
	}
}
