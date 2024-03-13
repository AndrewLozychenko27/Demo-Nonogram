package ua.lozychenko.nonogram.service.generator;

import ua.lozychenko.nonogram.data.entity.Puzzle;

public interface PuzzleGenerator {
    Puzzle generate(Puzzle puzzle);
}