package ua.lozychenko.nonogram.data.service.impl;

import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.data.entity.Cell;
import ua.lozychenko.nonogram.data.entity.Game;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.data.entity.State;
import ua.lozychenko.nonogram.data.entity.User;
import ua.lozychenko.nonogram.data.repo.GameRepo;
import ua.lozychenko.nonogram.data.service.GameService;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultGameService extends DefaultBaseService<Game> implements GameService {
    private final GameRepo repo;

    public DefaultGameService(GameRepo repo) {
        super(repo);
        this.repo = repo;
    }

    @Override
    public Boolean check(Puzzle puzzle, User user, List<Cell> cells) {
        boolean solved = true;
        Game game = repo.findByPuzzleIdAndUserId(puzzle.getId(), user.getId()).orElse(new Game(puzzle, user));

        if (puzzle.getCells().stream().anyMatch(cell -> !cells.contains(cell))) {
            solved = false;
        } else if (cells.stream().anyMatch(cell -> !puzzle.getCells().contains(cell))) {
            solved = false;
        }

        game.setAttempts(game.getAttempts() + 1);
        if (solved) {
            game.setState(State.SOLVED);
        }
        game.clearAndAddCells(cells);
        repo.save(game);

        return solved;
    }

    @Override
    public Optional<Game> findByPuzzleIdAndUserId(Long puzzleId, Long userId) {
        return repo.findByPuzzleIdAndUserId(puzzleId, userId);
    }
}