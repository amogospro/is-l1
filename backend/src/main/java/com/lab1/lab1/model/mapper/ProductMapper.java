package com.lab1.lab1.model.mapper;

import com.lab1.lab1.model.dto.ProductDTO;
import com.lab1.lab1.model.entities.Product;

import java.time.LocalDate;
import java.util.Date;

public class ProductMapper {
    public static ProductDTO toDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setCoordinates(product.getCoordinates());
        productDTO.setCreationDate(product.getCreationDate());
        productDTO.setUnitOfMeasure(product.getUnitOfMeasure());
        productDTO.setManufacturer(OrganizationMapper.toDTO(product.getManufacturer()));
        productDTO.setPrice(product.getPrice());
        productDTO.setManufactureCost(product.getManufactureCost());
        productDTO.setRating(product.getRating());
        productDTO.setOwner(PersonMapper.toDTO(product.getOwner()));
        productDTO.setUserOwner(UserMapper.toDTO(product.getUserOwner()));
        productDTO.setUpdatedAt(product.getUpdatedAt());
        return productDTO;
    }

    public static Product toEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setCoordinates(productDTO.getCoordinates());
        product.setCreationDate(productDTO.getCreationDate());
        product.setUnitOfMeasure(productDTO.getUnitOfMeasure());
        product.setManufacturer(OrganizationMapper.toEntity(productDTO.getManufacturer()));
        product.setPrice(productDTO.getPrice());
        product.setManufactureCost(productDTO.getManufactureCost());
        product.setRating(productDTO.getRating());
        product.setOwner(PersonMapper.toEntity(productDTO.getOwner()));
        product.setUserOwner(UserMapper.toEntity(productDTO.getUserOwner()));
        product.setUpdatedAt(productDTO.getUpdatedAt());

        if (product.getCreationDate() == null) {
            product.setCreationDate(LocalDate.now());
        }

        if (product.getUpdatedAt() == null) {
            product.setUpdatedAt(new Date());
        }

        return product;
    }
}
