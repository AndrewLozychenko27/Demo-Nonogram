package ua.lozychenko.nonogram.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.lozychenko.nonogram.constraint.group.CredentialsGroup;
import ua.lozychenko.nonogram.constraint.group.PasswordGroup;
import ua.lozychenko.nonogram.constraint.group.UniqueEmailGroup;
import ua.lozychenko.nonogram.constraint.util.ValidationHelper;
import ua.lozychenko.nonogram.controller.composite.UserEditForm;
import ua.lozychenko.nonogram.data.entity.Role;
import ua.lozychenko.nonogram.data.entity.User;
import ua.lozychenko.nonogram.data.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    public static final String BINDING_RESULT = "org.springframework.validation.BindingResult.";
    public static final String BINDING_RESULT_EDIT_FORM = BINDING_RESULT + "userEditForm";
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
            userService.save(user);
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
    public String edit(@Validated(CredentialsGroup.class) UserEditForm editForm,
                       BindingResult result,
                       Model model) {
        String view;

        if (result.hasErrors()) {
            model.addAttribute(BINDING_RESULT_EDIT_FORM, ValidationHelper.filterErrors(result));
            model.addAttribute("changes", editForm.getChanges());
            view = "user-edit";
        } else {
            userService.edit(editForm);
            view = "redirect:/user/profile";
        }

        return view;
    }

    @GetMapping("/reset-password")
    public String resetPasswordForm() {
        return "user-reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@Validated(PasswordGroup.class) UserEditForm editForm,
                                BindingResult result,
                                Model model) {
        String view;

        if (result.hasErrors()) {
            model.addAttribute("changes", editForm.getChanges());
            result = ValidationHelper.filterErrors(result);
            result = ValidationHelper.renameFieldError(result, "changes", "changes.passwordConfirmation");
            model.addAttribute(BINDING_RESULT_EDIT_FORM, result);
            view = "user-reset-password";
        } else {
            userService.edit(editForm);
            view = "redirect:/user/profile";
        }

        return view;
    }

    @GetMapping("/delete")
    public String deleteForm() {
        return "user-delete";
    }

    @PostMapping("/delete")
    public String delete(@AuthenticationPrincipal User user,
                         String password,
                         Model model,
                         HttpServletRequest httpServletRequest) throws ServletException {
        String view;

        if (userService.isPasswordMatched(user.getId(), password)) {
            userService.delete(user.getId());
            httpServletRequest.logout();
            view = "redirect:/";
        } else {
            model.addAttribute("error", "Wrong password");
            view = "user-delete";
        }

        return view;
    }

    @GetMapping("/list")
    public String users(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Optional<String> nickname,
                        Model model) {
        Page<User> users;

        if (nickname.isPresent()) {
            users = userService.findAll(nickname.get(), pageable);
        } else {
            users = userService.findAll(pageable);
        }
        model.addAttribute("users", users);

        return "user-list";
    }

    @PostMapping("/change-role")
    @Transactional
    public String changeRole(@RequestParam("userId") User user, Role role, Long page) {
        user.setRole(role);

        return "redirect:/user/list?page=" + page;
    }

    @PostMapping("/change-status")
    @Transactional
    public String changeStatus(@RequestParam("userId") User user, Boolean status, Long page) {
        user.setActivated(status);

        return "redirect:/user/list?page=" + page;
    }
}