package ua.lozychenko.nonogram.service.generator.impl.base;

import ua.lozychenko.nonogram.config.property.PuzzleGeneratorProperty;
import ua.lozychenko.nonogram.data.entity.Cell;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.service.data.CellService;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

public abstract class MirroredPuzzleGenerator extends BasePuzzleGenerator {
    private final CellService cellService;


    public MirroredPuzzleGenerator(PuzzleGeneratorProperty properties, CellService cellService) {
        super(properties, cellService);
        this.cellService = cellService;
    }

    protected Set<Cell> mirrorCells(Puzzle puzzle, BiFunction<Puzzle, Cell, Cell> mirrorFunction) {
        Set<Cell> cells = new HashSet<>();

        for (Cell cell : puzzle.getCells()) {
            cells.add(cellService.findOrCreate(mirrorFunction.apply(puzzle, cell)));
        }

        return cells;
    }
}
