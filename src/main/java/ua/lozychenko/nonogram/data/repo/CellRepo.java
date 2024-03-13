package ua.lozychenko.nonogram.data.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.lozychenko.nonogram.data.entity.Cell;

import java.util.List;
import java.util.Optional;

@Repository
public interface CellRepo extends JpaRepository<Cell, Long> {
    Optional<Cell> findByXAndY(short x, short y);

    @Query("FROM Cell c WHERE c.x < :x AND c.y < :y")
    List<Cell> findAllByLimit(@Param("x") short x, @Param("y") short y);

    @Query("FROM Cell c WHERE c.x < :x AND c.y < :y AND c.y <= c.x")
    List<Cell> findAllByLimitDiagonally(@Param("x") short x, @Param("y") short y);

    @Query("FROM Cell c WHERE c.x < :x AND c.y < :y AND c.y < :x - c.x")
    List<Cell> findAllByLimitDiagonallyReverse(@Param("x") short x, @Param("y") short y);
}