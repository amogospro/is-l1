package com.lab1.lab1.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lab1.lab1.controller.WebSocketEndpoint;
import com.lab1.lab1.model.entities.Person;
import com.lab1.lab1.model.entities.Product;
import com.lab1.lab1.model.entities.Role;
import com.lab1.lab1.model.entities.User;
import com.lab1.lab1.model.mapper.ProductMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.Arrays;
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

        if (filterBy != null && isValidColumn(filterBy) && filter != null && !filter.isEmpty()) {
            queryStr += " AND p." + filterBy + " LIKE :filter";
        }

        if (sortBy != null && sortDirection != null && isValidColumn(sortBy) && isValidSortDirection(sortDirection)) {
            queryStr += " ORDER BY p." + sortBy + " " + sortDirection;
        }

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

    private boolean isValidColumn(String value) {
        // Разрешённые колонки для фильтрации
        List<String> validColumns = Arrays.asList("name", "manufacturer.name", "owner.name");
        return validColumns.contains(value);
    }

    // Метод для проверки направления сортировки
    private boolean isValidSortDirection(String sortDirection) {
        // Допустимые направления сортировки
        return "ASC".equalsIgnoreCase(sortDirection) || "DESC".equalsIgnoreCase(sortDirection);
    }

    public Double calculateAverageRating() {
        return em.createQuery("SELECT AVG(p.rating) FROM Product p", Double.class)
                .getSingleResult();
    }

    public List<Product> findProductsWithRatingGreaterThan(Integer minRating) {
        return em.createQuery("SELECT p FROM Product p WHERE p.rating > :minRating", Product.class)
                .setParameter("minRating", minRating)
                .getResultList();
    }

    public List<Person> findUniqueOwners() {
        return em.createQuery("SELECT DISTINCT p.owner FROM Product p", Person.class)
                .getResultList();
    }

    public List<Product> findProductsByPriceRange(int minPrice, int maxPrice) {
        return em.createQuery("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice", Product.class)
                .setParameter("minPrice", minPrice)
                .setParameter("maxPrice", maxPrice)
                .getResultList();
    }

    @Transactional
    public void increasePriceForAllProducts(Double percentage, User user) throws IOException {
        List<Product> products;

        // Проверка роли пользователя
        if (user.getRole() == Role.ADMIN) {
            // Если пользователь — админ, выбираем все продукты
            products = em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
        } else {
            // Если пользователь — не админ, выбираем только те продукты, которые ему принадлежат
            products = em.createQuery("SELECT p FROM Product p WHERE p.userOwner = :user", Product.class)
                    .setParameter("user", user)
                    .getResultList();
        }

        // Обновляем цену для всех выбранных продуктов
        for (Product product : products) {
            int updatedPrice = (int) Math.round(product.getPrice() * (1 + percentage / 100));
            product.setPrice(updatedPrice);
            em.merge(product); // Сохраняем обновление

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String updateJson = objectMapper.writeValueAsString(ProductMapper.toDTO(product));

            WebSocketEndpoint.sendUpdate(updateJson);
        }
    }

    public Product findByName(String name) {
        try {
            return em.createQuery("SELECT p FROM Product p WHERE p.name = :name", Product.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
