package com.igp.handles.endpoints;

import com.igp.handles.mappers.Reports.ReportMapper;
import com.igp.handles.models.Report.PayoutAndTaxReportSummaryModel;
import com.igp.handles.models.Report.PincodeModelListWithSummary;
import com.igp.handles.models.Report.ReportOrderWithSummaryModel;
import com.igp.handles.models.Report.VendorModelListWithSummary;
import com.igp.handles.response.HandleServiceResponse;
import com.igp.handles.response.ReportResponse;
import com.igp.handles.utils.Order.OrderStatusUpdateUtil;
import com.igp.handles.utils.Reports.PayoutAndTaxesReport;
import com.igp.handles.utils.Reports.SummaryFunctionsUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.igp.handles.mappers.Reports.ReportMapper.*;
import static com.igp.handles.utils.Reports.SummaryFunctionsUtil.getTimestampString;


/**
 * Created by shanky on 21/9/17.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class Reports {
    @GET
    @Path("/v1/handels/getOrderReport")
    public ReportResponse getOrderReport(@QueryParam("fkAssociateId") String fkAssociateId, @QueryParam("orderDateFrom") String startDate, @QueryParam("orderDateTo")String endDate , @QueryParam("startLimit") String startLimit, @QueryParam("endLimit") String endLimit , @QueryParam("orderNumber") Integer orderNo,@QueryParam("delhiveryDate") String delhiveryDate,@QueryParam("status")  String status,@QueryParam("deliveryDateFrom") String deliveryDateFrom,@QueryParam("deliveryDateTo") String deliveryDateTo){

        ReportResponse reportResponse=new ReportResponse();
        startDate=getTimestampString(startDate,0);
        endDate=getTimestampString(endDate,1);
        delhiveryDate=getTimestampString(delhiveryDate,0);
        deliveryDateTo=getTimestampString(deliveryDateTo,1);
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
        VendorModelListWithSummary vendorModelListWithSummary=null;
        vendorModelListWithSummary=getVendorSummaryDetails(fkAssociateId,startLimit,endLimit);
        reportResponse.setSummary(vendorModelListWithSummary.getSummaryModelList());
        List<Object> objectList = new ArrayList<Object>(vendorModelListWithSummary.getVendorReportObjectList());
        reportResponse.setTableData(objectList);

        return reportResponse;
    }



    @PUT
    @Path("/v1/handels/handleComponentChange")
    public HandleServiceResponse updateComponentDetail(@QueryParam("fkAssociateId") String fkAssociateId, @QueryParam("componentId") String componentId, @QueryParam("price")String updatePrice , @QueryParam("inStock") Boolean inStock){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        Integer updateflag=0;
        String message="",componentName="";
        componentName= SummaryFunctionsUtil.getComponentName(componentId);
        if (inStock!=null){
            updateflag=1;
            if(inStock==true){
                message="Need to change status of component "+componentName+" to Instock";
            }else {
                message="Need to change status of component "+componentName+" to Out of stock";
            }
        }
        else if (updatePrice!=null){
            updateflag=2;
            message="Need to change price of component "+componentName+" to "+updatePrice;
        }
        boolean result=updateComponentMapper(updateflag,fkAssociateId,componentId);
        OrderStatusUpdateUtil.sendEmailToHandelsTeamToTakeAction(0,fkAssociateId,"",message);
        handleServiceResponse.setResult(result);
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
        PincodeModelListWithSummary pincodeModelListWithSummary=null;
        pincodeModelListWithSummary=getPincodeSummaryDetails(fkAssociateId,startLimit,endLimit);
        reportResponse.setSummary(pincodeModelListWithSummary.getSummaryModelList());
        List<Object> objectList = new ArrayList<Object>(pincodeModelListWithSummary.getPincodeReportModelList());
        reportResponse.setTableData(objectList);

        return reportResponse;
    }


    @PUT
    @Path("/v1/handels/handlePincodeChange")
    public HandleServiceResponse updatePincodeDetail(@QueryParam("fkAssociateId") String fkAssociateId, @QueryParam("pincode") String pincode, @QueryParam("shipCharge")Double updatePrice , @QueryParam("shipType") String shipType,@QueryParam("updateStatus") Integer updateStatus){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        Integer updateflag=0;
        boolean result=true;
        String message="";
        if (updateStatus!=null){
            updateflag=1;
            message="Need to Enable/Disable "+shipType+" for pincode "+pincode;
        }
        else if (updatePrice!=null){
            updateflag=2;
            message="Need to update the price of "+shipType+" for pincode "+pincode+" to "+updatePrice;
        }
//        boolean result=updatePincodetMapper(updateflag,fkAssociateId,pincode,shipType,updateStatus,updatePrice);
        OrderStatusUpdateUtil.sendEmailToHandelsTeamToTakeAction(0,fkAssociateId,"",message);
        handleServiceResponse.setResult(result);
        return handleServiceResponse;
    }

    @POST
    @Path("/v1/handels/addVendorComponent")
    public HandleServiceResponse addVendorComponent(@QueryParam("fkAssociateId") String fkAssociateId,@QueryParam("componentCode") String componentCode,
                                                    @QueryParam("componentName") String componentName,@DefaultValue("0")@QueryParam("type") int type,
                                                    @DefaultValue("0")@QueryParam("price") int price){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        boolean result=false;
        result=ReportMapper.addVendorComponent(fkAssociateId,componentCode,componentName,type, price);
        handleServiceResponse.setResult(result);
        return handleServiceResponse;
    }
    @POST
    @Path("/v1/handels/addVendorPincode")
    public HandleServiceResponse addVendorPincode(@QueryParam("fkAssociateId") String fkAssociateId,@QueryParam("pincode") int pincode,
        @DefaultValue("0")@QueryParam("cityId") int cityId,@QueryParam("shipType") int shipType,@QueryParam("shipCharge")int shipCharge
        ){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        boolean result=false;
        result=ReportMapper.addVendorPincode(fkAssociateId,pincode,cityId,shipType,shipCharge);
        handleServiceResponse.setResult(result);
        return handleServiceResponse;
    }

    @GET
    @Path("/v1/handels/getPayoutAndTaxesReport")
    public ReportResponse getPayoutAndTaxes(@QueryParam("fkAssociateId") int fkAssociateId, @QueryParam("orderId")int orderId,
                                        @QueryParam("orderDateFrom") String orderDateFrom, @QueryParam("orderDateTo")String orderDateTo,
                                        @QueryParam("startLimit") String startLimit, @QueryParam("endLimit") String endLimit ){
        ReportResponse reportResponse=new ReportResponse();
        PayoutAndTaxesReport payoutAndTaxesReport=new PayoutAndTaxesReport();
        try{
            reportResponse.setTableHeaders(new String[]{"orderId","date purchased","pincode","delivery date","invoice number","taxable amount"
                ,"tax","total amount","payment status"});

            PayoutAndTaxReportSummaryModel payoutAndTaxReportSummaryModel=payoutAndTaxesReport.getPayoutAndTaxes(fkAssociateId,
                                                                        orderId,orderDateFrom,orderDateTo,startLimit,endLimit);
            reportResponse.setSummary(payoutAndTaxReportSummaryModel.getSummaryModelList());
            List<Object> objectList = new ArrayList<Object>(payoutAndTaxReportSummaryModel.getOrderTaxReportList());
            reportResponse.setTableData(objectList);

        }catch (Exception exception){

        }

        return reportResponse;
    }

}
