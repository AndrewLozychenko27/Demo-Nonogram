package ua.lozychenko.nonogram.data.service;

import ua.lozychenko.nonogram.data.entity.Hints;
import ua.lozychenko.nonogram.data.entity.Puzzle;

import java.util.Optional;

public interface PuzzleService extends BaseService<Puzzle> {
    Optional<Puzzle> findByName(String name);

    Hints generateHints(Puzzle puzzle);

    Puzzle parseCells(Puzzle puzzle, String[] coordinates);
}