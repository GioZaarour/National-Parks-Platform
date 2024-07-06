package edu.usc.csci310.project.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "users") // Using lowercase for compatibility across different DBMS
@jakarta.persistence.Entity
public class User {
    @jakarta.persistence.Id
    private Long id;

    @NotBlank(message = "Username cannot be empty") // Consolidated to NotBlank for simplicity
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password cannot be empty") // Consolidated to NotBlank for simplicity
    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    @Column(updatable = false) // Ensures this field is only set when the record is first created
    private LocalDateTime createdAt;

    @Column(name = "private", nullable = false)
    private boolean isPrivate = false;

    // Custom constructor for username and password only
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
