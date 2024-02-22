package ua.lozychenko.nonogram.service;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.data.entity.User;
import ua.lozychenko.nonogram.service.data.PuzzleService;
import ua.lozychenko.nonogram.service.data.UserService;

import java.util.Optional;

@Service
public class SecurityHelper {
    private final PuzzleService puzzleService;
    private final UserService userService;

    public SecurityHelper(PuzzleService puzzleService, UserService userService) {
        this.puzzleService = puzzleService;
        this.userService = userService;
    }

    public boolean isSelfEdit(User user, Long userId) {
        return user.getId().equals(userId);
    }

    public boolean isSelfEdit(OAuth2User oAuth2User, Long userId) {
        Optional<User> user = userService.findByEmail(oAuth2User.getAttribute("email"));
        return user.isPresent() && user.get().getId().equals(userId);
    }

    public boolean isPuzzleOwner(User user, Long puzzleId) {
        return puzzleService.findById(puzzleId).getUser().getId().equals(user.getId());
    }

    public boolean isPuzzleOwner(OAuth2User oAuth2User, Long puzzleId) {
        Optional<User> user = userService.findByEmail(oAuth2User.getAttribute("email"));
        return user.isPresent() && puzzleService.findById(puzzleId).getUser().getId().equals(user.get().getId());
    }
}