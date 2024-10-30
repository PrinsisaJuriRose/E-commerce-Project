package com.example.demo.security;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class UserDetailsServiceImplTest {
    private final UserRepository userRepo = mock(UserRepository.class);
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Before
    public void setUp() {
        userDetailsServiceImpl = new UserDetailsServiceImpl();
        com.example.demo.TestUtils.injectObjects(userDetailsServiceImpl, "userRepository", userRepo);
    }

    @Test
    public void load_user_by_username_happy() {
        User user = new User();
        user.setId(0L);
        user.setUsername("test");
        user.setPassword( "testPassword");
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(user.getUsername());
        assertNotNull(userDetails);
        Collection<? extends GrantedAuthority> authorityCollection = userDetails.getAuthorities();
        assertNotNull(authorityCollection);
        assertEquals(0, authorityCollection.size());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertEquals(user.getUsername(), userDetails.getUsername());
    }
}
