package com.banreservas.product.controller;

import com.banreservas.product.dto.ProductDTO;
import com.banreservas.product.entity.Product;
import com.banreservas.product.messaging.MessageQueue;
import com.banreservas.product.service.ProductService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import io.quarkus.cache.CacheResult;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
//@Authenticated
public class ProductController {

    private final ProductService productService;
    private final MessageQueue messageQueue;

    public ProductController(ProductService productService, MessageQueue messageQueue) {
        this.productService = productService;
        this.messageQueue = messageQueue;
    }

    //@RolesAllowed("Admin")
    @POST
    public Response createProduct(ProductDTO productDTO) {
        Product createdProduct = productService.createProduct(productDTO);
        messageQueue.sendMessage("{\"event\": \"product_created\", \"product\": " + createdProduct.toString() + "}");
        return Response.status(Response.Status.CREATED).entity(createdProduct).build();
    }


    //@CacheResult(cacheName = "products-cache")
    @GET
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GET
    @Path("/category/{category}")
    public List<Product> getProductsByCategory(@PathParam("category") String category) {
        return productService.getProductsByCategory(category);
    }

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") Long id) {
        return productService.getProductById(id)
                .map(product -> Response.ok(product).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @PUT
    //@RolesAllowed("Admin")
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") Long id, ProductDTO productDTO) {
        return productService.updateProduct(id, productDTO)
                .map(product -> Response.ok(product).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @RolesAllowed("Admin")
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {
        if (productService.deleteProduct(id)) {
            messageQueue.sendMessage("{\"event\": \"product_deleted\", \"productId\": " + id + "}");
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
