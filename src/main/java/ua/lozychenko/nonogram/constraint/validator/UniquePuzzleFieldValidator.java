package ua.lozychenko.nonogram.constraint.validator;

import ua.lozychenko.nonogram.constraint.UniquePuzzleField;
import ua.lozychenko.nonogram.data.service.PuzzleService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniquePuzzleFieldValidator implements ConstraintValidator<UniquePuzzleField, String> {
    private final PuzzleService puzzleService;

    public UniquePuzzleFieldValidator(PuzzleService puzzleService) {
        this.puzzleService = puzzleService;
    }

    @Override
    public void initialize(UniquePuzzleField constraintAnnotation) {
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return puzzleService.findByName(name).isEmpty();
    }
}