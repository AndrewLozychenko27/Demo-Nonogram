package ua.lozychenko.nonogram.data.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.lozychenko.nonogram.data.entity.Cell;

import java.util.Optional;

@Repository
public interface CellRepo extends JpaRepository<Cell, Long> {
    Optional<Cell> findByXAndY(short x, short y);
}