package ua.lozychenko.nonogram.service.data.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.config.property.GameScoreProperty;
import ua.lozychenko.nonogram.config.property.PuzzleGeneratorProperty;
import ua.lozychenko.nonogram.data.entity.Cell;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.data.entity.util.Hints;
import ua.lozychenko.nonogram.data.repo.PuzzleRepo;
import ua.lozychenko.nonogram.service.data.CellService;
import ua.lozychenko.nonogram.service.data.PuzzleService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
public class DefaultPuzzleService extends DefaultBaseService<Puzzle> implements PuzzleService {
    private final PuzzleRepo repo;
    private final CellService cellService;

    private final PuzzleGeneratorProperty puzzleGeneratorProperty;

    public DefaultPuzzleService(PuzzleRepo repo, CellService cellService, GameScoreProperty gameScoreProperty, PuzzleGeneratorProperty puzzleGeneratorProperty) {
        super(repo);
        this.repo = repo;
        this.cellService = cellService;
        this.puzzleGeneratorProperty = puzzleGeneratorProperty;
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
    public boolean delete(Long id) {
        Puzzle puzzle = repo.getReferenceById(id);

        puzzle.setVisible(false);
        repo.save(puzzle);

        return true;
    }

    @Override
    public Hints generateKeys(Puzzle puzzle) {
        Hints hints = new Hints();

        Set<Cell> cells = puzzle.getCells();

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

    private List<Short> extractLineByNum(Set<Cell> cells, Predicate<Cell> filter, Function<Cell, Short> target) {
        return cells.stream()
                .filter(filter)
                .sorted(Comparator.comparing(target))
                .map(target)
                .toList();
    }

    private Stream<Short> extractLines(Set<Cell> cells, Function<Cell, Short> function) {
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

    @Override
    public Puzzle generatePuzzleRandomly(Puzzle puzzle) {
        //puzzle.addCells(generators.get(new Random().nextInt(generators.size())).generate(puzzle.getWidth(), puzzle.getHeight()));
        puzzle.setGenerated(true);

        return puzzle;
    }

    @Override
    public List<Puzzle> generatePuzzlesByImage(Puzzle puzzle, byte[] bytes) throws IOException {
        List<Puzzle> puzzles = new LinkedList<>();

        puzzles.addAll(generate(bytes, () -> new Puzzle(puzzle.getName(), puzzle.getWidth(), puzzle.getHeight(), puzzle.getUser())));
        puzzles.addAll(reversePuzzles(puzzles));

        return puzzles;
    }

    private List<Puzzle> generate(byte[] bytes, Supplier<Puzzle> copyProvider) throws IOException {
        List<Puzzle> puzzles = new LinkedList<>();
        Puzzle copy;

        for (double multiplier : puzzleGeneratorProperty.thresholdMultipliers()) {
            copy = copyProvider.get();
            copy.setGenerated(true);
            copy.addCells(generatePuzzleByImage(bytes, copy.getWidth(), copy.getHeight(), multiplier));
            if (isPuzzleEmpty(copy) || isPuzzleFull(copy)) {
                continue;
            }
            puzzles.add(copy);
        }

        return puzzles;
    }

    private boolean isPuzzleFull(Puzzle puzzle) {
        return puzzle.getCells().size() >= (puzzle.getWidth() * puzzle.getHeight()) - (puzzle.getWidth() * puzzle.getHeight() * puzzleGeneratorProperty.emptyPuzzle());
    }

    private boolean isPuzzleEmpty(Puzzle puzzle) {
        return puzzle.getCells().size() <= puzzle.getWidth() * puzzle.getHeight() * puzzleGeneratorProperty.emptyPuzzle();
    }

    private List<Puzzle> reversePuzzles(List<Puzzle> puzzles) {
        Puzzle copy;
        List<Puzzle> reversed = new LinkedList<>();

        for (Puzzle puzzle : puzzles) {
            copy = new Puzzle(puzzle.getName(), puzzle.getWidth(), puzzle.getHeight(), puzzle.getUser());
            copy.setGenerated(true);
            copy.addCells(cellService.findAllByLimit(copy.getWidth(), copy.getHeight()));
            copy.getCells().removeAll(puzzle.getCells());
            reversed.add(copy);
        }

        return reversed;
    }

    private Set<Cell> generatePuzzleByImage(byte[] bytes, short width, short height, double thresholdMultiplier) throws IOException {
        Set<Cell> cells = new HashSet<>();
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
        int threshold = (int) (getAverageRGB(image, 0, 0, width, height) * thresholdMultiplier);
        int sectorWidth = image.getWidth() / width;
        int sectorHeight = image.getHeight() / height;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (getAverageRGB(image, x * sectorWidth, y * sectorHeight, sectorWidth, sectorHeight) >= threshold) {
                    cells.add(cellService.findOrCreate(new Cell(x, y)));
                }
            }
        }

        return cells;
    }

    private int getAverageRGB(BufferedImage image, int startX, int startY, int width, int height) {
        return (int) Arrays.stream(image.getRGB(startX, startY, width, height, null, 0, width))
                .average()
                .orElseThrow();
    }
}