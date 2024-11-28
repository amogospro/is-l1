package com.lab1.lab1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lab1.lab1.controller.WebSocketEndpoint;
import com.lab1.lab1.model.entities.*;
import com.lab1.lab1.model.mapper.ProductMapper;
import com.lab1.lab1.repository.OrganizationRepository;
import com.lab1.lab1.repository.PersonRepository;
import com.lab1.lab1.repository.ProductRepository;
import com.lab1.lab1.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
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

        Person owner = product.getOwner();

        if (owner.getId() != 0) {
            // Если передан id, то ищем владельца по id
            Person existingOwner = personRepository.findById(owner.getId());


            if (existingOwner == null) {
                throw new Exception("Product owner not found");
            }


            product.setOwner(existingOwner);
        }

        Organization organization = product.getManufacturer();

        if (organization != null) {
            if (organization.getId() != null) {
                Organization existingOrganization = organizationRepository.findById(organization.getId());

                System.out.println(existingOrganization);
                if (existingOrganization == null) {
                    throw new Exception("Manufacturer not found");
                }

                product.setManufacturer(existingOrganization);
            }
        }



        product.setUserOwner(user);
        if (productRepository.findByName(product.getName()) != null) {
            throw new Exception("Продукт с таким именем уже существует");
        }
        productRepository.create(product);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String updateJson = objectMapper.writeValueAsString(ProductMapper.toDTO(product));

        // Отправляем данные асинхронно через WebSocket
        WebSocketService.sendUpdateAsync(updateJson);
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
            if (organization != null) {
                if (organization.getId() != null) {
                    Organization existingOrganization = organizationRepository.findById(organization.getId());
                    if (existingOrganization == null) {
                        throw new Exception("Manufacturer not found");
                    }
                    currentProduct.setManufacturer(existingOrganization);
                }
            } else {
                currentProduct.setManufacturer(null);
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
            objectMapper.registerModule(new JavaTimeModule());
            String updateJson = objectMapper.writeValueAsString(ProductMapper.toDTO(currentProduct));

            // Отправляем данные асинхронно через WebSocket
            WebSocketService.sendUpdateAsync(updateJson);
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
                objectMapper.registerModule(new JavaTimeModule());
                String updateJson = objectMapper.writeValueAsString(ProductMapper.toDTO(product));

                // Отправляем данные асинхронно через WebSocket
                WebSocketService.sendUpdateAsync(updateJson);
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

    public List<Product> getProductsWithRatingGraterThan(Integer minRating) {
        return productRepository.findProductsWithRatingGreaterThan(minRating);
    }

    public List<Person> getUniqueOwners() {
        return productRepository.findUniqueOwners();
    }

    public List<Product> getProductsByPriceRange(int minPrice, int maxPrice) {
        return productRepository.findProductsByPriceRange(minPrice, maxPrice);
    }

    @Transactional
    public void increasePriceForAllProducts(Double percentage, User user) throws IOException {
        productRepository.increasePriceForAllProducts(percentage, user);
    }
}

