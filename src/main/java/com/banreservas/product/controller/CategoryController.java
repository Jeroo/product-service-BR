package com.banreservas.product.controller;

import com.banreservas.product.entity.Category;
import com.banreservas.product.messaging.MessageQueue;
import com.banreservas.product.service.CategoryService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
//@Authenticated
public class CategoryController {

    private final CategoryService categoryService;
    private final MessageQueue messageQueue;

    @Inject
    public CategoryController(CategoryService categoryService, MessageQueue messageQueue) {
        this.categoryService = categoryService;
        this.messageQueue = messageQueue;
    }

    //@RolesAllowed("Admin")
    @GET
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GET
    @Path("/{id}")
    public Response getCategoryById(@PathParam("id") Long id) {
//        Optional<Category> category = categoryService.getCategoryById(id);
//        return category.map(Response::ok)
//                .orElse(Response.status(Response.Status.NOT_FOUND).build());

        return categoryService.getCategoryById(id)
                .map(category -> Response.ok(category).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());

    }

    @POST
    public Response createCategory(Category category) {
        Category createdCategory = categoryService.createCategory(category);
        messageQueue.sendMessage("{\"event\": \"category_created\", \"category\": " + createdCategory.toString() + "}");
        return Response.status(Response.Status.CREATED).entity(createdCategory).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCategory(@PathParam("id") Long id, Category updatedCategory) {
        return categoryService.updateCategory(id, updatedCategory)
                .map(category -> Response.ok(category).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCategory(@PathParam("id") Long id) {
        boolean deleted = categoryService.deleteCategory(id);
        if (deleted) {
            messageQueue.sendMessage("{\"event\": \"category_deleted\", \"categoryId\": " + id + "}");
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
