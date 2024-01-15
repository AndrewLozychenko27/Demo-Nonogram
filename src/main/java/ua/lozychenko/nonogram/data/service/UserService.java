package ua.lozychenko.nonogram.data.service;

import ua.lozychenko.nonogram.data.entity.User;

import java.util.Optional;

public interface UserService extends BaseService<User> {
    Optional<User> findByNickname(String nickname);
}