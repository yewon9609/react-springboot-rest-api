package com.prgrms.repository.query;

public enum ProductSQL {

    SELECT_ALL("SELECT * FROM products"),
    INSERT(
        "INSERT INTO products(product_id, product_name, category, price, description, created_at, updated_at)"
            + " VALUES(:productId, :productName, :category, :price, :description, :createdAt, :updatedAt)"),
    UPDATE(
        "UPDATE products SET product_name = :productName, category = :category, price = :price, description = :description, created_at = :createdAt, updated_at = :updatedAt"
            + "WHERE product_id = :productId"),
    FIND_BY_ID("SELECT * FROM products WHERE product_id = :productId"),
    FIND_BY_NAME("SELECT * FROM products WHERE product_name = :productName"),
    FIND_BY_CATEGORY("SELECT * FROM products WHERE category = :category"),
    DELETE_ALL("DELETE FROM products");

    private final String sql;

    ProductSQL(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }
}
