package ua.lozychenko.nonogram.service.generator.impl.base;

import ua.lozychenko.nonogram.config.property.PuzzleGeneratorProperty;
import ua.lozychenko.nonogram.data.entity.Cell;
import ua.lozychenko.nonogram.service.data.CellService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public abstract class MirroredPuzzleGenerator extends BasePuzzleGenerator {
    private final CellService cellService;


    public MirroredPuzzleGenerator(PuzzleGeneratorProperty properties, CellService cellService) {
        super(properties, cellService);
        this.cellService = cellService;
    }

    protected Set<Cell> mirrorCells(Set<Cell> cells, Function<Cell, Cell> mirrorFunction) {
        Set<Cell> res = new HashSet<>();

        for (Cell cell : cells) {
            res.add(cellService.findOrCreate(mirrorFunction.apply(cell)));
        }

        return res;
    }
}
