package ua.lozychenko.nonogram.service.generator.impl.base;

import ua.lozychenko.nonogram.config.property.PuzzleGeneratorProperty;
import ua.lozychenko.nonogram.data.entity.Cell;
import ua.lozychenko.nonogram.service.data.CellService;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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

    protected Set<Cell> getRandomCells(List<Cell> cells, int count) {
        int index;
        Random random = new Random();
        Set<Cell> res = new HashSet<>();

        for (int i = 0; i < count; i++) {
            index = random.nextInt(cells.size());
            res.add(cells.get(index));
            cells.remove(index);
        }

        return res;
    }

    protected Set<Cell> getCellsByLimit(short x, short y) {
        return cellService.findAllByLimit(x, y);
    }
}