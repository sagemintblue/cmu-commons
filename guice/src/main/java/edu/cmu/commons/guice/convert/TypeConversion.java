package edu.cmu.commons.guice.convert;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.TypeConverter;

/**
 * Combines Matcher and TypeConverter interfaces.
 * 
 * @author hazen
 */
public interface TypeConversion extends Matcher<TypeLiteral<?>>, TypeConverter {
	/**
	 * Registers this type conversion with the given Binder.
	 * 
	 * @param binder
	 */
	public void register(Binder binder);
}
