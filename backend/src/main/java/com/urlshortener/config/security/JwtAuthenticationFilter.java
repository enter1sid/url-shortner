package com.urlshortener.config.security;

import com.urlshortener.model.User;
import com.urlshortener.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Log all headers for debugging
        java.util.Enumeration<String> headerNames = request.getHeaderNames();
        System.out.println("--- Incoming Request Headers ---");
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            System.out.println(name + ": " + request.getHeader(name));
        }
        System.out.println("-------------------------------");
        String header = request.getHeader("Authorization");
        String token = null;
        String username = null;
        String email = null;
        if (header != null) {
            token = header.replaceAll("Bearer ", "");
            username = jwtUtil.getUsernameFromToken(token);
            email = jwtUtil.getEmailFromToken(token);
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<User> userOpt = userService.findByUsername(username);
            if (userOpt.isEmpty() && email != null) {
                userOpt = userService.findByEmail(email);
            }
            if (userOpt.isPresent() && jwtUtil.validateToken(token)) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userOpt.get(), null, null);
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }
}
