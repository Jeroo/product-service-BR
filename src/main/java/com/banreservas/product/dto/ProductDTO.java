package com.banreservas.product.dto;

import java.math.BigDecimal;

public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Long categoryId; // Changed to Long
    private String sku;

    public ProductDTO() {  }

    public ProductDTO(Long id, String name, String description, BigDecimal price, Long id1, String sku) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = getCategoryId();
        this.sku = sku;
    }


    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Long getCategoryId() { return categoryId; } // Changed getter
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; } // Changed setter
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}