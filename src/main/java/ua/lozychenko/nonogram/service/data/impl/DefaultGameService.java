package ua.lozychenko.nonogram.service.data.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.data.entity.Cell;
import ua.lozychenko.nonogram.data.entity.Game;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.data.entity.State;
import ua.lozychenko.nonogram.data.entity.User;
import ua.lozychenko.nonogram.data.repo.GameRepo;
import ua.lozychenko.nonogram.service.data.GameService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Service
public class DefaultGameService extends DefaultBaseService<Game> implements GameService {
    @Value("${game.hints.threshold}")
    private Double threshold;

    @Value("${game.hints.min}")
    private Integer minHintsCount;

    private final GameRepo repo;

    public DefaultGameService(GameRepo repo) {
        super(repo);
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
    public List<Cell> giveHints(Puzzle puzzle, User user) {
        Game game = createIfNotPlayed(puzzle, user);
        List<Cell> hints = new LinkedList<>();
        Random random = new Random();

        List<Cell> cells = puzzle.getCells().stream()
                .filter(cell -> !game.getCells().contains(cell))
                .toList();

        int hintsCount = (int) (puzzle.getCells().size() * threshold);
        if (hintsCount < minHintsCount) {
            hintsCount = minHintsCount;
        }

        IntStream.range(0, hintsCount).forEach(i -> {
            hints.add(cells.get(random.nextInt(cells.size())));
        });

        game.addCells(hints);
        game.setHints(game.getHints() + 1);
        check(puzzle, user, new ArrayList<>(game.getCells()), true);

        return hints;
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
}