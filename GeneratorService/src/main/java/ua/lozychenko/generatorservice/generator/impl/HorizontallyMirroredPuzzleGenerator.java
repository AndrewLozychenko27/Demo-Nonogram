package ua.lozychenko.generatorservice.generator.impl;

import org.springframework.stereotype.Service;
import ua.lozychenko.generatorservice.config.property.PuzzleGeneratorProperty;
import ua.lozychenko.generatorservice.data.Cell;
import ua.lozychenko.generatorservice.generator.PuzzleGenerator;
import ua.lozychenko.generatorservice.generator.impl.base.MirroredPuzzleGenerator;
import ua.lozychenko.generatorservice.service.util.CellHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class HorizontallyMirroredPuzzleGenerator extends MirroredPuzzleGenerator implements PuzzleGenerator {
    public HorizontallyMirroredPuzzleGenerator(PuzzleGeneratorProperty properties) {
        super(properties);
    }

    @Override
    public Set<Cell> generate(Set<Cell> cells, short width, short height) {
        Cell[][] parsedCells = CellHelper.parseCells(cells, width, height);
        Set<Cell> res = getRandomCells(
                new LinkedList<>(getCellsByLimit(List.copyOf(cells), width, (short) (height / 2))),
                getCount(width * height / 2));

        res.addAll(mirrorCells(res, c -> parsedCells[c.getX()][height - 1 - c.getY()]));

        return res;
    }
}