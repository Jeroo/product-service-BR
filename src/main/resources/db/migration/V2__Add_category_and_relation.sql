CREATE TABLE categories (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(255),
                            description TEXT
);

ALTER TABLE products
    ADD COLUMN category_id BIGINT REFERENCES categories(id);

CREATE INDEX idx_products_category_id ON products (category_id);