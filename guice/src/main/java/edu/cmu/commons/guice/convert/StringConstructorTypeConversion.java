package edu.cmu.commons.guice.convert;

import java.io.File;
import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.TypeLiteral;

/**
 * Support conversion from String to any type which defines a constructor
 * accepting a single String argument, for instance {@link File#File(String)}.
 * 
 * @author hazen
 */
public class StringConstructorTypeConversion extends AbstractTypeConversion {
	private static final Logger log = LoggerFactory
			.getLogger(StringConstructorTypeConversion.class);

	@Override
	public boolean matches(TypeLiteral<?> typeLiteral) {
		boolean matches = getConstructor(typeLiteral) != null;
		log.debug("Type '" + typeLiteral + "' "
				+ (matches ? "matches" : "does not match"));
		return matches;
	}

	@Override
	public Object convert(String text, TypeLiteral<?> typeLiteral) {
		log.debug("Converting text '" + text + "' into '" + typeLiteral + "'");
		Constructor<?> ctor = getConstructor(typeLiteral);
		try {
			return ctor.newInstance(text);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param typeLiteral
	 * @return a Constructor which accepts a single String argument, or
	 * <code>null</code> if the type associated with typeLiteral does not define
	 * such a constructor.
	 */
	private Constructor<?> getConstructor(TypeLiteral<?> typeLiteral) {
		Class<?> cls = typeLiteral.getRawType();
		Constructor<?> ctor = null;
		try {
			ctor = cls.getConstructor(String.class);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			// ignore
		}
		return ctor;
	}
}
