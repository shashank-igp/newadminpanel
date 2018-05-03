package com.igp.admin.Blogs.mappers;

import com.igp.admin.Blogs.models.BlogMainModel;
import com.igp.admin.Blogs.utils.BlogsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by suditi on 2/5/18.
 */
public class BlogsMapper {
    private static final Logger logger = LoggerFactory.getLogger(BlogsMapper.class);
    public String createBlog(BlogMainModel blogMainModel){

        String url = "";
        BlogsUtil blogUtil = new BlogsUtil();
        try{
            url = blogUtil.createBlog(blogMainModel);
        }catch (Exception exception){
            logger.debug("error occured while creating blog post ",exception);
        }
        return url;
    }

    public boolean updateBlog(BlogMainModel blogMainModel){

        boolean result = false;
        BlogsUtil blogUtil=new BlogsUtil();
        try{
            result = blogUtil.updateBlog(blogMainModel);
        }catch (Exception exception){
            logger.debug("error occured while updating blog post ",exception);
        }
        return result;
    }

    public boolean deleteBlog(BlogMainModel blogMainModel){

        boolean result = false;
        BlogsUtil blogUtil=new BlogsUtil();
        try{
            result = blogUtil.deleteBlog(blogMainModel);
        }catch (Exception exception){
            logger.debug("error occured while deleting blog post ",exception);
        }
        return result;
    }
}
