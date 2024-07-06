package edu.usc.csci310.project;

import edu.usc.csci310.project.controller.UserController;
import edu.usc.csci310.project.model.User;
import edu.usc.csci310.project.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void whenSignupSuccess_returnSavedUser() throws Exception {
        User user = new User();
        user.setUsername("user");
        user.setPassword("pass");

        when(userService.registerNewUserAccount(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"user\",\"password\":\"pass\"}"))
                .andExpect(status().isOk()).andExpect(content().json("{\"username\":\"user\",\"password\":\"pass\"}"));
        verify(userService).registerNewUserAccount(any(User.class));
    }
    @Test
    public void whenSignupFails_thenReturnBadRequest() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        given(userService.registerNewUserAccount(any(User.class))).willThrow(new RuntimeException("User registration failed"));

        mockMvc.perform(post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"testuser\",\"password\":\"testpassword\"}"))
                .andExpect(status().isBadRequest()).andExpect(content().string("User registration failed"));
    }
    @Test
    public void whenSignupException_returnBadRequest() throws Exception {
        // Setup
        User user = new User("username", "password");
        when(userService.registerNewUserAccount(any(User.class))).thenThrow(new RuntimeException("Error occurred"));
    
        // Execute & Assert
        mockMvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"user\",\"password\":\"pass\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenLoginSuccess_returnOk() throws Exception {
        User user = new User();
        user.setUsername("user");
        user.setPassword("pass");
        when(userService.findUserByUsername("user")).thenReturn(user);
        when(bCryptPasswordEncoder.matches("pass", "pass")).thenReturn(true);
        mockMvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"user\",\"password\":\"pass\"}"))
                .andExpect(status().isOk()).andExpect(content().string("{\"response\":\"User logged in successfully\"}"));
    }

    @Test
    void whenUserNotFound_returnBadRequest() throws Exception {
        when(userService.findUserByUsername("user")).thenReturn(null);
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"user\",\"password\":\"pass\"}"))
                .andExpect(status().isBadRequest()).andExpect(content().string("{\"response\":\"Invalid username or password\"}"));
    }

    @Test
    void whenPasswordIncorrect_returnBadRequest() throws Exception {
        User user = new User();
        user.setUsername("user");
        user.setPassword("correctPassword");
        when(userService.findUserByUsername("user")).thenReturn(user);
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"password\":\"wrongPassword\"}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("{\"response\":\"Invalid username or password\"}"));
    }
}
