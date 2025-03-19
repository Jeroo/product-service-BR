package com.banreservas.product.entity;

import jakarta.persistence.*;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product extends PanacheEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String sku;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ", \"name\":\"" + name + "\"" +
                ", \"description\":\"" + description + "\"" +
                ", \"price\":" + price +
                ", \"sku\":\"" + sku + "\"" +
                '}';
    }
}