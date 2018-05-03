package com.igp.admin.Blogs.endpoints;

import com.igp.admin.Blogs.models.CategoryModel;
import com.igp.admin.Blogs.mappers.*;
import com.igp.admin.response.EntityFoundResponse;
import com.igp.admin.response.EntityNotFoundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by suditi on 3/5/18.
 */
public class Categories {
    private static final Logger logger = LoggerFactory.getLogger(Categories.class);

    @POST
    @Path("/v1/createcategory")
    public Response createCategory(CategoryModel categoryModel) {
        Response response=null;
        CategoryMapper categoryMapper = new  CategoryMapper();
        int id = 0;
        try{
            id=categoryMapper.createCategory(categoryModel);
            if(id!=0){
                Map<String,String> createCategoryResponse=new HashMap<>();
                createCategoryResponse.put("data","Category created with id "+id);
                response= EntityFoundResponse.entityFoundResponseBuilder(createCategoryResponse);
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Could not able to create Category");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while creating Category post ",exception);
        }
        return response;
    }
    @PUT
    @Path("/v1/updatecategory")
    public Response updateCategory(CategoryModel categoryModel) {
        Response response=null;
        CategoryMapper categoryMapper =  new CategoryMapper();
        boolean result = false;
        try{
            result = categoryMapper.updateCategory(categoryModel);
            if(result==true){
                Map<String,String> updateCategoryResponse=new HashMap<>();
                updateCategoryResponse.put("data","Category updated succesfully.");
                response= EntityFoundResponse.entityFoundResponseBuilder(updateCategoryResponse);
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Could not able to update Category");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while updating Category post ",exception);
        }
        return response;
    }
    @DELETE
    @Path("/v1/deletecategory")
    public Response deleteCategory(CategoryModel categoryModel) {
        Response response=null;
        CategoryMapper categoryMapper =  new CategoryMapper();
        boolean result = false;
        try{
            result = categoryMapper.deleteCategory(categoryModel);
            if(result==true){
                Map<String,String> updateCategoryResponse=new HashMap<>();
                updateCategoryResponse.put("data","Category deleted succesfully.");
                response= EntityFoundResponse.entityFoundResponseBuilder(updateCategoryResponse);
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Could not able to delete Category");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while deleting Category post ",exception);
        }
        return response;
    }
}
