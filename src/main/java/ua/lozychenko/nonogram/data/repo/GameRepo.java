package ua.lozychenko.nonogram.data.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.lozychenko.nonogram.data.entity.Game;
import ua.lozychenko.nonogram.data.entity.util.GameStats;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepo extends JpaRepository<Game, Long> {
    Optional<Game> findByPuzzleIdAndUserId(Long puzzleId, Long userId);

    @Query(value = "SELECT u.nickname, u.email, u.score FROM game g JOIN users u ON u.id = g.user_id GROUP BY u.nickname, u.email, u.score ORDER BY u.score DESC",
            nativeQuery = true)
    List<GameStats> findLeaders();
}