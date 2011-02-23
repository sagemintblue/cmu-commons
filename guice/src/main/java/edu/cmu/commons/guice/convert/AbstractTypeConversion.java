package edu.cmu.commons.guice.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;

/**
 * Helper class for TypeConversion implementations.
 * 
 * @author hazen
 */
public abstract class AbstractTypeConversion extends
		AbstractMatcher<TypeLiteral<?>> implements TypeConversion {
	private static final Logger log = LoggerFactory
			.getLogger(AbstractTypeConversion.class);

	@Override
	public void register(Binder binder) {
		log.debug("Registering type conversion '" + getClass().getName()
				+ "' with binder '" + binder + "'");
		binder.convertToTypes(this, this);
	}
}
