package ua.lozychenko.nonogram.constraint;

import ua.lozychenko.nonogram.constraint.validator.PasswordConfirmationValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordConfirmationValidator.class)
@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConfirmation {
    String message() default "Password confirmation must match the password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}