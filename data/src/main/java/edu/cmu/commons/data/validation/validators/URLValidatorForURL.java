package edu.cmu.commons.data.validation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.constraints.URL;

/**
 * @author hazen
 */
public class URLValidatorForURL implements
		ConstraintValidator<URL, java.net.URL> {
	private URL target;

	@Override
	public void initialize(URL target) {
		this.target = target;
	}

	@Override
	public boolean isValid(java.net.URL test, ConstraintValidatorContext context) {
		if (!target.protocol().isEmpty() //
				&& test.getProtocol() != null //
				&& !target.protocol().equals(test.getProtocol())) return false;
		if (!target.host().isEmpty() //
				&& test.getHost() != null //
				&& !target.host().equals(test.getHost())) return false;
		if (target.port() >= 0 //
				&& test.getPort() >= 0 //
				&& target.port() != test.getPort()) return false;
		return true;
	}
}
