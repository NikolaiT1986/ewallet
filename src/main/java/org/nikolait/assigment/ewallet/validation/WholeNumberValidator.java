package org.nikolait.assigment.ewallet.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class WholeNumberValidator implements ConstraintValidator<WholeNumber, BigDecimal> {

    @Override
    public boolean isValid(BigDecimal bigDecimal, ConstraintValidatorContext constraintValidatorContext) {
        if (bigDecimal == null) return true;
        return bigDecimal.scale() == 0;
    }

}
