package edu.cmu.commons.data.validation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.constraints.URL;

/**
 * @author hazen
 */
public class URLValidatorForURI implements
		ConstraintValidator<URL, java.net.URI> {
	private URL target;

	@Override
	public void initialize(URL target) {
		this.target = target;
	}

	@Override
	public boolean isValid(java.net.URI test, ConstraintValidatorContext context) {
		if (!target.protocol().isEmpty() //
				&& test.getScheme() != null //
				&& !target.protocol().equals(test.getScheme())) return false;
		if (!target.host().isEmpty() //
				&& test.getHost() != null //
				&& !target.host().equals(test.getHost())) return false;
		if (target.port() >= 0 //
				&& test.getPort() >= 0 //
				&& target.port() != test.getPort()) return false;
		return true;
	}
}
