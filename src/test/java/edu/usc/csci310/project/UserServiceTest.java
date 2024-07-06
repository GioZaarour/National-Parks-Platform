package edu.usc.csci310.project;

import edu.usc.csci310.project.model.User;
import edu.usc.csci310.project.repository.UserRepository;
import edu.usc.csci310.project.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
    }

    @Test
    public void testRegisterNewUserAccount_Success() throws Exception {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(user);
        User createUser = userService.registerNewUserAccount(user);
        assertNotNull(createUser);
        assertEquals(user.getUsername(), createUser.getUsername());
    }

    @Test
    public void testRegisterNewUserAccount_ThrowsException() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        Exception exception = assertThrows(Exception.class, () -> userService.registerNewUserAccount(user));
        assertTrue(exception.getMessage().contains("There is an account with that username"));
    }

    @Test
    public void testFindUserByUsername() {
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        User foundUser = userService.findUserByUsername("testUser");

        assertNotNull(foundUser);
        assertEquals("testUser", foundUser.getUsername());
    }
}
