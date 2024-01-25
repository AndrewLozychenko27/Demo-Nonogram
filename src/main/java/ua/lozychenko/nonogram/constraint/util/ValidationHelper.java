package ua.lozychenko.nonogram.constraint.util;

import org.hibernate.validator.constraints.Length;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ua.lozychenko.nonogram.constraint.PasswordConfirmation;
import ua.lozychenko.nonogram.constraint.StrongPassword;
import ua.lozychenko.nonogram.constraint.UniqueEmail;
import ua.lozychenko.nonogram.data.entity.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationHelper {
    public static Map<String, Integer> PRIORITY = Map.of(
            NotEmpty.class.getSimpleName(), 0,
            Size.class.getSimpleName(), 1,
            Length.class.getSimpleName(), 1,
            PasswordConfirmation.class.getSimpleName(), 1,
            Pattern.class.getSimpleName(), 2,
            StrongPassword.class.getSimpleName(), 2,
            UniqueEmail.class.getSimpleName(), 3
    );

    public static Map<String, String> OBJECT_TO_FIELD = Map.of(
            User.class.getSimpleName().toLowerCase(Locale.ROOT), "passwordConfirmation",
            "changes", "passwordConfirmation"
    );

    public static BindingResult filterErrors(BindingResult source) {
        BindingResult filtered = new BeanPropertyBindingResult(source.getTarget(), source.getObjectName());

        if (source.hasFieldErrors()) {
            filterFieldsErrors(source, filtered);
        }
        if (source.hasGlobalErrors()) {
            filterObjectErrors(source, filtered);
        }

        return filtered;
    }

    public static BindingResult renameFieldError(BindingResult result, String target, String name) {
        FieldError fieldError = result.getFieldError(target);

        if (fieldError != null) {
            result.addError(new FieldError(fieldError.getObjectName(), name, Objects.requireNonNull(fieldError.getDefaultMessage())));
        }

        return result;
    }

    private static void filterFieldsErrors(BindingResult source, BindingResult filtered) {
        Set<String> fields = source.getFieldErrors().stream()
                .map(FieldError::getField)
                .collect(Collectors.toSet());

        fields.forEach(field ->
                filtered.addError(source.getFieldErrors(field).stream()
                        .min(Comparator.comparing(error -> PRIORITY.get(error.getCode())))
                        .orElseThrow(IllegalArgumentException::new))
        );
    }

    private static void filterObjectErrors(BindingResult source, BindingResult filtered) {
        filtered.addError(source.getGlobalErrors().stream()
                .min(Comparator.comparing(error -> PRIORITY.get(error.getCode())))
                .map(error -> new FieldError(error.getObjectName(), OBJECT_TO_FIELD.get(error.getObjectName()), Objects.requireNonNull(error.getDefaultMessage())))
                .orElseThrow(IllegalArgumentException::new));
    }
}