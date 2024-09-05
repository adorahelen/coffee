package edu.portfolio.coffee.repository;

import edu.portfolio.coffee.model.Order;

public interface OrderRepository {
    Order insert(Order order);
}
