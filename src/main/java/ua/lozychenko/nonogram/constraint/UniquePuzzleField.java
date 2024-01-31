package ua.lozychenko.nonogram.constraint;

import ua.lozychenko.nonogram.constraint.validator.UniquePuzzleFieldValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniquePuzzleFieldValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniquePuzzleField {
    String message() default "This puzzle name has already been used";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}