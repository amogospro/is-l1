package com.lab1.lab1.controller;

import com.lab1.lab1.model.dto.PersonDTO;
import com.lab1.lab1.model.dto.ProductDTO;
import com.lab1.lab1.model.entities.Person;
import com.lab1.lab1.model.entities.Product;
import com.lab1.lab1.model.entities.User;
import com.lab1.lab1.model.mapper.PersonMapper;
import com.lab1.lab1.model.mapper.ProductMapper;
import com.lab1.lab1.service.ProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * RESTful API for managing movies
 */
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {

    @Inject
    private ProductService productService;

    @Context
    private SecurityContext securityContext;

    @GET
    public Response getProducts(@QueryParam("page") @DefaultValue("1") int page,
                                @QueryParam("size") @DefaultValue("10") int size,
                                @QueryParam("filterBy") String filterBy,
                                @QueryParam("filter") String filter,
                                @QueryParam("sortBy") @DefaultValue("name") String sortBy,
                                @QueryParam("sortDirection") @DefaultValue("ASC") String sortDirection) {
        List<Product> products = productService.getAllProducts(page, size, filterBy, filter, sortBy, sortDirection);
        List<ProductDTO>productsDTO = products.stream()
                                                .map(ProductMapper::toDTO)
                                                .collect(Collectors.toList());
        return Response.ok(productsDTO).build();
    }

    @GET
    @Path("/{id}")
    public Response getProductsById(@PathParam("id") int id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return Response.ok(ProductMapper.toDTO(product)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    public Response createProduct(Product product) {
        User user = (User) securityContext.getUserPrincipal();
        try {
            productService.createProduct(product, user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") int id, Product updatedProduct) {
        try {
            User user = (User) securityContext.getUserPrincipal();
            updatedProduct.setId(id);
            productService.updateProduct(updatedProduct, user);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") int id) {
        try {
            User user = (User) securityContext.getUserPrincipal();
            productService.deleteProduct(id, user);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/average-rating")
    public Response getAverageRating() {
        try {
            Double averageRating = productService.getAverageRating();
            return Response.ok(averageRating).build();  // Возвращаем значение с кодом 200
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error calculating average rating").build();
        }
    }

    @GET
    @Path("/rating-above")
    public Response getProductsWithRatingGraterThan(@QueryParam("minRating") Integer minRating) {
        try {
            List<Product> products = productService.getProductsWithRatingGraterThan(minRating);

            List<ProductDTO> productDTO = products.stream()
                    .map(ProductMapper::toDTO)
                    .collect(Collectors.toList());

            return Response.ok(productDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error getting products with rating grater than " + minRating).build();
        }
    }

    @GET
    @Path("/unique-owners")
    public Response getUniqueOwners() {
        try {
            List<Person> uniqueOwners = productService.getUniqueOwners();
            List<PersonDTO> uniqueOwnersDTO = uniqueOwners.stream()
                    .map(PersonMapper::toDTO)
                    .collect(Collectors.toList());
            return Response.ok(uniqueOwnersDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error getting unique owners").build();
        }

    }

    @GET
    @Path("/price-range")
    public Response getProductsByPriceRange(@QueryParam("minPrice") int minPrice,
                                                 @QueryParam("maxPrice") int maxPrice) {
        try {
            List<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice);
            List<ProductDTO> productsDTO = products.stream()
                    .map(ProductMapper::toDTO)
                    .collect(Collectors.toList());
            return Response.ok(productsDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error getting products by price in range " + minPrice + "-" + maxPrice).build();
        }
    }

    @GET
    @Path("/increase-price")
    public Response increasePriceForAllProducts(@QueryParam("percentage") Double percentage) {
        try {
            User user = (User) securityContext.getUserPrincipal();
            productService.increasePriceForAllProducts(percentage, user);
            return Response.ok("Prices increased by " + percentage + "% for all products.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to increase prices: " + percentage + "%")
                    .build();
        }
    }
}
