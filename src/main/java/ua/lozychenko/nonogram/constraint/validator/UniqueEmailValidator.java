package ua.lozychenko.nonogram.constraint.validator;

import ua.lozychenko.nonogram.constraint.UniqueEmail;
import ua.lozychenko.nonogram.data.entity.User;
import ua.lozychenko.nonogram.service.data.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, User> {
    private final UserService userService;

    public UniqueEmailValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
    }


    @Override
    public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {
        Optional<User> other = userService.findByEmail(user.getEmail());

        return other.isEmpty() || (other.get().getId().equals(user.getId()));
    }
}