package ua.lozychenko.nonogram.data.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.controller.composite.UserEditForm;
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
    public User edit(UserEditForm userEditForm) {
        User source = userEditForm.getSource();
        User changes = userEditForm.getChanges();

        source.setNickname(changes.getNickname());
        if (!source.getEmail().equals(changes.getEmail())) {
            source.setEmail(changes.getEmail());
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
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        return findByNickname(nickname).orElseThrow(() -> new UsernameNotFoundException(String.format("User with a nickname \"%s\" is not found", nickname)));
    }
}