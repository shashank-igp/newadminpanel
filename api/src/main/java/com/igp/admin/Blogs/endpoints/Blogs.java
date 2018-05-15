package com.igp.admin.Blogs.endpoints;
import com.igp.admin.Blogs.mappers.*;

import com.igp.admin.Blogs.models.BlogMainModel;
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
    @Path("/v1/createblog")
    public Response createBlog(BlogMainModel blogMainModel) {
        Response response=null;
        BlogsMapper blogsMapper = new  BlogsMapper();
        String url = "";
        try{
            url=blogsMapper.createBlog(blogMainModel);
            if(!url.isEmpty()){
                Map<String,String> createBlogResponse=new HashMap<>();
                createBlogResponse.put("data","blog created with url "+url);
                response= EntityFoundResponse.entityFoundResponseBuilder(createBlogResponse);
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Could not able to create blog");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while creating blog post ",exception);
        }
        return response;
    }
    @PUT
    @Path("/v1/updateblog")
    public Response updateBlog(BlogMainModel blogMainModel) {
        Response response=null;
        BlogsMapper blogsMapper = new  BlogsMapper();
        boolean result = false;
        try{
            result = blogsMapper.updateBlog(blogMainModel);
            if(result==true){
                Map<String,String> updateBlogResponse=new HashMap<>();
                updateBlogResponse.put("data","blog updated succesfully.");
                response= EntityFoundResponse.entityFoundResponseBuilder(updateBlogResponse);
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Could not able to update blog");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while updating blog post ",exception);
        }
        return response;
    }
    @DELETE
    @Path("/v1/deleteblog")
    public Response deleteBlog(BlogMainModel blogMainModel) {
        Response response=null;
        BlogsMapper blogsMapper = new  BlogsMapper();
        boolean result = false;
        try{
            result = blogsMapper.deleteBlog(blogMainModel);
            if(result==true){
                Map<String,String> updateBlogResponse=new HashMap<>();
                updateBlogResponse.put("data","blog deleted succesfully.");
                response= EntityFoundResponse.entityFoundResponseBuilder(updateBlogResponse);
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Could not able to delete blog");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while deleting blog post ",exception);
        }
        return response;
    }
}
