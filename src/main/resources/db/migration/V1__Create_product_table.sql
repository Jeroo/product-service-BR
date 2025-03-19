CREATE TABLE products (
          id BIGSERIAL PRIMARY KEY,
          name VARCHAR(255),
          description TEXT,
          price NUMERIC,
          category VARCHAR(255),
          sku VARCHAR(255)
);