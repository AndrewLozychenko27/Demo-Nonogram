package ua.lozychenko.nonogram.service.generator.impl;

import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.config.property.PuzzleGeneratorProperty;
import ua.lozychenko.nonogram.data.entity.Cell;
import ua.lozychenko.nonogram.service.data.CellService;
import ua.lozychenko.nonogram.service.generator.PuzzleGenerator;
import ua.lozychenko.nonogram.service.generator.impl.base.BasePuzzleGenerator;

import java.util.LinkedList;
import java.util.Set;

@Service
public class FullRandomPuzzleGenerator extends BasePuzzleGenerator implements PuzzleGenerator {

    public FullRandomPuzzleGenerator(PuzzleGeneratorProperty properties, CellService cellService) {
        super(properties, cellService);
    }

    @Override
    public Set<Cell> generate(short width, short height) {
        return getRandomCells(new LinkedList<>(getCellsByLimit(width, height)), getCount(width * height));
    }
}
