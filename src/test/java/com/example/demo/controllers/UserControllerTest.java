package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp()
    {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository",userRepo);
        TestUtils.injectObjects(userController, "cartRepository",cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder",encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception
    {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");
        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed",u.getPassword());
    }

    @Test
    public void find_by_id_happy_path()
    {
        User u = new User();
        u.setId(1);
        u.setUsername("Nedaa");
        u.setPassword("pass12345");
        when(userRepo.findById(u.getId())).thenReturn(Optional.of(u));
        final ResponseEntity<User> response = userController.findById(u.getId());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Nedaa", response.getBody().getUsername());
        verify(userRepo, times(1)).findById(u.getId());
    }

    @Test
    public void find_by_name_happy_path()
    {
        User u = new User();
        u.setId(1);
        u.setUsername("Nedaa");
        u.setPassword("pass12345");

        when(userRepo.findByUsername(u.getUsername())).thenReturn(u);
        final ResponseEntity<User> response = userController.findByUserName(u.getUsername());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Nedaa", response.getBody().getUsername());
        verify(userRepo, times(1)).findByUsername(u.getUsername());
    }
}
