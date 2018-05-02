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
    public Integer createBlog(BlogMainModel blogMainModel){
        Integer blogId=null;
        BlogsUtil blogUtil=new BlogsUtil();
        try{
            blogId=blogUtil.createBlog(blogMainModel);
        }catch (Exception exception){
            logger.debug("error occured while creating blog post ",exception);
        }
        return blogId;
    }
}
