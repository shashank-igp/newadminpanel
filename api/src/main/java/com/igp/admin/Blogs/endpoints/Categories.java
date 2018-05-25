package com.igp.admin.Blogs.endpoints;

import com.igp.admin.Blogs.models.BlogResultModel;
import com.igp.admin.Blogs.models.CategoryModel;
import com.igp.admin.Blogs.mappers.*;
import com.igp.admin.Blogs.models.CategorySubCategoryModel;
import com.igp.admin.response.EntityFoundResponse;
import com.igp.admin.response.EntityNotFoundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suditi on 3/5/18.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Categories {
    private static final Logger logger = LoggerFactory.getLogger(Categories.class);

    @POST
    @Path("/v1/categories/createcategory")
    public Response createCategory(CategoryModel categoryModel) {
        Response response=null;
        CategoryMapper categoryMapper = new  CategoryMapper();
        BlogResultModel blogResultModel ;

        try{
            blogResultModel = categoryMapper.createCategory(categoryModel);
            if(!blogResultModel.isError()){
                response= EntityFoundResponse.entityFoundResponseBuilder(blogResultModel.getObject());
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error",blogResultModel.getMessage());
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while creating Category post "+exception);
        }
        return response;
    }
    @PUT
    @Path("/v1/categories/updatecategory")
    public Response updateCategory(CategoryModel categoryModel) {
        Response response=null;
        CategoryMapper categoryMapper =  new CategoryMapper();
        BlogResultModel blogResultModel ;
        try{
            blogResultModel = categoryMapper.updateCategory(categoryModel);
            if(!blogResultModel.isError()){
                Map<String,String> updateCategoryResponse=new HashMap<>();
                updateCategoryResponse.put("data","Category updated succesfully.");
                response= EntityFoundResponse.entityFoundResponseBuilder(updateCategoryResponse);
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                if(!blogResultModel.getMessage().isEmpty()){
                    errorResponse.put("error",blogResultModel.getMessage());
                }else {
                    errorResponse.put("error", "Could not able to update Category");
                }
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while updating Category post "+exception);
        }
        return response;
    }
    @DELETE
    @Path("/v1/categories/deletecategory")
    public Response deleteCategory(@DefaultValue("-1") @QueryParam("id") int id) {
        Response response=null;
        CategoryMapper categoryMapper =  new CategoryMapper();
        boolean result = false;
        try{
            if(id == -1){
                Map<String,String> errorResponse=new HashMap<>();
                errorResponse.put("error","Parameter not specified");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }else{
                result = categoryMapper.deleteCategory(id);
                if(result){
                    Map<String,String> updateCategoryResponse=new HashMap<>();
                    updateCategoryResponse.put("data","Category deleted succesfully.");
                    response= EntityFoundResponse.entityFoundResponseBuilder(updateCategoryResponse);
                }else{
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error","Could not able to delete Category");
                    response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
                }
            }

        }catch (Exception exception){
            logger.debug("error occured while deleting Category post "+exception);
        }
        return response;
    }
    @GET
    @Path("/v1/categories/categorylist")
    public Response getCategoryList(@DefaultValue("5") @QueryParam("fkAssociateId") int fkAssociateId,
                                    @DefaultValue("0") @QueryParam("startLimit") int startLimit,
                                    @DefaultValue("100") @QueryParam("endLimit") int endLimit) {
        Response response=null;
        CategoryMapper categoryMapper =  new CategoryMapper();
        try{
            List<CategoryModel> categorySubCategoryLists = categoryMapper.getCategoryList(fkAssociateId,startLimit,endLimit);
            if(categorySubCategoryLists!= null && !categorySubCategoryLists.isEmpty()){
               response= EntityFoundResponse.entityFoundResponseBuilder(categorySubCategoryLists);
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Could not get list of Categories");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured getting list of Categories."+exception);
        }
        return response;
    }

    @GET
    @Path("/v1/categories/validatecategory")
    public Response validateCategory(@QueryParam("fkAssociateId") @DefaultValue("5") int fkAssociateId, @QueryParam("categoryName") String categoryName, @QueryParam("subCategoryName") String subCategoryName) {
        Response response=null;
        CategoryMapper categoryMapper =  new CategoryMapper();
        boolean result = false;
        try{
            result = categoryMapper.validateCategory(fkAssociateId, categoryName, subCategoryName);
            if(result==true){
                Map<String,String> validateCategoryResponse=new HashMap<>();
                validateCategoryResponse.put("data","Category validated succesfully.");
                response = EntityFoundResponse.entityFoundResponseBuilder(validateCategoryResponse);
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Category validation unsuccessful");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while updating Category post ",exception);
        }
        return response;
    }
}
