package com.training.library.validators;

import java.util.Calendar;
import java.util.Date;

import com.training.library.constraint.DateValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomdateValidator implements ConstraintValidator<DateValidator, Date> {

	@Override
	public boolean isValid(Date value, ConstraintValidatorContext context) {
		Date dt = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt); 
		c.add(Calendar.DATE, 1);
		dt = c.getTime();
		if(dt.before(value)) {
			return true;
		}
		return false;
	}

}
