package ua.lozychenko.nonogram.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.lozychenko.nonogram.controller.composite.UserEditForm;
import ua.lozychenko.nonogram.data.entity.User;
import ua.lozychenko.nonogram.data.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping("/create")
    public String createUserForm() {
        return "user-create";
    }

    @PostMapping("/create")
    public String createUser(User user) {
        userService.add(user);

        return "redirect:/";
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
    public String edit(User user, UserEditForm editForm) {
        editForm.setSource(user);
        userService.edit(editForm.getSource(), editForm.getChanges());

        return "redirect:/user/profile";
    }
}