package edu.cmu.commons.data.validation.validators;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Allows constraint validation of arbitrary value (via
 * {@link Object#toString()}) having annotation of type A using a
 * {@link ConstraintValidator} implementation which accepts String values.
 * @author hazen
 * @param <A> Annotation type used to label values to be validated.
 */
public class StringValidatorAdapter<A extends Annotation> implements
		ConstraintValidator<A, Object> {

	private ConstraintValidator<A, String> validator;

	public StringValidatorAdapter(ConstraintValidator<A, String> validator) {
		this.validator = validator;
	}

	@Override
	public void initialize(A constraintAnnotation) {
		validator.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		return validator.isValid(value.toString(), context);
	}
}
