package com.igp.admin.CustomerCareRelatedPanles.endpoints;

import com.igp.admin.CustomerCareRelatedPanles.mappers.SendFollowUpMailPanelMapper;
import com.igp.admin.marketplace.mappers.MarketPlaceMapper;
import com.igp.admin.response.EntityFoundResponse;
import com.igp.admin.response.EntityNotFoundResponse;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shanky on 23/8/18.
 */
@Path("/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class SendFollowUpMailPanel {
    private static final Logger logger = LoggerFactory.getLogger(SendFollowUpMailPanel.class);
    @POST
    @Path("/v1/admin/uploadFolloUpTrackingNumberFile")
    public Response uploadFolloUpTrackingNumberFile(@QueryParam("user")@DefaultValue("temp") String user,
                                    @QueryParam("value")@DefaultValue("temp") String userValue,
                                    @QueryParam("fkasid") @DefaultValue("0") int loginId,
                                    @QueryParam("issue")@DefaultValue("AddressRelated") String issue,
                                    final FormDataMultiPart multiPart) throws ParseException{
        Response response = null;
        MarketPlaceMapper marketPlaceMapper = new MarketPlaceMapper();
        Map<Integer, Map<String, String>> data = null;
        int fkAssociateId = 0,numColumn=1;
        SendFollowUpMailPanelMapper sendFollowUpMailPanelMapper=new SendFollowUpMailPanelMapper();
        List<String> mailSendResponse=null;
        try{
            data = marketPlaceMapper.parseExcelForMarketPlace(multiPart, user + fkAssociateId,numColumn);
            if(data.size()>0){
                mailSendResponse=sendFollowUpMailPanelMapper.uploadFolloUpTrackingNumberFile(data,issue);
            }
            if(mailSendResponse==null){
                List<String> errorResponse = new ArrayList<>();
                errorResponse.add("Could not upload/send emails at this time please try again later");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }else{
                response= EntityFoundResponse.entityFoundResponseBuilder(mailSendResponse);
            }
        }catch (Exception exception){
            logger.error("Exeception occured while sending followup mails to customers",exception);
        }
        return response;
    }
}
