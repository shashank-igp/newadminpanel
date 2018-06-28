package com.igp.admin.marketplace.endpoints;

import com.igp.admin.marketplace.mappers.MarketPlaceMapper;
import com.igp.admin.marketplace.models.ErrorModel;
import com.igp.admin.marketplace.models.MarketPlaceFinalOrderResponseModel;
import com.igp.admin.marketplace.models.ValidationModel;
import com.igp.admin.response.EntityFoundResponse;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.java2d.loops.ProcessPath;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by suditi on 8/1/18.
 */
@Path("/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class MarketPlaceOrder {
    public static String semaphore = "false";
    // we will serve only one request at a time
    private static final Logger logger = LoggerFactory.getLogger(MarketPlaceOrder.class);
    @POST
    @Path("/v1/admin/marketplaceorder")
    public synchronized Response performCheckOnCorporateOrder(@QueryParam("user") String user,
                                                              @QueryParam("value") String userValue,
                                                              @QueryParam("fkasid") @DefaultValue("0") int loginId,
                                                              final FormDataMultiPart multiPart) throws ParseException {
        MarketPlaceFinalOrderResponseModel marketPlaceFinalOrderResponseModel = new MarketPlaceFinalOrderResponseModel();
        Response response = null;
        MarketPlaceMapper  marketPlaceMapper = new MarketPlaceMapper();
        Map<Integer, Map<String, String>> data = null;
        ErrorModel errorModel = new ErrorModel();
        List<ErrorModel> errorModelList = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        int fkAssociateId = 0;
        try{
            fkAssociateId = marketPlaceMapper.findVendor(userValue);
            logger.debug("file uploaded : "+formatter.format(date) + " with vendor id : "+fkAssociateId);
            String processName = ManagementFactory.getRuntimeMXBean().getName();
            System.out.println("Process ID for this request = " + processName);
            if(fkAssociateId!=0 && loginId == 1 ) {

                if(semaphore.equals("false") && marketPlaceMapper.setSemaphore(processName).equals(semaphore)){
                    // system is not serving any other request / current process is chosen to be served
                    // parse the excel file.
                    System.out.println("semaphore is = " + semaphore);

                    semaphore = "true"; // lock taken
                    System.out.println("semaphore is = " + semaphore);

                    data = marketPlaceMapper.parseExcelForMarketPlace(multiPart, user + fkAssociateId, fkAssociateId);
                    if (!data.isEmpty()) {
                        // fill the models.
                        List<ValidationModel> validationModelList = marketPlaceMapper.refineDataAndPopulateModels(data, userValue, fkAssociateId);
                        // validate all the models and create order.
                        marketPlaceFinalOrderResponseModel = marketPlaceMapper.validateDataForMarketPlace(validationModelList);
                    } else {
                        errorModel.setMsg("File is Empty/Invalid.");
                        errorModelList.add(errorModel);
                        marketPlaceFinalOrderResponseModel.setError(errorModelList);
                    }
                    semaphore = "false"; // lock released
                }
                else {
                    errorModel.setMsg("Some One is already uploading the file. Please Wait !!!");
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
        logger.debug("Response of file upload : "+formatter.format(date) + " with vendor id : "+fkAssociateId+ " is "+marketPlaceFinalOrderResponseModel.toString());
        response = EntityFoundResponse.entityFoundResponseBuilder(marketPlaceFinalOrderResponseModel);
        System.out.println("semaphore is = " + semaphore);
        return response;
    }
}
