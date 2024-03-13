package ua.lozychenko.nonogram.service.generator.impl;

import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.config.property.PuzzleGeneratorProperty;
import ua.lozychenko.nonogram.data.entity.Cell;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.service.data.CellService;
import ua.lozychenko.nonogram.service.generator.PuzzleGenerator;
import ua.lozychenko.nonogram.service.generator.impl.base.MirroredPuzzleGenerator;

@Service
public class HorizontallyMirroredPuzzleGenerator extends MirroredPuzzleGenerator implements PuzzleGenerator {
    public HorizontallyMirroredPuzzleGenerator(PuzzleGeneratorProperty properties, CellService cellService) {
        super(properties, cellService);
    }

    @Override
    public Puzzle generate(Puzzle puzzle) {
        puzzle = addRandomCells(puzzle,
                getCellsByLimit(puzzle.getWidth(), (short) (puzzle.getHeight() / 2)),
                getCount(puzzle.getWidth() * puzzle.getHeight() / 2));

        puzzle.addCells(mirrorCells(puzzle, (p, c) -> new Cell(c.getX(), p.getHeight() - 1 - c.getY())));

        return puzzle;
    }
}