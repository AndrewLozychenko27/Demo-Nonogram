package ua.lozychenko.nonogram.data.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.data.entity.util.Hints;

import java.util.Optional;

public interface PuzzleService extends BaseService<Puzzle> {
    Optional<Puzzle> findByName(String name);

    Page<Puzzle> findAll(Long userId, Pageable pageable);

    Hints generateHints(Puzzle puzzle);
}