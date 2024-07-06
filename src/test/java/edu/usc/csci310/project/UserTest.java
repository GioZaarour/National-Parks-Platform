package edu.usc.csci310.project;

import edu.usc.csci310.project.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    public void testUsername() {
        String expectedUsername = "testUser";
        user.setUsername(expectedUsername);
        assertEquals(expectedUsername, user.getUsername());
    }

    @Test
    public void testPassword() {
        String expectedPassword = "password";
        user.setPassword(expectedPassword);
        assertEquals(expectedPassword, user.getPassword());
    }
}
