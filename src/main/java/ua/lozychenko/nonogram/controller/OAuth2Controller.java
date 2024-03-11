package ua.lozychenko.nonogram.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.lozychenko.nonogram.data.entity.Role;
import ua.lozychenko.nonogram.data.entity.User;
import ua.lozychenko.nonogram.service.data.UserService;

import static ua.lozychenko.nonogram.constants.ControllerConstants.SESSION_USER;

@Controller
@RequestMapping("/oauth")
public class OAuth2Controller {

    public OAuth2Controller(UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;

    @GetMapping("/success")
    public String onSuccess(@AuthenticationPrincipal OAuth2User oAuth2User,
                            HttpSession session) {
        User user = userService.findByEmail(oAuth2User.getAttribute("email"))
                .orElse(new User(
                        oAuth2User.getAttribute("name"),
                        oAuth2User.getAttribute("email"),
                        oAuth2User.getName(),
                        oAuth2User.getName()
                ));

        if (user.getId() == null) {
            user = userService.save(user);
        }

        session.setAttribute(SESSION_USER, user);
        return "redirect:/";
    }
}