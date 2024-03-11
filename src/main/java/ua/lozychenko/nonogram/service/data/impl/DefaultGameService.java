package ua.lozychenko.nonogram.service.data.impl;

import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.config.property.GameProperty;
import ua.lozychenko.nonogram.data.entity.Cell;
import ua.lozychenko.nonogram.data.entity.Game;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.data.entity.State;
import ua.lozychenko.nonogram.data.entity.User;
import ua.lozychenko.nonogram.data.entity.util.GameStats;
import ua.lozychenko.nonogram.data.entity.util.PuzzleStats;
import ua.lozychenko.nonogram.data.repo.GameRepo;
import ua.lozychenko.nonogram.data.repo.UserRepo;
import ua.lozychenko.nonogram.service.data.GameService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class DefaultGameService extends DefaultBaseService<Game> implements GameService {
    public static final int MIN_PUZZLE_SCORE = 50;
    public static final double PUZZLE_SIZE_MULTIPLIER = 0.1;
    public static final int PERFECT_MULTIPLIER = 2;
    public static final double THIRD = 0.33;
    public static final int PERFECT_THRESHOLD = 3;
    public static final int HINTS_PERFECT_THRESHOLD = 15;
    public static final int HINTS_THRESHOLD = 85;
    public static final int WITHOUT_HINTS = 0;
    public static final int WITHOUT_HINTS_MULTIPLIER = 2;
    public static final int ALMOST_WITHOUT_HINTS_THRESHOLD = 15;
    public static final double ALMOST_WITHOUT_HINTS_MULTIPLIER = 1.35;
    public static final double MIN_PUZZLE_CELLS_PERCENT = 0.2;
    public static final double MAX_PUZZLE_CELLS_PERCENT = 0.8;
    public static final double OUT_OF_PUZZLE_SIZE_PENALTY = 0.5;
    private final GameProperty property;

    private final GameRepo repo;
    private final UserRepo userRepo;

    public DefaultGameService(GameProperty property, GameRepo repo, UserRepo userRepo) {
        super(repo);
        this.property = property;
        this.repo = repo;
        this.userRepo = userRepo;
    }

    @Override
    public Boolean check(Puzzle puzzle, User user, List<Cell> cells) {
        return check(puzzle, user, cells, false);
    }

    private Boolean check(Puzzle puzzle, User user, List<Cell> cells, boolean isForHint) {
        boolean solved = true;
        Game game = createIfNotPlayed(puzzle, user);

        if (puzzle.getCells().stream().anyMatch(cell -> !cells.contains(cell))) {
            solved = false;
        } else if (cells.stream().anyMatch(cell -> !puzzle.getCells().contains(cell))) {
            solved = false;
        }

        if (!isForHint) {
            game.setAttempts(game.getAttempts() + 1);
        }

        if (solved) {
            game.setState(State.SOLVED);
            user.setScore(user.getScore() + calculateGameScore(game));
            userRepo.save(user);
        }

        game.clearAndAddCells(cells);
        repo.save(game);

        return solved;
    }

    private Integer calculateGameScore(Game game) {
        int res = MIN_PUZZLE_SCORE;

        int minCellsCount = (int) (game.getPuzzle().getHeight() * game.getPuzzle().getWidth() * MIN_PUZZLE_CELLS_PERCENT);
        int maxCellsCount = (int) (game.getPuzzle().getHeight() * game.getPuzzle().getWidth() * MAX_PUZZLE_CELLS_PERCENT);

        if (game.getPuzzle().getCells().size() < minCellsCount || game.getPuzzle().getCells().size() > maxCellsCount) {
            res = (int) (res * OUT_OF_PUZZLE_SIZE_PENALTY);
        }

        if (getHintsPercentage(game) <= HINTS_THRESHOLD) {
            res = (int) (res * ((game.getPuzzle().getWidth() * PUZZLE_SIZE_MULTIPLIER) + (game.getPuzzle().getHeight() * PUZZLE_SIZE_MULTIPLIER)));

            if (game.getAttempts() <= PERFECT_THRESHOLD && getHintsPercentage(game) <= HINTS_PERFECT_THRESHOLD) {
                res = (int) (res * (PERFECT_MULTIPLIER * (THIRD * (PERFECT_THRESHOLD - game.getAttempts()))));
            }

            if (getHintsPercentage(game) <= WITHOUT_HINTS) {
                res = res * WITHOUT_HINTS_MULTIPLIER;
            } else if (getHintsPercentage(game) <= ALMOST_WITHOUT_HINTS_THRESHOLD) {
                res = (int) (res * ALMOST_WITHOUT_HINTS_MULTIPLIER);
            }
        }

        return res;
    }

    private double getHintsPercentage(Game game) {
        return (double) game.getHints().size() / (double) game.getPuzzle().getCells().size() * 100;
    }

    @Override
    public List<Cell> giveHints(Puzzle puzzle, User user, List<Cell> cells) {
        int hintsCount = getHintsCount(puzzle);
        List<Cell> cellsToHint;
        List<Cell> hints = new ArrayList<>();
        List<Cell> toCheck = new ArrayList<>();
        Game game = createIfNotPlayed(puzzle, user);
        boolean isAttemptNotCounts = new HashSet<>(game.getCells()).containsAll(cells) && new HashSet<>(cells).containsAll(game.getCells());

        cells = excludeRemoved(cells, game);
        game = save(puzzle, user, cells);
        cellsToHint = getDifference(puzzle.getCells(), game.getCells());

        if (!cellsToHint.isEmpty()) {
            hints = hintByAdding(hintsCount, cellsToHint);
            game.addHints(hints);
            toCheck.addAll(hints);
        } else if (game.getCells().size() > puzzle.getCells().size()) {
            hints = hintByRemoving(hintsCount, getDifference(cells, puzzle.getCells()), game.getCells());
            game.addRemoved(hints);
        }
        repo.save(game);
        toCheck.addAll(game.getCells());
        check(puzzle, user, toCheck, isAttemptNotCounts);

        return hints;
    }

    private List<Cell> excludeRemoved(List<Cell> cells, Game game) {
        return cells.stream().filter(cell -> !game.containsRemoved(cell.getX(), cell.getY())).toList();
    }

    private List<Cell> hintByRemoving(int hintsCount, List<Cell> cellsToRemove, List<Cell> cells) {
        Cell hint;
        Random random = new Random();
        List<Cell> removed = new LinkedList<>();

        for (int i = 0; i < hintsCount && !cellsToRemove.isEmpty(); i++) {
            hint = cellsToRemove.get(random.nextInt(cellsToRemove.size()));
            cells.remove(hint);
            cellsToRemove.remove(hint);
            removed.add(hint);
        }

        return removed;
    }

    private List<Cell> hintByAdding(int hintsCount, List<Cell> cellsToHint) {
        Cell hint;
        Random random = new Random();
        List<Cell> hints = new LinkedList<>();

        for (int i = 0; i < hintsCount && !cellsToHint.isEmpty(); i++) {
            hint = cellsToHint.get(random.nextInt(cellsToHint.size()));
            hints.add(hint);
            cellsToHint.remove(hint);
        }

        return hints;
    }

    private int getHintsCount(Puzzle puzzle) {
        int hintsCount;

        hintsCount = (int) (puzzle.getCells().size() * property.getHint().getThreshold());
        if (hintsCount < property.getHint().getMin()) {
            hintsCount = property.getHint().getMin();
        }

        return hintsCount;
    }

    private List<Cell> getDifference(List<Cell> cells, List<Cell> target) {
        return cells.stream()
                .filter(cell -> !target.contains(cell))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Game save(Puzzle puzzle, User user, List<Cell> cells) {
        Game game = createIfNotPlayed(puzzle, user);
        game.clearAndAddCells(cells);

        return repo.save(game);
    }

    private Game createIfNotPlayed(Puzzle puzzle, User user) {
        return repo.findByPuzzleIdAndUserId(puzzle.getId(), user.getId()).orElse(new Game(puzzle, user));
    }

    @Override
    public List<GameStats> getLeaders() {
        return repo.findLeaders();
    }

    @Override
    public PuzzleStats getPuzzleStats(Puzzle puzzle) {
        List<Game> games = puzzle.getGames();

        return new PuzzleStats(
                games.size(),
                games.stream().filter(game -> game.getState().equals(State.SOLVED)).count(),
                games.stream().map(Game::getAttempts).min(Comparator.comparingInt(a -> a)).orElse(0),
                games.stream().map(game -> game.getHints().size()).min(Comparator.comparingInt(h -> h)).orElse(0)
        );
    }
}