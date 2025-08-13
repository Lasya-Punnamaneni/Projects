package com.login.usermanagement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.login.usermanagement.util.JwtUtil;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // Signup endpoint
    @PostMapping("/signup")
    public String registerUser(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return "Username already exists";
        }

        // Set default role if not provided
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }

        userRepository.save(user);
        return "User successfully registered";
    }

    // Login endpoint
    @PostMapping("/login")
    public String loginUser(@RequestBody User loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());

        if (user == null) {
            return "User not found";
        }

        if (user.getPassword().equals(loginRequest.getPassword())) {
            // Generate token with username and role
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
            return "Login successful. Token: " + token;
        } else {
            return "Invalid credentials";
        }
    }

    // Get user by username
    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userRepository.findByUsername(username);
    }

    // Get all users
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @GetMapping("/signup")
public String testSignupGet() {
    return "Please use POST request with JSON body to register a user.";
}
 
}
