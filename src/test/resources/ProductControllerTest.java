
package com.banreservas.product.controller;
import com.banreservas.product.dto.ProductDTO;
import com.banreservas.product.entity.Product;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
@TestHTTPEndpoint(ProductController.class)
public class ProductControllerTest {

    private ProductService productService;
    private MessageQueue messageQueue;

    @BeforeEach
    public void setup() {
        productService = Mockito.mock(ProductService.class);
        messageQueue = Mockito.mock(MessageQueue.class);
        RestAssured.config = RestAssured.config().objectMapperConfig(
                io.restassured.config.ObjectMapperConfig.objectMapperConfig().defaultObjectMapper(new io.quarkus.jackson.ObjectMapperProducer().produce()));
    }

    @Test
    public void testCreateProduct() {
        ProductDTO productDTO = new ProductDTO("Test Product", "Test Description", BigDecimal.TEN, "123");
        Product createdProduct = new Product();
        createdProduct.setId(1L);
        createdProduct.setName("Test Product");
        createdProduct.setDescription("Test Description");
        createdProduct.setPrice(BigDecimal.TEN);
        createdProduct.setSku("123");

        when(productService.createProduct(any(ProductDTO.class))).thenReturn(createdProduct);

        given()
                .contentType(ContentType.JSON)
                .body(productDTO)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("id", is(1))
                .body("name", is("Test Product"))
                .body("description", is("Test Description"))
                .body("price", is(10))
                .body("sku", is("123"));
        Mockito.verify(messageQueue,Mockito.times(1)).sendMessage(any(String.class));
    }

    @Test
    public void testGetAllProducts() {
        List<ProductDTO> productDTOList = Collections.singletonList(new ProductDTO("Product 1", "Description 1", BigDecimal.ONE, "SKU1"));
        when(productService.getAllProducts()).thenReturn(productDTOList);

        given()
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].name", is("Product 1"));
    }

    @Test
    public void testGetProductsByCategory() {
        List<Product> productList = Collections.singletonList(new Product());
        when(productService.getProductsByCategory("TestCategory")).thenReturn(productList);

        given()
                .when()
                .get("/category/TestCategory")
                .then()
                .statusCode(200)
                .body("$", hasSize(1));
    }

    @Test
    public void testGetProductById_Found() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product 1");
        product.setDescription("Description 1");
        product.setPrice(BigDecimal.ONE);
        product.setSku("SKU1");
        Optional<Product> optionalProduct = Optional.of(product);
        when(productService.getProductById(1L)).thenReturn(optionalProduct);

        given()
                .when()
                .get("/1")
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Product 1"));
    }

    @Test
    public void testGetProductById_NotFound() {
        when(productService.getProductById(1L)).thenReturn(Optional.empty());

        given()
                .when()
                .get("/1")
                .then()
                .statusCode(404);
    }

    @Test
    public void testUpdateProduct_Found() {
        ProductDTO productDTO = new ProductDTO("Updated Product", "Updated Description", BigDecimal.TEN, "123");
        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Updated Product");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(BigDecimal.TEN);
        updatedProduct.setSku("123");
        Optional<Product> optionalProduct = Optional.of(updatedProduct);

        when(productService.updateProduct(1L, productDTO)).thenReturn(optionalProduct);

        given()
                .contentType(ContentType.JSON)
                .body(productDTO)
                .when()
                .put("/1")
                .then()
                .statusCode(200)
                .body("name", is("Updated Product"))
                .body("description", is("Updated Description"));
    }

    @Test
    public void testUpdateProduct_NotFound() {
        when(productService.updateProduct(1L, new ProductDTO())).thenReturn(Optional.empty());

        given()
                .contentType(ContentType.JSON)
                .body(new ProductDTO())
                .when()
                .put("/1")
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteProduct_Found() {
        when(productService.deleteProduct(1L)).thenReturn(true);

        given()
                .when()
                .delete("/1")
                .then()
                .statusCode(204);
        Mockito.verify(messageQueue,Mockito.times(1)).sendMessage(any(String.class));
    }

    @Test
    public void testDeleteProduct_NotFound() {
        when(productService.deleteProduct(1L)).thenReturn(false);

        given()
                .when()
                .delete("/1")
                .then()
                .statusCode(404);
    }
}