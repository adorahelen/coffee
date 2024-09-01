package edu.portfolio.coffee.service;

import edu.portfolio.coffee.model.Category;
import edu.portfolio.coffee.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> getProductsByCategory(Category category);

    List<Product> getAllProducts();

    Product createProduct(String productName, Category category, long price);

    Product createProduct(String productName, Category category, long price, String description);

}

