package edu.cmu.commons.data.dao.mongo.reflect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a property of a composite type should be considered
 * polymorphic for the purposes of marshalling. Any {@code Marshaller} which
 * supports the property should encode the property value's runtime type within
 * marshalling output so that later on an instance of the appropriate type may
 * be created to store unmarshalled data.
 * @author hazen
 * @see ReflectionMarshallerFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface Polymorphic {}
