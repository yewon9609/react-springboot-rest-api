package com.prgrms.repository;

import static com.prgrms.repository.query.OrderSQL.INSERT_ORDERS;
import static com.prgrms.repository.query.OrderSQL.INSERT_ORDER_ITEMS;
import static com.prgrms.repository.query.OrderSQL.SELECT_ALL_ORDER_ITEMS;
import static com.prgrms.repository.query.OrderSQL.SELECT_ORDER_ITEMS_BY_EMAIL;
import static com.prgrms.repository.query.OrderSQL.SELECT_ORDER_ITEMS_BY_PRODUCT_ID;

import com.prgrms.exception.FailedInsertException;
import com.prgrms.model.Category;
import com.prgrms.model.Order;
import com.prgrms.model.OrderItem;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class OrderJdbcRepository implements OrderRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Order insert(Order order) {
        try {
            int update = jdbcTemplate.update(INSERT_ORDERS.getSql(), toOrderParamMap(order));

            if (update != 1) {
                throw new FailedInsertException("Order insert 에 실패하였습니다. *Order: " + order);
            }

            batchInsertOrderItems(order);


        } catch (DataAccessException e) {
            throw new FailedInsertException("Order insert 에 실패하였습니다. *Order: " + order, e);
        }
        return new Order(order);
    }


    public void batchInsertOrderItems(Order order) {
        String sql = INSERT_ORDER_ITEMS.getSql();
        List<MapSqlParameterSource> params = new ArrayList<>();

        order.getOrderItems().forEach(
            orderItem -> params.add(toOrderItemParamMap(order.getOrderId(), order.getCreatedAt(),
                order.getUpdatedAt(), orderItem)));
        jdbcTemplate.batchUpdate(sql, params.toArray(MapSqlParameterSource[]::new));
    }

    @Override
    public List<OrderItem> findAllOrderItems() {
        return jdbcTemplate.query(SELECT_ALL_ORDER_ITEMS.getSql(), orderItemRowMapper);
    }

    @Override
    public List<OrderItem> findOrderItemsByProductId(String productId) {
        return jdbcTemplate.query(SELECT_ORDER_ITEMS_BY_PRODUCT_ID.getSql(),
            Map.of("productId", productId),
            orderItemRowMapper);
    }

    @Override
    public List<OrderItem> findOrderItemsByEmail(String email) {
        return jdbcTemplate.query(SELECT_ORDER_ITEMS_BY_EMAIL.getSql(), Map.of("email", email),
            orderItemRowMapper);
    }


    private Map<String, Object> toOrderParamMap(Order order) {
        return Map.of(
            "orderId", String.valueOf(order.getOrderId()),
            "email", order.getEmail().getAddress(),
            "address", order.getAddress(),
            "postcode", order.getPostcode(),
            "orderStatus", order.getOrderStatus().toString(),
            "createdAt", order.getCreatedAt(),
            "updatedAt", order.getUpdatedAt()
        );
    }

    private MapSqlParameterSource toOrderItemParamMap(UUID orderId, LocalDateTime createdAt,
        LocalDateTime updatedAt, OrderItem item) {
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("orderId", String.valueOf(orderId));
        source.addValue("productId", String.valueOf(item.productId()));
        source.addValue("category", item.category().toString());
        source.addValue("price", item.price());
        source.addValue("quantity", item.quantity());
        source.addValue("createdAt", createdAt);
        source.addValue("updatedAt", updatedAt);
        return source;
    }

    private static final RowMapper<OrderItem> orderItemRowMapper = (resultSet, i) -> {

        UUID productId = UUID.fromString(resultSet.getString("product_id"));
        Category category = Category.valueOf(resultSet.getString("category"));
        long price = resultSet.getLong("price");
        int quantity = resultSet.getInt("quantity");

        return new OrderItem(productId, category, price, quantity);
    };

    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp.toLocalDateTime();
    }
}
