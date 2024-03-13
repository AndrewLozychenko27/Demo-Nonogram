package ua.lozychenko.nonogram.service.generator.impl.base;

import ua.lozychenko.nonogram.config.property.PuzzleGeneratorProperty;
import ua.lozychenko.nonogram.data.entity.Cell;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.service.data.CellService;

import java.util.List;
import java.util.Random;

public abstract class BasePuzzleGenerator {
    private final PuzzleGeneratorProperty properties;
    private final CellService cellService;

    public BasePuzzleGenerator(PuzzleGeneratorProperty properties, CellService cellService) {
        this.properties = properties;
        this.cellService = cellService;
    }

    protected int getCount(int puzzleSize) {
        Random random = new Random();
        return (int) (puzzleSize * (properties.fillPercent() + random.nextDouble(properties.fillPercentRange() * -1, properties.fillPercentRange())));
    }

    protected Puzzle addRandomCells(Puzzle puzzle, List<Cell> cells, int count) {
        int index;
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            index = random.nextInt(cells.size());
            puzzle.addCell(cells.get(index));
            cells.remove(index);
        }

        return puzzle;
    }

    protected List<Cell> getCellsByLimit(short x, short y) {
        return cellService.findAllByLimit(x, y);
    }
}