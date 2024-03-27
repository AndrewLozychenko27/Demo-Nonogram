package ua.lozychenko.nonogram.service.generator.impl;

import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.config.property.PuzzleGeneratorProperty;
import ua.lozychenko.nonogram.data.entity.Cell;
import ua.lozychenko.nonogram.service.data.CellService;
import ua.lozychenko.nonogram.service.generator.PuzzleGenerator;
import ua.lozychenko.nonogram.service.generator.impl.base.MirroredPuzzleGenerator;

import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

@Service
public class DiagonallyMirroredPuzzleGenerator extends MirroredPuzzleGenerator implements PuzzleGenerator {
    private final CellService cellService;

    public DiagonallyMirroredPuzzleGenerator(PuzzleGeneratorProperty properties, CellService cellService) {
        super(properties, cellService);
        this.cellService = cellService;
    }

    @Override
    public Set<Cell> generate(short width, short height) {
        boolean isReverse = new Random().nextBoolean();
        Set<Cell> cells = getRandomCells(new LinkedList<>(cellService.findAllByLimitDiagonally(width, height, isReverse)),
                getCount(width * height / 2));

        cells.addAll(mirrorCells(
                cells,
                isReverse
                        ? c -> new Cell(height - 1 - c.getY(), width - 1 - c.getX())
                        : c -> new Cell(c.getY(), c.getX())
        ));

        return cells;
    }
}