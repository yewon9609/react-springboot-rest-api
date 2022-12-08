package com.prgrms.repository.query;

public enum OrderSQL {
    INSERT_ORDERS(
        "INSERT INTO orders(order_id, email, address, postcode, order_status, created_at, updated_at)"
            + "VALUES (:orderId, :email, :address, :postcode, :orderStatus, :createdAt, :updatedAt)"),
    INSERT_ORDER_ITEMS(
        "INSERT INTO order_items(order_id, product_id, category, price, quantity, created_at, updated_at)"
            + "VALUES (:orderId, :productId, :category, :price, :quantity, :createdAt, :updatedAt) "),
    SELECT_ALL_ORDER_ITEMS("SELECT * FROM order_items"),
    SELECT_ORDER_ITEMS_BY_PRODUCT_ID("SELECT * FROM order_items WHERE product_id = :productId"),
    SELECT_ORDER_ITEMS_BY_EMAIL(
        "SELECT i.product_id, i.category, i.price, i.quantity FROM order_items i join orders o on o.order_id = i.order_id WHERE o.email = :email");


    private final String sql;

    OrderSQL(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }
}
