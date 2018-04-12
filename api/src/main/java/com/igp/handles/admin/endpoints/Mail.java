package com.igp.handles.admin.endpoints;

import com.igp.handles.admin.mappers.Mail.MailMapper;
import com.igp.handles.vendorpanel.response.HandleServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by shanky on 29/1/18.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class Mail {
    private static final Logger logger = LoggerFactory.getLogger(Mail.class);
    @POST
    @Path("/v1/admin/handels/sendEmailToVendor")
    public HandleServiceResponse sendMailToVendor(@DefaultValue("")@QueryParam("body")String mailBody,
                                                        @DefaultValue("72")@QueryParam("fkAssociateId") int vendorId,
                                                    @QueryParam("subject")String subject){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        MailMapper mailMapper=new MailMapper();
        try{
            handleServiceResponse.setResult(mailMapper.sendMailToVendor(mailBody,vendorId,"Action Required - IGP"));
        }catch (Exception exception){
            logger.error("error occured while sending mail to vendor ",exception);
        }

        return handleServiceResponse;
    }

    @POST
    @Path("/v1/admin/handels/sendSmsToVendor")
    public HandleServiceResponse sendSmsToVendor(@DefaultValue("")@QueryParam("body")String smsBody,
        @DefaultValue("72")@QueryParam("fkAssociateId") int vendorId,@QueryParam("subject")String subject){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        MailMapper mailMapper=new MailMapper();
        try{
            handleServiceResponse.setResult(mailMapper.sendSmsToVendor(smsBody,vendorId));
        }catch (Exception exception){
            logger.error("error occured while sending mail to vendor ",exception);
        }

        return handleServiceResponse;
    }

}
