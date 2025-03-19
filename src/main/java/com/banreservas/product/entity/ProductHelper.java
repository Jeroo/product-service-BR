package com.banreservas.product.entity;

import com.banreservas.product.repository.CategoryRepository;

public class ProductHelper {

    public static String getCategoryName(Product product) {
        if (product.getCategory() != null) {
            return product.getCategory().getName();
        } else {
            return null; // Or handle the case where the category is null
        }
    }

    public static void setCategoryName(Product product, String newCategoryName, CategoryRepository categoryRepository) {
        if (product.getCategory() != null) {
            Category category = product.getCategory();
            category.setName(newCategoryName);
            categoryRepository.persist(category); // Persist the updated category
        } else {
            // Handle the case where the product has no category
            Category category = new Category();
            category.setName(newCategoryName);
            categoryRepository.persist(category);
            product.setCategory(category);
        }
    }
}

