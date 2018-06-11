package com.igp.admin.Voucher.endpoints;

import com.igp.admin.Voucher.mappers.VoucherMapper;
import com.igp.admin.Voucher.models.VoucherModel;
import com.igp.admin.response.EntityFoundResponse;
import com.igp.admin.response.EntityNotFoundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suditi on 8/6/18.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Voucher {

    private static final Logger logger = LoggerFactory.getLogger(Voucher.class);

    @POST
    @Path("/v1/voucher/createvoucher")
    public Response createVoucher(VoucherModel voucherModel) {
        Response response=null;
        VoucherMapper voucherMapper = new VoucherMapper();
        try{
            boolean result = voucherMapper.createVoucher(voucherModel);
            if(result==true){
                response= EntityFoundResponse.entityFoundResponseBuilder("true ");
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Voucher not created.");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while creating Voucher post ",exception);
        }
        return response;
    }
    @PUT
    @Path("/v1/voucher/updatevoucher")
    public Response updateVoucher(VoucherModel voucherModel) {
        Response response=null;
        VoucherMapper voucherMapper = new VoucherMapper();
        VoucherModel VoucherModel = new VoucherModel();
        try{
            boolean result = voucherMapper.updateVoucher(voucherModel);
            if(result==true){
                Map<String,String> updateVoucherResponse=new HashMap<>();
                updateVoucherResponse.put("data","Voucher updated succesfully.");
                response= EntityFoundResponse.entityFoundResponseBuilder(updateVoucherResponse);
            }else{
                Map<String, String> errorResponse = new HashMap<>();
            //    errorResponse.put("error",VoucherModel.getMessage());
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while updating Voucher post ",exception);
        }
        return response;
    }
    @DELETE
    @Path("/v1/voucher/deletevoucher")
    public Response deleteVoucher(@DefaultValue("-1") @QueryParam("id") int id) {
        Response response=null;
        VoucherMapper voucherMapper = new VoucherMapper();
        boolean result = false;
        try{
            result = voucherMapper.deleteVoucher(id);
            if(result==true){
                Map<String,String> deleteVoucherResponse=new HashMap<>();
                deleteVoucherResponse.put("data","Voucher deleted succesfully.");
                response= EntityFoundResponse.entityFoundResponseBuilder(deleteVoucherResponse);
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Could not delete Voucher");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while deleting Voucher post ",exception);
        }
        return response;
    }


    @GET
    @Path("/v1/vouchers/getvoucher")
    public Response getVoucherlist(@DefaultValue("-1") @QueryParam("fkAssociateId") int fkAssociateId,
                                @DefaultValue("-1") @QueryParam("id") int id,
                                @DefaultValue("0") @QueryParam("startLimit") int startLimit,
                                @DefaultValue("1000") @QueryParam("endLimit") int endLimit) {
        Response response=null;
        VoucherMapper voucherMapper=new VoucherMapper();

        try{
            List<VoucherModel>  voucherModelList= voucherMapper.getVoucherList(id,startLimit,endLimit);
            if(voucherModelList.size()!=0 && !voucherModelList.isEmpty()){
                response= EntityFoundResponse.entityFoundResponseBuilder(voucherModelList);
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Not able to get list of Vouchers");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while get list of Vouchers "+exception);
        }
        return response;
    }

}


