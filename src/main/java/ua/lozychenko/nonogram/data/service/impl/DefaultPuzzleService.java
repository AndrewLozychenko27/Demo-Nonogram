package ua.lozychenko.nonogram.data.service.impl;

import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.data.entity.Cell;
import ua.lozychenko.nonogram.data.entity.Hints;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.data.repo.PuzzleRepo;
import ua.lozychenko.nonogram.data.service.CellService;
import ua.lozychenko.nonogram.data.service.PuzzleService;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DefaultPuzzleService extends DefaultBaseService<Puzzle> implements PuzzleService {
    private final PuzzleRepo repo;
    private final CellService cellService;

    public DefaultPuzzleService(PuzzleRepo repo, CellService cellService) {
        super(repo);
        this.repo = repo;
        this.cellService = cellService;
    }

    @Override
    public Optional<Puzzle> findByName(String name) {
        return repo.findByName(name);
    }

    @Override
    public Hints generateHints(Puzzle puzzle) {
        Hints hints = new Hints();

        List<Cell> cells = puzzle.getCells();

        extractLines(cells, Cell::getX).forEach(rowNum -> {
            HashSet<Short> row = extractLineByNum(cells, (Cell cell) -> cell.getX().equals(rowNum), Cell::getY);
            hints.addVertical(rowNum, countSequences(row));
        });
        extractLines(cells, Cell::getY).forEach(colNum -> {
            HashSet<Short> col = extractLineByNum(cells, (Cell cell) -> cell.getY().equals(colNum), Cell::getX);
            hints.addHorizontal(colNum, countSequences(col));
        });

        return hints;
    }

    @Override
    public Puzzle parseCells(Puzzle puzzle, String[] coordinates) {
        puzzle.addCells(Arrays.stream(coordinates)
                .map(coordinate -> cellService.findOrCreate(cellService.parseCell(coordinate)))
                .toList());
        repo.save(puzzle);

        return puzzle;
    }

    private HashSet<Short> extractLineByNum(List<Cell> cells, Predicate<Cell> filter, Function<Cell, Short> target) {
        return cells.stream()
                .filter(filter)
                .sorted(Comparator.comparing(target))
                .map(target)
                .collect(Collectors.toCollection(HashSet::new));
    }

    private Stream<Short> extractLines(List<Cell> cells, Function<Cell, Short> function) {
        return cells.stream().map(function).distinct();
    }

    private List<Short> countSequences(HashSet<Short> row) {
        List<Short> sequences = new LinkedList<>();
        short sequenceLength;
        short skitTo = -1;

        for (Short cell : row) {
            if (cell > skitTo) {
                sequenceLength = 1;

                while (row.contains((short) (cell + sequenceLength))) {
                    sequenceLength++;
                }

                sequences.add(sequenceLength);
                skitTo = (short) (cell + sequenceLength);
            }
        }

        return sequences;
    }
}