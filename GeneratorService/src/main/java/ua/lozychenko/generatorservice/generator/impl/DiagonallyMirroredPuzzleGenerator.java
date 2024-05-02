package ua.lozychenko.generatorservice.generator.impl;

import org.springframework.stereotype.Service;
import ua.lozychenko.generatorservice.config.property.PuzzleGeneratorProperty;
import ua.lozychenko.generatorservice.data.Cell;
import ua.lozychenko.generatorservice.generator.PuzzleGenerator;
import ua.lozychenko.generatorservice.generator.impl.base.MirroredPuzzleGenerator;
import ua.lozychenko.generatorservice.service.util.CellHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class DiagonallyMirroredPuzzleGenerator extends MirroredPuzzleGenerator implements PuzzleGenerator {
    public DiagonallyMirroredPuzzleGenerator(PuzzleGeneratorProperty properties) {
        super(properties);
    }

    @Override
    public Set<Cell> generate(Set<Cell> cells, short width, short height) {
        boolean isReverse = new Random().nextBoolean();
        Cell[][] parsedCells = CellHelper.parseCells(cells, width, height);
        Set<Cell> res = getRandomCells(new LinkedList<>(isReverse
                        ? getCellsByLimitDiagonallyReversed(List.copyOf(cells), width, height)
                        : getCellsByLimitDiagonally(List.copyOf(cells), width, height)),
                getCount(width * height / 2));

        res.addAll(mirrorCells(
                res,
                isReverse
                        ? c -> parsedCells[height - 1 - c.getY()][width - 1 - c.getX()]
                        : c -> parsedCells[c.getY()][c.getX()]
        ));

        return res;
    }
}