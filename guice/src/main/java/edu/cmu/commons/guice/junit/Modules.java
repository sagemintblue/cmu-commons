package edu.cmu.commons.guice.junit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.Module;

/**
 * Used to specify which Guice Modules should be created and supplied to Guice
 * during Injector creation.
 * @author hazen
 * @see GuiceTestRunner
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Modules {
	/**
	 * Guice modules to create and supply to injector.
	 */
	Class<? extends Module>[] value();
}
