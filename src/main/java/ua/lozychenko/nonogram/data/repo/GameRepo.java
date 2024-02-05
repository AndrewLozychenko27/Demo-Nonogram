package ua.lozychenko.nonogram.data.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.lozychenko.nonogram.data.entity.Game;

import java.util.Optional;

@Repository
public interface GameRepo extends JpaRepository<Game, Long> {
    Optional<Game> findByPuzzleIdAndUserId(Long puzzleId, Long userId);
}