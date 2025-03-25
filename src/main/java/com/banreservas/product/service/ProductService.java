package com.banreservas.product.service;

import com.banreservas.product.config.CacheMetrics;
import com.banreservas.product.config.ExchangeRate;
import com.banreservas.product.config.ExchangeRateClient;
import com.banreservas.product.entity.Category;
import com.banreservas.product.entity.Product;
import com.banreservas.product.entity.ProductHelper;
import com.banreservas.product.messaging.MessageQueue;
import com.banreservas.product.repository.CategoryRepository; // Add this import
import com.banreservas.product.repository.ProductRepository;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.banreservas.product.dto.ProductDTO;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.hibernate.internal.util.collections.ArrayHelper;

@ApplicationScoped
public class ProductService {

    @Inject
    CacheMetrics cacheMetrics;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository; // Add CategoryRepository
    private final ExchangeRateClient exchangeRateClient;


    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, @RestClient ExchangeRateClient exchangeRateClient) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository; // Initialize CategoryRepository
        this.exchangeRateClient = exchangeRateClient;
    }

    @Transactional
    @CacheInvalidate(cacheName = "product-cache")
    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setSku(productDTO.getSku());

        // Find or create the Category
        Category category = categoryRepository.findById(productDTO.getCategoryId());
        if (category == null) {
            category = new Category();
            category.setName(ProductHelper.getCategoryName(product));
            categoryRepository.persist(category);
        }
        product.setCategory(category);
        cacheMetrics.incrementCacheMiss();
        productRepository.persist(product);
        return product;
    }

    @CacheResult(cacheName = "product-cache")
    public List<ProductDTO> getAllProducts() {
        List<Product> listProducts = productRepository.listAll();
        cacheMetrics.incrementCacheHit();
        List<ProductDTO> listProductDTO = new ArrayList<>();

        for (Product product : listProducts) {
            Category category = product.getCategory();
            Long categoryId = (category != null) ? category.getId() : null;

            ProductDTO productDTO = new ProductDTO(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    categoryId,
                    product.getSku()
            );

            listProductDTO.add(productDTO);
        }

        return listProductDTO;
    }

    @CacheResult(cacheName = "product-cache")
    public List<ProductDTO> getAllProductsByCurrency(String targetCurrency) {
        List<Product> listProducts = productRepository.listAll();
        List<ProductDTO> listProductDTO = new ArrayList<>();

        for (Product product : listProducts) {
            Category category = product.getCategory();
            Long categoryId = (category != null) ? category.getId() : null;

            BigDecimal convertedPrice = product.getPrice();
            if (targetCurrency != null && !targetCurrency.isEmpty()) {
                convertedPrice = convertPrice(product.getPrice(), targetCurrency);
            }

            ProductDTO productDTO = new ProductDTO(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    convertedPrice,
                    categoryId,
                    product.getSku()
            );

            listProductDTO.add(productDTO);
        }

        return listProductDTO;
    }

    @CacheResult(cacheName = "product-cache")
    public List<Product> getProductsByCategory(String categoryName) {
        Category category = categoryRepository.findByName(categoryName);
        cacheMetrics.incrementCacheHit();
        if (category == null) {
            return List.of(); // return empty list if category not found.
        }
        return productRepository.findByCategory(category.getName());
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findByIdOptional(id);
    }

    @Transactional
    @CacheInvalidate(cacheName = "product-cache")
    public Optional<Product> updateProduct(Long id, ProductDTO productDTO) {
        cacheMetrics.incrementCacheMiss();
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

           // messageQueue.publish("{\"event\": \"product_updated\", \"product\": " + product.toString() + "}");
            return Optional.of(product);
        }
        return Optional.empty();
    }

    @Transactional
    @CacheInvalidate(cacheName = "product-cache")
    public boolean deleteProduct(Long id) {
        //messageQueue.publish("{\"event\": \"product_deleted\", \"product_id\": " + id + "}");
        cacheMetrics.incrementCacheMiss();
        return productRepository.deleteById(id);
    }

    @CacheResult(cacheName = "exchange-rate-cache")
    public BigDecimal convertPrice(BigDecimal price, String targetCurrency) {
        String apiKey = "127dedcb248a73fb4b64ef57"; // Replace with your actual API key
        String baseCurrency = "USD"; // Assuming base currency is USD

        ExchangeRate exchangeRate = exchangeRateClient.getExchangeRate(apiKey, baseCurrency, targetCurrency);
        return price.multiply(BigDecimal.valueOf(exchangeRate.getConversion_rate()));
    }
}