create table carts
(
    id           BINARY(16) default (UUID_TO_BIN(UUID())) not null primary key,
    date_created DATE       default (CURDATE())           not null
);

create table cart_items
(
    id         bigint auto_increment primary key,
    cart_id    BINARY(16)    not null,
    product_id bigint        not null,
    quantity   int default 1 not null,
    constraint cart_items_cart_product_unique unique (cart_id, product_id),
    constraint cart_items_carts_id_fk foreign key (cart_id) references carts (id) on delete cascade,
    constraint cart_items_products_id_fk foreign key (product_id) references products (id) on delete cascade
);