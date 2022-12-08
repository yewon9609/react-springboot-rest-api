package com.prgrms.controller.api;

import com.prgrms.model.Category;
import com.prgrms.model.Product;
import com.prgrms.service.ProductService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/api/v1/products")
    public List<Product> productList(@RequestParam Optional<Category> category) {
        return category.map(productService::getProductsByCategory)
            .orElseGet(productService::getAllProducts);
    }
}
