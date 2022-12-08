package com.prgrms.dto;

import com.prgrms.model.Category;

public record CreateProductRequest(String productName, Category category, long price,
                                   String description) {

}
