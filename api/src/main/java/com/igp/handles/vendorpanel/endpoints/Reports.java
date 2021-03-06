package com.igp.handles.vendorpanel.endpoints;

import com.igp.handles.admin.models.Reports.PincodeModelListHavingSummaryModel;
import com.igp.handles.admin.models.Reports.ProductModelListHavingSummaryModel;
import com.igp.handles.admin.models.Reports.TableDataActionHandels;
import com.igp.handles.admin.utils.Reports.ReportUtil;
import com.igp.handles.vendorpanel.mappers.Reports.ReportMapper;
import com.igp.handles.vendorpanel.models.Report.PayoutAndTaxReportSummaryModel;
import com.igp.handles.vendorpanel.models.Report.ReportOrderWithSummaryModel;
import com.igp.handles.vendorpanel.response.HandleServiceResponse;
import com.igp.handles.vendorpanel.response.ReportResponse;
import com.igp.handles.vendorpanel.utils.Order.OrderStatusUpdateUtil;
import com.igp.handles.vendorpanel.utils.Reports.SummaryFunctionsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.igp.handles.vendorpanel.mappers.Reports.ReportMapper.*;
import static com.igp.handles.vendorpanel.utils.Reports.SummaryFunctionsUtil.getTimestampString;


/**
 * Created by shanky on 21/9/17.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class Reports {

    private static final Logger logger = LoggerFactory.getLogger(Reports.class);

    @GET
    @Path("/v1/handels/getOrderReport")
    public ReportResponse getOrderReport(@QueryParam("fkAssociateId") String fkAssociateId,
                                         @QueryParam("orderDateFrom") String startDate, @QueryParam("orderDateTo")String endDate ,
                                         @QueryParam("startLimit") String startLimit, @QueryParam("endLimit") String endLimit ,
                                         @QueryParam("orderNumber") Integer orderNo,@QueryParam("delhiveryDate") String delhiveryDate,
                                         @QueryParam("status")  String status,@QueryParam("deliveryDateFrom") String deliveryDateFrom,
                                         @QueryParam("deliveryDateTo") String deliveryDateTo){

        ReportResponse reportResponse=new ReportResponse();
        startDate=getTimestampString(startDate,0);
        endDate=getTimestampString(endDate,0);
        delhiveryDate=getTimestampString(delhiveryDate,0);
        deliveryDateTo=getTimestampString(deliveryDateTo,0);
        deliveryDateFrom=getTimestampString(deliveryDateFrom,0);

        reportResponse.setTableHeaders(new String[]{"Order_No","Date","Occasion","City","Pincode","Delivery_Date"
            ,"Delivery_Type","Recipient_Name","Phone","Amount","Status"});
        ReportOrderWithSummaryModel reportOrderWithSummaryModel1= null;
        reportOrderWithSummaryModel1=getSummaryDetails(fkAssociateId,startDate,endDate,startLimit,endLimit,orderNo,delhiveryDate,status,deliveryDateFrom,deliveryDateTo);
        reportResponse.setSummary(reportOrderWithSummaryModel1.getSummaryModelList());
        List<Object> objectList = new ArrayList<Object>(reportOrderWithSummaryModel1.getOrderReportObjectModelList());
        reportResponse.setTableData( objectList);
        return reportResponse;
    }

    @GET
    @Path("/v1/handels/getVendorReport")
    public ReportResponse getVendorReport(@QueryParam("fkAssociateId") String fkAssociateId,@QueryParam("startLimit") String startLimit, @QueryParam("endLimit") String endLimit ){

        ReportResponse reportResponse=new ReportResponse();
        List<Map.Entry<String,List<String>>> tableDataAction=new ArrayList<>();
        reportResponse.setTableHeaders(new String[]{"Component_Id_Hide","Component Image","Component_Name","Price","InStock"});

        ReportMapper.fillDataActionComponent(tableDataAction);
        reportResponse.setTableDataAction(tableDataAction);
        ProductModelListHavingSummaryModel productModelListHavingSummaryModel = getVendorSummaryDetails(fkAssociateId,startLimit,endLimit);
        reportResponse.setSummary(productModelListHavingSummaryModel.getSummaryModelList());
        List<Object> objectList = new ArrayList<Object>(productModelListHavingSummaryModel.getProductTableDataModelList());
        reportResponse.setTableData(objectList);

        return reportResponse;
    }



    @PUT
    @Path("/v1/handels/handleComponentChange")
    public HandleServiceResponse updateComponentDetail(@Context HttpServletRequest request,
                                                       @QueryParam("fkAssociateId") String fkAssociateId,
                                                       @QueryParam("componentId") String componentId,
                                                       @QueryParam("oldPrice") String oldPrice,
                                                       @QueryParam("reqPrice") String reqPrice,
                                                       @QueryParam("inStock") String inStock){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        ReportUtil reportUtil = new ReportUtil();
        Integer updateflag=0;
        String message="",componentName="";
        TableDataActionHandels  tableDataActionHandels = new TableDataActionHandels();
        Map<String,TableDataActionHandels> response = new HashMap<>();
        componentName= SummaryFunctionsUtil.getComponentName(componentId);
        if (inStock!=null){
            if(inStock.equals("1")){
                updateflag=1;
                message="Need to change status of component "+componentName+" to In stock for vendor id "+fkAssociateId;
                tableDataActionHandels.setValue("Out of Stock");
                tableDataActionHandels.setRequestType("");
                tableDataActionHandels.setRequestValue("In Stock");
            }else {
                updateflag=2;
                message="Need to change status of component "+componentName+" to Out of stock for vendor id "+fkAssociateId;
                tableDataActionHandels.setValue("Out of Stock");
                tableDataActionHandels.setRequestType("InStock");
                tableDataActionHandels.setRequestValue("Out of Stock");
            }
        }
        else if(reqPrice!=null){
            updateflag=3;
            message="Need to change price of component "+componentName+" from "+oldPrice+" to "+oldPrice+" for vendor id "+fkAssociateId;
            tableDataActionHandels.setValue(oldPrice);
            tableDataActionHandels.setRequestType("");
            tableDataActionHandels.setRequestValue(reqPrice);
        }
        boolean result=updateComponentMapper(updateflag,fkAssociateId,componentId,reqPrice);
        if(result == true && updateflag == 3){
            response.put("Price",tableDataActionHandels);
        }
        else if(result == true && (updateflag == 1 || updateflag == 2)){
            response.put("InStock",tableDataActionHandels);
        }else{
            handleServiceResponse.setError(true);
        }
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        reportUtil.setVendorGeneralInstruction(new Integer(fkAssociateId),1,componentId+"",message,ipAddress,userAgent);
        OrderStatusUpdateUtil.sendEmailToHandelsTeamToTakeAction(0,fkAssociateId,"",message);
        handleServiceResponse.setResult(response);
        return handleServiceResponse;
    }


    @GET
    @Path("/v1/handels/getPincodeReport")
    public ReportResponse getPincodeReport(@QueryParam("fkAssociateId") String fkAssociateId,@QueryParam("startLimit") String startLimit, @QueryParam("endLimit") String endLimit ){
        ReportResponse reportResponse=new ReportResponse();
        List<Map.Entry<String,List<String>>> tableDataAction=new ArrayList<>();

        reportResponse.setTableHeaders(new String[]{"Pincode","Standard Delivery","Fixed Time Delivery","Midnight Delivery"});
        ReportMapper.fillDataActionpincode(tableDataAction);
        reportResponse.setTableDataAction(tableDataAction);
        PincodeModelListHavingSummaryModel pincodeModelListHavingSummaryModel=getPincodeSummaryDetails(fkAssociateId,startLimit,endLimit);
        reportResponse.setSummary(pincodeModelListHavingSummaryModel.getSummaryModelList());
        List<Object> objectList = new ArrayList<Object>(pincodeModelListHavingSummaryModel.getPincodeTableDataModelList());
        reportResponse.setTableData(objectList);

        return reportResponse;
    }


    @PUT
    @Path("/v1/handels/handlePincodeChange")
    public HandleServiceResponse updatePincodeDetail(@Context HttpServletRequest request,
                                                     @QueryParam("fkAssociateId") String fkAssociateId,
                                                     @QueryParam("pincode") String pincode,
                                                     @QueryParam("shipCharge") Double oldPrice ,
                                                     @QueryParam("reqPrice") Double reqPrice,
                                                     @QueryParam("shipType") String shipType,
                                                     @QueryParam("updateStatus") Integer updateStatus){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        ReportUtil reportUtil = new ReportUtil();
        Integer updateflag=0;
        String message="";
        TableDataActionHandels  tableDataActionHandels = new TableDataActionHandels();
        Map<String,TableDataActionHandels> response = new HashMap<>();
        if (updateStatus!=null){
            if(updateStatus==1){
                updateflag=1;
                message="Need to Enable "+shipType+" for pincode "+pincode+" for vendor id "+fkAssociateId;
                tableDataActionHandels.setValue("Not Serviceable");
                tableDataActionHandels.setRequestType("");
                tableDataActionHandels.setRequestValue("Not Serviceable");
            }else {
                updateflag=2;
                message="Need to Disable "+shipType+" for pincode "+pincode+" for vendor id "+fkAssociateId;
                tableDataActionHandels.setValue("Not Serviceable");
                tableDataActionHandels.setRequestType("Enable");
                tableDataActionHandels.setRequestValue(oldPrice+"");
            }
        }
        else if (reqPrice!=null){
            updateflag=3;
            message="Need to update the price of "+shipType+" for pincode "+pincode+" from "+oldPrice+" to "+reqPrice+" for vendor id "+fkAssociateId;
            tableDataActionHandels.setValue(oldPrice+"");
            tableDataActionHandels.setRequestType("");
            tableDataActionHandels.setRequestValue(reqPrice+"");
        }
        boolean result=updatePincodeMapper(updateflag,fkAssociateId,pincode,shipType,updateStatus,reqPrice);
        if(result == true && updateflag == 3){
            response.put("Price",tableDataActionHandels);
        }
        else if(result == true && (updateflag == 1 || updateflag == 2)){
            response.put(shipType,tableDataActionHandels);
        }else {
            handleServiceResponse.setError(true);
        }
        String ipAddress=request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        reportUtil.setVendorGeneralInstruction(new Integer(fkAssociateId),0,pincode+"",message,ipAddress,userAgent);
        OrderStatusUpdateUtil.sendEmailToHandelsTeamToTakeAction(0,fkAssociateId,"",message);
        handleServiceResponse.setResult(result);
        return handleServiceResponse;
    }

    @POST
    @Path("/v1/handels/addVendorComponent")
    public HandleServiceResponse addVendorComponent(@Context HttpServletRequest request,
                                                    @QueryParam("fkAssociateId") String fkAssociateId,
                                                    @QueryParam("componentCode") String componentCode,
                                                    @QueryParam("componentName") String componentName,
                                                    @DefaultValue("0")@QueryParam("type") int type,
                                                    @DefaultValue("0")@QueryParam("price") int price){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        ReportMapper reportMapper = new ReportMapper();
        String ipAddress=request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        boolean result=reportMapper.addVendorComponent(fkAssociateId,componentCode,componentName,type, price,ipAddress,userAgent);
        if(result == false){
            handleServiceResponse.setError(true);
            handleServiceResponse.setErrorCode("ERROR OCCURRED ADDING COMPONENT");
        }
        handleServiceResponse.setResult(result);
        return handleServiceResponse;
    }
    @POST
    @Path("/v1/handels/addVendorPincode")
    public HandleServiceResponse addVendorPincode(@Context HttpServletRequest request,
                                                  @QueryParam("fkAssociateId") String fkAssociateId,
                                                  @QueryParam("pincode") int pincode,
                                                  @DefaultValue("0")@QueryParam("cityId") int cityId,
                                                  @QueryParam("shipType") int shipType,
                                                  @QueryParam("shipCharge")int shipCharge){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        ReportMapper reportMapper = new ReportMapper();
        String ipAddress=request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        boolean result = reportMapper.addVendorPincode(fkAssociateId,pincode,cityId,shipType,shipCharge,ipAddress,userAgent);
        if(result == false){
            handleServiceResponse.setError(true);
            handleServiceResponse.setErrorCode("ERROR OCCURRED ADDING PINCODE");
        }
        handleServiceResponse.setResult(result);
        return handleServiceResponse;
    }

    @GET
    @Path("/v1/handels/getPayoutAndTaxesReport")
    public ReportResponse getPayoutAndTaxes(@QueryParam("fkAssociateId") int fkAssociateId, @QueryParam("orderNumber")int orderId,
                                            @QueryParam("orderDateFrom") String orderDateFrom, @QueryParam("orderDateTo")String orderDateTo,
                                            @QueryParam("deliveryDateFrom") String orderDeliveryDateFrom,@QueryParam("deliveryDateTo") String orderDeliveryDateTo,
                                            @QueryParam("startLimit") String startLimit, @QueryParam("endLimit") String endLimit ){
        ReportResponse reportResponse=new ReportResponse();
        ReportMapper reportMapper=new ReportMapper();

        try{
            reportResponse.setTableHeaders(new String[]{"invoice number","orderId","date purchased","delivery date"
                ,"pincode","order status","taxable amount","tax","total amount","payment status"});


            orderDateFrom=getTimestampString(orderDateFrom,0);
            orderDeliveryDateFrom=getTimestampString(orderDeliveryDateFrom,2);
            orderDeliveryDateTo=getTimestampString(orderDeliveryDateTo,2);

            if(orderDateFrom!=null&&orderDateTo!=null){
                orderDateTo=getTimestampString(orderDateTo,1);
            }else {
                orderDateTo=getTimestampString(orderDateTo,0);
            }

            PayoutAndTaxReportSummaryModel payoutAndTaxReportSummaryModel=reportMapper.getPayoutAndTaxes(fkAssociateId,
                orderId,orderDateFrom,orderDateTo,orderDeliveryDateFrom,
                orderDeliveryDateTo,startLimit,endLimit);
            reportResponse.setSummary(payoutAndTaxReportSummaryModel.getSummaryModelList());
            List<Object> objectList = new ArrayList<Object>(payoutAndTaxReportSummaryModel.getOrderTaxReportList());
            reportResponse.setTableData(objectList);

        }catch (Exception exception){
            logger.error("Error occured at getPayoutAndTaxes ",exception);
        }
        return reportResponse;
    }
    @GET
    @Path("/v1/handels/getInvoicePdfData")
    public HandleServiceResponse getInvoicePdfDate(@QueryParam("fkAssociateId") int fkAssociateId, @QueryParam("orderId")int orderId){

        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        ReportMapper reportMapper=new ReportMapper();
        try {
            handleServiceResponse.setResult(reportMapper.getInvoicePdfDate(fkAssociateId,orderId));
        }catch (Exception exception){
            logger.error("Error occured at getInvoicePdfDate ",exception);
        }
        return handleServiceResponse;
    }

}
