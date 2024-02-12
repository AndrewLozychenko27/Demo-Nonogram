package ua.lozychenko.nonogram.data.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.data.entity.Role;
import ua.lozychenko.nonogram.data.entity.User;
import ua.lozychenko.nonogram.data.repo.UserRepo;
import ua.lozychenko.nonogram.data.service.UserService;

import java.util.Optional;

@Service
public class DefaultUserService extends DefaultBaseService<User> implements UserService, UserDetailsService {
    private final UserRepo repo;
    private final PasswordEncoder encoder;

    public DefaultUserService(UserRepo repo, PasswordEncoder encoder) {
        super(repo);
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public User save(User user) {
        user.setActivated(true);
        user.setRole(Role.PLAYER);
        user.setPassword(encoder.encode(user.getPassword()));

        return super.save(user);
    }

    @Override
    public User edit(User source, User changes) {
        source.setNickname(getIfChanged(source.getNickname(), changes.getNickname()));
        source.setEmail(getIfChanged(source.getEmail(), changes.getEmail()));
        source.setRole(getIfChanged(source.getRole(), changes.getRole()));
        source.setActivated(getIfChanged(source.isActivated(), changes.isActivated()));
        if (changes.getPassword() != null && !encoder.matches(changes.getPassword(), source.getPassword())) {
            source.setPassword(encoder.encode(changes.getPassword()));
        }

        return repo.save(source);
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        return repo.findByNickname(nickname);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repo.findByEmail(email);
    }

    @Override
    public boolean isPasswordMatched(Long id, String password) {
        return encoder.matches(password, repo.getReferenceById(id).getPassword());
    }

    @Override
    public Page<User> findAll(String nickname, Pageable pageable) {
        return repo.findAllByNicknameContaining(nickname, pageable);
    }

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        return findByNickname(nickname).orElseThrow(() -> new UsernameNotFoundException(String.format("User with a nickname \"%s\" is not found", nickname)));
    }
}