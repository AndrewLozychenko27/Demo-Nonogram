package ua.lozychenko.nonogram.data.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.lozychenko.nonogram.data.entity.Puzzle;

import java.util.Optional;

@Repository
public interface PuzzleRepo extends JpaRepository<Puzzle, Long> {
    Optional<Puzzle> findByName(String name);

    @Query("FROM Puzzle p WHERE p.visible = true AND (p.user.id = :user_id OR p.cells IS NOT EMPTY)")
    Page<Puzzle> findAllVisibleForUser(@Param("user_id") Long userId, Pageable pageable);
}