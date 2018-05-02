package com.igp.admin.marketplace.endpoints;

import com.igp.admin.marketplace.mappers.MarketPlaceMapper;
import com.igp.admin.marketplace.models.ErrorModel;
import com.igp.admin.marketplace.models.MarketPlaceFinalOrderResponseModel;
import com.igp.admin.marketplace.models.ValidationModel;
import com.igp.admin.response.EntityFoundResponse;
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
 * Created by suditi on 8/1/18.
 */
@Path("/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class MarketPlaceOrder {
    private static final Logger logger = LoggerFactory.getLogger(MarketPlaceOrder.class);
    @POST
    @Path("/v1/admin/marketplaceorder")
    public Response performCheckOnCorporateOrder(@QueryParam("user") String user,
                                                 @QueryParam("value") String userValue,
                                                 @QueryParam("fkasid") @DefaultValue("0") int loginId,
                                                 final FormDataMultiPart multiPart) throws ParseException {
        MarketPlaceFinalOrderResponseModel marketPlaceFinalOrderResponseModel = new MarketPlaceFinalOrderResponseModel();
        Response response = null;
        MarketPlaceMapper  marketPlaceMapper = new MarketPlaceMapper();
        Map<Integer, Map<String, String>> data = null;
        ErrorModel errorModel = new ErrorModel();
        List<ErrorModel> errorModelList = new ArrayList<>();
        try{
            int fkAssociateId = marketPlaceMapper.findVendor(userValue);
            if(fkAssociateId!=0 && loginId == 1 ) {
                // parse the excel file.
                data = marketPlaceMapper.parseExcelForMarketPlace(multiPart, user + fkAssociateId);
                if(!data.isEmpty()) {
                    // fill the models.
                    List<ValidationModel> validationModelList = marketPlaceMapper.refineDataAndPopulateModels(data, userValue, fkAssociateId);
                    // validate all the models and create order.
                    marketPlaceFinalOrderResponseModel = marketPlaceMapper.validateDataForMarketPlace(validationModelList);
                }else {
                    errorModel.setMsg("File is Empty/Invalid.");
                    errorModelList.add(errorModel);
                    marketPlaceFinalOrderResponseModel.setError(errorModelList);
                }
            }
            else {
                errorModel.setMsg("Wrong Vendor Details.");
                errorModelList.add(errorModel);
                marketPlaceFinalOrderResponseModel.setError(errorModelList);
            }
        }catch (Exception e){
            errorModel.setMsg("Something Went Wrong.");
            errorModelList.add(errorModel);
            marketPlaceFinalOrderResponseModel.setError(errorModelList);
        }
        response = EntityFoundResponse.entityFoundResponseBuilder(marketPlaceFinalOrderResponseModel);
        return response;
    }
}
