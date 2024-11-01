package com.lab1.lab1.controller;

import com.lab1.lab1.model.entities.Person;
import com.lab1.lab1.model.entities.Product;
import com.lab1.lab1.model.entities.User;
import com.lab1.lab1.service.ProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.security.PublicKey;
import java.util.List;

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
        return Response.ok(products).build();
    }

    @GET
    @Path("/{id}")
    public Response getProductsById(@PathParam("id") int id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return Response.ok(product).build();
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
            return Response.status(Response.Status.BAD_REQUEST).entity("Product owner not found").build();
        }
        return Response.status(Response.Status.CREATED).entity(product).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") int id, Product updatedProduct) {
        try {
            User user = (User) securityContext.getUserPrincipal();
            updatedProduct.setId(id);
            productService.updateProduct(updatedProduct, user);
            return Response.ok(updatedProduct).build();
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
    public Double getAverageRating() {
        return productService.getAverageRating();
    }

    @GET
    @Path("/rating-above")
    public List<Product> getProductsWithRatingGraterThan(@QueryParam("minRating") Double minRating) {
        return productService.getProductsWithRatingGraterThan(minRating);
    }

    @GET
    @Path("/unique-owners")
    public List<Person> getUniqueOwners() {
        return productService.getUniqueOwners();
    }

    @GET
    @Path("/price-range")
    public List<Product> getProductsByPriceRange(@QueryParam("minPrice") Double minPrice,
                                                 @QueryParam("maxPrice") Double maxPrice) {
        return productService.getProductsByPriceRange(minPrice, maxPrice);
    }

    @GET
    @Path("/increase-price")
    public void increasePriceForAllProducts(@QueryParam("percentage") Double percentage) {
        productService.increasePriceForAllProducts(percentage);
    }
}
