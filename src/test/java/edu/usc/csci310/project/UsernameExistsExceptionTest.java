package edu.usc.csci310.project;

import edu.usc.csci310.project.exception.UsernameExistsException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UsernameExistsExceptionTest {

    @Test
    public void whenExceptionThrown_thenMessageIsCorrect() {
        // Given
        String expectedMessage = "This username already exists.";

        // When
        UsernameExistsException exception = new UsernameExistsException(expectedMessage);

        // Then
        assertNotNull(exception, "UsernameExistsException should not be null");
        assertEquals(expectedMessage, exception.getMessage(), "Exception message should match the expected message.");
    }
}
