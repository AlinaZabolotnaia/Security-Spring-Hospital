package org.main.controllers;

import org.main.entities.Role;
import org.main.entities.User;
import org.main.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.put("message", "Пользователь с таким именем уже зарегистрирован.");
            return "registration";
        }

        String password = user.getPassword();
        if (password == null || password.length() < 8) {
            model.put("message", "Пароль должен содержать не менее 8 символов.");
            return "registration";
        }
        if (!password.matches(".*[A-Za-z].*") || !password.matches(".*[0-9].*")) {
            model.put("message", "Пароль должен содержать буквы и цифры.");
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(password));
        userRepo.save(user);

        return "redirect:/login?registered";
    }
}
