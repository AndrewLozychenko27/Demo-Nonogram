package ua.lozychenko.nonogram.service.data;

import ua.lozychenko.nonogram.data.entity.Cell;
import ua.lozychenko.nonogram.data.entity.Game;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.data.entity.User;
import ua.lozychenko.nonogram.data.entity.util.GameStats;
import ua.lozychenko.nonogram.data.entity.util.PuzzleStats;

import java.util.List;
import java.util.Optional;

public interface GameService extends BaseService<Game> {
    Boolean check(Puzzle puzzle, User user, List<Cell> cells);

    List<Cell> giveHints(Puzzle puzzle, User user, List<Cell> cells);

    Game save(Puzzle puzzle, User user, List<Cell> cells);

    List<GameStats> getLeaders();

    PuzzleStats getPuzzleStats(Puzzle puzzle);
}