package edu.cmu.commons.data.dao.mongo;

/**
 * A factory able to create/retrieve {@link Marshaller} instances.
 * @author hazen
 */
public interface MarshallerFactory {
	public <T> Marshaller<T, Object> getMarshaller(Class<T> sourceClass);
}
