package edu.portfolio.coffee.controller.api;

import edu.portfolio.coffee.model.Category;
import edu.portfolio.coffee.model.Product;
import edu.portfolio.coffee.service.ProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductRestController {

    private final ProductService productService;
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping("/api/v1/products")
    public List<Product> getAllProducts(@RequestParam Optional<Category> category) {

        return category.map(productService::getProductsByCategory)
                .orElse(productService.getAllProducts());
      //  return productService.getAllProducts();
    }
}
