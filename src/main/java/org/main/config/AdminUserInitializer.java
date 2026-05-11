package org.main.config;

import lombok.RequiredArgsConstructor;
import org.main.entities.Role;
import org.main.entities.User;
import org.main.repository.UserRepo;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer implements ApplicationRunner {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepo.findByUsername("admin") != null) {
            return;
        }
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setActive(true);
        admin.setRoles(Collections.singleton(Role.ADMIN));
        userRepo.save(admin);
    }
}
