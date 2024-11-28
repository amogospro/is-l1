package com.lab1.lab1.repository;

import com.lab1.lab1.model.entities.ImportHistory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ImportHistoryRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager em;

    @Transactional
    public void create(ImportHistory history) {
        em.persist(history);
    }
}
