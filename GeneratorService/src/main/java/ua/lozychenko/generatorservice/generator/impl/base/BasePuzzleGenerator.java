package ua.lozychenko.generatorservice.generator.impl.base;

import ua.lozychenko.generatorservice.config.property.PuzzleGeneratorProperty;
import ua.lozychenko.generatorservice.data.Cell;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BasePuzzleGenerator {
    private final PuzzleGeneratorProperty properties;

    public BasePuzzleGenerator(PuzzleGeneratorProperty properties) {
        this.properties = properties;
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

    protected Set<Cell> getCellsByLimit(List<Cell> cells, short x, short y) {
        return cells.stream().filter(cell -> cell.getX() < x && cell.getY() < y).collect(Collectors.toSet());
    }

    protected Set<Cell> getCellsByLimitDiagonally(List<Cell> cells, short x, short y) {
        return cells.stream().filter(cell -> cell.getX() < x && cell.getY() < y && cell.getY() <= cell.getX()).collect(Collectors.toSet());
    }

    protected Set<Cell> getCellsByLimitDiagonallyReversed(List<Cell> cells, short x, short y) {
        return cells.stream().filter(cell -> cell.getX() < x && cell.getY() < y && cell.getY() < x - cell.getX()).collect(Collectors.toSet());
    }
}