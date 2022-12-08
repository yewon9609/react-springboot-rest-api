package com.prgrms.controller.api;

import com.prgrms.dto.CreateOrderRequest;
import com.prgrms.model.Email;
import com.prgrms.model.Order;
import com.prgrms.model.OrderItem;
import com.prgrms.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public Order createOrder(@Valid @RequestBody CreateOrderRequest orderRequest) {
        return orderService.createdOrder(
            new Email(orderRequest.email()),
            orderRequest.address(),
            orderRequest.postcode(),
            orderRequest.orderItems()
        );
    }

    @GetMapping("/orderItems")
    public List<OrderItem> findAllOrderItem() {
        return orderService.getOrderItemList();
    }


    @GetMapping("/orderItems/{email}")
    public List<OrderItem> findOrderItemsByEmail(@PathVariable String email) {
        return orderService.getOrderItemListByEmail(email);
    }


}

