package com.exam.product.appliction.repository;

import com.exam.product.appliction.ProductInfo;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ProductRepository {

    private final Map<String, ProductInfo> productStorage;

    public ProductRepository() {
        productStorage = loadProduct();
    }

    public Optional<ProductInfo> get(String productId) {
        return Optional.ofNullable(productStorage.get(productId));
    }

    private Map<String, ProductInfo> loadProduct() {
        Map<String, ProductInfo> products = new HashMap<>();
        products.put("hello", new ProductInfo(1, "test", LocalDateTime.now()));
        return products;
    }
}
