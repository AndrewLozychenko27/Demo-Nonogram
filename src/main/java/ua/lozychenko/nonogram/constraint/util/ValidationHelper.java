package ua.lozychenko.nonogram.constraint.util;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ua.lozychenko.nonogram.constraint.PasswordConfirmation;
import ua.lozychenko.nonogram.constraint.StrongPassword;
import ua.lozychenko.nonogram.constraint.UniqueEmail;
import ua.lozychenko.nonogram.constraint.UniquePuzzleName;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.data.entity.User;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationHelper {
    public static Map<String, Integer> PRIORITY = new HashMap<>();
    public static Map<ObjectCodePair, String> OBJECT_TO_FIELD = new HashMap<>();


    static {
        PRIORITY.put(NotEmpty.class.getSimpleName(), 0);
        PRIORITY.put(NotBlank.class.getSimpleName(), 0);
        PRIORITY.put(NotNull.class.getSimpleName(), 0);
        PRIORITY.put(Size.class.getSimpleName(), 1);
        PRIORITY.put(Min.class.getSimpleName(), 1);
        PRIORITY.put(Max.class.getSimpleName(), 1);
        PRIORITY.put(Length.class.getSimpleName(), 1);
        PRIORITY.put(PasswordConfirmation.class.getSimpleName(), 1);
        PRIORITY.put(Pattern.class.getSimpleName(), 2);
        PRIORITY.put(StrongPassword.class.getSimpleName(), 2);
        PRIORITY.put(UniqueEmail.class.getSimpleName(), 3);
        PRIORITY.put(UniquePuzzleName.class.getSimpleName(), 3);

        OBJECT_TO_FIELD.put(new ObjectCodePair(User.class.getSimpleName().toLowerCase(), PasswordConfirmation.class.getSimpleName()), "passwordConfirmation");
        OBJECT_TO_FIELD.put(new ObjectCodePair(User.class.getSimpleName().toLowerCase(), UniqueEmail.class.getSimpleName()), "email");
        OBJECT_TO_FIELD.put(new ObjectCodePair(Puzzle.class.getSimpleName().toLowerCase(), UniquePuzzleName.class.getSimpleName()), "name");
    }

    public static BindingResult filterErrors(BindingResult source) {
        BindingResult filtered = new BeanPropertyBindingResult(source.getTarget(), source.getObjectName());

        if (source.hasGlobalErrors()) {
            filterObjectErrors(source, source);
        }
        if (source.hasFieldErrors()) {
            filterFieldsErrors(source, filtered);
        }

        return filtered;
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
                .map(error -> new FieldError(
                        error.getObjectName(),
                        OBJECT_TO_FIELD.get(new ObjectCodePair(error.getObjectName(), error.getCode())),
                        null,
                        false,
                        error.getCodes(),
                        error.getArguments(),
                        Objects.requireNonNull(error.getDefaultMessage())))
                .orElseThrow(IllegalArgumentException::new));
    }

    private static class ObjectCodePair {
        private String object;
        private String code;

        public ObjectCodePair(String object, String code) {
            this.object = object;
            this.code = code;
        }

        public String getObject() {
            return object;
        }

        public void setObject(String object) {
            this.object = object;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        @Override
        public int hashCode() {
            return object.length() * 32 + code.length();
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj
                    || (obj instanceof ObjectCodePair pair)
                    && this.getObject().equals(pair.getObject())
                    && this.getCode().equals(pair.getCode());
        }
    }
}