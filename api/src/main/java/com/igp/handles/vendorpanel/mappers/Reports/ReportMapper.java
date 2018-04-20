package com.igp.handles.vendorpanel.mappers.Reports;

/**
 * Created by shal on 22/9/17.
 */

import com.igp.handles.admin.models.Reports.PincodeModelListHavingSummaryModel;
import com.igp.handles.admin.models.Reports.ProductModelListHavingSummaryModel;
import com.igp.handles.admin.utils.Reports.ReportUtil;
import com.igp.handles.vendorpanel.models.Report.*;
import com.igp.handles.vendorpanel.utils.Order.OrderStatusUpdateUtil;
import com.igp.handles.vendorpanel.utils.Reports.PayoutAndTaxesReport;
import com.igp.handles.vendorpanel.utils.Reports.SummaryFunctionsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.igp.handles.vendorpanel.utils.Reports.SummaryFunctionsUtil.*;

public class ReportMapper {

    private static final Logger logger = LoggerFactory.getLogger(ReportMapper.class);


    public  static ReportOrderWithSummaryModel getSummaryDetails(String fkAssociateId, String startDate, String endDate, String startLimit,String endLimit,Integer orderNo,String delhiveryDate,String status,String deliveryDateFrom,String deliveryDateTo){

        ReportOrderWithSummaryModel reportOrderWithSummaryModel=null;
        reportOrderWithSummaryModel=getSummaryDetailsForVendor(fkAssociateId,startDate,endDate,startLimit,endLimit,orderNo,delhiveryDate,status,deliveryDateFrom,deliveryDateTo);
        return reportOrderWithSummaryModel;
    }
    public static ProductModelListHavingSummaryModel getVendorSummaryDetails(String fkAssociateId, String startLimit, String endLimit){

        ProductModelListHavingSummaryModel productModelListHavingSummaryModel=getVendorDetailsFunction(fkAssociateId,startLimit,endLimit);
        return  productModelListHavingSummaryModel;

    }
    public static PincodeModelListHavingSummaryModel getPincodeSummaryDetails(String fkAssociateId, String startLimit, String endLimit){

        PincodeModelListHavingSummaryModel pincodeModelListHavingSummaryModel=getPincodeDetailsFunction(fkAssociateId,startLimit,endLimit);
        return  pincodeModelListHavingSummaryModel;

    }
    public static boolean updateComponentMapper(Integer flag,String fk_associate_id,String  componentId,String price){
        boolean result=updateVendorComponent(flag,fk_associate_id,componentId,price);
        return result;
    }

    public static boolean updatePincodeMapper(Integer flag,String fk_associate_id,String pincode,String shipType,Integer updateStatus,Double updatePrice){
        boolean result=updateVendorPincode(flag,fk_associate_id,pincode,shipType,updateStatus,updatePrice);
        return result;
    }
    public boolean addVendorComponent(String fkAssociateId,String componentCode,String componentName,int type,int price,String ipAddress,String userAgent){
        boolean result = false;
        ReportUtil reportUtil =  new ReportUtil();
        SummaryFunctionsUtil summaryFunctionsUtil = new SummaryFunctionsUtil();
        String message="Need to add new component name :- "+componentCode+" with price :- "+price;
        int compId=summaryFunctionsUtil.addVendorComponent(fkAssociateId,componentCode,price);
        if(compId!=0){
            reportUtil.setVendorGeneralInstruction(new Integer(fkAssociateId),1,compId+"",message,ipAddress,userAgent);
            result = true;
        }
        OrderStatusUpdateUtil.sendEmailToHandelsTeamToTakeAction(0,fkAssociateId,"",message);
        return result;
    }


    public PayoutAndTaxReportSummaryModel getPayoutAndTaxes(int fkAssociateId,int orderId,String orderDateFrom,String orderDateTo,String orderDeliveryDateFrom,
                                                            String orderDeliveryDateTo,String startLimit,String endLimit){
        PayoutAndTaxesReport payoutAndTaxesReport=new PayoutAndTaxesReport();
        PayoutAndTaxReportSummaryModel payoutAndTaxReportSummaryModel=null;
        String vendorName="";
        try{
            payoutAndTaxReportSummaryModel=payoutAndTaxesReport.getPayoutAndTaxes(
                fkAssociateId,orderId,orderDateFrom,orderDateTo,orderDeliveryDateFrom,
                orderDeliveryDateTo,startLimit,endLimit,false,vendorName);
        }catch (Exception exception){
            logger.error("Error in getPayoutAndTaxes ",exception);
        }
        return payoutAndTaxReportSummaryModel;
    }

    public VendorInvoiceModel getInvoicePdfDate(int fkAssociateId,int orderId){
        VendorInvoiceModel vendorInvoiceModel =null;
        PayoutAndTaxesReport payoutAndTaxesReport=new PayoutAndTaxesReport();
        try {
            vendorInvoiceModel=payoutAndTaxesReport.getInvoicePdfDate(fkAssociateId,orderId);
        }catch (Exception exception){
            logger.error("Error in getInvoicePdfDate ",exception);
        }
        return vendorInvoiceModel;
    }

    public boolean addVendorPincode(String fkAssociateId,int pincode,int cityId,int shipType,int shipCharge,String ipAddress,String userAgent){
        ReportUtil reportUtil = new ReportUtil();
        if(shipType==4){
            shipType=1; // all fixed date are standard
        }
        Map<Integer,String> map= new HashMap<>();
        map.put(1,"Standard Delivery");
        map.put(2,"Fixed Time Delivery");
        map.put(3,"Mid Night Delivery");
        String message="Need to add new pincode :- "+pincode+" with shipping type :- "+map.get(shipType)+" and shipping charge :- "+shipCharge;
        boolean result = reportUtil.addNewVendorPincodeUtil(new Integer(fkAssociateId).intValue(),pincode,cityId,shipType,shipCharge,0,message,ipAddress,userAgent);
        OrderStatusUpdateUtil.sendEmailToHandelsTeamToTakeAction(0,fkAssociateId,"",message);
        return result;
    }
    public static void fillDataActionComponent(List<Map.Entry<String,List<String>>> tableDataAction){
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Price",new ArrayList<String>(
            Arrays.asList("Edit"))));
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("InStock",new ArrayList<String>(
            Arrays.asList("InStock/Out of Stock"))));
    }
    public static void fillDataActionpincode(List<Map.Entry<String,List<String>>> tableDataAction){
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Standard Delivery",new ArrayList<String>(
            Arrays.asList("Edit","Enable/Disable"))));
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Fixed Time Delivery",new ArrayList<String>(
            Arrays.asList("Edit","Enable/Disable"))));
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Midnight Delivery",new ArrayList<String>(
            Arrays.asList("Edit","Enable/Disable"))));
    }
}
