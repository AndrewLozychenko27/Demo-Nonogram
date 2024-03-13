package ua.lozychenko.nonogram.service.generator.impl;

import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.config.property.PuzzleGeneratorProperty;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.service.data.CellService;
import ua.lozychenko.nonogram.service.generator.PuzzleGenerator;
import ua.lozychenko.nonogram.service.generator.impl.base.BasePuzzleGenerator;

@Service
public class FullRandomPuzzleGenerator extends BasePuzzleGenerator implements PuzzleGenerator {

    public FullRandomPuzzleGenerator(PuzzleGeneratorProperty properties, CellService cellService) {
        super(properties, cellService);
    }

    @Override
    public Puzzle generate(Puzzle puzzle) {
        return addRandomCells(puzzle, getCellsByLimit(puzzle.getWidth(), puzzle.getHeight()), getCount(puzzle.getWidth()) * puzzle.getHeight());
    }
}
