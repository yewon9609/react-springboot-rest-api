package com.prgrms.service;

import com.prgrms.model.Email;
import com.prgrms.model.Order;
import com.prgrms.model.OrderItem;
import java.util.List;

public interface OrderService {

    Order createdOrder(Email email, String address, String postcode, List<OrderItem> orderItems);

    List<OrderItem> getOrderItemList();

    List<OrderItem> getOrderItemListByProductId(String productId);

    List<OrderItem> getOrderItemListByEmail(String email);

}
