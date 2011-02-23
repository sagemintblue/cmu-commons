package edu.cmu.commons.guice.junit;

public class Dog implements Animal {
	@Override
	public String makeNoise() {
		return "woof";
	}
}
