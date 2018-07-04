package com.igp.admin.Voucher.endpoints;

import com.igp.admin.Blogs.models.SeoBlogModel;
import com.igp.admin.Voucher.mappers.VoucherMapper;
import com.igp.admin.Voucher.models.*;
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
    public Response createVoucher(VoucherRequestContainer voucherRequestContainer) {
        Response response=null;
        VoucherMapper voucherMapper = new VoucherMapper();
        VoucherModel voucherModel = voucherRequestContainer.getVoucherModel();
        RowLimitModel rowLimit = voucherRequestContainer.getRowLimitModel();
        try{
            ResultModel result = voucherMapper.createVoucher(voucherModel);
            if(result.isError()==false){
                VoucherListModel voucherModelList= voucherMapper.getVoucherList(-1,voucherModel.getFkAssociateId(),rowLimit.getStartIndex(),rowLimit.getRowsCount());
                if(voucherModelList!= null && voucherModelList.getVoucherModelList() != null && !voucherModelList.getVoucherModelList().isEmpty()){
                    response= EntityFoundResponse.entityFoundResponseBuilder(voucherModelList);
                }else{
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error","Could not get list of vouchers");
                    response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
                }
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Voucher not created.");
                if(result.getObject() != null){
                    errorResponse.put("error_reason", result.getObject().toString());
                }
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while creating Voucher post ",exception);
        }
        return response;
    }
    @PUT
    @Path("/v1/voucher/updatevoucher")
    public Response updateVoucher(VoucherRequestContainer voucherRequestContainer) {
        Response response=null;
        VoucherMapper voucherMapper = new VoucherMapper();
        VoucherModel voucherModel = voucherRequestContainer.getVoucherModel();
        RowLimitModel rowLimitModel = voucherRequestContainer.getRowLimitModel();
        try{
            ResultModel result = voucherMapper.updateVoucher(voucherModel);
            if(result.isError()==false){
                VoucherListModel voucherModelList= voucherMapper.getVoucherList(-1,voucherModel.getFkAssociateId(),rowLimitModel.getStartIndex(),rowLimitModel.getRowsCount());
                if(voucherModelList!= null && voucherModelList.getVoucherModelList() != null && !voucherModelList.getVoucherModelList().isEmpty()){
                    response= EntityFoundResponse.entityFoundResponseBuilder(voucherModelList);
                }else{
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error","Could not get list of vouchers");
                    response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
                }
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","voucher could't be updated.");
                if(result.getObject() != null){
                    errorResponse.put("error_reason", result.getObject().toString());
                }
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while updating Voucher post ",exception);
        }
        return response;
    }
    @DELETE
    @Path("/v1/voucher/deletevoucher")
    public Response deleteVoucher(@DefaultValue("-1") @QueryParam("id") int id,
                                    @QueryParam("fkAssociateId") int fkAssociateId,
                                    @DefaultValue("") @QueryParam("modifiedby") String modifiedBy,
                                    @DefaultValue("0") @QueryParam("startIndex") int startIndex,
                                    @QueryParam("rowsCount") int rowsCount) {
        Response response=null;
        VoucherMapper voucherMapper = new VoucherMapper();
        try{
            ResultModel result = voucherMapper.deleteVoucher(id,modifiedBy);
            if(result.isError()==false){
                VoucherListModel voucherModelList= voucherMapper.getVoucherList(-1,fkAssociateId,startIndex,rowsCount);
                if(voucherModelList!= null && voucherModelList.getVoucherModelList() != null && !voucherModelList.getVoucherModelList().isEmpty()){
                    response= EntityFoundResponse.entityFoundResponseBuilder(voucherModelList);
                }else{
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error","Could not get list of vouchers");
                    response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
                }
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Could not delete Voucher");
                if(result.getObject() != null){
                    errorResponse.put("error_reason", result.getObject().toString());
                }
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while deleting Voucher "+exception);
        }
        return response;
    }


    @GET
    @Path("/v1/vouchers/getvoucher")
    public Response getVoucherlist(@DefaultValue("-1") @QueryParam("fkAssociateId") int fkAssociateId,
                                @DefaultValue("-1") @QueryParam("id") int id,
                                @DefaultValue("0") @QueryParam("startLimit") int startLimit,
                                @DefaultValue("2000") @QueryParam("endLimit") int endLimit) {
        Response response=null;
        VoucherMapper voucherMapper=new VoucherMapper();

        try{
            VoucherListModel voucherModelList= voucherMapper.getVoucherList(id,fkAssociateId,startLimit,endLimit);
            if(voucherModelList.getVoucherModelList().size()!=0 && !voucherModelList.getVoucherModelList().isEmpty()){
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

    @GET
    @Path("/v1/voucher/validatevoucher")
    public Response validateCategory(@QueryParam("fkAssociateId") @DefaultValue("5") int fkAssociateId,
                                     @QueryParam("vouchercode") String vouchercode) {
        Response response=null;
        boolean result = false;
        VoucherMapper voucherMapper = new VoucherMapper();
        try{
            result = voucherMapper.validateVoucher(fkAssociateId,vouchercode);
            if(result==false){
                Map<String,String> validateCategoryResponse=new HashMap<>();
                validateCategoryResponse.put("data","Voucher code doesn't exist.");
                response = EntityFoundResponse.entityFoundResponseBuilder(validateCategoryResponse);
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Voucher already exists.");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while validating voucher "+exception);
        }
        return response;
    }



    @GET
    @Path("/v1/voucher/getTemporaryBlackListProdCats")
    public Response getExistingBlackListProdCats() {
        Response response=null;
        VoucherMapper voucherMapper=new VoucherMapper();
        List<String> blackList = null;

        try{
            blackList = voucherMapper.getExistingBlackListProdCats();
            if(blackList != null && !blackList.isEmpty()){
                response= EntityFoundResponse.entityFoundResponseBuilder(blackList);
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error","Not able to get list of black list product categories");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }catch (Exception exception){
            logger.debug("error occured while get list of Vouchers "+exception);
        }
        return response;
    }


    @GET
    @Path("/v1/voucher/getAssociates")
    public Response getAssociates()
    {
        Response response = null;
        VoucherMapper voucherMapper = new VoucherMapper();

        try
        {
            List<SeoBlogModel> seoBlogModel = voucherMapper.getAssociates();
            if (seoBlogModel != null && !seoBlogModel.isEmpty())
            {
                response = EntityFoundResponse.entityFoundResponseBuilder(seoBlogModel);
            }
            else
            {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Not able to get associates data");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }
        catch (Exception exception)
        {
            logger.debug("error occured while get associates data " + exception);
        }
        return response;
    }

    @GET
    @Path("/v1/voucher/getMetaData")
    public Response getVoucherMetaData()
    {
        Response response = null;
        VoucherMapper voucherMapper = new VoucherMapper();

        try
        {
            VoucherMetaData voucherMetaData = voucherMapper.getVoucherMetaData();
            if (voucherMetaData != null){
                response = EntityFoundResponse.entityFoundResponseBuilder(voucherMetaData);
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Not able to get voucher meta data");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }
        catch (Exception exception)
        {
            logger.debug("error occured while fetching voucher meta data " + exception);
        }
        return response;
    }

    @GET
    @Path("/v1/voucher/getCategories")
    public Response getCategories(@DefaultValue("133824") @QueryParam("parentId") int parentCatId,
                                @QueryParam("categoryId") int categoryId)
    {
        Response response = null;
        VoucherMapper voucherMapper = new VoucherMapper();

        try
        {
            List<CategoriesModel> categoriesModelList = voucherMapper.getCategories(parentCatId, categoryId);
            if (categoriesModelList != null){
                response = EntityFoundResponse.entityFoundResponseBuilder(categoriesModelList);
            }else{
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Not able to get categories.");
                response = EntityNotFoundResponse.entityNotFoundResponseBuilder(errorResponse);
            }
        }
        catch (Exception exception)
        {
            logger.debug("error occured while fetching voucher meta data " + exception);
        }
        return response;
    }
}
