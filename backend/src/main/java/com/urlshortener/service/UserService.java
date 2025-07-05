package com.urlshortener.service;

import com.urlshortener.model.User;
import com.urlshortener.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.urlshortener.dto.UserDto;
import com.urlshortener.dto.UrlDto;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(String username, String email, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, email, encodedPassword);
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDto toDto(User user) {
        if (user == null) return null;
        return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getUrls() == null ? new ArrayList<>() : user.getUrls().stream().map(url -> new UrlDto(url.getId(), url.getShortUrl(), url.getLongUrl(), url.getTitle())).collect(Collectors.toList())
        );
    }
}
