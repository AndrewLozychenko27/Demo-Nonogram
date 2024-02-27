package ua.lozychenko.nonogram.data.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.lozychenko.nonogram.data.entity.Game;
import ua.lozychenko.nonogram.data.entity.util.GameStats;

import java.util.Optional;

@Repository
public interface GameRepo extends JpaRepository<Game, Long> {
    Optional<Game> findByPuzzleIdAndUserId(Long puzzleId, Long userId);

    @Query(value = "SELECT " +
            "nickname, email," +
            "ROUND(AVG(hints.percentage),2) AS hints," +
            "ROUND(AVG(attempts.percentage),2) AS attempts " +
            "FROM game g " +
            "JOIN users u ON g.user_id = u.id " +
            "JOIN (SELECT g.id AS game_id, " +
            "CAST((SELECT COUNT(c.id) FROM hint h " +
            "JOIN cell c ON c.id = h.cell_id AND h.game_id = g.id) AS decimal) " +
            "/ CAST((SELECT COUNT(c.id) FROM solution s " +
            "JOIN cell c ON c.id = s.cell_id AND s.puzzle_id = g.puzzle_id) AS decimal) * 100 AS percentage " +
            "FROM game g " +
            "GROUP BY g.id) AS hints ON hints.game_id = g.id " +
            "JOIN (SELECT g.user_id,(CAST(g.attempts AS decimal) / CAST(MAX(g1.attempts) AS decimal) * 100) AS percentage FROM game g " +
            "JOIN puzzle p ON p.id = g.puzzle_id AND g.state = 'SOLVED' " +
            "JOIN game g1 ON g1.puzzle_id = g.puzzle_id " +
            "GROUP BY p.name, g.user_id, g.attempts) AS attempts ON attempts.user_id = g.user_id " +
            "GROUP BY u.id, u.nickname, u.email " +
            "ORDER BY hints, attempts DESC",
            nativeQuery = true)
    Page<GameStats> findLeaders(Pageable pageable);
}