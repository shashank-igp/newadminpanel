package com.igp.handles.admin.endpoints;

import com.igp.handles.admin.mappers.Reports.ReportMapper;
import com.igp.handles.admin.models.Reports.PincodeModelListHavingSummaryModel;
import com.igp.handles.admin.models.Reports.ProductModelListHavingSummaryModel;
import com.igp.handles.admin.models.Reports.VendorDetailsHavingSummaryModel;
import com.igp.handles.vendorpanel.models.Report.PayoutAndTaxReportSummaryModel;
import com.igp.handles.vendorpanel.response.HandleServiceResponse;
import com.igp.handles.vendorpanel.response.ReportResponse;
import com.igp.handles.vendorpanel.utils.Reports.SummaryFunctionsUtil;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.igp.handles.vendorpanel.utils.Reports.SummaryFunctionsUtil.getTimestampString;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
/**
 * Created by suditi on 30/1/18.
 */
public class Reports {

    private static final Logger logger = LoggerFactory.getLogger(Reports.class);

    @GET
    @Path("/v1/admin/handels/getPincodeReport")
    public ReportResponse getPincodeReport(@QueryParam("fkAssociateId") @DefaultValue("565") String fkAssociateId,
                                           @QueryParam("startLimit") String startLimit, @QueryParam("endLimit") String endLimit ){
        ReportResponse reportResponse=new ReportResponse();
        List<Map.Entry<String,List<String>>> tableDataAction=new ArrayList<>();
        reportResponse.setTableHeaders(new String[]{"Vendor ID","Vendor Name","Pincode","Standard Delivery","Fixed Time Delivery","Midnight Delivery","Change Required"});
        com.igp.handles.vendorpanel.mappers.Reports.ReportMapper.fillDataActionpincode(tableDataAction);
        reportResponse.setTableDataAction(tableDataAction);
        PincodeModelListHavingSummaryModel pincodeModelListHavingSummaryModel =ReportMapper.getPincodeSummaryDetails(fkAssociateId,startLimit,endLimit);
        reportResponse.setSummary(pincodeModelListHavingSummaryModel.getSummaryModelList());
        List<Object> objectList = new ArrayList<Object>(pincodeModelListHavingSummaryModel.getPincodeTableDataModelList());
        reportResponse.setTableData(objectList);
        return reportResponse;
    }

    @PUT
    @Path("/v1/admin/handels/handlePincodeChange")
    public HandleServiceResponse updatePincodeDetail(@QueryParam("fkAssociateId") int fkAssociateId,
                                                     @QueryParam("pincode") String pincode, @QueryParam("shipCharge")Integer updatePrice ,
                                                     @QueryParam("shipType") Integer shipType, @QueryParam("updateStatus") Integer updateStatus){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        ReportMapper reportMapper = new ReportMapper();
        Integer updateflag=0;
        Boolean result = false;
        String message="";
        if (updateStatus!=null){
            updateflag=1;
            message="Enable/Disable "+shipType+" for Pincode "+pincode+" : ";
        }
        else if (updatePrice!=null){
            updateflag=2;
            message="Update the price of "+shipType+" for Pincode "+pincode+" to "+updatePrice+" : ";
        }
        int status=reportMapper.updatePincodeMapper(updateflag,fkAssociateId,pincode,shipType,updateStatus,updatePrice,message);
        if(status==0){
            handleServiceResponse.setError(true);
            handleServiceResponse.setErrorCode("ERROR OCCURRED HANDELING PINCODE");
        }
        else {
            result=true;
        }
        handleServiceResponse.setResult(result);
        return handleServiceResponse;
    }

    @POST
    @Path("/v1/admin/handels/addVendorComponent")
    public HandleServiceResponse addVendorComponent(@QueryParam("fkAssociateId") int fkAssociateId,@QueryParam("componentCode") String componentCode,
                                                    @QueryParam("componentName") String componentName,@DefaultValue("0")@QueryParam("type") int type,
                                                    @DefaultValue("0")@QueryParam("price") int price){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        ReportMapper reportMapper = new ReportMapper();
        boolean result;
        result= reportMapper.addNewComponentMapper(fkAssociateId,componentCode,componentName,type, price);
        if(result==false){
            handleServiceResponse.setError(true);
            handleServiceResponse.setErrorCode("ERROR OCCURRED ADDING COMPONENT");
        }
        handleServiceResponse.setResult(result);
        return handleServiceResponse;
    }
    @POST
    @Path("/v1/admin/handels/addVendorPincode")
    public HandleServiceResponse addVendorPincode(@QueryParam("fkAssociateId") int fkAssociateId,@QueryParam("pincode") int pincode,
                                                  @DefaultValue("0")@QueryParam("cityId") int cityId,@QueryParam("shipType") int shipType,@QueryParam("shipCharge")int shipCharge ){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        ReportMapper reportMapper = new ReportMapper();
        boolean result;
        result= reportMapper.addNewVendorPincodeMapper(fkAssociateId,pincode,cityId,shipType,shipCharge);
        if(result==false){
            handleServiceResponse.setError(true);
            handleServiceResponse.setErrorCode("ERROR OCCURRED ADDING PINCODE");
        }
        handleServiceResponse.setResult(result);
        return handleServiceResponse;
    }

    @GET
    @Path("/v1/admin/handels/getVendorReport")
    public ReportResponse getVendorReport(@QueryParam("fkAssociateId") @DefaultValue("565") String fkAssociateId,
                                          @QueryParam("startLimit") String startLimit,
                                          @QueryParam("endLimit") String endLimit ){

        ReportResponse reportResponse=new ReportResponse();
        List<Map.Entry<String,List<String>>> tableDataAction=new ArrayList<>();
        ReportMapper reportMapper = new ReportMapper();
        reportResponse.setTableHeaders(new String[]{"Component_Id_Hide","Component Image","Component_Name","Price","InStock","Required Price"});
        com.igp.handles.vendorpanel.mappers.Reports.ReportMapper.fillDataActionComponent(tableDataAction);
        reportResponse.setTableDataAction(tableDataAction);
        ProductModelListHavingSummaryModel productModelListHavingSummaryModel = reportMapper.getProductSummaryDetails(fkAssociateId,startLimit,endLimit);
        reportResponse.setSummary(productModelListHavingSummaryModel.getSummaryModelList());
        List<Object> objectList = new ArrayList<Object>(productModelListHavingSummaryModel.getProductTableDataModelList());
        reportResponse.setTableData(objectList);

        return reportResponse;
    }


    @PUT
    @Path("/v1/admin/handels/handleComponentChange")
    public HandleServiceResponse updateComponentDetail(@QueryParam("fkAssociateId") int fkAssociateId,
                                                       @QueryParam("componentId") String componentId,
                                                       @DefaultValue("0") @QueryParam("price")int updatePrice,
                                                       @QueryParam("inStock") String inStock){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        Integer updateflag=0;
        String message="",componentName="";
        ReportMapper reportMapper = new ReportMapper();
        componentName= SummaryFunctionsUtil.getComponentName(componentId);
        if (inStock!=null){
            updateflag=1;
            if(inStock.equals(1)){
                message="Change status of component "+componentName+" to Instock : ";
            }else {
                message="Change status of component "+componentName+" to Out of stock : ";
            }
        }
        else if (updatePrice!=0){
            updateflag=2;
            message="Change price of component "+componentName+" to "+updatePrice+" : ";
        }
        boolean result=reportMapper.updateComponentMapper(updateflag,fkAssociateId,componentId,message,updatePrice,inStock);
        if(result==false){
            handleServiceResponse.setError(true);
            handleServiceResponse.setErrorCode("ERROR OCCURRED CHANGING COMPONENT INFO");
        }
        handleServiceResponse.setResult(result);
        return handleServiceResponse;
    }

    @GET
    @Path("/v1/admin/handels/getPayoutAndTaxesReport")
    public ReportResponse getPayoutAndTaxes(@DefaultValue("565")@QueryParam("fkAssociateId") int fkAssociateId, @QueryParam("orderNumber")int orderId,
                                            @QueryParam("orderDateFrom") String orderDateFrom, @QueryParam("orderDateTo")String orderDateTo,
                                            @QueryParam("deliveryDateFrom") String orderDeliveryDateFrom,@QueryParam("deliveryDateTo") String orderDeliveryDateTo,
                                            @QueryParam("startLimit") String startLimit, @QueryParam("endLimit") String endLimit ){


        ReportResponse reportResponse=new ReportResponse();
        ReportMapper reportMapper=new ReportMapper();
        try{
            reportResponse.setTableHeaders(new String[]{"vendorId","vendor","invoice number","orderId","date purchased","delivery date"
                ,"pincode","password","order status","taxable amount","tax","total amount","payment status"});


            orderDateFrom=getTimestampString(orderDateFrom,2);
            orderDateTo=getTimestampString(orderDateTo,2);
            orderDeliveryDateFrom=getTimestampString(orderDeliveryDateFrom,2);
            orderDeliveryDateTo=getTimestampString(orderDeliveryDateTo,2);

            PayoutAndTaxReportSummaryModel payoutAndTaxReportSummaryModel=reportMapper.getPayoutAndTaxes
                (fkAssociateId,orderId,orderDateFrom,orderDateTo,
                    orderDeliveryDateFrom,orderDeliveryDateTo,
                    startLimit,endLimit);
            reportResponse.setSummary(payoutAndTaxReportSummaryModel.getSummaryModelList());
            List<Object> objectList = new ArrayList<Object>(payoutAndTaxReportSummaryModel.getOrderTaxReportList());
            reportResponse.setTableData(objectList);

        }catch (Exception exception){
            logger.error("Error occured at getPayoutAndTaxes ",exception);
        }
        return reportResponse;


    }
    @GET
    @Path("/v1/admin/handels/getVendorDetails")
    public ReportResponse getVendorDetails(@DefaultValue("0") @QueryParam("fkAssociateId") int fkAssociateId,
                                           @DefaultValue("0") @QueryParam("startLimit") int startLimit,
                                           @DefaultValue("0") @QueryParam("endLimit") int endLimit ){
        ReportResponse reportResponse=new ReportResponse();
        ReportMapper reportMapper = new ReportMapper();
        VendorDetailsHavingSummaryModel vendorDetailsHavingSummaryModel = new VendorDetailsHavingSummaryModel();
        List<Map.Entry<String,List<String>>> tableDataAction=new ArrayList<>();
        try{
        reportMapper.fillDataActionVendor(tableDataAction);
        reportResponse.setTableDataAction(tableDataAction);
        reportResponse.setTableHeaders(new String[]{"fkAssociateId","associateName","contactPerson","emailId",
            "address","phone","userId","password","status"});

            vendorDetailsHavingSummaryModel = reportMapper.getVendorDetails(fkAssociateId,startLimit,endLimit);
            reportResponse.setSummary(vendorDetailsHavingSummaryModel.getSummaryModelList());
            List<Object> objectList = new ArrayList<Object>(vendorDetailsHavingSummaryModel.getVendorDetailsModels());
            reportResponse.setTableData(objectList);
        }catch (Exception exception){
            logger.error("Error occured at getVendorDetails ",exception);
        }
        return reportResponse;
    }
    @PUT
    @Path("/v1/admin/handels/modifyVendorDetails")
    public HandleServiceResponse modifyVendorDetails(@QueryParam("fkAssociateId") int fkAssociateId,
                                                     @DefaultValue("") @QueryParam("associateName") String associateName,
                                                     @DefaultValue("") @QueryParam("contactPerson") String contactPerson,
                                                     @DefaultValue("") @QueryParam("email") String email,
                                                     @DefaultValue("") @QueryParam("address") String address,
                                                     @DefaultValue("") @QueryParam("phone") String phone,
                                                     @DefaultValue("") @QueryParam("userId") String userId,
                                                     @DefaultValue("") @QueryParam("password") String password,
                                                     @DefaultValue("0") @QueryParam("status") int status){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        ReportMapper reportMapper=new ReportMapper();
        boolean result=false;
        try{
            result = reportMapper.modifyVendorDetails(fkAssociateId,associateName,contactPerson,email,address,phone,userId,password,status);
        }catch (Exception exception){
            logger.error("Error occured at modifyVendorDetails ",exception);
        }
        if(result==false){
            handleServiceResponse.setError(true);
            handleServiceResponse.setErrorCode("ERROR OCCURRED CHANGING VENDOR INFO");
        }
        handleServiceResponse.setResult(result);
        return handleServiceResponse;
    }

    @POST
    @Path("/v1/admin/handels/addNewVendor")
    public HandleServiceResponse addNewVendor(@QueryParam("associateName") String associateName,
                                              @QueryParam("contactPerson") String contactPerson,
                                              @QueryParam("email") String email,
                                              @QueryParam("address") String address,
                                              @QueryParam("user") String user,
                                              @QueryParam("password") String password,
                                              @QueryParam("phone") String phone,
                                              @DefaultValue("2") @QueryParam("status") int status){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        ReportMapper reportMapper=new ReportMapper();
        boolean result =false;

        try{
          result =  reportMapper.addNewVendorMapper(associateName,user,password,contactPerson,email,address,phone,status);
        }catch (Exception exception){
            logger.error("Error occured at modifyVendorDetails ",exception);
        }
        if(result==false){
            handleServiceResponse.setError(true);
            handleServiceResponse.setErrorCode("ERROR OCCURRED WHILE ADDING VENDOR");
        }
        handleServiceResponse.setResult(result);
        return handleServiceResponse;

    }

}
