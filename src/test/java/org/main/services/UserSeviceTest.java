package org.main.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.main.entities.Role;
import org.main.entities.User;
import org.main.repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserSeviceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserSevice userSevice;

    @Test
    public void loadUserByUsername_returnsUserWhenFound() {
        User user = new User();
        user.setUsername("alice");
        user.setRoles(new HashSet<>(Collections.singletonList(Role.USER)));
        when(userRepo.findByUsername("alice")).thenReturn(user);

        UserDetails details = userSevice.loadUserByUsername("alice");

        assertSame(user, details);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_throwsWhenMissing() {
        when(userRepo.findByUsername("nobody")).thenReturn(null);

        userSevice.loadUserByUsername("nobody");
    }

    @Test
    public void getUser_returnsEntityFromRepo() {
        User user = new User();
        when(userRepo.findByUsername("bob")).thenReturn(user);

        assertSame(user, userSevice.getUser("bob"));
    }
}
