package ua.lozychenko.nonogram.constraint.validator;

import ua.lozychenko.nonogram.constraint.UniquePuzzleName;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.data.service.PuzzleService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UniquePuzzleNameValidator implements ConstraintValidator<UniquePuzzleName, Puzzle> {
    private final PuzzleService puzzleService;

    public UniquePuzzleNameValidator(PuzzleService puzzleService) {
        this.puzzleService = puzzleService;
    }

    @Override
    public void initialize(UniquePuzzleName constraintAnnotation) {
    }

    @Override
    public boolean isValid(Puzzle puzzle, ConstraintValidatorContext constraintValidatorContext) {
        Optional<Puzzle> other = puzzleService.findByName(puzzle.getName());

        return other.isEmpty() || (other.get().getId().equals(puzzle.getId()));
    }
}