package edu.cmu.commons.data.validation.validators;

import java.net.URL;

public class URLValidatorForURLTest extends ValidatorTest<URLContainer> {
	@Override
	protected URLContainer getValidInstance() throws Exception {
		return new URLContainer(new URL("ftp://example.com/"));
	}

	@Override
	protected URLContainer getInvalidInstance() throws Exception {
		return new URLContainer(new URL("http://example.com/"));
	}
}
