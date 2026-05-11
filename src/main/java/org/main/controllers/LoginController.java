package org.main.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "registered", required = false) String registered,
            Model model
    ) {
        if (logout != null) {
            model.addAttribute("loggedOut", true);
        }
        if (error != null) {
            model.addAttribute("loginError", true);
        }
        if (registered != null) {
            model.addAttribute("registrationSuccess", true);
        }
        return "login";
    }
}
