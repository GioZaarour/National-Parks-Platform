package edu.usc.csci310.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import java.util.Map;
import edu.usc.csci310.project.exception.UsernameExistsException;
import edu.usc.csci310.project.exception.GlobalExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webRequest = new ServletWebRequest(new MockHttpServletRequest());
    }

    @Test
    void handleUsernameExistsException() {
        UsernameExistsException exception = new UsernameExistsException("Username already exists");
        ResponseEntity<?> responseEntity = globalExceptionHandler.handleUsernameExistsException(exception);
        assertEquals(400, responseEntity.getStatusCode().value());
        assertEquals("Username already exists", responseEntity.getBody());
    }

    @SuppressWarnings("unchecked") // Suppress the unchecked cast warning
    @Test
    void handleValidationExceptions() {
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, new BeanPropertyBindingResult(null, "user"));
        exception.getBindingResult().addError(new FieldError("user", "username", "Username is required"));

        ResponseEntity<?> responseEntity = globalExceptionHandler.handleValidationExceptions(exception);
        assertEquals(400, responseEntity.getStatusCode().value());
        assertEquals("Username is required", ((Map<String, String>) responseEntity.getBody()).get("username"));
    }
}