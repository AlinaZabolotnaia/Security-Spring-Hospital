package org.main.controllers;

import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginControllerTest {

    private final LoginController loginController = new LoginController();

    @Test
    public void login_setsLoggedOutWhenLogoutParameterPresent() {
        ExtendedModelMap model = new ExtendedModelMap();

        String view = loginController.login("true", null, null, model);

        assertEquals("login", view);
        assertTrue(model.containsAttribute("loggedOut"));
        assertEquals(true, model.get("loggedOut"));
    }

    @Test
    public void login_setsLoginErrorWhenErrorParameterPresent() {
        ExtendedModelMap model = new ExtendedModelMap();

        loginController.login(null, "true", null, model);

        assertTrue(model.containsAttribute("loginError"));
        assertEquals(true, model.get("loginError"));
    }

    @Test
    public void login_setsRegistrationSuccessWhenRegisteredParameterPresent() {
        ExtendedModelMap model = new ExtendedModelMap();

        loginController.login(null, null, "true", model);

        assertTrue(model.containsAttribute("registrationSuccess"));
        assertEquals(true, model.get("registrationSuccess"));
    }

    @Test
    public void login_withoutFlags_skipsMessageAttributes() {
        ExtendedModelMap model = new ExtendedModelMap();

        String view = loginController.login(null, null, null, model);

        assertEquals("login", view);
        assertFalse(model.containsAttribute("loggedOut"));
        assertFalse(model.containsAttribute("loginError"));
        assertFalse(model.containsAttribute("registrationSuccess"));
    }
}
