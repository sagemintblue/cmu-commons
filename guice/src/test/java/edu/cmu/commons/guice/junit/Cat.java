package edu.cmu.commons.guice.junit;

public class Cat implements Animal {
	@Override
	public String makeNoise() {
		return "meow";
	}
}
