package edu.cmu.commons.data.validation.validators;

import java.net.URI;

public class URLValidatorForURITest extends ValidatorTest<URIContainer> {
	@Override
	protected URIContainer getValidInstance() throws Exception {
		return new URIContainer(new URI("ftp://example.com/"));
	}

	@Override
	protected URIContainer getInvalidInstance() throws Exception {
		return new URIContainer(new URI("http://example.com/"));
	}
}
