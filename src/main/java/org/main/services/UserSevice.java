package org.main.services;

import org.main.entities.User;
import org.main.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserSevice implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (loginAttemptService.isBlocked(username)) {
            throw new LockedException("Аккаунт временно заблокирован из-за множества неудачных попыток. Попробуйте через 15 минут.");
        }
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return user;
    }

    public User getUser(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }
}
