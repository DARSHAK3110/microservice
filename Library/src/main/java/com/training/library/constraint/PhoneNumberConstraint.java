package com.training.library.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.training.library.validators.CustomPhoneValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented 
@Constraint(validatedBy = CustomPhoneValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumberConstraint {
	String message() default "validation.phoneNumberSize";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
