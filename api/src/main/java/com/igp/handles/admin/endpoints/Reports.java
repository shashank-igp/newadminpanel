package com.igp.handles.admin.endpoints;

import com.igp.handles.admin.mappers.Mail.MailMapper;
import com.igp.handles.admin.mappers.Reports.ReportMapper;
import com.igp.handles.admin.models.Reports.*;
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

        try {
            if(deliveryDateFrom==null&&deliveryDateTo==null&&status==null&&orderNo==null&&endDate==null&&startDate==null){
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                LocalDate localDate = LocalDate.now();
                deliveryDateFrom=dtf.format(localDate);
                //set today's date by default
            }
            startDate=getTimestampString(startDate,0);
            deliveryDateTo=getTimestampString(deliveryDateTo,0);
            deliveryDateFrom=getTimestampString(deliveryDateFrom,0);

            if(startDate!=null&&endDate!=null){
                endDate=getTimestampString(endDate,1);
            }else {
                endDate=getTimestampString(endDate,0);
            }

            reportResponse.setTableHeaders(new String[]{"Order_No","Order_Product_Id","Vendor_Name","Date","Occasion","City","Pincode","Delivery_Date"
                ,"Delivery_Type","Amount","Status","Vendor_List"});
            ReportOrderWithSummaryModel reportOrderWithSummaryModel1 = reportMapper.getOrderReportMapper(fkAssociateId,startDate,endDate,startLimit,endLimit,orderNo,status,deliveryDateFrom,deliveryDateTo);
            reportResponse.setSummary(reportOrderWithSummaryModel1.getSummaryModelList());
            List<Object> objectList = new ArrayList<Object>(reportOrderWithSummaryModel1.getOrderReportObjectModelList());
            reportResponse.setTableData( objectList);
        }catch (Exception exception){
            logger.error("Error occured at getOrderReport ",exception);
        }
        return reportResponse;
    }


    @GET
    @Path("/v1/admin/handels/getPincodeReport")
    public ReportResponse getPincodeReport(@QueryParam("fkAssociateId") @DefaultValue("565") String fkAssociateId,
                                           @QueryParam("startLimit") String startLimit,
                                           @QueryParam("endLimit") String endLimit){
        ReportResponse reportResponse = new ReportResponse();
        ReportMapper reportMapper = new ReportMapper();
        try {
            List<Map.Entry<String,List<String>>> tableDataAction = new ArrayList<>();
            reportResponse.setTableHeaders(new String[]{"Vendor Name","Pincode","Standard Delivery","Fixed Time Delivery","Midnight Delivery"});
            reportMapper.fillDataActionPincode(tableDataAction);
            reportResponse.setTableDataAction(tableDataAction);
            PincodeModelListHavingSummaryModel pincodeModelListHavingSummaryModel = ReportMapper.getPincodeSummaryDetails(fkAssociateId,startLimit,endLimit);
            reportResponse.setSummary(pincodeModelListHavingSummaryModel.getSummaryModelList());
            List<Object> objectList = new ArrayList<Object>(pincodeModelListHavingSummaryModel.getPincodeTableDataModelList());
            reportResponse.setTableData(objectList);
        }catch (Exception exception){
            logger.error("Error occured at getPincodeReport ",exception);
        }
        return reportResponse;
    }

    @PUT
    @Path("/v1/admin/handels/handlePincodeChange")
    public HandleServiceResponse updatePincodeDetail(@QueryParam("fkAssociateId") int fkAssociateId,
                                                     @QueryParam("pincode") int pincode,
                                                     @QueryParam("shipCharge")int updatePrice,
                                                     @QueryParam("field") String field,
                                                     @QueryParam("shipType") int shipType,
                                                     @QueryParam("updateStatus") Integer flag){
        HandleServiceResponse handleServiceResponse = new HandleServiceResponse();
        ReportMapper reportMapper = new ReportMapper();
        MailMapper mailMapper = new MailMapper();
        try{
            Boolean result = false;
            String message = "";
            if (flag != null && flag == 1){
                if(flag == 1)
                    message = "Enable shipping type "+shipType+" for Pincode "+pincode+" for vendor id "+fkAssociateId;
                else
                    message = "Disable shipping type "+shipType+" for Pincode "+pincode+" for vendor id "+fkAssociateId;
            }
            else {
                message = "Update the price of shipping type "+shipType+" for Pincode "+pincode+" to "+updatePrice+" for vendor id "+fkAssociateId;
            }
            boolean status = reportMapper.updatePincodeMapper(flag,fkAssociateId,pincode,shipType,updatePrice,message,field);
            if(status == false){
                handleServiceResponse.setError(true);
                handleServiceResponse.setErrorCode("ERROR OCCURRED HANDELING PINCODE");
            }
            else {
                result = true;
                mailMapper.sendMailToVendor(message,fkAssociateId,"Pincode details modified - IGP");
            }
            handleServiceResponse.setResult(result);
        }catch (Exception exception){
            logger.error("Error occured at updatePincodeDetail ",exception);
        }
        return handleServiceResponse;
    }

    @POST
    @Path("/v1/admin/handels/addVendorComponent")
    public HandleServiceResponse addVendorComponent(@QueryParam("fkAssociateId") int fkAssociateId,
                                                    @QueryParam("componentCode") String componentCode,
                                                    @QueryParam("componentName") String componentName,
                                                    @DefaultValue("0")@QueryParam("type") int type,
                                                    @DefaultValue("0")@QueryParam("price") int price){

        // `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0 for General, 1 for Cake-Only'
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        ReportMapper reportMapper = new ReportMapper();
        try{
            boolean result;
            result = reportMapper.addNewComponentMapper(fkAssociateId,componentCode,componentName,type, price);
            if(result == false){
                handleServiceResponse.setError(true);
                handleServiceResponse.setErrorCode("ERROR OCCURRED ADDING COMPONENT");
            }
            handleServiceResponse.setResult(result);
        }catch (Exception exception){
            logger.error("Error occured at addVendorComponent ",exception);
        }
        return handleServiceResponse;
    }

    @PUT
    @Path("/v1/admin/handels/handleComponentChange")
    public HandleServiceResponse updateComponentDetail(@QueryParam("fkAssociateId") int fkAssociateId,
                                                       @QueryParam("componentId") String componentId,
                                                       @QueryParam("oldPrice") String oldPrice,
                                                       @DefaultValue("-1") @QueryParam("reqPrice") String updatePrice,
                                                       @DefaultValue("-1") @QueryParam("inStock") String inStock,
                                                       @QueryParam("field") String field){
        HandleServiceResponse handleServiceResponse = new HandleServiceResponse();
        ReportMapper reportMapper = new ReportMapper();
        MailMapper mailMapper = new MailMapper();
        int newPrice = -1;
        try{
            String message = "",componentName = "";
            componentName= SummaryFunctionsUtil.getComponentName(componentId);
            if (!updatePrice.equals("-1")){
                newPrice = new Integer(updatePrice);
                message="Change price of component "+componentName+" from "+oldPrice+" to "+updatePrice+" for vendor id "+fkAssociateId;
            }else if(inStock.equals("1")){
                message="Change status of component "+componentName+" to Instock for vendor id "+fkAssociateId;
            }else if(inStock.equals("0")){
                message="Change status of component "+componentName+" to Out of stock for vendor id "+fkAssociateId;
            }

            boolean result = reportMapper.updateComponentMapper(fkAssociateId,componentId,message,newPrice,inStock,field);
            if(result == false){
                handleServiceResponse.setError(true);
                handleServiceResponse.setErrorCode("ERROR OCCURRED CHANGING COMPONENT INFO");
            }else {
                mailMapper.sendMailToVendor(message,fkAssociateId,"Component details modified - IGP");
            }
            handleServiceResponse.setResult(result);
        }catch (Exception exception){
            logger.error("Error occured at updateComponentDetail ",exception);
        }
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
        try{
            boolean result;
            result = reportMapper.addNewVendorPincodeMapper(fkAssociateId,pincode,cityId,shipType,shipCharge);
            if(result == false){
                handleServiceResponse.setError(true);
                handleServiceResponse.setErrorCode("ERROR OCCURRED ADDING PINCODE");
            }
            handleServiceResponse.setResult(result);
        }catch (Exception exception){
            logger.error("Error occured at addVendorPincode ",exception);
        }
        return handleServiceResponse;
    }

    @GET
    @Path("/v1/admin/handels/getVendorReport")
    public ReportResponse getVendorReport(@QueryParam("fkAssociateId") @DefaultValue("565") String fkAssociateId,
                                          @QueryParam("startLimit") String startLimit,
                                          @QueryParam("endLimit") String endLimit ){

        ReportResponse reportResponse = new ReportResponse();
        ReportMapper reportMapper = new ReportMapper();
        try{
            List<Map.Entry<String,List<String>>> tableDataAction = new ArrayList<>();
            reportResponse.setTableHeaders(new String[]{"Component_Id_Hide","Component Image","Component_Name","Price","InStock"});
            com.igp.handles.vendorpanel.mappers.Reports.ReportMapper.fillDataActionComponent(tableDataAction);
            reportResponse.setTableDataAction(tableDataAction);
            ProductModelListHavingSummaryModel productModelListHavingSummaryModel = reportMapper.getProductSummaryDetails(fkAssociateId,startLimit,endLimit);
            reportResponse.setSummary(productModelListHavingSummaryModel.getSummaryModelList());
            List<Object> objectList = new ArrayList<Object>(productModelListHavingSummaryModel.getProductTableDataModelList());
            reportResponse.setTableData(objectList);
        }catch (Exception exception){
            logger.error("Error occured at getVendorReport ",exception);
        }

        return reportResponse;
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
                ,"pincode","order status","taxable amount","tax","total amount","payment status"});

            orderDateFrom=getTimestampString(orderDateFrom,0);
            orderDeliveryDateFrom=getTimestampString(orderDeliveryDateFrom,2);
            orderDeliveryDateTo=getTimestampString(orderDeliveryDateTo,2);
            if(orderDateFrom!=null&&orderDateTo!=null){
                orderDateTo=getTimestampString(orderDateTo,1);
            }else {
                orderDateTo=getTimestampString(orderDateTo,0);
            }

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
    public HandleServiceResponse addNewVendor(@QueryParam("associateName") String vendorName,
                                              @QueryParam("contactPerson") String contactPerson,
                                              @QueryParam("email") String email,
                                              @QueryParam("address") String address,
                                              @QueryParam("user") String user,
                                              @QueryParam("password") String password,
                                              @QueryParam("phone") String phone,
                                              @DefaultValue("1") @QueryParam("status") int status){
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
    @POST
    @Path("/v1/admin/handels/approveAndReject")
    public HandleServiceResponse approveAndReject(@QueryParam("approveReject") String approveReject, // 1 : approve, 0 : reject
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
            BarcodeReportResponseModel barcodeReportResponseModel = reportMapper.getListOfBarcodesMapper(startLimit,endLimit);
            handleServiceResponse.setResult(barcodeReportResponseModel);
        }catch (Exception exception){
            logger.error("Error occured at getVendorDetails ",exception);
        }
        return handleServiceResponse;
    }

    @GET
    @Path("/v1/admin/handels/getOrderFileUploadReport")
    public ReportResponse getOrderFileUploadReport(@QueryParam("fkAssociateId") String fkAssociateId,
                                                   @QueryParam("orderDateFrom") String startDate,
                                                   @QueryParam("orderDateTo")String endDate ,
                                                   @DefaultValue("0") @QueryParam("startLimit") String startLimit,
                                                   @DefaultValue("10") @QueryParam("endLimit") String endLimit ,
                                                   @QueryParam("orderNumber") Integer orderNo,
                                                   @QueryParam("deliveryDateFrom") String deliveryDateFrom,
                                                   @QueryParam("deliveryDateTo") String deliveryDateTo){

        ReportResponse reportResponse = new ReportResponse();
        ReportMapper reportMapper = new ReportMapper();
        try {

            startDate=getTimestampString(startDate,0);
            deliveryDateFrom=getTimestampString(deliveryDateFrom,0);
            deliveryDateTo=getTimestampString(deliveryDateTo,0);
            if(startDate!=null&&endDate!=null){
                endDate=getTimestampString(endDate,1);
            }else {
                endDate=getTimestampString(endDate,0);
            }

            reportResponse.setTableHeaders(new String[]{"Order_No","Vendor_Name","Date","Delivery_Date"
                ,"Product_Image","Out_Of_Delivery","Proof_Of_Delivery"});
            OrderProductUploadFileReportWithSummary orderProductUploadFileReportWithSummary = reportMapper.getOrderFileUploadReport(fkAssociateId,startDate,endDate,startLimit,endLimit,orderNo,deliveryDateFrom,deliveryDateTo);
            reportResponse.setSummary(orderProductUploadFileReportWithSummary.getSummaryModelList());
            List<Object> objectList = new ArrayList<Object>(orderProductUploadFileReportWithSummary.getOrderProductUploadFileModelList());
            reportResponse.setTableData( objectList);

        }catch (Exception exception){
            logger.error("Error occured at getOrderFileUploadReport ",exception);
        }
        return reportResponse;
    }

    @GET
    @Path("/v1/admin/handels/getSlaReport")
    public ReportResponse getSlaReport(@QueryParam("fkAssociateId") String fkAssociateId,
                                            @QueryParam("assignDateFrom") String assignStartDate,
                                            @QueryParam("assignDateTo")String assignEndDate ,
                                            @DefaultValue("0") @QueryParam("startLimit") String startLimit,
                                            @DefaultValue("10") @QueryParam("endLimit") String endLimit ,
                                            @QueryParam("orderNumber") Integer orderNo,
                                            @QueryParam("deliveryDateFrom") String deliveryDateFrom,
                                            @QueryParam("deliveryDateTo") String deliveryDateTo){
        ReportResponse reportResponse = new ReportResponse();
        ReportMapper reportMapper = new ReportMapper();
        SlaReportWithSummary slaReportWithSummary=null;
        try{
            assignStartDate=getTimestampString(assignStartDate,0);
            deliveryDateFrom=getTimestampString(deliveryDateFrom,0);
            deliveryDateTo=getTimestampString(deliveryDateTo,0);
            if(assignStartDate!=null&&assignEndDate!=null){
                assignEndDate=getTimestampString(assignEndDate,1);
            }else {
                assignEndDate=getTimestampString(assignEndDate,0);
            }
            reportResponse.setTableHeaders(new String[]{"Order_No","Vendor_Name","Status","Assign_Date","Delivery_Date"
                ,"Delivery_Type","Confirm_Time","Sla1","OFD_Time","Sla2","Delivered_Time","Sla3"});
            slaReportWithSummary=reportMapper.getSlaReport(fkAssociateId,assignStartDate,assignEndDate,startLimit,endLimit,orderNo,deliveryDateFrom,deliveryDateTo);
            reportResponse.setSummary(slaReportWithSummary.getSummaryModelList());
            List<Object> objectList = new ArrayList<Object>(slaReportWithSummary.getSlaReportModelList());
            reportResponse.setTableData(objectList);

        }catch (Exception exception){
            logger.error("Error occured at getSlaReport ",exception);
        }

        return reportResponse;
    }

}
