package com.igp.admin.Blogs.endpoints;
import com.igp.admin.Blogs.mappers.*;

import com.igp.admin.Blogs.models.BlogListResponseModel;
import com.igp.admin.Blogs.models.BlogMainModel;
import com.igp.admin.Blogs.models.BlogResultModel;
import com.igp.admin.Blogs.models.SeoBlogModel;
import com.igp.admin.response.EntityFoundResponse;
import com.igp.admin.response.EntityNotFoundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by suditi on 2/5/18.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Blogs {
    private static final Logger logger = LoggerFactory.getLogger(Blogs.class);

    @POST
    @Path("/v1/blogs/createblog")
    public Response createBlog(BlogMainModel blogMainModel) {
        Response response=null;
        BlogsMapper blogsMapper = new  BlogsMapper();
        try{
            BlogResultModel blogResultModel = blogsMapper.createBlog(blogMainModel);
            if(blogResultModel.isError()==false){
                response= EntityFoundResponse.entityFoundResponseBuilder("url : "+blogResultModel.getObject());
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error",blogResultModel.getMessage());
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while creating blog post ",exception);
        }
        return response;
    }
    @PUT
    @Path("/v1/blogs/updateblog")
    public Response updateBlog(BlogMainModel blogMainModel) {
        Response response=null;
        BlogsMapper blogsMapper = new  BlogsMapper();
        BlogResultModel blogResultModel = new BlogResultModel();
        try{
            blogResultModel = blogsMapper.updateBlog(blogMainModel);
            if(blogResultModel.isError()==false){
                Map<String,String> updateBlogResponse=new HashMap<>();
                updateBlogResponse.put("data","blog updated succesfully.");
                response= EntityFoundResponse.entityFoundResponseBuilder(updateBlogResponse);
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error",blogResultModel.getMessage());
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while updating blog post ",exception);
        }
        return response;
    }
    @DELETE
    @Path("/v1/blogs/deleteblog")
    public Response deleteBlog(@DefaultValue("-1") @QueryParam("id") int id) {
        Response response=null;
        BlogsMapper blogsMapper = new  BlogsMapper();
        boolean result = false;
        try{
            result = blogsMapper.deleteBlog(id);
            if(result==true){
                Map<String,String> deleteBlogResponse=new HashMap<>();
                deleteBlogResponse.put("data","Blog deleted succesfully.");
                response= EntityFoundResponse.entityFoundResponseBuilder(deleteBlogResponse);
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Could not delete blog");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while deleting blog post ",exception);
        }
        return response;
    }

    @GET
    @Path("/v1/blogs/validateblogurl")
    public Response validateBlogUrl(@QueryParam("fkAssociateId") int fkAssociateId,
                                    @DefaultValue("") @QueryParam("url") String url,
                                    @DefaultValue("") @QueryParam("imageurl") String imageUrl){
        Response response = null;
        BlogsMapper blogsMapper = new BlogsMapper();
        BlogResultModel result ;
        Map<String, String> validateBlogUrlResult = new HashMap<>();
        try{

            if(url.trim().isEmpty() && imageUrl.trim().isEmpty()){
                //failed validations
                validateBlogUrlResult.put("error", "Parameter not specified");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(validateBlogUrlResult);
            }else {

                result = blogsMapper.validateBlogUrl(fkAssociateId, url, imageUrl);
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
            }

        }catch (Exception e){
            logger.debug("error occured while validating blog url ",e);
            validateBlogUrlResult.put("error", e.getMessage());
            response = EntityNotFoundResponse.entityNotFoundResponseBuilder(validateBlogUrlResult);
        }
        return response;
    }
    @GET
    @Path("/v1/blogs/getblogs")
    public Response getbloglist(@DefaultValue("-1") @QueryParam("fkAssociateId") int fkAssociateId,
                                @DefaultValue("-1") @QueryParam("id") int id,
                                @DefaultValue("0") @QueryParam("startLimit") int startLimit,
                                @DefaultValue("10") @QueryParam("endLimit") int endLimit) {
        Response response=null;
        BlogsMapper blogMapper=new BlogsMapper();

        try{
            BlogListResponseModel blogListResponseModel = blogMapper.getBlogList(fkAssociateId,id,startLimit,endLimit);
            if(blogListResponseModel.getCount()!=0 && !blogListResponseModel.getBlogList().isEmpty()){
                response= EntityFoundResponse.entityFoundResponseBuilder(blogListResponseModel);
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Not able to get list of blogs");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while get list of blogs ",exception);
        }
        return response;
    }
    @PUT
    @Path("/v1/blogs/updateblogstatus")
    public Response updateBlogStatus(@DefaultValue("-1") @QueryParam("id") int id,
                                     @DefaultValue("-1") @QueryParam("status") int status) {
        Response response=null;
        BlogsMapper blogsMapper = new  BlogsMapper();
        BlogResultModel blogResultModel = new BlogResultModel();
        Map<String, String> errorResponse = new HashMap<>();
        try{

            if(id == -1 || status == -1){
                errorResponse.put("error", "Parameter not specified");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }else{
                blogResultModel = blogsMapper.updateBlogStatus(id, status);
                if(blogResultModel.isError()==false){
                    Map<String,String> updateBlogResponse=new HashMap<>();
                    updateBlogResponse.put("data","blog status updated succesfully.");
                    response= EntityFoundResponse.entityFoundResponseBuilder(updateBlogResponse);
                }else{
                    errorResponse.put("error",blogResultModel.getMessage());
                    response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
                }
            }
        }catch (Exception exception){
            logger.debug("error occured while updating blog post status ",exception);
        }
        return response;
    }
    @GET
    @Path("/v1/blogs/getmetahome")
    public Response getmetahome(@DefaultValue("5") @QueryParam("fkasid") int fkAssociateId) {
        Response response=null;
        BlogsMapper blogMapper=new BlogsMapper();

        try{
            SeoBlogModel seoBlogModel = blogMapper.getMetaHome(fkAssociateId);
            if(seoBlogModel.getId()!=null){
                response = EntityFoundResponse.entityFoundResponseBuilder(seoBlogModel);
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Not able to get meta home");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while get meta home "+exception);
        }
        return response;
    }
    @PUT
    @Path("/v1/blogs/updatemetahome")
    public Response updateMetaHome(SeoBlogModel seoBlogModel) {
        Response response=null;
        BlogsMapper blogsMapper = new  BlogsMapper();
        boolean result = false;
        try{
            result = blogsMapper.updateMetaHome(seoBlogModel);
            if(result==true){
                Map<String,String> updateMetaHome=new HashMap<>();
                updateMetaHome.put("data","meta home updated succesfully.");
                response= EntityFoundResponse.entityFoundResponseBuilder(updateMetaHome);
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Error while updating meta home.");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while updating meta home: "+exception);
        }
        return response;
    }

}
