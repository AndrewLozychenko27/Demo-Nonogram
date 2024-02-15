package ua.lozychenko.nonogram.service.data;

import ua.lozychenko.nonogram.data.entity.Cell;
import ua.lozychenko.nonogram.data.entity.Game;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.data.entity.User;

import java.util.List;
import java.util.Optional;

public interface GameService extends BaseService<Game> {
    Boolean check(Puzzle puzzle, User user, List<Cell> cells);

    List<Cell> giveHints(Puzzle puzzle, User user);

    Game save(Puzzle puzzle, User user, List<Cell> cells);
}