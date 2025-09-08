package com.products.redis.spring.config;

import com.products.redis.spring.entity.AppUser;
import com.products.redis.spring.entity.Role;
import com.products.redis.spring.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createUserIfNotExists("superadmin", "super123", Role.SUPERADMIN);
        createUserIfNotExists("admin", "admin123", Role.ADMIN);
        createUserIfNotExists("user", "user123", Role.USER);
    }

    private void createUserIfNotExists(String username, String password, Role role) {
        if (!userRepository.existsByUsername(username)) {
            AppUser user = new AppUser();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            userRepository.save(user);
            System.out.println(role + " created: " + username + " / " + password);
        }
    }
}
