package com.banreservas.product.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Table(name = "products")
public class Product extends PanacheEntityBase {

    @Id
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String sku;
    //private Long category_id;

    @ManyToOne
    @JoinColumn(name = "category_id") // Correct join column
    private Category category;

    public Product() {}
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