package com.banreservas.product.service;

import com.banreservas.product.entity.Category;
import com.banreservas.product.repository.CategoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Inject
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Category createCategory(Category category) {
        categoryRepository.persist(category);
        return category;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.listAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findByIdOptional(id);
    }

    @Transactional
    public Optional<Category> updateCategory(Long id, Category updatedCategory) {
        Optional<Category> categoryOptional = categoryRepository.findByIdOptional(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setName(updatedCategory.getName());
            category.setDescription(updatedCategory.getDescription());
            categoryRepository.persist(category);
            return Optional.of(category);
        }
        return Optional.empty();
    }

    @Transactional
    public boolean deleteCategory(Long id) {
        return categoryRepository.deleteById(id);
    }
}