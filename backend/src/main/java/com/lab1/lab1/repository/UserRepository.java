package com.lab1.lab1.repository;

import com.lab1.lab1.model.entities.Person;
import com.lab1.lab1.model.entities.Product;
import com.lab1.lab1.model.entities.Role;
import com.lab1.lab1.model.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class UserRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager em;

    @Transactional
    public void create(User user) {
        em.persist(user);
    }

    @Transactional
    public void update(User user) {
        em.merge(user);
    }

    public User findByUsername(String username) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public User findByPasswordHash(String passwordHash) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.passwordHash = :passwordHash", User.class)
                    .setParameter("passwordHash", passwordHash)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean existsAdmin() {
        Long count = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.role = :role", Long.class)
                .setParameter("role", Role.ADMIN)
                .getSingleResult();
        return count > 0;
    }

    public User findById(Long userId) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.id = :userId", User.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<User> findAllPendingAdmins() {
        List<User> users = em.createQuery("SELECT u FROM User u WHERE u.role = :role", User.class)
                .setParameter("role", Role.PENDING_ADMIN)
                .getResultList();


        return users;
    }

    // Other user-related queries
}

