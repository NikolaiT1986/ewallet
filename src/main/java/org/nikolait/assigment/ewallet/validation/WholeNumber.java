package org.nikolait.assigment.ewallet.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WholeNumberValidator.class)
public @interface WholeNumber {
    String message() default "Must be a whole number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
