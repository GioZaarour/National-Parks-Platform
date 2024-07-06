package edu.usc.csci310.project.controller;

//import edu.usc.csci310.project.exception.InvalidCredentialsException;
import edu.usc.csci310.project.model.User;
import edu.usc.csci310.project.repository.UserRepository;
import edu.usc.csci310.project.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import javax.validation.Valid;
import java.util.Collections;
import org.springframework.http.HttpStatus;


@CrossOrigin
@RestController("UserController")
@AllArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/api/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.registerNewUserAccount(user);
            return ResponseEntity.ok(createdUser); // Returns the saved user object
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpServletRequest request) {
        User foundUser = userService.findUserByUsername(user.getUsername());
        if (foundUser == null || !passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("response", "Invalid username or password"));
        } else /*if (foundUser != null && passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) */ {
            request.getSession().setAttribute("user", foundUser);
            return ResponseEntity.ok().body(Collections.singletonMap("response", "User logged in successfully"));
        }
    }

    @PostMapping("/setPrivate")
    public ResponseEntity<?> setPrivate(@RequestParam String username, @RequestParam boolean isPrivate) {
        try {
            userService.setPrivate(username, isPrivate);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("User does not exist");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }   

    @GetMapping("/isListPrivate")
    public ResponseEntity<Boolean> isListPrivate(@RequestParam String username) {
        try {
            boolean isPrivate = userService.isListPrivate(username);
            return ResponseEntity.ok(isPrivate);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/api/checkSession")
    public ResponseEntity<?> checkSession(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            System.out.println("User session found for: " + user.getUsername());
            return ResponseEntity.ok().body(user);
        } else {
            System.out.println("No user session found");
            return ResponseEntity.ok().body(null);
        }
    }

    @PostMapping("/api/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok().body(Collections.singletonMap("response", "User logged out successfully"));
    }


    @GetMapping("/getUserByUsername")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        User user = userRepository.findByUsername(username);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }
}



