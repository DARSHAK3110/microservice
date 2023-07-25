package com.training.authentication.validator;

import com.training.authentication.constraint.RoleConstraint;
import com.training.authentication.entity.enums.Roles;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleConstraintValidator implements ConstraintValidator<RoleConstraint, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return (value.equals(Roles.USER.name()) || value.equals(Roles.ADMIN.name()));
	}

}
