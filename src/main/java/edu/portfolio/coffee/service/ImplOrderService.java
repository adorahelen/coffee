package edu.portfolio.coffee.service;

import edu.portfolio.coffee.model.Email;
import edu.portfolio.coffee.model.Order;
import edu.portfolio.coffee.model.OrderItem;
import edu.portfolio.coffee.model.OrderStatus;
import edu.portfolio.coffee.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ImplOrderService implements OrderService {

    private final OrderRepository orderRepository;

    public ImplOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(Email email, String address, String postcode, List<OrderItem> orderItems) {
        Order order = new Order(
                UUID.randomUUID(),
                email,
                address,
                postcode,
                orderItems,
                OrderStatus.ACCEPTED,
                LocalDateTime.now(),
                LocalDateTime.now());
        return orderRepository.insert(order);
    }

}
