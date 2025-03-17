CREATE SCHEMA product;

CREATE TABLE product.product(
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    price MONEY NOT NULL,
    create_date TIMESTAMP WITHOUT TIME ZONE DEFAULT current_timestamp,
    CONSTRAINT product_uk1 UNIQUE (name)
);