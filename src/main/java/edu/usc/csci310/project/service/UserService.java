package edu.usc.csci310.project.service;

// import org.springframework.beans.factory.annotation.Autowired;
import edu.usc.csci310.project.exception.UsernameExistsException;
import edu.usc.csci310.project.model.User;
import edu.usc.csci310.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Service("UserService")
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User registerNewUserAccount(User user) throws UsernameExistsException {
        if(userRepository.findByUsername(user.getUsername()) != null) {
            throw new UsernameExistsException("There is an account with that username: " + user.getUsername());
        }
        user.setId(generateUniqueId());
        user.setPrivate(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(java.time.LocalDateTime.now());
        return userRepository.save(user);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void setPrivate(String username, boolean isPrivate) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setPrivate(isPrivate);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User does not exist");
        }
    }

    public boolean isListPrivate(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.isPrivate();
        }
        throw new IllegalArgumentException("User does not exist");
    }
    
    private Long generateUniqueId() {
        // Implement a method to generate a unique ID.
        // This is just a placeholder and needs to be implemented.
        return System.currentTimeMillis(); // Example, not recommended for production use
    }
}
