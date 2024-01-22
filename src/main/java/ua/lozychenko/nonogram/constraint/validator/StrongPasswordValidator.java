package ua.lozychenko.nonogram.constraint.validator;

import ua.lozychenko.nonogram.constraint.StrongPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {
    public static final String MESSAGE_TEMPLATE = "Password must contain at least 2 %s";

    private String message;

    @Override
    public void initialize(StrongPassword constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        boolean isValid = false;

        if (!s.matches("^.*\\d{2,}.*$")) {
            message = String.format(MESSAGE_TEMPLATE, "numbers");
        } else if (!s.matches("^.*[a-z]{2,}.*$")) {
            message = String.format(MESSAGE_TEMPLATE, "small letters");
        } else if (!s.matches("^.*[A-Z]{2,}.*$")) {
            message = String.format(MESSAGE_TEMPLATE, "capital letters");
        } else {
            isValid = true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();

        return isValid;
    }
}