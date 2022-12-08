package com.prgrms.dto;

import com.prgrms.model.OrderItem;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateOrderRequest(
    @NotNull
    String email,
    @NotNull
    String address,
    @NotNull
    String postcode,
    @NotNull
    List<OrderItem> orderItems
) {

}
