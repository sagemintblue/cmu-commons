package edu.cmu.commons.data.dao.mongo.reflect;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import org.bson.BSONObject;
import org.bson.types.BSONTimestamp;
import org.bson.types.Binary;
import org.bson.types.Code;
import org.bson.types.CodeWScope;
import org.bson.types.ObjectId;
import org.bson.types.Symbol;

import edu.cmu.commons.data.dao.mongo.Marshaller;

/**
 * @author hazen
 * @param <T> Simple type.
 */
final class SimpleMarshaller<T> implements Marshaller<T, T> {
	public static final Set<Class<?>> SIMPLE_TYPES = new HashSet<Class<?>>(
			Arrays.<Class<?>> asList(
					// Simple types
					String.class, Boolean.class,
					byte[].class,
					// Number types
					Byte.class, Short.class, Integer.class, AtomicInteger.class,
					Long.class, AtomicLong.class, Float.class, Double.class,
					// Special Java types
					Date.class, UUID.class, Pattern.class,
					// BSON types
					BSONObject.class, ObjectId.class, Binary.class, Symbol.class,
					BSONTimestamp.class, Code.class, CodeWScope.class));

	private static final SimpleMarshaller<?> INSTANCE =
			new SimpleMarshaller<Object>();

	@SuppressWarnings("unchecked")
	public static <T> SimpleMarshaller<T> getInstance() {
		return (SimpleMarshaller<T>) INSTANCE;
	}

	private SimpleMarshaller() {}

	@Override
	public T marshal(T source, T target) {
		return source;
	}

	@Override
	public T unmarshal(T target, T source) {
		return target;
	}
}
