package edu.cmu.commons.guice.convert;

import java.util.Arrays;

import com.google.inject.AbstractModule;

/**
 * Include this Module when building your Injector to automatically register all
 * type converters provided by this package:
 * 
 * <pre>
 * Injector injector = Guice.buildInjector(new TypeConversionModule(), ... );
 * </pre>
 * 
 * @author hazen
 */
public class TypeConversionExtensionsModule extends AbstractModule {
	@Override
	protected void configure() {
		for (TypeConversion conversion : Arrays.asList(
				new StringConstructorTypeConversion(),
				new PropertyEditorTypeConversion()))
			conversion.register(binder());
	}
}
