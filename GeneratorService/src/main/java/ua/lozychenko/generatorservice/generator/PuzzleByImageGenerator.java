package ua.lozychenko.generatorservice.generator;

import ua.lozychenko.generatorservice.data.Cell;

import java.io.IOException;
import java.util.Set;

public interface PuzzleByImageGenerator {
    Set<Cell> generate(Set<Cell> cells, short width, short height, byte[] bytes, double thresholdMultiplier) throws IOException;
}