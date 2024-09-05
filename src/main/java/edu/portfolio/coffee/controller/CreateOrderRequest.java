package edu.portfolio.coffee.controller;


import edu.portfolio.coffee.model.OrderItem;

import java.util.List;

public record CreateOrderRequest(
        String email, String address, String postcode, List<OrderItem> orderItems
) {
}
