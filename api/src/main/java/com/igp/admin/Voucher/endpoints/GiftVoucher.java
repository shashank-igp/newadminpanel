package com.igp.admin.Voucher.endpoints;

import com.igp.admin.Voucher.mappers.GiftVoucherMapper;
import com.igp.admin.Voucher.models.*;
import com.igp.admin.response.EntityFoundResponse;
import com.igp.admin.response.EntityNotFoundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nikhil on 21/08/18.
 */

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GiftVoucher
{

    private static final Logger logger = LoggerFactory.getLogger(GiftVoucher.class);

    @POST
    @Path("/v1/gv/creategiftvoucher")
    public Response createGiftVoucher(GiftVoucherRequestContainer giftVoucherRequestContainer) {
        Response response=null;
        GiftVoucherMapper giftVoucherMapper = new GiftVoucherMapper();
        GiftVoucherModel giftVoucherModel = giftVoucherRequestContainer.getGiftVoucherModel();
        RowLimitModel rowLimit = giftVoucherRequestContainer.getRowLimitModel();
        try{
            ResultModel result = giftVoucherMapper.createGiftVoucher(giftVoucherModel);
            if(!result.isError()){
                GiftVoucherListModel giftVoucherModelList = giftVoucherMapper.getGiftVoucherList(-1,giftVoucherModel.getFkAssociateId(),rowLimit.getStartIndex(),rowLimit.getRowsCount());
                if(giftVoucherModelList!= null && giftVoucherModelList.getGiftVoucherModelList() != null && !giftVoucherModelList.getGiftVoucherModelList().isEmpty()){
                    response= EntityFoundResponse.entityFoundResponseBuilder(giftVoucherModelList);
                }else{
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error","Could not get list of gift vouchers");
                    response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
                }
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Could not create gift voucher.");
                if(result.getObject() != null){
                    errorResponse.put("error", result.getObject().toString());
                }
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while creating gift voucher post ",exception);
        }
        return response;
    }

    @PUT
    @Path("/v1/gv/updategiftvoucher")
    public Response updateGiftVoucher(GiftVoucherRequestContainer giftVoucherRequestContainer) {
        Response response=null;
        GiftVoucherMapper giftVoucherMapper = new GiftVoucherMapper();
        GiftVoucherModel giftVoucherModel = giftVoucherRequestContainer.getGiftVoucherModel();
        RowLimitModel rowLimitModel = giftVoucherRequestContainer.getRowLimitModel();
        try{
            ResultModel result = giftVoucherMapper.updateGiftVoucher(giftVoucherModel);
            if(!result.isError()){
                GiftVoucherListModel giftVoucherModelList= giftVoucherMapper.getGiftVoucherList(-1,giftVoucherModel.getFkAssociateId(),rowLimitModel.getStartIndex(),rowLimitModel.getRowsCount());
                if(giftVoucherModelList!= null && giftVoucherModelList.getGiftVoucherModelList() != null && !giftVoucherModelList.getGiftVoucherModelList().isEmpty()){
                    response= EntityFoundResponse.entityFoundResponseBuilder(giftVoucherModelList);
                }else{
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error","Could not get list of gift vouchers");
                    response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
                }
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Gift voucher could not be updated.");
                if(result.getObject() != null){
                    errorResponse.put("error", result.getObject().toString());
                }
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while updating gift voucher post ",exception);
        }
        return response;
    }


    @DELETE
    @Path("/v1/gv/deletegiftvoucher")
    public Response deleteGiftVoucher(@DefaultValue("-1") @QueryParam("id") int id,
                                    @QueryParam("fkAssociateId") int fkAssociateId,
                                    @DefaultValue("0") @QueryParam("startIndex") int startIndex,
                                    @QueryParam("rowsCount") int rowsCount) {
        Response response=null;
        GiftVoucherMapper giftVoucherMapper = new GiftVoucherMapper();
        try{
            ResultModel result = giftVoucherMapper.deleteGiftVoucher(id, fkAssociateId);
            if(!result.isError()){
                GiftVoucherListModel giftVoucherModelList= giftVoucherMapper.getGiftVoucherList(-1,fkAssociateId,startIndex,rowsCount);
                if(giftVoucherModelList!= null && giftVoucherModelList.getGiftVoucherModelList() != null && !giftVoucherModelList.getGiftVoucherModelList().isEmpty()){
                    response= EntityFoundResponse.entityFoundResponseBuilder(giftVoucherModelList);
                }else{
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error","Could not get list of vouchers");
                    response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
                }
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Could not delete Voucher");
                if(result.getObject() != null){
                    errorResponse.put("error", result.getObject().toString());
                }
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while deleting Voucher "+exception);
        }
        return response;
    }


    @GET
    @Path("/v1/gv/getgiftvoucher")
    public Response getGiftVoucherlist(@DefaultValue("-1") @QueryParam("fkAssociateId") int fkAssociateId,
                                @DefaultValue("-1") @QueryParam("id") int id,
                                @DefaultValue("0") @QueryParam("startLimit") int startLimit,
                                @DefaultValue("2000") @QueryParam("endLimit") int endLimit) {
        Response response=null;
        GiftVoucherMapper giftVoucherMapper = new GiftVoucherMapper();

        try{
            GiftVoucherListModel giftVoucherModelList = giftVoucherMapper.getGiftVoucherList(id,fkAssociateId,startLimit,endLimit);
            if(giftVoucherModelList.getGiftVoucherModelList().size()!=0 && !giftVoucherModelList.getGiftVoucherModelList().isEmpty()){
                response= EntityFoundResponse.entityFoundResponseBuilder(giftVoucherModelList);
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Not able to get list of gift vouchers");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while get list of gift vouchers "+exception);
        }
        return response;
    }

    @GET
    @Path("/v1/gv/validategiftvoucher")
    public Response validateCategory(@QueryParam("fkAssociateId") @DefaultValue("5") int fkAssociateId,
                                     @QueryParam("giftvouchercode") String giftVouchercode) {
        Response response=null;
        boolean result = false;
        GiftVoucherMapper giftVoucherMapper = new GiftVoucherMapper();
        try{
            result = giftVoucherMapper.validateGiftVoucher(fkAssociateId,giftVouchercode);
            if(!result){
                Map<String,String> validateCategoryResponse=new HashMap<>();
                validateCategoryResponse.put("data","Gift voucher code does not exist.");
                response = EntityFoundResponse.entityFoundResponseBuilder(validateCategoryResponse);
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Gift voucher already exists.");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while validating gift voucher "+exception);
        }
        return response;
    }
}
