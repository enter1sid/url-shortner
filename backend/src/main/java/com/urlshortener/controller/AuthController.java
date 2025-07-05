package com.urlshortener.controller;

import com.urlshortener.config.security.JwtUtil;
import com.urlshortener.model.User;
import com.urlshortener.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String email = request.get("email");
        String password = request.get("password");
        if (userService.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if (userService.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        userService.registerUser(username, email, password);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String usernameOrEmail = request.get("usernameOrEmail");
        String password = request.get("password");
        Optional<User> userOpt = userService.findByUsername(usernameOrEmail);
        if (userOpt.isEmpty()) {
            userOpt = userService.findByEmail(usernameOrEmail);
        }
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            String token = jwtUtil.generateToken(userOpt.get().getUsername(), userOpt.get().getEmail());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
