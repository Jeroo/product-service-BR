package com.banreservas.product.controller;

import com.banreservas.product.entity.Category;
import com.banreservas.product.service.CategoryService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryController {

    private final CategoryService categoryService;

    @Inject
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

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
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
