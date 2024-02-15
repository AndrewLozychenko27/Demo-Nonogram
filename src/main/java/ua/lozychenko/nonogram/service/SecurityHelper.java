package ua.lozychenko.nonogram.service;

import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.data.entity.User;
import ua.lozychenko.nonogram.service.data.PuzzleService;

@Service
public class SecurityHelper {
    private final PuzzleService puzzleService;

    public SecurityHelper(PuzzleService puzzleService) {
        this.puzzleService = puzzleService;
    }

    public boolean isSelfEdit(User user, Long userId) {
        return user.getId().equals(userId);
    }

    public boolean isPuzzleOwner(User user, Long puzzleId) {
        return puzzleService.findById(puzzleId).getUser().getId().equals(user.getId());
    }
}