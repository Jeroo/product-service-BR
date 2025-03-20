CREATE TABLE products (
          id BIGSERIAL PRIMARY KEY,
          name VARCHAR(255),
          description TEXT,
          price NUMERIC,
          sku VARCHAR(255)
);