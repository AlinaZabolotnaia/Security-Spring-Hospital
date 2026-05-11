package org.main.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.main.entities.Role;
import org.main.entities.User;
import org.main.repository.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationControllerTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrationController registrationController;

    @Test
    public void addUser_whenUsernameFree_encodesPasswordRedirectsToLogin() {
        when(userRepo.findByUsername("newuser")).thenReturn(null);
        when(passwordEncoder.encode("secret")).thenReturn("{bcrypt}stub");

        User incoming = new User();
        incoming.setUsername("newuser");
        incoming.setPassword("secret");

        String view = registrationController.addUser(incoming, new HashMap<>());

        assertEquals("redirect:/login?registered", view);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(captor.capture());
        User saved = captor.getValue();
        assertEquals("{bcrypt}stub", saved.getPassword());
        assertTrue(saved.isActive());
        assertTrue(saved.getRoles().contains(Role.USER));
    }

    @Test
    public void addUser_whenUsernameTaken_staysOnRegistrationWithMessage() {
        User existing = new User();
        existing.setUsername("taken");
        when(userRepo.findByUsername("taken")).thenReturn(existing);

        User incoming = new User();
        incoming.setUsername("taken");
        incoming.setPassword("x");

        Map<String, Object> model = new HashMap<>();
        String view = registrationController.addUser(incoming, model);

        assertEquals("registration", view);
        assertEquals("Пользователь с таким именем уже зарегистрирован.", model.get("message"));
        verify(userRepo, never()).save(any());
        verifyZeroInteractions(passwordEncoder);
    }
}
