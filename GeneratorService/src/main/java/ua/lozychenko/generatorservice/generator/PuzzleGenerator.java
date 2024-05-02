package ua.lozychenko.generatorservice.generator;

import ua.lozychenko.generatorservice.data.Cell;

import java.util.Set;

public interface PuzzleGenerator {
    Set<Cell> generate(Set<Cell> cells, short width, short height);
}