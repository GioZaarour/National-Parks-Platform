package edu.usc.csci310.project;

import edu.usc.csci310.project.DTO.UsernameRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsernameRequestTest {

    private UsernameRequest usernameRequest;

    @BeforeEach
    public void setUp() {
        usernameRequest = new UsernameRequest();
    }

    @Test
    public void testSetAndGetUsername() {
        String expectedUsername = "testUser";
        usernameRequest.setUsername(expectedUsername);
        String actualUsername = usernameRequest.getUsername();
        assertEquals(expectedUsername, actualUsername, "The getter or setter for username is not working correctly.");
    }
}
