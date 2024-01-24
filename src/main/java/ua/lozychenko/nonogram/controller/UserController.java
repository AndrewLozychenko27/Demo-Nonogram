package ua.lozychenko.nonogram.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.lozychenko.nonogram.constraint.group.CredentialsGroup;
import ua.lozychenko.nonogram.constraint.group.PasswordGroup;
import ua.lozychenko.nonogram.constraint.group.UniqueEmailGroup;
import ua.lozychenko.nonogram.constraint.util.ValidationHelper;
import ua.lozychenko.nonogram.controller.composite.UserEditForm;
import ua.lozychenko.nonogram.data.entity.User;
import ua.lozychenko.nonogram.data.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
    public static final String BINDING_RESULT = "org.springframework.validation.BindingResult.";
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
    public String createUser(@Validated({UniqueEmailGroup.class, CredentialsGroup.class, PasswordGroup.class}) User user,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        String view;

        if (result.hasErrors()) {
            model.addAttribute(BINDING_RESULT + "user", ValidationHelper.filterErrors(result));
            view = "user-create";
        } else {
            userService.add(user);
            redirectAttributes.addFlashAttribute("nickname", user.getNickname());
            view = "redirect:/login";
        }

        return view;
    }

    @GetMapping("/profile")
    public String profile() {
        return "user-profile";
    }

    @GetMapping("/edit")
    public String editForm(@ModelAttribute UserEditForm userEditForm) {
        return "user-edit";
    }

    @PostMapping("/edit")
    public String edit(@Validated(CredentialsGroup.class) UserEditForm editForm, BindingResult result, Model model) {
        String view;

        if (result.hasErrors()) {
            model.addAttribute(BINDING_RESULT + "userEditForm", ValidationHelper.filterErrors(result));
            model.addAttribute("changes", editForm.getChanges());
            view = "user-edit";
        } else {
            userService.edit(editForm);
            view = "redirect:/user/profile";
        }

        return view;
    }
}