package ua.lozychenko.nonogram.data.service;

import ua.lozychenko.nonogram.controller.composite.UserEditForm;
import ua.lozychenko.nonogram.data.entity.User;

import java.util.Optional;

public interface UserService extends BaseService<User> {
    User edit(UserEditForm userEditForm);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByEmail(String email);
}