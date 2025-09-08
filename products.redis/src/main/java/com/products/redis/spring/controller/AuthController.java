package com.products.redis.spring.controller;
import com.products.redis.spring.entity.AppUser;
import com.products.redis.spring.entity.Role;
import com.products.redis.spring.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;

    private final Key secretKey = Keys.hmacShaKeyFor("my_super_secret_key_that_is_at_least_32_chars!".getBytes());

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String,String> req) {
        String username = req.get("username");
        String password = req.get("password");
        Role role = Role.valueOf(req.get("role"));

        AppUser user = new AppUser(username, passwordEncoder.encode(password), role);
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message","User registered successfully!"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> req) {
        String username = req.get("username");
        String password = req.get("password");

        authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        String token = Jwts.builder()
                .setSubject(username)
                .claim("role", userRepository.findByUsername(username).get().getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return ResponseEntity.ok(Map.of("token", token));
    }
}
