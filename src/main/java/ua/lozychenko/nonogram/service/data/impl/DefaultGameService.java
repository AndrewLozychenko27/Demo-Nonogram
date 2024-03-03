package ua.lozychenko.nonogram.service.data.impl;

import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.config.property.GameProperty;
import ua.lozychenko.nonogram.data.entity.Cell;
import ua.lozychenko.nonogram.data.entity.Game;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.data.entity.State;
import ua.lozychenko.nonogram.data.entity.User;
import ua.lozychenko.nonogram.data.entity.util.GameStats;
import ua.lozychenko.nonogram.data.repo.GameRepo;
import ua.lozychenko.nonogram.data.repo.UserRepo;
import ua.lozychenko.nonogram.service.data.GameService;

import java.util.ArrayList;
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
            res = (int) ((res * game.getPuzzle().getWidth() * PUZZLE_SIZE_MULTIPLIER) + (res * game.getPuzzle().getHeight() * PUZZLE_SIZE_MULTIPLIER));

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
        Cell hint;
        int hintsCount;
        List<Cell> cellsToHint;
        Random random = new Random();
        List<Cell> hints = new LinkedList<>();
        List<Cell> toCheck = new ArrayList<>();
        Game game = createIfNotPlayed(puzzle, user);
        boolean isAttemptCounts = new HashSet<>(game.getCells()).containsAll(cells) && new HashSet<>(cells).containsAll(game.getCells());

        game = save(puzzle, user, cells);
        cellsToHint = getCellsToHint(puzzle, game);

        if (!cellsToHint.isEmpty()) {
            hintsCount = (int) (puzzle.getCells().size() * property.getHint().getThreshold());
            if (hintsCount < property.getHint().getMin()) {
                hintsCount = property.getHint().getMin();
            }

            for (int i = 0; i < hintsCount; i++) {
                hint = cellsToHint.get(random.nextInt(cellsToHint.size()));
                hints.add(hint);
                cellsToHint.remove(hint);
            }

            game.addHints(hints);
            repo.save(game);
        }
        toCheck.addAll(game.getCells());
        toCheck.addAll(hints);
        check(puzzle, user, toCheck, isAttemptCounts);

        return hints;
    }

    private List<Cell> getCellsToHint(Puzzle puzzle, Game game) {
        return puzzle.getCells().stream()
                .filter(cell -> !game.getCells().contains(cell))
                .collect(Collectors.toCollection(LinkedList::new));
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
}