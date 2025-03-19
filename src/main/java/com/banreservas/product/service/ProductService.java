package com.banreservas.product.service;

import com.banreservas.product.entity.Category;
import com.banreservas.product.entity.Product;
import com.banreservas.product.entity.ProductHelper;
import com.banreservas.product.messaging.MessageQueue;
import com.banreservas.product.repository.CategoryRepository; // Add this import
import com.banreservas.product.repository.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import com.banreservas.product.dto.ProductDTO;

@ApplicationScoped
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository; // Add CategoryRepository

    @Inject
    MessageQueue messageQueue;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository; // Initialize CategoryRepository
    }

    @Transactional
    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setSku(productDTO.getSku());

        // Find or create the Category
        Category category = categoryRepository.findByName(ProductHelper.getCategoryName(product));
        if (category == null) {
            category = new Category();
            category.setName(ProductHelper.getCategoryName(product));
            categoryRepository.persist(category);
        }
        product.setCategory(category);

        productRepository.persist(product);
        messageQueue.publish("{\"event\": \"product_created\", \"product\": " + product.toString() + "}");

        return product;
    }

    public List<Product> getAllProducts() {
        return productRepository.listAll();
    }

    public List<Product> getProductsByCategory(String categoryName) {
        Category category = categoryRepository.findByName(categoryName);
        if (category == null) {
            return List.of(); // return empty list if category not found.
        }
        return productRepository.findByCategory(category.getName());
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findByIdOptional(id);
    }

    @Transactional
    public Optional<Product> updateProduct(Long id, ProductDTO productDTO) {
        Optional<Product> optionalProduct = productRepository.findByIdOptional(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setSku(productDTO.getSku());

            // Find or create the Category
            Category category = categoryRepository.findByName(ProductHelper.getCategoryName(product));
            if (category == null) {
                category = new Category();
                category.setName(ProductHelper.getCategoryName(product));
                categoryRepository.persist(category);
            }
            product.setCategory(category);

            messageQueue.publish("{\"event\": \"product_updated\", \"product\": " + product.toString() + "}");
            return Optional.of(product);
        }
        return Optional.empty();
    }

    @Transactional
    public boolean deleteProduct(Long id) {
        messageQueue.publish("{\"event\": \"product_deleted\", \"product_id\": " + id + "}");
        return productRepository.deleteById(id);
    }
}