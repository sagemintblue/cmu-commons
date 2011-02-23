package edu.cmu.commons.data.validation.validators;

import java.io.IOException;
import java.util.Set;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author hazen
 * @param <T>
 */
public abstract class ValidatorTest<T> {
	protected abstract T getValidInstance() throws Exception;

	protected abstract T getInvalidInstance() throws Exception;

	@Test(expected = ValidationException.class)
	public void validateURLContainerWithoutCSPValidators() throws Exception {
		ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
		Validator validator = vf.getValidator();
		T value = getValidInstance();
		Set<ConstraintViolation<T>> violations = validator.validate(value);
		Assert.assertNotNull(violations);
		Assert.assertTrue(violations.isEmpty());
	}

	private Validator getValidator() throws IOException {
		Configuration<?> config = Validation.byDefaultProvider().configure();
		ValidatorFactory vf = config
				.addMapping(
						ClassLoader
								.getSystemResource(
										"edu/cmu/commons/data/validation/constraint-definitions.xml")
								.openStream()).buildValidatorFactory();
		return vf.getValidator();
	}

	@Test
	public void validateURLContainerWithCSPValidators() throws Exception {
		Validator validator = getValidator();
		T value = getValidInstance();
		Set<ConstraintViolation<T>> violations = validator.validate(value);
		Assert.assertNotNull(violations);
		Assert.assertTrue(violations.isEmpty());
	}

	@Test
	public void validateURLContainerWithBadProtocol() throws Exception {
		Validator validator = getValidator();
		T value = getInvalidInstance();
		Set<ConstraintViolation<T>> violations = validator.validate(value);
		Assert.assertNotNull(violations);
		Assert.assertTrue(!violations.isEmpty());
	}
}
