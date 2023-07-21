package com.training.library.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.training.library.validators.CustomdateValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomdateValidator.class)
@Documented
public @interface DateValidator {
	String message() default "{author.validation.date}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
