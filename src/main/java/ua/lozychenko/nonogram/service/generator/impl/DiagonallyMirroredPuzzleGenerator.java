package ua.lozychenko.nonogram.service.generator.impl;

import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.config.property.PuzzleGeneratorProperty;
import ua.lozychenko.nonogram.data.entity.Cell;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.service.data.CellService;
import ua.lozychenko.nonogram.service.generator.PuzzleGenerator;
import ua.lozychenko.nonogram.service.generator.impl.base.MirroredPuzzleGenerator;

import java.util.Random;

@Service
public class DiagonallyMirroredPuzzleGenerator extends MirroredPuzzleGenerator implements PuzzleGenerator {
    private final CellService cellService;

    public DiagonallyMirroredPuzzleGenerator(PuzzleGeneratorProperty properties, CellService cellService) {
        super(properties, cellService);
        this.cellService = cellService;
    }

    @Override
    public Puzzle generate(Puzzle puzzle) {
        boolean isReverse = new Random().nextBoolean();

        puzzle = addRandomCells(puzzle,
                cellService.findAllByLimitDiagonally(puzzle.getWidth(), puzzle.getHeight(), isReverse),
                getCount(puzzle.getWidth() * puzzle.getHeight() / 2));

        puzzle.addCells((mirrorCells(
                puzzle,
                isReverse
                        ? (p, c) -> new Cell(p.getHeight() - 1 - c.getY(), p.getWidth() - 1 - c.getX())
                        : (p, c) -> new Cell(c.getY(), c.getX()))));

        return puzzle;
    }
}