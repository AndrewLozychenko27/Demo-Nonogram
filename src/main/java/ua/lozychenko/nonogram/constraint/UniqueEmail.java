package ua.lozychenko.nonogram.constraint;

import ua.lozychenko.nonogram.constraint.validator.UniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueEmailValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "This email has already been used";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}