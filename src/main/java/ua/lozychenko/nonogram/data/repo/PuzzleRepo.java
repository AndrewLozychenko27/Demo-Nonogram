package ua.lozychenko.nonogram.data.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.lozychenko.nonogram.data.entity.Puzzle;

import java.util.Optional;

@Repository
public interface PuzzleRepo extends JpaRepository<Puzzle, Long> {
    Optional<Puzzle> findByName(String name);
}