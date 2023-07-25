package com.training.authentication.validator;

import com.training.authentication.constraint.PhoneNumberConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomPhoneValidator implements ConstraintValidator<PhoneNumberConstraint, Long> {

	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {
		return value != null && value.toString().matches("[0-9]+") && (value.toString().length() == 10);

	}

}
