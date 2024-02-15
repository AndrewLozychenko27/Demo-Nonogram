package ua.lozychenko.nonogram.service.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.lozychenko.nonogram.data.entity.User;

import java.util.Optional;

public interface UserService extends BaseService<User> {
    Optional<User> findByNickname(String nickname);

    Optional<User> findByEmail(String email);

    boolean isPasswordMatched(Long id, String password);

    Page<User> findAll(String nickname, Pageable pageable);
}