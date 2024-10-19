package com.lab1.lab1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab1.lab1.controller.WebSocketEndpoint;
import com.lab1.lab1.model.entities.*;
import com.lab1.lab1.repository.OrganizationRepository;
import com.lab1.lab1.repository.PersonRepository;
import com.lab1.lab1.repository.ProductRepository;
import com.lab1.lab1.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
public class ProductService {
    @Inject
    private ProductRepository productRepository;

    @Inject
    private PersonRepository personRepository;

    @Inject
    private OrganizationRepository organizationRepository;

    @Inject
    private UserRepository userRepository;

    @Transactional
    public void createProduct(Product product, User user) throws Exception {

        // Validate product fields
        // Set owner
        // Проверяем, передан ли id владельца
        Person owner = product.getOwner();
        Organization organization = product.getManufacturer();

        if (owner.getId() != 0) {
            // Если передан id, то ищем владельца по id
            Person existingOwner = personRepository.findById(owner.getId());


            if (existingOwner == null) {
                throw new Exception("Owner not found");
            }


            product.setOwner(existingOwner);
        }

        if (organization.getId() != null) {
            Organization existingOrganization = organizationRepository.findById(organization.getId());

            if (existingOrganization == null) {
                throw new Exception("Manufacturer not found");
            }

            product.setManufacturer(existingOrganization);
        }

        product.setUserOwner(user);
        productRepository.create(product);

        ObjectMapper objectMapper = new ObjectMapper();
        String updateJson = objectMapper.writeValueAsString(product);

        WebSocketEndpoint.sendUpdate(updateJson);
    }

    public Product getProductById(int id) {
        return productRepository.findById(id);
    }

    @Transactional
    public void updateProduct(Product product, User user) throws Exception {
        Product currentProduct = getProductById(product.getId());
        // Check if user is owner or admin
        if (currentProduct.getUserOwner().equals(user) || user.getRole() == Role.ADMIN) {
            currentProduct.setName(product.getName());
            currentProduct.setCoordinates(product.getCoordinates());
            currentProduct.setCreationDate(product.getCreationDate());
            currentProduct.setUnitOfMeasure(product.getUnitOfMeasure());

            Organization organization = product.getManufacturer();
            if (organization.getId() != null){
                Organization existingOrganization = organizationRepository.findById(organization.getId());
                if (existingOrganization == null) {
                    throw new Exception("Manufacturer not found");
                }
                currentProduct.setManufacturer(existingOrganization);
            }

            Person owner = product.getOwner();
            if (owner.getId() != 0) {
                Person existingOwner = personRepository.findById(owner.getId());
                if (existingOwner == null) {
                    throw new Exception("Owner not found");
                }
                currentProduct.setOwner(existingOwner);
            }


            currentProduct.setPrice(product.getPrice());
            currentProduct.setManufactureCost(product.getManufactureCost());
            currentProduct.setRating(product.getRating());
            productRepository.update(currentProduct);

            ObjectMapper objectMapper = new ObjectMapper();
            String updateJson = objectMapper.writeValueAsString(product);

            WebSocketEndpoint.sendUpdate(updateJson);
        } else {
            throw new Exception("Access Denied");
        }
    }

    public void deleteProduct(int id, User user) throws Exception {
        Product product = productRepository.findById(id);
        if (product != null) {
            // Check if user is owner or admin
            if (product.getUserOwner().equals(user) || user.getRole() == Role.ADMIN) {
                // Check for related objects
                // If related objects exist, throw exception
                productRepository.delete(product);

                ObjectMapper objectMapper = new ObjectMapper();
                String updateJson = objectMapper.writeValueAsString(product);

                WebSocketEndpoint.sendUpdate(updateJson);
            } else {
                throw new Exception("Access Denied");
            }
        }
    }

    public List<Product> getAllProducts(int page, int size, String filterBy, String filter, String sortBy, String sortDirection) {
        int offset = (page - 1) * size;
        return productRepository.findAll(filterBy, filter, sortBy, sortDirection, offset, size);
    }

    public Double getAverageRating() {
        return productRepository.calculateAverageRating();
    }

    public List<Product> getProductsWithRatingGraterThan(Double minRating) {
        return productRepository.findProductsWithRatingGreaterThan(minRating);
    }

    public List<Person> getUniqueOwners() {
        return productRepository.findUniqueOwners();
    }

    public List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findProductsByPriceRange(minPrice, maxPrice);
    }

    @Transactional
    public void increasePriceForAllProducts(Double percentage) {
        productRepository.increasePriceForAllProducts(percentage);
    }
}

