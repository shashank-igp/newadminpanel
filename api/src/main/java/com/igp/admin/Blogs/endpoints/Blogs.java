package com.igp.admin.Blogs.endpoints;
import com.igp.admin.Blogs.mappers.*;

import com.igp.admin.Blogs.models.BlogMainModel;
import com.igp.admin.response.EntityFoundResponse;
import com.igp.admin.response.EntityNotFoundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by suditi on 2/5/18.
 */
public class Blogs {
    private static final Logger logger = LoggerFactory.getLogger(Blogs.class);
    
    @POST
    @Path("/v1/createblog")
    public Response createBlog(BlogMainModel blogMainModel) {
        Response response=null;
        BlogsMapper blogsMapper = new  BlogsMapper();
        Integer blogId=null;
        try{
            blogId=blogsMapper.createBlog(blogMainModel);
            if(blogId!=null){
                Map<String,String> createBlogResponse=new HashMap<>();
                createBlogResponse.put("data","blog created with Id "+blogId);
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

}
