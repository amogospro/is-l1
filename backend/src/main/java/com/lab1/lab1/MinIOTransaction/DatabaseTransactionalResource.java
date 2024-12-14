package com.lab1.lab1.MinIOTransaction;

import com.lab1.lab1.model.entities.Product;
import com.lab1.lab1.service.ProductService;
import com.lab1.lab1.model.entities.User;

public class DatabaseTransactionalResource implements TransactionalResource {
    private final ProductService productService;
    private final Product product;
    private final User user;
    private boolean prepared = false;

    public DatabaseTransactionalResource(ProductService productService, Product product, User user) {
        this.productService = productService;
        this.product = product;
        this.user = user;
    }

    @Override
    public void prepare() throws Exception {
        // Ваша проверка уникальности имени продукта
        if (productService.getProductById(product.getId()) != null) {
            throw new Exception("Продукт с таким ID уже существует.");
        }
        prepared = true;
    }

    @Override
    public void commit() throws Exception {
        if (prepared) {
            productService.createProduct(product, user); // Сохраняем в БД
        } else {
            throw new IllegalStateException("Database resource not prepared!");
        }
    }

    @Override
    public void rollback() {
        // Удаляем созданный продукт, если нужно
        try {
            if (productService.getProductById(product.getId()) != null) {
                productService.deleteProduct(product.getId(), user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
