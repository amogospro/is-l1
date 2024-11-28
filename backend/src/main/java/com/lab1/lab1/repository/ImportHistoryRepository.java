package com.lab1.lab1.repository;

import com.lab1.lab1.model.entities.ImportHistory;
import com.lab1.lab1.model.entities.Role;
import com.lab1.lab1.model.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ImportHistoryRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager em;

    @Transactional
    public void create(ImportHistory history) {
        em.persist(history);
    }

    public List<ImportHistory> findByUser(User user) {
        if (user.getRole() == Role.ADMIN) {
            return em.createQuery("SELECT h FROM ImportHistory h", ImportHistory.class)
                    .getResultList();
        } else {
            return em.createQuery("SELECT h FROM ImportHistory h WHERE h.userName = :username", ImportHistory.class)
                    .setParameter("username", user.getName())
                    .getResultList();
        }
    }
}
