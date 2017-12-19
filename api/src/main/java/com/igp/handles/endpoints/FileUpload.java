package com.igp.handles.endpoints;

import com.igp.handles.models.FileUpload.FileUploadModel;
import com.igp.handles.response.HandleServiceResponse;
import com.igp.handles.utils.FileUpload.UploadUtil;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * Created by shanky on 8/9/17.
 */
@Path("/")
public class FileUpload {
    private static final Logger logger = LoggerFactory.getLogger(FileUpload.class);
    @POST
    @Path("/v1/handels/fileupload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public HandleServiceResponse uploadFile(@Context HttpServletResponse response,@Context HttpServletRequest request,
                                    @QueryParam("orderId") int orderId, @QueryParam("orderProductId") int orderProductId,
                                    @QueryParam("status") String status, final FormDataMultiPart multiPart) throws IOException {

        StringBuffer fileDetails = new StringBuffer("");
        UploadUtil uploadUtil=new UploadUtil();
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        FileUploadModel fileUploadModel=uploadUtil.uploadOrderRelatedFile(multiPart,orderId,orderProductId,status);
        handleServiceResponse.setResult(fileUploadModel);
        response.addHeader("token",request.getHeader("token"));
        return handleServiceResponse;
    }
    @DELETE
    @Path("/v1/handels/filedelete")
    @Produces(MediaType.APPLICATION_JSON)
    public HandleServiceResponse deleteFile(@Context HttpServletResponse response,@Context HttpServletRequest request,
                                            @QueryParam("orderId") int orderId, @QueryParam("orderProductId") int orderProductId,
                                            @QueryParam("filePath") String filePath){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        UploadUtil uploadUtil=new UploadUtil();
        handleServiceResponse.setResult(uploadUtil.deleteFile(orderId,orderProductId,filePath));
        response.addHeader("token",request.getHeader("token"));
        return handleServiceResponse;
    }
}
