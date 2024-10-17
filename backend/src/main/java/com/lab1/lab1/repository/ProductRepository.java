package com.lab1.lab1.repository;

import com.lab1.lab1.model.entities.Product;
import com.lab1.lab1.model.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ProductRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager em;

    @Transactional
    public void create(Product product) { em.persist(product); }

    public Product findById(int id) { return em.find(Product.class, id); }

    @Transactional
    public void update(Product product) {
        em.merge(product);
    }

    @Transactional
    public void delete(Product product) {
        em.remove(em.contains(product) ? product : em.merge(product));
    }


    public List<Product> findAll(int offset, int limit) {
        List<Product> products = em.createQuery("SELECT p FROM Product p", Product.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

//         Очищаем поле passwordHash у каждого владельца фильма
        products.forEach(product -> {
            User owner = product.getUserOwner();
            if (owner != null) {
                owner.setPasswordHash(null); // Очищаем значение passwordHash
            }
        });

        return products;
    }
}
