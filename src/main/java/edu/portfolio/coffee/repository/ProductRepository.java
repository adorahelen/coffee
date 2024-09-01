package edu.portfolio.coffee.repository;

import edu.portfolio.coffee.model.Category;
import edu.portfolio.coffee.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    List<Product> findAll();

    Product insert(Product product);

    Product update(Product product);

    Optional<Product> findById(UUID ProductId);
    Optional<Product> findByName(String ProductName);
    List<Product> findByCategory(Category category);
    void deleteAll();
}
