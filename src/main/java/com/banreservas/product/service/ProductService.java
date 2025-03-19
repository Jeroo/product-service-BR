package com.banreservas.product.service;

import com.banreservas.product.entity.Product;
import com.banreservas.product.messaging.MessageQueue;
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

    @Inject
    MessageQueue messageQueue;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCategory(productDTO.getCategory());
        product.setSku(productDTO.getSku());
        productRepository.persist(product);
        messageQueue.publish("{\"event\": \"product_created\", \"product\": " + product.toString() + "}");

        return product;
    }

    public List<Product> getAllProducts() {
        return productRepository.listAll();
    }


    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
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
            product.setCategory(productDTO.getCategory());
            product.setSku(productDTO.getSku());
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
