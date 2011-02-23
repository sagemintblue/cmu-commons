package edu.cmu.commons.guice.junit;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

@RunWith(GuiceTestRunner.class)
@Modules(CatModule.class)
public class GuiceTestRunnerTest {
	@Inject
	private Animal animal;

	@Test
	public void isAlive() {
		Assert.assertNotNull(animal.makeNoise());
	}

	@Test
	public void meow() {
		Assert.assertEquals("meow", animal.makeNoise());
	}

	@Test
	public void isCat() {
		Assert.assertEquals(Cat.class.getName(), animal.getClass().getName());
	}
}
