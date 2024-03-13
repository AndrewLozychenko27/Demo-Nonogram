package ua.lozychenko.nonogram.service.generator.impl;

import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.config.property.PuzzleGeneratorProperty;
import ua.lozychenko.nonogram.data.entity.Cell;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.service.data.CellService;
import ua.lozychenko.nonogram.service.generator.PuzzleGenerator;
import ua.lozychenko.nonogram.service.generator.impl.base.MirroredPuzzleGenerator;

@Service
public class VerticallyMirroredPuzzleGenerator extends MirroredPuzzleGenerator implements PuzzleGenerator {
    public VerticallyMirroredPuzzleGenerator(PuzzleGeneratorProperty properties, CellService cellService) {
        super(properties, cellService);
    }

    @Override
    public Puzzle generate(Puzzle puzzle) {
        puzzle = addRandomCells(puzzle,
                getCellsByLimit((short) (puzzle.getWidth() / 2), puzzle.getHeight()),
                getCount(puzzle.getWidth() * puzzle.getHeight() / 2));

        puzzle.addCells(mirrorCells(puzzle, (p, c) -> new Cell(p.getWidth() - 1 - c.getX(), c.getY())));

        return puzzle;
    }
}