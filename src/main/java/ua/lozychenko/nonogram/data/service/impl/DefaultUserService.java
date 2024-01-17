package ua.lozychenko.nonogram.data.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.data.entity.User;
import ua.lozychenko.nonogram.data.repo.UserRepo;
import ua.lozychenko.nonogram.data.service.UserService;

import java.util.Optional;

@Service
public class DefaultUserService extends DefaultBaseService<User> implements UserService, UserDetailsService {
    private UserRepo repo;

    public DefaultUserService(UserRepo repo) {
        super(repo);
        this.repo = repo;
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