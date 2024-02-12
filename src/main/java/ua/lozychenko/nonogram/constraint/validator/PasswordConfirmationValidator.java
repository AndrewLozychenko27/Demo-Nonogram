package ua.lozychenko.nonogram.constraint.validator;

import ua.lozychenko.nonogram.constraint.PasswordConfirmation;
import ua.lozychenko.nonogram.data.entity.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConfirmationValidator implements ConstraintValidator<PasswordConfirmation, User> {
    @Override
    public void initialize(PasswordConfirmation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {
        return user.getPassword() != null && user.getPassword().equals(user.getPasswordConfirmation());
    }
}