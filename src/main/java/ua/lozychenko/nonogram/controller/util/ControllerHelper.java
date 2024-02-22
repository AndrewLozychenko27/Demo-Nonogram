package ua.lozychenko.nonogram.controller.util;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import ua.lozychenko.nonogram.data.entity.User;

import static ua.lozychenko.nonogram.constants.ControllerConstants.SESSION_USER;

public class ControllerHelper {
    public static User getCurrentUser(HttpSession session) {
        User user;
        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (o instanceof OAuth2User) {
            user = (User) session.getAttribute(SESSION_USER);
        } else {
            user = (User) o;
        }

        return user;
    }
}