package ua.lozychenko.nonogram.data.service;

import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.data.entity.User;

import java.util.Optional;

@Service
public interface UserService extends BaseService<User> {
    Optional<User> findByNickname(String nickname);
}