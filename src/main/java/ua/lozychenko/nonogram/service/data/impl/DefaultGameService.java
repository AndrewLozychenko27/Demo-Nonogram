package ua.lozychenko.nonogram.service.data.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.config.property.GameProperty;
import ua.lozychenko.nonogram.data.entity.Cell;
import ua.lozychenko.nonogram.data.entity.Game;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.data.entity.State;
import ua.lozychenko.nonogram.data.entity.User;
import ua.lozychenko.nonogram.data.entity.util.GameStats;
import ua.lozychenko.nonogram.data.repo.GameRepo;
import ua.lozychenko.nonogram.service.data.GameService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Service
public class DefaultGameService extends DefaultBaseService<Game> implements GameService {

    private final GameProperty property;

    private final GameRepo repo;

    public DefaultGameService(GameProperty property, GameRepo repo) {
        super(repo);
        this.property = property;
        this.repo = repo;
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
        }
        game.clearAndAddCells(cells);
        repo.save(game);

        return solved;
    }

    @Override
    public List<Cell> giveHints(Puzzle puzzle, User user, List<Cell> cells) {
        Game game = createIfNotPlayed(puzzle, user);
        List<Cell> hints = new LinkedList<>();
        Random random = new Random();
        int hintsCount;
        List<Cell> toCheck = new ArrayList<>();
        List<Cell> cellsToHint;
        boolean isAttemptCounts = new HashSet<>(game.getCells()).containsAll(cells) && new HashSet<>(cells).containsAll(game.getCells());

        game = save(puzzle, user, cells);
        cellsToHint = getCellsToHint(puzzle, game);

        if (!cellsToHint.isEmpty()) {
            hintsCount = (int) (puzzle.getCells().size() * property.getHint().getThreshold());
            if (hintsCount < property.getHint().getMin()) {
                hintsCount = property.getHint().getMin();
            }

            IntStream.range(0, hintsCount).forEach(i -> {
                hints.add(cellsToHint.get(random.nextInt(cellsToHint.size())));
            });

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
                .toList();
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
    public Page<GameStats> getLeaders(Pageable pageable) {
        return repo.findLeaders(pageable);
    }
}