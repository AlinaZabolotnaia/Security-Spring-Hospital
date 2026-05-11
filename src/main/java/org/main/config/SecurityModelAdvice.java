package org.main.config;

import org.main.entities.Role;
import org.main.entities.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class SecurityModelAdvice {

    @ModelAttribute
    public void addSecurityToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("isAuthenticated", false);
        model.addAttribute("isAdmin", false);
        model.addAttribute("isUser", false);
        model.addAttribute("name", "Guest");

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            return;
        }

        User user = (User) principal;
        model.addAttribute("isAuthenticated", true);
        model.addAttribute("name", user.getUsername());
        if (user.getRoles() != null) {
            model.addAttribute("isAdmin", user.getRoles().contains(Role.ADMIN));
            model.addAttribute("isUser", user.getRoles().contains(Role.USER));
        }
    }
}
