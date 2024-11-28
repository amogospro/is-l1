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
import jakarta.jws.soap.SOAPBinding;
import jakarta.transaction.Transactional;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

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
                .claim("id", user.getId())
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

            String username = claimsJws.getBody().getSubject(); // Извлекаем имя пользователя из токена
            String role = claimsJws.getBody().get("role", String.class); // Извлекаем роль из токена
            Long id = claimsJws.getBody().get("id", Long.class); // Извлекаем ID пользователя из токена

            // Создаём объект User без обращения к БД
            User user = new User();
            user.setUsername(username);
            user.setRole(Role.valueOf(role));
            user.setId(id);
            return user;
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

        User uniqueUserPassword = userRepository.findByPasswordHash(PasswordUtil.hashPassword(password));

        if (uniqueUserPassword != null) {
            throw new Exception("Password already exists by "+ uniqueUserPassword.getUsername());
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
            userRepository.update(pendingUser);
        } else {
            throw new Exception("Invalid user or already approved");
        }
    }

    public List<User> getAllPendingAdmins() {
        return userRepository.findAllPendingAdmins();
    }
}

