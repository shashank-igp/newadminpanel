package com.igp.handles.admin.endpoints;

import com.igp.handles.admin.mappers.Mail.MailMapper;
import com.igp.handles.vendorpanel.response.HandleServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by shanky on 29/1/18.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class Mail {
    private static final Logger logger = LoggerFactory.getLogger(Mail.class);
    @GET
    @Path("/v1/admin/handels/sendMailToVendor")
    public HandleServiceResponse sendMailToVendor(){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        MailMapper mailMapper=new MailMapper();
        try{

        }catch (Exception exception){
            logger.error("error occured while sending mail to vendor ",exception);
        }

        return handleServiceResponse;
    }

}
