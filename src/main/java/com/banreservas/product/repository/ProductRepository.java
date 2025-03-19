package com.banreservas.product.repository;

import com.banreservas.product.entity.Product;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
    public List<Product> findByCategory(String category) {
        return find("category", category).list();
    }
}
