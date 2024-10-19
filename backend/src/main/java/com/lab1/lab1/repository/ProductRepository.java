package com.lab1.lab1.repository;

import com.lab1.lab1.model.entities.Product;
import com.lab1.lab1.model.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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


    public List<Product> findAll(String filterBy, String filter, String sortBy, String sortDirection, int offset, int limit) {
        String  queryStr = "SELECT p FROM Product p WHERE 1=1";

        System.out.println(filterBy);
        System.out.println(filter);

        if (filterBy != null && filter != null && !filter.isEmpty()) {
            queryStr += " AND p." + filterBy + " LIKE :filter";
        }

        if (sortBy != null && sortDirection != null) {
            queryStr += " ORDER BY p." + sortBy + " " + sortDirection;
        }

        System.out.println("ABOBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        System.out.println(queryStr);

        TypedQuery<Product> query = em.createQuery(queryStr, Product.class);

        if (filter != null && !filter.isEmpty()) {
            query.setParameter("filter", "%" + filter + "%");
        }

        query.setFirstResult(offset);
        query.setMaxResults(limit);

        List<Product> products = query.getResultList();

        products.forEach(product -> {
            User owner = product.getUserOwner();
            if (owner != null) {
                owner.setPasswordHash(null); // Очищаем значение passwordHash
            }
        });

        return products;
    }
}
