package ua.lozychenko.nonogram.service.data.impl;

import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.config.property.GameHintsProperty;
import ua.lozychenko.nonogram.config.property.GameScoreProperty;
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
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DefaultGameService extends DefaultBaseService<Game> implements GameService {
    private static final double THIRD = 0.33;
    private final GameHintsProperty hintsProperties;
    private final GameScoreProperty scoreProperties;
    private final GameRepo repo;
    private final UserRepo userRepo;

    public DefaultGameService(GameHintsProperty hintsProperties, GameScoreProperty scoreProperties, GameRepo repo, UserRepo userRepo) {
        super(repo);
        this.hintsProperties = hintsProperties;
        this.scoreProperties = scoreProperties;
        this.repo = repo;
        this.userRepo = userRepo;
    }

    @Override
    public Boolean check(Puzzle puzzle, User user, Set<Cell> cells) {
        return check(puzzle, user, cells, false);
    }

    private Boolean check(Puzzle puzzle, User user, Set<Cell> cells, boolean isForHint) {
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
        int res = scoreProperties.minPuzzleScore();

        int minCellsCount = (int) (game.getPuzzle().getHeight() * game.getPuzzle().getWidth() * scoreProperties.minPuzzleCellPercent());
        int maxCellsCount = (int) (game.getPuzzle().getHeight() * game.getPuzzle().getWidth() * scoreProperties.maxPuzzleCellPercent());

        if (game.getPuzzle().getCells().size() < minCellsCount || game.getPuzzle().getCells().size() > maxCellsCount) {
            res = (int) (res * scoreProperties.outOfPuzzleSizePenalty());
        }

        if (getHintsPercentage(game) <= scoreProperties.hintsThreshold()) {
            res = (int) (res * ((game.getPuzzle().getWidth() * scoreProperties.puzzleSizeMultiplier()) + (game.getPuzzle().getHeight() * scoreProperties.puzzleSizeMultiplier())));

            if (game.getAttempts() < scoreProperties.perfectThreshold() && getHintsPercentage(game) < scoreProperties.hintsPerfectThreshold()) {
                res = (int) (res * (scoreProperties.perfectMultiplier() * (THIRD * (scoreProperties.perfectThreshold() - game.getAttempts()))));
            }

            if (getHintsPercentage(game) <= scoreProperties.withoutHints()) {
                res = res * scoreProperties.withoutHintsMultiplier();
            } else if (getHintsPercentage(game) <= scoreProperties.almostWithoutHintsThreshold()) {
                res = (int) (res * scoreProperties.almostWithoutHintsMultiplier());
            }
        }

        return res;
    }

    private double getHintsPercentage(Game game) {
        return (double) game.getHints().size() / (double) game.getPuzzle().getCells().size() * 100;
    }

    @Override
    public Set<Cell> giveHints(Puzzle puzzle, User user, Set<Cell> cells) {
        int hintsCount = getHintsCount(puzzle);
        Set<Cell> cellsToHint;
        Set<Cell> hints = new HashSet<>();
        Set<Cell> toCheck = new HashSet<>();
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

    private Set<Cell> excludeRemoved(Set<Cell> cells, Game game) {
        return cells.stream().filter(cell -> !game.containsRemoved(cell.getX(), cell.getY())).collect(Collectors.toSet());
    }

    private Set<Cell> hintByRemoving(int hintsCount, Set<Cell> cellsToRemove, Set<Cell> cells) {
        Cell hint;
        Random random = new Random();
        Set<Cell> removed = new HashSet<>();
        List<Cell> cellList = new ArrayList<>(cellsToRemove);

        for (int i = 0; i < hintsCount && !cellsToRemove.isEmpty(); i++) {
            hint = cellList.get(random.nextInt(cellsToRemove.size()));
            cells.remove(hint);
            cellList.remove(hint);
            removed.add(hint);
        }

        return removed;
    }

    private Set<Cell> hintByAdding(int hintsCount, Set<Cell> cellsToHint) {
        Cell hint;
        Random random = new Random();
        Set<Cell> hints = new HashSet<>();
        List<Cell> cellList = new ArrayList<>(cellsToHint);

        for (int i = 0; i < hintsCount && !cellsToHint.isEmpty(); i++) {
            hint = cellList.get(random.nextInt(cellsToHint.size()));
            hints.add(hint);
            cellList.remove(hint);
        }

        return hints;
    }

    private int getHintsCount(Puzzle puzzle) {
        int hintsCount;

        hintsCount = (int) (puzzle.getCells().size() * hintsProperties.threshold());
        if (hintsCount < hintsProperties.min()) {
            hintsCount = hintsProperties.min();
        }

        return hintsCount;
    }

    private Set<Cell> getDifference(Set<Cell> cells, Set<Cell> target) {
        return cells.stream()
                .filter(cell -> !target.contains(cell))
                .collect(Collectors.toSet());
    }

    @Override
    public Game save(Puzzle puzzle, User user, Set<Cell> cells) {
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