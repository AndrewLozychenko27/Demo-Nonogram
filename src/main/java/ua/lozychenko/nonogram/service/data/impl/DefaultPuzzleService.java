package ua.lozychenko.nonogram.service.data.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.data.entity.Cell;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.data.entity.util.Hints;
import ua.lozychenko.nonogram.data.repo.PuzzleRepo;
import ua.lozychenko.nonogram.service.data.PuzzleService;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Service
public class DefaultPuzzleService extends DefaultBaseService<Puzzle> implements PuzzleService {
    private final PuzzleRepo repo;

    public DefaultPuzzleService(PuzzleRepo repo) {
        super(repo);
        this.repo = repo;
    }

    @Override
    public Optional<Puzzle> findByName(String name) {
        return repo.findByName(name);
    }

    @Override
    public Page<Puzzle> findAll(Long userId, Pageable pageable) {
        return repo.findAllVisibleForUser(userId, pageable);
    }

    @Override
    public Puzzle edit(Puzzle source, Puzzle changes) {
        source.setName(getIfChanged(source.getName(), changes.getName()));

        return repo.save(source);
    }

    @Override
    public boolean delete(Long id) {
        Puzzle puzzle = repo.getReferenceById(id);

        puzzle.setVisible(false);
        repo.save(puzzle);

        return true;
    }

    @Override
    public Hints generateKeys(Puzzle puzzle) {
        Hints hints = new Hints();

        List<Cell> cells = puzzle.getCells();

        extractLines(cells, Cell::getX).forEach(rowNum -> {
            List<Short> row = extractLineByNum(cells, (Cell cell) -> cell.getX().equals(rowNum), Cell::getY);
            hints.addVertical(rowNum, countSequences(row));
        });
        extractLines(cells, Cell::getY).forEach(colNum -> {
            List<Short> col = extractLineByNum(cells, (Cell cell) -> cell.getY().equals(colNum), Cell::getX);
            hints.addHorizontal(colNum, countSequences(col));
        });

        return hints;
    }

    private List<Short> extractLineByNum(List<Cell> cells, Predicate<Cell> filter, Function<Cell, Short> target) {
        return cells.stream()
                .filter(filter)
                .sorted(Comparator.comparing(target))
                .map(target)
                .toList();
    }

    private Stream<Short> extractLines(List<Cell> cells, Function<Cell, Short> function) {
        return cells.stream().map(function).distinct();
    }

    private List<Short> countSequences(List<Short> row) {
        List<Short> sequences = new LinkedList<>();
        short sequenceLength;
        short skipTo = -1;

        for (Short cell : row) {
            if (cell > skipTo) {
                sequenceLength = 1;

                while (row.contains((short) (cell + sequenceLength))) {
                    sequenceLength++;
                }

                sequences.add(sequenceLength);
                skipTo = (short) (cell + sequenceLength);
            }
        }

        return sequences;
    }
}