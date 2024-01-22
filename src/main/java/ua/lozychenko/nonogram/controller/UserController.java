package ua.lozychenko.nonogram.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.lozychenko.nonogram.controller.composite.UserEditForm;
import ua.lozychenko.nonogram.data.entity.User;
import ua.lozychenko.nonogram.data.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public User getUser() {
        User user = new User();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated() && authentication.getPrincipal().getClass() == User.class) {
            user = (User) authentication.getPrincipal();
        }

        return user;
    }

    @ModelAttribute("userEditForm")
    public UserEditForm getUserEditForm() {
        return new UserEditForm(getUser(), new User());
    }

    @GetMapping("/create")
    public String createUserForm() {
        return "user-create";
    }

    @PostMapping("/create")
    public String createUser(@Valid User user, BindingResult result, Model model) {
        String view;

        if (result.hasErrors()) {
            view = "user-create";
        } else {
            userService.add(user);
            view = "redirect:/";
        }

        return view;
    }

    @GetMapping("/profile")
    public String profile() {
        return "user-profile";
    }

    @GetMapping("/edit")
    public String editForm() {
        return "user-edit";
    }

    @PostMapping("/edit")
    public String edit(UserEditForm editForm) {
        userService.edit(editForm);

        return "redirect:/user/profile";
    }
}