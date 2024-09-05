package edu.portfolio.coffee.service;

import edu.portfolio.coffee.model.Email;
import edu.portfolio.coffee.model.Order;
import edu.portfolio.coffee.model.OrderItem;

import java.util.List;

public interface OrderService {
    Order createOrder(Email email, String address, String postcode, List<OrderItem> orderItems);
}

