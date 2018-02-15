package com.igp.handles.admin.endpoints;

import com.igp.handles.admin.mappers.Reports.ReportMapper;
import com.igp.handles.admin.models.Reports.PincodeModelListHavingSummaryModel;
import com.igp.handles.admin.models.Reports.ProductModelListHavingSummaryModel;
import com.igp.handles.admin.models.Reports.BarcodeToComponentListHavingSummary;
import com.igp.handles.admin.models.Reports.VendorDetailsHavingSummaryModel;
import com.igp.handles.vendorpanel.models.Report.PayoutAndTaxReportSummaryModel;
import com.igp.handles.vendorpanel.models.Report.ReportOrderWithSummaryModel;
import com.igp.handles.vendorpanel.response.HandleServiceResponse;
import com.igp.handles.vendorpanel.response.ReportResponse;
import com.igp.handles.vendorpanel.utils.Reports.SummaryFunctionsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    @Path("/v1/admin/handels/getOrderReport")
    public ReportResponse getOrderReport(@QueryParam("fkAssociateId") String fkAssociateId,
                                         @QueryParam("orderDateFrom") String startDate,
                                         @QueryParam("orderDateTo")String endDate ,
                                         @QueryParam("startLimit") String startLimit,
                                         @QueryParam("endLimit") String endLimit ,
                                         @QueryParam("orderNumber") Integer orderNo,
                                         @QueryParam("status")  String status,
                                         @QueryParam("deliveryDateFrom") String deliveryDateFrom,
                                         @QueryParam("deliveryDateTo") String deliveryDateTo){

        ReportResponse reportResponse = new ReportResponse();
        ReportMapper reportMapper = new ReportMapper();
        startDate=getTimestampString(startDate,0);
        endDate=getTimestampString(endDate,0);
        if(deliveryDateFrom==null){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();
            deliveryDateFrom=dtf.format(localDate);
            //set today's date by default
        }
        deliveryDateTo=getTimestampString(deliveryDateTo,0);
        deliveryDateFrom=getTimestampString(deliveryDateFrom,0);

        reportResponse.setTableHeaders(new String[]{"Order_No","Vendor_Name","Date","Occasion","City","Pincode","Delivery_Date"
            ,"Delivery_Type","Recipient_Name","Phone","Amount","Status"});
        ReportOrderWithSummaryModel reportOrderWithSummaryModel1 = reportMapper.getOrderReportMapper(fkAssociateId,startDate,endDate,startLimit,endLimit,orderNo,status,deliveryDateFrom,deliveryDateTo);
        reportResponse.setSummary(reportOrderWithSummaryModel1.getSummaryModelList());
        List<Object> objectList = new ArrayList<Object>(reportOrderWithSummaryModel1.getOrderReportObjectModelList());
        reportResponse.setTableData( objectList);
        return reportResponse;
    }


    @GET
    @Path("/v1/admin/handels/getPincodeReport")
    public ReportResponse getPincodeReport(@QueryParam("fkAssociateId") @DefaultValue("565") String fkAssociateId,
                                           @QueryParam("startLimit") String startLimit,
                                           @QueryParam("endLimit") String endLimit){
        ReportResponse reportResponse = new ReportResponse();
        ReportMapper reportMapper = new ReportMapper();
        List<Map.Entry<String,List<String>>> tableDataAction = new ArrayList<>();
        reportResponse.setTableHeaders(new String[]{"Vendor Name","Pincode","Standard Delivery","Fixed Time Delivery","Midnight Delivery"});
        reportMapper.fillDataActionPincode(tableDataAction);
        reportResponse.setTableDataAction(tableDataAction);
        PincodeModelListHavingSummaryModel pincodeModelListHavingSummaryModel = ReportMapper.getPincodeSummaryDetails(fkAssociateId,startLimit,endLimit);
        reportResponse.setSummary(pincodeModelListHavingSummaryModel.getSummaryModelList());
        List<Object> objectList = new ArrayList<Object>(pincodeModelListHavingSummaryModel.getPincodeTableDataModelList());
        reportResponse.setTableData(objectList);
        return reportResponse;
    }

    @PUT
    @Path("/v1/admin/handels/handlePincodeChange")
    public HandleServiceResponse updatePincodeDetail(@QueryParam("fkAssociateId") int fkAssociateId,
                                                     @QueryParam("pincode") int pincode,
                                                     @QueryParam("shipCharge")int updatePrice,
                                                     @QueryParam("field") String field,
                                                     @QueryParam("shipType") int shipType,
                                                     @QueryParam("flag") @DefaultValue("0") int flag){
        HandleServiceResponse handleServiceResponse = new HandleServiceResponse();
        ReportMapper reportMapper = new ReportMapper();
        Boolean result = false;
        String message="";
        if (flag == 1){
            message = "Enable/Disable "+shipType+" for Pincode "+pincode+" : ";
        }
        else {
            message = "Update the price of "+shipType+" for Pincode "+pincode+" to "+updatePrice+" : ";
        }
        boolean status = reportMapper.updatePincodeMapper(flag,fkAssociateId,pincode,shipType,updatePrice,message,field);
        if(status == false){
            handleServiceResponse.setError(true);
            handleServiceResponse.setErrorCode("ERROR OCCURRED HANDELING PINCODE");
        }
        else {
            result = true;
        }
        handleServiceResponse.setResult(result);
        return handleServiceResponse;
    }

    @POST
    @Path("/v1/admin/handels/addVendorComponent")
    public HandleServiceResponse addVendorComponent(@QueryParam("fkAssociateId") int fkAssociateId,
                                                    @QueryParam("componentCode") String componentCode,
                                                    @QueryParam("componentName") String componentName,
                                                    @DefaultValue("0")@QueryParam("type") int type,
                                                    @DefaultValue("0")@QueryParam("price") int price){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        ReportMapper reportMapper = new ReportMapper();
        boolean result;
        result = reportMapper.addNewComponentMapper(fkAssociateId,componentCode,componentName,type, price);
        if(result == false){
            handleServiceResponse.setError(true);
            handleServiceResponse.setErrorCode("ERROR OCCURRED ADDING COMPONENT");
        }
        handleServiceResponse.setResult(result);
        return handleServiceResponse;
    }
    @POST
    @Path("/v1/admin/handels/addVendorPincode")
    public HandleServiceResponse addVendorPincode(@QueryParam("fkAssociateId") int fkAssociateId,
                                                  @QueryParam("pincode") int pincode,
                                                  @DefaultValue("0") @QueryParam("cityId") int cityId,
                                                  @QueryParam("shipType") int shipType,
                                                  @QueryParam("shipCharge") int shipCharge){
        HandleServiceResponse handleServiceResponse = new HandleServiceResponse();
        ReportMapper reportMapper = new ReportMapper();
        boolean result;
        result = reportMapper.addNewVendorPincodeMapper(fkAssociateId,pincode,cityId,shipType,shipCharge);
        if(result == false){
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

        ReportResponse reportResponse = new ReportResponse();
        List<Map.Entry<String,List<String>>> tableDataAction = new ArrayList<>();
        ReportMapper reportMapper = new ReportMapper();
        reportResponse.setTableHeaders(new String[]{"Component_Id_Hide","Component Image","Component_Name","Price","InStock"});
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
                                                       @DefaultValue("-1") @QueryParam("updatePrice")int updatePrice,
                                                       @DefaultValue("-1") @QueryParam("inStock") int inStock,
                                                       @QueryParam("field") String field){
        HandleServiceResponse handleServiceResponse = new HandleServiceResponse();
        String message = "",componentName = "";
        ReportMapper reportMapper = new ReportMapper();
        componentName= SummaryFunctionsUtil.getComponentName(componentId);
        if (updatePrice!=-1){
            message="Change price of component "+componentName+" to "+updatePrice+" : ";
        }
        else if(inStock==1){
            message="Change status of component "+componentName+" to Instock : ";
        }
        else {
            message="Change status of component "+componentName+" to Out of stock : ";
        }

        boolean result = reportMapper.updateComponentMapper(fkAssociateId,componentId,message,updatePrice,inStock,field);
        if(result == false){
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
        ReportResponse reportResponse = new ReportResponse();
        ReportMapper reportMapper = new ReportMapper();
        VendorDetailsHavingSummaryModel vendorDetailsHavingSummaryModel = new VendorDetailsHavingSummaryModel();
        List<Map.Entry<String,List<String>>> tableDataAction = new ArrayList<>();
        try{

            reportMapper.fillDataActionVendor(tableDataAction);
            reportResponse.setTableDataAction(tableDataAction);
            reportResponse.setTableHeaders(new String[]{"Vendor_Id","Vendor_Name","Contact_Person","Email",
                "Address","Phone","User_Id","Password","Status"});

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
                                                     @DefaultValue("") @QueryParam("Vendor_Name") String vendorName,
                                                     @DefaultValue("") @QueryParam("Contact_Person") String contactPerson,
                                                     @DefaultValue("") @QueryParam("Email") String email,
                                                     @DefaultValue("") @QueryParam("Address") String address,
                                                     @DefaultValue("") @QueryParam("Phone") String phone,
                                                     @DefaultValue("") @QueryParam("User_Id") String userId,
                                                     @DefaultValue("") @QueryParam("Password") String password,
                                                     @DefaultValue("-1") @QueryParam("Status") int status){
        HandleServiceResponse handleServiceResponse = new HandleServiceResponse();
        ReportMapper reportMapper = new ReportMapper();
        boolean result = false;
        try{
            result = reportMapper.modifyVendorDetails(fkAssociateId,vendorName,contactPerson,email,address,phone,userId,password,status);
        }catch (Exception exception){
            logger.error("Error occured at modifyVendorDetails ",exception);
        }
        if(result == false){
            handleServiceResponse.setError(true);
            handleServiceResponse.setErrorCode("ERROR OCCURRED CHANGING VENDOR INFO");
        }
        handleServiceResponse.setResult(result);
        return handleServiceResponse;
    }

    @POST
    @Path("/v1/admin/handels/addNewVendor")
    public HandleServiceResponse addNewVendor(@QueryParam("Vendor_Name") String vendorName,
                                              @QueryParam("Contact_Person") String contactPerson,
                                              @QueryParam("Email") String email,
                                              @QueryParam("Address") String address,
                                              @QueryParam("User_Id") String user,
                                              @QueryParam("Password") String password,
                                              @QueryParam("Phone") String phone,
                                              @DefaultValue("1") @QueryParam("Status") int status){
        HandleServiceResponse handleServiceResponse = new HandleServiceResponse();
        ReportMapper reportMapper = new ReportMapper();
        boolean result = false;

        try{
            result =  reportMapper.addNewVendorMapper(vendorName,user,password,contactPerson,email,address,phone,status);
        }catch (Exception exception){
            logger.error("Error occured at add new vendor ",exception);
        }
        if(result == false){
            handleServiceResponse.setError(true);
            handleServiceResponse.setErrorCode("ERROR OCCURRED WHILE ADDING VENDOR");
        }
        handleServiceResponse.setResult(result);
        return handleServiceResponse;
    }
    @PUT
    @Path("/v1/admin/handels/approveAndReject")
    public HandleServiceResponse approveAndReject(@QueryParam("approveReject") boolean approveReject, // true : approve, false : reject
                                                  @QueryParam("reportType") String reportType,
                                                  @QueryParam("colName") String columnName,
                                                  @QueryParam("fkAssociateId") int fkAssociateId,
                                                  @QueryParam("object") String object){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        ReportMapper reportMapper = new ReportMapper();
        boolean response = false;
        try{
            response =  reportMapper.approveAndRejectMapper(object,reportType,columnName,fkAssociateId,approveReject);
        }catch (Exception exception){
            logger.error("Error occured at add acceptAndReject ",exception);
        }
        if(response == false){
            handleServiceResponse.setError(true);
            handleServiceResponse.setErrorCode("ERROR OCCURRED WHILE APPROVE AND REJECT");
        }
        handleServiceResponse.setResult(response);
        return handleServiceResponse;
    }
    @GET
    @Path("/v1/admin/handels/getBarcodeToComponentReport")
    public ReportResponse getBarcodeToComponentReport(@QueryParam("Product_Code") @DefaultValue("0") String productCode,
                                                      @QueryParam("startLimit") @DefaultValue("0") int startLimit,
                                                      @QueryParam("endLimit") @DefaultValue("0") int endLimit){

        ReportResponse reportResponse = new ReportResponse();
        ReportMapper reportMapper = new ReportMapper();
        List<Map.Entry<String,List<String>>> tableDataAction = new ArrayList<>();
        try{
            reportMapper.fillDataActionBarcode(tableDataAction);
            reportResponse.setTableDataAction(tableDataAction);
            reportResponse.setTableHeaders(new String[]{"Product_Code","Product_Name","Product_Image","Component_Code",
                "Component_Name","Quantity","Component_Image"});
            BarcodeToComponentListHavingSummary barcodeToComponentListHavingSummary = reportMapper.getBarcodeToComponentsMapper(productCode,startLimit,endLimit);
            reportResponse.setSummary(barcodeToComponentListHavingSummary.getSummaryModelList());
            List<Object> objectList = new ArrayList<Object>(barcodeToComponentListHavingSummary.getBarcodeToComponentDataModelList());
            reportResponse.setTableData(objectList);
        }catch (Exception exception){
            logger.error("Error occured at getVendorDetails ",exception);
        }
        return reportResponse;
    }
    @PUT
    @Path("/v1/admin/handels/deleteBarcode")
    public HandleServiceResponse deleteBarcode(@QueryParam("Product_Code") String productCode){
        HandleServiceResponse handleServiceResponse = new HandleServiceResponse();
        ReportMapper reportMapper = new ReportMapper();
        boolean response = false;
        try{
            response = reportMapper.deleteBarcodeMapper(productCode);
        }catch (Exception exception){
            logger.error("Error occured at deleteBarcode ",exception);
        }
        if(response == false){
            handleServiceResponse.setError(true);
            handleServiceResponse.setErrorCode("ERROR OCCURRED WHILE DELETING BARCODE");
        }
        handleServiceResponse.setResult(response);
        return handleServiceResponse;
    }
    @PUT
    @Path("/v1/admin/handels/changeBarcodeComponent")
    public HandleServiceResponse changeBarcodeComponent(@QueryParam("Product_Code") String productCode,
                                                        @QueryParam("Component_Code") String componentCode,
                                                        @QueryParam("Quantity") int quantity){
        HandleServiceResponse handleServiceResponse = new HandleServiceResponse();
        ReportMapper reportMapper = new ReportMapper();
        boolean response = false;
        try{
            response = reportMapper.changeBarcodeComponentMapper(productCode,componentCode,quantity);
        }catch (Exception exception){
            logger.error("Error occured at changeBarcodeComponent ",exception);
        }
        if(response == false){
            handleServiceResponse.setError(true);
            handleServiceResponse.setErrorCode("ERROR OCCURRED WHILE CHANGING BARCODE COMPONENT");
        }
        handleServiceResponse.setResult(response);
        return handleServiceResponse;
    }
    @GET
    @Path("/v1/admin/handels/getListOfBarcodes")
    public HandleServiceResponse getListOfBarcodes(@QueryParam("startLimit") @DefaultValue("0") int startLimit,
                                                   @QueryParam("endLimit") @DefaultValue("0") int endLimit){
        HandleServiceResponse handleServiceResponse = new HandleServiceResponse();
        ReportMapper reportMapper = new ReportMapper();
        try{
            Map<List<String>,Map<String,Integer>> productCodeList = reportMapper.getListOfBarcodesMapper(startLimit,endLimit);
            handleServiceResponse.setResult(productCodeList);
        }catch (Exception exception){
            logger.error("Error occured at getVendorDetails ",exception);
        }
        return handleServiceResponse;
    }


}
