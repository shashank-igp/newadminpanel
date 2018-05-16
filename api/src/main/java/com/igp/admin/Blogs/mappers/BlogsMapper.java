package com.igp.admin.Blogs.mappers;

import com.igp.admin.Blogs.models.BlogListResponseModel;
import com.igp.admin.Blogs.models.BlogMainModel;
import com.igp.admin.Blogs.models.BlogResultModel;
import com.igp.admin.Blogs.utils.BlogsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by suditi on 2/5/18.
 */
public class BlogsMapper {
    private static final Logger logger = LoggerFactory.getLogger(BlogsMapper.class);
    public BlogResultModel createBlog(BlogMainModel blogMainModel){
        BlogResultModel blogResultModel = new BlogResultModel();
        BlogsUtil blogUtil = new BlogsUtil();
        try{
            blogResultModel = blogUtil.createBlog(blogMainModel);
        }catch (Exception exception){
            logger.debug("error occured while creating blog post ",exception);
        }
        return blogResultModel;
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

    public BlogResultModel validateBlogUrl(int fkAssociateId, String url){
        BlogResultModel result = new BlogResultModel();
        BlogsUtil blogsUtil = new BlogsUtil();
        try{
            result = blogsUtil.validateBlogUrl(fkAssociateId, url);
        }catch (Exception e){
            logger.debug("error occured while validating blog post url",e);
            result.setError(true);
            result.setMessage(e.getMessage());
        }
        return result;
    }
    public BlogListResponseModel getBlogList(int fkAssociateId, boolean isCategory, String categoryName, String subCategoryName, int start, int end){
        BlogsUtil blogUtil=new BlogsUtil();
        BlogListResponseModel blogListResponseModel = new BlogListResponseModel();
        try{
            blogListResponseModel = blogUtil.getListBlog(fkAssociateId,isCategory,categoryName,subCategoryName,start,end);
        }catch (Exception exception){
            logger.debug("error occured while getBlogList. ",exception);
        }
        return blogListResponseModel;
    }
}
