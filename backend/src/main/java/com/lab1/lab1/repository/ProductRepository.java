package com.lab1.lab1.repository;

import com.lab1.lab1.model.entities.Person;
import com.lab1.lab1.model.entities.Product;
import com.lab1.lab1.model.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

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

        if (filterBy != null && isValidFilterColumn(filterBy) && filter != null && !filter.isEmpty()) {
            queryStr += " AND p." + filterBy + " LIKE :filter";
        }

        if (sortBy != null && sortDirection != null && isValidSortColumn(sortBy) && isValidSortDirection(sortDirection)) {
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

    private boolean isValidFilterColumn(String filterBy) {
        // Разрешённые колонки для фильтрации
        List<String> validColumns = Arrays.asList("name", "manufacturer.name", "owner.name");
        return validColumns.contains(filterBy);
    }

    // Метод для проверки допустимых значений для сортировки
    private boolean isValidSortColumn(String sortBy) {
        // Разрешённые колонки для сортировки
        List<String> validColumns = Arrays.asList("name", "price", "rating");
        return validColumns.contains(sortBy);
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

    public List<Product> findProductsWithRatingGreaterThan(Double minRating) {
        return em.createQuery("SELECT p FROM Product p WHERE p.rating > :minRating", Product.class)
                .setParameter("minRating", minRating)
                .getResultList();
    }

    public List<Person> findUniqueOwners() {
        return em.createQuery("SELECT DISTINCT p.owner FROM Product p", Person.class)
                .getResultList();
    }

    public List<Product> findProductsByPriceRange(Double minPrice, Double maxPrice) {
        return em.createQuery("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice", Product.class)
                .setParameter("minPrice", minPrice)
                .setParameter("maxPrice", maxPrice)
                .getResultList();
    }

    @Transactional
    public void increasePriceForAllProducts(Double percentage) {
        // Обновляем цену всех продуктов, увеличивая её на указанный процент
        em.createQuery("UPDATE Product p SET p.price = p.price * (1 + :percentage)")
                .setParameter("percentage", percentage / 100)
                .executeUpdate();
    }

}
