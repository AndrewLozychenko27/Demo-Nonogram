package ua.lozychenko.nonogram.data.service.impl;

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
    public User add(User user) {
        user.setActivated(true);
        user.setRole(Role.PLAYER);
        user.setPassword(encoder.encode(user.getPassword()));

        return super.add(user);
    }

    @Override
    public User edit(User source, User changes) {
        source.setNickname(changes.getNickname());
        source.setEmail(changes.getEmail());
        source.setPassword(encoder.encode(source.getPassword()));

        return repo.save(source);
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        return repo.findByNickname(nickname);
    }

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        return findByNickname(nickname).orElseThrow(() -> new UsernameNotFoundException(String.format("User with a nickname \"%s\" is not found", nickname)));
    }
}