package ua.lozychenko.nonogram.service.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.data.entity.util.Hints;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PuzzleService extends BaseService<Puzzle> {
    Optional<Puzzle> findByName(String name);

    Page<Puzzle> findAll(Long userId, Pageable pageable);

    Hints generateKeys(Puzzle puzzle);

    Puzzle generatePuzzleRandomly(Puzzle puzzle);

    List<Puzzle> generatePuzzlesByImage(Puzzle puzzle, byte[] bytes) throws IOException;
}