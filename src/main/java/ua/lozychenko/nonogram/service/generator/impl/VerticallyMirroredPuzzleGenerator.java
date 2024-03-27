package ua.lozychenko.nonogram.service.generator.impl;

import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.config.property.PuzzleGeneratorProperty;
import ua.lozychenko.nonogram.data.entity.Cell;
import ua.lozychenko.nonogram.service.data.CellService;
import ua.lozychenko.nonogram.service.generator.PuzzleGenerator;
import ua.lozychenko.nonogram.service.generator.impl.base.MirroredPuzzleGenerator;

import java.util.LinkedList;
import java.util.Set;

@Service
public class VerticallyMirroredPuzzleGenerator extends MirroredPuzzleGenerator implements PuzzleGenerator {
    public VerticallyMirroredPuzzleGenerator(PuzzleGeneratorProperty properties, CellService cellService) {
        super(properties, cellService);
    }

    @Override
    public Set<Cell> generate(short width, short height) {
        Set<Cell> cells = getRandomCells(
                new LinkedList<>(getCellsByLimit((short) (width / 2), height)),
                getCount(width * height / 2));

        cells.addAll(mirrorCells(cells, c -> new Cell(width - 1 - c.getX(), c.getY())));

        return cells;
    }
}