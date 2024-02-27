package ua.lozychenko.nonogram.service.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.lozychenko.nonogram.data.entity.Cell;
import ua.lozychenko.nonogram.data.entity.Game;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.data.entity.User;
import ua.lozychenko.nonogram.data.entity.util.GameStats;

import java.util.List;

public interface GameService extends BaseService<Game> {
    Boolean check(Puzzle puzzle, User user, List<Cell> cells);

    List<Cell> giveHints(Puzzle puzzle, User user, List<Cell> cells);

    Game save(Puzzle puzzle, User user, List<Cell> cells);

    Page<GameStats> getLeaders(Pageable pageable);
}