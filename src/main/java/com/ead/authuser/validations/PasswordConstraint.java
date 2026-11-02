package com.ead.authuser.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PasswordConstraintImpl.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConstraint {
    String message() default """
              A senha deve ter entre 6 e 20 caracteres, incluindo número, letra maiúscula, letra minúscula e caractere especial, sem espaços.
            """;

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
