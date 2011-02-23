package edu.cmu.commons.data.dao.mongo.reflect;

import java.lang.reflect.Method;

import com.mongodb.DBObject;

import edu.cmu.commons.data.dao.mongo.Marshaller;

/**
 * @author hazen
 * @param <T>
 */
class PropertyInfo<T> {
	private final String name;
	private final String alias;
	private final Class<T> type;
	private final Method getter;
	private final Method setter;
	private final Marshaller<T, Object> marshaller;

	public PropertyInfo(String name, String alias, Class<T> type, Method getter,
			Method setter, Marshaller<T, Object> marshaller) {
		super();
		this.name = name;
		this.alias = alias;
		this.type = type;
		this.getter = getter;
		this.setter = setter;
		this.marshaller = marshaller;
	}

	public PropertyInfo(String name, Class<T> type, Method getter, Method setter,
			Marshaller<T, Object> marshaller) {
		this(name, name, type, getter, setter, marshaller);
	}

	public String getName() {
		return name;
	}

	public String getAlias() {
		return alias;
	}

	public Class<T> getType() {
		return type;
	}

	public Method getGetter() {
		return getter;
	}

	public Method getSetter() {
		return setter;
	}

	public Marshaller<T, Object> getMarshaller() {
		return marshaller;
	}

	@SuppressWarnings("unchecked")
	public T get(Object composite) {
		try {
			return (T) getter.invoke(composite);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void set(Object composite, T value) {
		try {
			setter.invoke(composite, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Retrieves source property value from {@code composite} and marshals it into
	 * {@code destination}.
	 * @param composite object from which source property value should be
	 * extracted.
	 * @param destination object into which source property value should be
	 * marshalled.
	 * @return {@code destination}, or a new instance if destination is invalid
	 * and construction of a valid instance is possible, updated to reflect data
	 * from source property value, or {@code null} if the source property value is
	 * {@code null}.
	 */
	public Object marshal(Object composite, Object destination) {
		T source = get(composite);
		if (source == null) return destination;
		return marshaller.marshal(source, destination);
	}

	/**
	 * Retrieves source property value from {@code composite}, marshals it, and
	 * registers the marshalled value in {@code dbo} using this property's alias.
	 * @param composite object from which the property value should be extracted.
	 * @param dbo object in which the marshalled version of the property value
	 * should be stored, under the key which matches this property's name.
	 * @return the marshalled property value, or {@code null} if the unmarshalled
	 * property value is {@code null}.
	 */
	public Object marshal(Object composite, DBObject dbo) {
		T value = get(composite);
		if (value == null) return null;
		Object marshalledValue = marshaller.marshal(value, dbo.get(alias));
		dbo.put(alias, marshalledValue);
		return marshalledValue;
	}

	/**
	 * Unmarshals property value from {@code source} and stores this value within
	 * {@code composite}.
	 * @param source object from which the property value should be unmarshalled.
	 * @param composite object in which the property value should be stored.
	 * @return the unmarshalled property value, or {@code null} if {@code source}
	 * is {@code null}.
	 */
	public T unmarshal(Object source, Object composite) {
		if (source == null) return null;
		T value = marshaller.unmarshal(source, get(composite));
		set(composite, value);
		return value;
	}

	/**
	 * Retrieves marshalled property value from {@code dbo} using this property's
	 * alias, unmarshals it, and stores the unmarshalled value in
	 * {@code composite}.
	 * @param dbo object from which the marshalled property value should be
	 * retrieved.
	 * @param composite object in which to store the unmarshalled property value.
	 * @return the unmarshalled property value, or {@code null} if the marshalled
	 * property value is {@code null}.
	 */
	public T unmarshal(DBObject dbo, Object composite) {
		Object marshalledValue = dbo.get(alias);
		if (marshalledValue == null) return null;
		T value = marshaller.unmarshal(marshalledValue, get(composite));
		set(composite, value);
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		PropertyInfo<?> other = (PropertyInfo<?>) obj;
		if (alias == null) {
			if (other.alias != null) return false;
		} else if (!alias.equals(other.alias)) return false;
		return true;
	}
}
