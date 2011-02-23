package edu.cmu.commons.guice.junit;

import com.google.inject.AbstractModule;

public class DogModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(Animal.class).to(Dog.class);
	}
}
