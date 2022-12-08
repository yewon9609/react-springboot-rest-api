package com.prgrms.service;

import com.prgrms.model.Email;
import com.prgrms.model.Order;
import com.prgrms.model.OrderItem;
import com.prgrms.model.OrderStatus;
import com.prgrms.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class DefaultOrderService implements OrderService {

    private final OrderRepository orderRepository;

    public DefaultOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createdOrder(Email email, String address, String postcode,
        List<OrderItem> orderItems) {
        Order order = new Order(
            UUID.randomUUID(),
            email,
            address,
            postcode,
            orderItems,
            OrderStatus.ACCEPTED,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        return orderRepository.insert(order);

    }

    @Override
    public List<OrderItem> getOrderItemList() {
        return orderRepository.findAllOrderItems();
    }

    @Override
    public List<OrderItem> getOrderItemListByProductId(String productId) {
        return orderRepository.findOrderItemsByProductId(productId);
    }

    @Override
    public List<OrderItem> getOrderItemListByEmail(String email) {
        return orderRepository.findOrderItemsByEmail(email);
    }

}
