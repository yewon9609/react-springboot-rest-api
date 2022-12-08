package com.prgrms.service;

import com.prgrms.model.Category;
import com.prgrms.model.Product;
import com.prgrms.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class DefaultProductService implements ProductService {

    private final ProductRepository repository;

    public DefaultProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Product> getProductsByCategory(Category category) {
        return repository.findByCategory(category);
    }

    @Override
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    @Override
    public Product createProduct(String productName, Category category, long price) {
        Product product = new Product(UUID.randomUUID(), productName, category, price);
        return repository.insert(product);
    }

    @Override
    public Product createProduct(String productName, Category category, long price,
        String description) {
        Product product = new Product(UUID.randomUUID(), productName, category, price, description,
            LocalDateTime.now(), LocalDateTime.now());
        return repository.insert(product);
    }
}
