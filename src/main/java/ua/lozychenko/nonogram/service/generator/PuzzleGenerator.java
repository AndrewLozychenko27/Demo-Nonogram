package ua.lozychenko.nonogram.service.generator;

import ua.lozychenko.nonogram.data.entity.Cell;

import java.util.Set;

public interface PuzzleGenerator {
    Set<Cell> generate(short width, short height);
}