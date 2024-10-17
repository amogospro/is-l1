package com.lab1.lab1.service;

import com.lab1.lab1.model.entities.Role;
import com.lab1.lab1.model.entities.User;
import com.lab1.lab1.repository.UserRepository;
import com.lab1.lab1.util.KeyUtil;
import com.lab1.lab1.util.PasswordUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@ApplicationScoped
public class AuthService {

    @Inject
    private UserRepository userRepository;

    private final Key key = Keys.hmacShaKeyFor(KeyUtil.getSecretKey());

    public User authenticate(String username, String password) throws NoSuchAlgorithmException {
        User user = userRepository.findByUsername(username);
        if (user != null && PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public String generateToken(User user) {
        long expirationTime = 1000 * 60 * 60 * 24; // Токен действует 24 часа
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public User validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            String username = claimsJws.getBody().getSubject();
            return userRepository.findByUsername(username);
        } catch (JwtException e) {
            // Токен недействителен
            return null;
        }
    }

    @Transactional
    public void register(String username, String password, boolean isAdminRequest) throws Exception {
        if (userRepository.findByUsername(username) != null) {
            throw new Exception("User already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(PasswordUtil.hashPassword(password));

        if (isAdminRequest) {
            if (userRepository.existsAdmin()) {
                user.setRole(Role.PENDING_ADMIN);
                // Notify existing admins for approval
            } else {
                user.setRole(Role.ADMIN);
            }
        } else {
            user.setRole(Role.USER);
        }

        userRepository.create(user);
    }

    // Method for admins to approve pending admin requests
    @Transactional
    public void approveAdminRequest(Long userId, User admin) throws Exception {
        if (admin.getRole() != Role.ADMIN) {
            throw new Exception("Access Denied");
        }
        User pendingUser = userRepository.findById(userId);
        if (pendingUser != null && pendingUser.getRole() == Role.PENDING_ADMIN) {
            pendingUser.setRole(Role.ADMIN);
            // Update user in the database
        } else {
            throw new Exception("Invalid user or already approved");
        }
    }
}

