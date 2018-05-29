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
                //response= EntityFoundResponse.entityFoundResponseBuilder(blogResultModel.getObject());
                List<CategoryModel> categorySubCategoryLists = categoryMapper.getCategoryList(categoryModel.getFkAssociateId(), 0, 100);
                if(categorySubCategoryLists!= null && !categorySubCategoryLists.isEmpty()){
                    response= EntityFoundResponse.entityFoundResponseBuilder(categorySubCategoryLists);
                }else{
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error","Could not get list of Categories");
                    response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
                }
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
                List<CategoryModel> categorySubCategoryLists = categoryMapper.getCategoryList(categoryModel.getFkAssociateId(), 0, 100);
                if(categorySubCategoryLists!= null && !categorySubCategoryLists.isEmpty()){
                    response= EntityFoundResponse.entityFoundResponseBuilder(categorySubCategoryLists);
                }else{
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error","Could not get list of Categories");
                    response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
                }

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
    public Response deleteCategory(@DefaultValue("-1") @QueryParam("id") int id,
                                   @QueryParam("fkAssociateId")int fkAssociateId) {
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
                    List<CategoryModel> categorySubCategoryLists = categoryMapper.getCategoryList(fkAssociateId, 0, 100);
                    if(categorySubCategoryLists!= null && !categorySubCategoryLists.isEmpty()){
                        response= EntityFoundResponse.entityFoundResponseBuilder(categorySubCategoryLists);
                    }else{
                        Map<String,String> updateCategoryResponse=new HashMap<>();
                        updateCategoryResponse.put("data","Category deleted succesfully.");
                        response= EntityFoundResponse.entityFoundResponseBuilder(updateCategoryResponse);
                    }
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
    public Response validateCategory(@QueryParam("fkAssociateId") @DefaultValue("5") int fkAssociateId,
                                     @QueryParam("categoryName") String categoryName,
                                     @QueryParam("subCategoryName") String subCategoryName) {
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

    @GET
    @Path("/v1/categories/validatecategoryurl")
    public Response validateCategoryUrl(@QueryParam("fkAssociateId")int fkAssociateId, @QueryParam("url") String url) {
        Response response=null;
        CategoryMapper categoryMapper =  new CategoryMapper();
        Map<String, String> validateBlogUrlResult = new HashMap<>();
        BlogResultModel result ;
        try{
            result = categoryMapper.validateCategoryUrl(fkAssociateId, url);
            if (result.isError()) {
                if ("urlexist".equalsIgnoreCase(result.getMessage())) {
                    //result is true, it means (fkAssociateId, url) combination already exist.
                    //return error as url must be different
                    validateBlogUrlResult.put("error", "Please choose different url");
                    validateBlogUrlResult.put("unique", "false");
                    response = EntityNotFoundResponse.entityNotFoundResponseBuilder(validateBlogUrlResult);
                }
                else {//status is not success - some error occured
                    validateBlogUrlResult.put("error", result.getMessage());
                    validateBlogUrlResult.put("unique", "false");
                    response = EntityNotFoundResponse.entityNotFoundResponseBuilder(validateBlogUrlResult);
                }
            } else {
                //url is valid
                validateBlogUrlResult.put("data", "Selected url is valid");
                validateBlogUrlResult.put("unique", "true");
                response = EntityFoundResponse.entityFoundResponseBuilder(validateBlogUrlResult);
            }
        }catch (Exception exception){
            logger.debug("error occured while validating Category url ",exception);
        }
        return response;
    }
}
