package ua.lozychenko.generatorservice.generator.impl.base;

import ua.lozychenko.generatorservice.config.property.PuzzleGeneratorProperty;
import ua.lozychenko.generatorservice.data.Cell;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public abstract class MirroredPuzzleGenerator extends BasePuzzleGenerator {
    public MirroredPuzzleGenerator(PuzzleGeneratorProperty properties) {
        super(properties);
    }

    protected Set<Cell> mirrorCells(Set<Cell> cells, Function<Cell, Cell> mirrorFunction) {
        Set<Cell> res = new HashSet<>();

        for (Cell cell : cells) {
            res.add(mirrorFunction.apply(cell));
        }

        return res;
    }
}