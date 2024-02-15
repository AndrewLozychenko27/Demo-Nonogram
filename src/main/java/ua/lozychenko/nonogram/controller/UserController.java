package ua.lozychenko.nonogram.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.lozychenko.nonogram.constraint.group.CredentialsGroup;
import ua.lozychenko.nonogram.constraint.group.PasswordGroup;
import ua.lozychenko.nonogram.constraint.util.ValidationHelper;
import ua.lozychenko.nonogram.data.entity.Role;
import ua.lozychenko.nonogram.data.entity.User;
import ua.lozychenko.nonogram.service.data.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ua.lozychenko.nonogram.constants.ControllerConstants.BINDING_RESULT;

@Controller
@RequestMapping("/user")
public class UserController {
    public static final String BINDING_RESULT_USER = BINDING_RESULT + "user";
    private final UserService userService;

    @Value("${pages.user.size.default}")
    private String pageSizeDefault;

    private final List<Integer> pageSizeRange;

    public UserController(UserService userService, @Value("${pages.user.size.range}") String pageSizeRange) {
        this.userService = userService;
        this.pageSizeRange = Arrays.stream(pageSizeRange.split(",")).map(Integer::parseInt).collect(Collectors.toList());
    }

    @ModelAttribute
    public User getUser() {
        return new User();
    }

    @GetMapping("/create")
    public String createUserForm() {
        return "user-create";
    }

    @PostMapping("/create")
    public String createUser(@Validated({CredentialsGroup.class, PasswordGroup.class}) User user,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        String view;

        if (result.hasErrors()) {
            model.addAttribute(BINDING_RESULT_USER, ValidationHelper.filterErrors(result));
            view = "user-create";
        } else {
            userService.save(user);
            redirectAttributes.addFlashAttribute("nickname", user.getNickname());
            view = "redirect:/login";
        }

        return view;
    }

    @GetMapping("/{user_id}/profile")
    public String profile(@PathVariable("user_id") User user,
                          Model model) {
        model.addAttribute("user", user);

        return "user-profile";
    }

    @GetMapping("/{user_id}/edit")
    public String editForm(@PathVariable("user_id") User user,
                           Model model) {
        model.addAttribute("user", user);

        return "user-edit";
    }

    @PostMapping("/{user_id}/edit")
    public String edit(@PathVariable("user_id") User user,
                       @Validated(CredentialsGroup.class) User changes,
                       BindingResult result,
                       Model model) {
        String view;

        if (result.hasErrors()) {
            model.addAttribute(BINDING_RESULT_USER, ValidationHelper.filterErrors(result));
            model.addAttribute("changes", changes);
            view = "user-edit";
        } else {
            userService.edit(user, changes);
            view = String.format("redirect:/user/%d/profile", user.getId());
        }

        return view;
    }

    @GetMapping("/{user_id}/reset-password")
    public String resetPasswordForm(@PathVariable("user_id") User user,
                                    Model model) {
        model.addAttribute("user", user);

        return "user-reset-password";
    }

    @PostMapping("/{user_id}/reset-password")
    public String resetPassword(@PathVariable("user_id") User user,
                                @Validated(PasswordGroup.class) User changes,
                                BindingResult result,
                                Model model) {
        String view;

        if (result.hasErrors()) {
            model.addAttribute("changes", changes);
            model.addAttribute(BINDING_RESULT_USER, ValidationHelper.filterErrors(result));
            view = "user-reset-password";
        } else {
            userService.edit(user, changes);
            view = String.format("redirect:/user/%d/profile", user.getId());
        }

        return view;
    }

    @GetMapping("/{user_id}/delete")
    public String deleteForm(@PathVariable("user_id") User user,
                             Model model) {
        model.addAttribute("user", user);

        return "user-delete";
    }

    @PostMapping("/{user_id}/delete")
    public String delete(@PathVariable("user_id") User user,
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
        Pageable configuredPageable = pageable;

        if (pageSizeRange.stream().noneMatch(size -> size == pageable.getPageSize())) {
            configuredPageable = PageRequest.of(pageable.getPageNumber(), Integer.parseInt(pageSizeDefault), pageable.getSort());
        }

        if (nickname.isPresent()) {
            users = userService.findAll(nickname.get(), configuredPageable);
        } else {
            users = userService.findAll(configuredPageable);
        }
        model.addAttribute("users", users);
        model.addAttribute("sizes", pageSizeRange);

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