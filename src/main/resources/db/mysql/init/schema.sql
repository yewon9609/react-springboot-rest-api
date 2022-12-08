create table products
(
    product_id   varchar(50) primary key,
    product_name varchar(20) not null,
    category     varchar(50) not null,
    price        bigint      not null,
    description  varchar(500),
    created_at   datetime(6) not null,
    updated_at   datetime(6) not null
);

create table orders
(
    order_id     varchar(50) primary key,
    email        varchar(50)  not null,
    address      varchar(200) not null,
    postcode     varchar(200) not null,
    order_status varchar(50)  not null,
    created_at   datetime(6)  not null,
    updated_at   datetime(6)  not null
);

create table order_items
(
    seq        bigint      not null primary key AUTO_INCREMENT,
    order_id   varchar(50) not null,
    product_id varchar(50) not null,
    category   varchar(50) not null,
    price      bigint      not null,
    quantity   int         not null,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    index (order_id),
    constraint fk_order_items_to_order foreign key (order_id) references orders (order_id) on delete cascade,
    constraint fk_order_items_to_product foreign key (product_id) references products (product_id)
);
