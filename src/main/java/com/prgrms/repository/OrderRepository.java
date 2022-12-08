package com.prgrms.repository;

import com.prgrms.model.Order;
import com.prgrms.model.OrderItem;
import java.util.List;

public interface OrderRepository {

    Order insert(Order order);

    List<OrderItem> findAllOrderItems();

    List<OrderItem> findOrderItemsByProductId(String productId);

    List<OrderItem> findOrderItemsByEmail(String email);

}
