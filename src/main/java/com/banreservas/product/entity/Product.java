package com.banreservas.product.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Table(name = "products")
public class Product extends PanacheEntityBase {

    @JsonProperty("id")
    @Id
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("price")
    private BigDecimal price;
    @JsonProperty("sku")
    private String sku;
    //private Long category_id;

    @JsonProperty("category_id")
    @ManyToOne
    @JoinColumn(name = "category_id") // Correct join column
    private Category category;

    public Product() {}
    // Getters and Setters
    @JsonProperty("id")
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    @JsonProperty("name")
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    @JsonProperty("description")
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    @JsonProperty("price")
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    @JsonProperty("sku")
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    @JsonProperty("category")
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