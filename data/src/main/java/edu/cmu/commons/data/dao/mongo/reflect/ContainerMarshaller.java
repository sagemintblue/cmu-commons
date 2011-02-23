package edu.cmu.commons.data.dao.mongo.reflect;

import edu.cmu.commons.data.dao.mongo.Marshaller;

/**
 * Non-functional interface used to mark a Marshaller implementation as a
 * "container" marshaller; One that handles Collections or Maps.
 * @author hazen
 * @param <A>
 * @param <B>
 */
public interface ContainerMarshaller<A, B> extends Marshaller<A, B> {}
