package ua.lozychenko.generatorservice.generator.impl;

import org.springframework.stereotype.Service;
import ua.lozychenko.generatorservice.config.property.PuzzleGeneratorProperty;
import ua.lozychenko.generatorservice.data.Cell;
import ua.lozychenko.generatorservice.generator.PuzzleGenerator;
import ua.lozychenko.generatorservice.generator.impl.base.BasePuzzleGenerator;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class FullRandomPuzzleGenerator extends BasePuzzleGenerator implements PuzzleGenerator {
    public FullRandomPuzzleGenerator(PuzzleGeneratorProperty properties) {
        super(properties);
    }

    @Override
    public Set<Cell> generate(Set<Cell> cells, short width, short height) {
        return getRandomCells(new LinkedList<>(getCellsByLimit(List.copyOf(cells), width, height)), getCount(width * height));
    }
}
