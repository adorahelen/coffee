package edu.portfolio.coffee.controller;

import edu.portfolio.coffee.model.Category;

public record CreateProductRequest(String productName, Category category, long price, String description) {
}
