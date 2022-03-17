package com.example.demo.controllertests;

import com.example.demo.UtilTesting;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup() {
        userController = new UserController();
        UtilTesting.injectObject(userController, "userRepository", userRepository);
        UtilTesting.injectObject(userController, "cartRepository", cartRepository);
        UtilTesting.injectObject(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = createUserRequest("Test", "testPassword", "testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("Test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void verifyFindById() throws Exception {
        // Creating User
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = createUserRequest("Test", "testPassword", "testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("Test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

        // Testing user lookup by Id
        when(userRepository.findById(u.getId())).thenReturn(Optional.of(u));
        final ResponseEntity<User> response2 = userController.findById(u.getId());
        User userFound = response2.getBody();
        assertEquals(200, response2.getStatusCodeValue());
        assertEquals(0, userFound.getId());
        assertEquals("Test", userFound.getUsername());
        assertEquals("thisIsHashed", userFound.getPassword());
    }

    @Test
    public void verifyFindByUsername() throws Exception {
        // Creating User
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = createUserRequest("Test", "testPassword", "testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("Test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

        // Testing user lookup by Username
        when(userRepository.findByUsername(u.getUsername())).thenReturn(u);
        final ResponseEntity<User> response2 = userController.findByUserName(u.getUsername());
        User userFound = response2.getBody();
        assertEquals(200, response2.getStatusCodeValue());
        assertEquals(0, userFound.getId());
        assertEquals("Test", userFound.getUsername());
        assertEquals("thisIsHashed", userFound.getPassword());
    }

    @Test
    public void verifyUserNotCreated() {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = createUserRequest("Test", "1234", "1234");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());

        User u = response.getBody();
        assertNull(u);
    }

    @Test
    public void verifyFindByIdNotFound() {
        final ResponseEntity<User> response = userController.findByUserName("Joan");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void verifyFindByUsernameNotFound() {
        final ResponseEntity<User> response = userController.findById(1L);
        assertEquals(404, response.getStatusCodeValue());
    }


    public CreateUserRequest createUserRequest(String username, String password, String confirmPassword) {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername(username);
        userRequest.setPassword(password);
        userRequest.setConfirmPassword(confirmPassword);
        return userRequest;
    }

}
