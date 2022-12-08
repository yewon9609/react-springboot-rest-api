package com.prgrms.repository;

import static com.prgrms.repository.query.ProductSQL.DELETE_ALL;
import static com.prgrms.repository.query.ProductSQL.FIND_BY_CATEGORY;
import static com.prgrms.repository.query.ProductSQL.FIND_BY_ID;
import static com.prgrms.repository.query.ProductSQL.FIND_BY_NAME;
import static com.prgrms.repository.query.ProductSQL.INSERT;
import static com.prgrms.repository.query.ProductSQL.SELECT_ALL;
import static com.prgrms.repository.query.ProductSQL.UPDATE;

import com.prgrms.exception.FailedInsertException;
import com.prgrms.exception.FailedUpdateException;
import com.prgrms.model.Category;
import com.prgrms.model.Product;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query(SELECT_ALL.getSql(), productRowMapper);
    }

    @Override
    public Product insert(Product product) {
        try {
            int update = jdbcTemplate.update(INSERT.getSql(), toParamMap(product));
            if (update != 1) {
                throw new FailedInsertException("Product insert 에 실패하였습니다. *Product: " + product);
            }
        } catch (DataAccessException e) {
            throw new FailedInsertException("Product insert 에 실패하였습니다. *Product: " + product, e);
        }
        return new Product(product);
    }


    @Override
    public Product update(Product product) {
        try {
            int update = jdbcTemplate.update(UPDATE.getSql(), toParamMap(product));
            if (update != 1) {
                throw new FailedUpdateException("Product 업데이트에 실패하였습니다");
            }
        } catch (DataAccessException e) {
            throw new FailedUpdateException("Product 업데이트에 실패하였습니다", e);
        }
        return new Product(product);
    }

    @Override
    public Optional<Product> findById(UUID productId) {
        try {
            return Optional.of(
                jdbcTemplate.queryForObject(FIND_BY_ID.getSql(),
                    Map.of("productId", String.valueOf(productId)), productRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Product> findByName(String productName) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(FIND_BY_NAME.getSql(),
                Map.of("productName", productName), productRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return jdbcTemplate.query(FIND_BY_CATEGORY.getSql(), Map.of("category", category.name()),
            productRowMapper);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(DELETE_ALL.getSql(), Collections.emptyMap());
    }

    private static final RowMapper<Product> productRowMapper = (resultSet, i) -> {
        UUID productId = UUID.fromString(resultSet.getString("product_id"));
        String productName = resultSet.getString("product_name");
        Category category = Category.valueOf(resultSet.getString("category"));
        long price = resultSet.getLong("price");
        String description = resultSet.getString("description");
        LocalDateTime createAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        LocalDateTime updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
        return new Product(productId, productName, category, price, description, createAt,
            updatedAt);
    };

    private static Map<String, Object> toParamMap(Product product) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("productId", String.valueOf(product.getProductId()));
        paramMap.put("productName", product.getProductName());
        paramMap.put("category", product.getCategory().name());
        paramMap.put("price", product.getPrice());
        paramMap.put("description", product.getDescription());
        paramMap.put("createdAt", product.getCreatedAt());
        paramMap.put("updatedAt", product.getUpdatedAt());
        return paramMap;
    }

    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp.toLocalDateTime();
    }
}
