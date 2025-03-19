package com.banreservas.product.repository;

import com.banreservas.product.entity.Category;
import com.banreservas.product.entity.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoryRepository implements PanacheRepository<Category> {

    public Category findByName(String name) { // Corrected parameter name
        return find("name", name).firstResult(); // Changed to firstResult()
    }
}
