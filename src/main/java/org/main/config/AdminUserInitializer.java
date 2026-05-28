package org.main.config;

import lombok.RequiredArgsConstructor;
import org.main.entities.Role;
import org.main.entities.User;
import org.main.repository.UserRepo;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.admin.username:admin}")
    private String adminUsername;

    @Value("${app.admin.password:}")
    private String adminPassword;

    @Override
    public void run(ApplicationArguments args) {
        if (adminPassword == null || adminPassword.trim().isEmpty()) {
            throw new IllegalStateException(
                    "Admin password not configured. Set ADMIN_PASSWORD env variable or app.admin.password property.");
        }
        if (userRepo.findByUsername(adminUsername) != null) {
            return;
        }
        User admin = new User();
        admin.setUsername(adminUsername);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setActive(true);
        admin.setRoles(Collections.singleton(Role.ADMIN));
        userRepo.save(admin);
    }
}
