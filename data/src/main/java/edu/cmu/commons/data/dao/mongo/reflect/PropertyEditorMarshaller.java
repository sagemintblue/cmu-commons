package edu.cmu.commons.data.dao.mongo.reflect;

import java.beans.PropertyEditor;

import edu.cmu.commons.data.dao.mongo.Marshaller;

/**
 * Marshaller supporting conversion between type {@code T} and {@code String}
 * via {@code T}'s {@link PropertyEditor}.
 * @author hazen
 * @param <T> Type for which a {@link PropertyEditor} exists.
 */
public class PropertyEditorMarshaller<T> implements Marshaller<T, String> {
	private final Class<T> valueClass;
	private final PropertyEditor propertyEditor;

	public PropertyEditorMarshaller(Class<T> valueClass,
			PropertyEditor propertyEditor) {
		super();
		this.valueClass = valueClass;
		this.propertyEditor = propertyEditor;
	}

	@Override
	public String marshal(T value, String marshalledValue) {
		propertyEditor.setValue(value);
		return propertyEditor.getAsText();
	}

	@Override
	public T unmarshal(String marshalledValue, T value) {
		propertyEditor.setAsText(marshalledValue);
		return valueClass.cast(propertyEditor.getValue());
	}
}
