package com.igp.handles.admin.mappers.Reports;

import com.igp.handles.admin.models.Reports.PincodeModelListHavingSummaryModel;
import com.igp.handles.admin.models.Reports.ProductModelListHavingSummaryModel;
import com.igp.handles.admin.models.Reports.VendorDetailsHavingSummaryModel;
import com.igp.handles.admin.models.Vendor.VendorInfoModel;
import com.igp.handles.admin.utils.Reports.ReportUtil;
import com.igp.handles.admin.utils.Vendor.VendorUtil;
import com.igp.handles.vendorpanel.models.Report.PayoutAndTaxReportSummaryModel;
import com.igp.handles.vendorpanel.models.Report.ReportOrderWithSummaryModel;
import com.igp.handles.vendorpanel.utils.Reports.PayoutAndTaxesReport;
import com.igp.handles.vendorpanel.utils.Reports.SummaryFunctionsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by suditi on 30/1/18.
 */
public class ReportMapper {

    private static final Logger logger = LoggerFactory.getLogger(ReportMapper.class);

    public  ReportOrderWithSummaryModel getOrderReportMapper(String fkAssociateId, String startDate, String endDate, String startLimit, String endLimit, Integer orderNo, String delhiveryDate, String status, String deliveryDateFrom, String deliveryDateTo){
            ReportUtil reportUtil = new ReportUtil();
        ReportOrderWithSummaryModel reportOrderWithSummaryModel=null;
        reportOrderWithSummaryModel=reportUtil.getOrders(fkAssociateId,startDate,endDate,startLimit,endLimit,orderNo,delhiveryDate,status,deliveryDateFrom,deliveryDateTo);
        return reportOrderWithSummaryModel;
    }

    public static PincodeModelListHavingSummaryModel getPincodeSummaryDetails(String fkAssociateId, String startLimit, String endLimit){

        PincodeModelListHavingSummaryModel pincodeModelListHavingSummaryModel =null;
        pincodeModelListHavingSummaryModel = ReportUtil.getPincodeDetailsFunctionAdmin(fkAssociateId,startLimit,endLimit);
        return pincodeModelListHavingSummaryModel;
    }

    public int updatePincodeMapper(Integer flag,int fk_associate_id,String pincode,Integer shipType,Integer updateStatus,Integer updatePrice, String message){
        ReportUtil reportUtil = new ReportUtil();
        int result = reportUtil.updateVendorPincode(flag,fk_associate_id+"",pincode,shipType,updateStatus,updatePrice);
        if(result==1){
            result = reportUtil.setVendorGeneralInstruction(fk_associate_id,0,pincode,message+"Done");
        }
        return result;
    }

    public boolean addNewVendorPincodeMapper(int fkAssociateId,int pincode,int cityId,int shipType,int shipCharge){
        Map<Integer,String> map= new HashMap<>();
        ReportUtil reportUtil = new ReportUtil();
        map.put(1,"Standard Delivery");
        map.put(2,"Fixed Time Delivery");
        map.put(3,"Mid Night Delivery");
        map.put(4,"Same Day Delivery");
        boolean result=true;
        String message="Added new pincode :- "+pincode+" with shipping type :- "+map.get(shipType)+" and shipping charge :- "+shipCharge+" : ";
        result = reportUtil.addNewVendorPincodeUtil(fkAssociateId,pincode,cityId,shipType,shipCharge);
        if(result==true){
            int response = reportUtil.setVendorGeneralInstruction(fkAssociateId,0,pincode+"",message+"Done");
        }
        return result;
    }

    public boolean addNewComponentMapper(int fkAssociateId,String componentCode,String componentName,int type,int price){
        boolean result=true;
        ReportUtil reportUtil =  new ReportUtil();
        String message="Added new Component : Name :- "+componentName+" With Price :- "+price+" : ";
        result = SummaryFunctionsUtil.addVendorComponent(fkAssociateId+"",componentCode,componentName,type,"dummy.jpg",price);
        if(result==true){
            int response = reportUtil.setVendorGeneralInstruction(fkAssociateId,1,componentCode,message+"Done");
        }
        return result;
    }
    public ProductModelListHavingSummaryModel getProductSummaryDetails(String fkAssociateId, String startLimit, String endLimit){
        ReportUtil reportUtil = new ReportUtil();
        ProductModelListHavingSummaryModel productModelListHavingSummaryModel =null;
        productModelListHavingSummaryModel =reportUtil.getProductDetail(fkAssociateId,startLimit,endLimit);
        return productModelListHavingSummaryModel;
    }

    public boolean updateComponentMapper(Integer flag,int fk_associate_id,String  componentId, String message, int updatePrice,String inStock){
        ReportUtil reportUtil = new ReportUtil();
        boolean result=reportUtil.updateProductComponent(flag,fk_associate_id,componentId,updatePrice,inStock);
        if(result==true){
            int response = reportUtil.setVendorGeneralInstruction(fk_associate_id,1,componentId,message+"Done");
        }
        return result;
    }
    public PayoutAndTaxReportSummaryModel getPayoutAndTaxes(int fkAssociateId,int orderId,String orderDateFrom,String orderDeliveryDateFrom,
        String orderDeliveryDateTo,String orderDateTo,String startLimit,String endLimit){
        PayoutAndTaxesReport payoutAndTaxesReport=new PayoutAndTaxesReport();
        PayoutAndTaxReportSummaryModel payoutAndTaxReportSummaryModel=null;
        VendorUtil vendorUtil=new VendorUtil();
        String vendorName="";
        try{
            vendorName=vendorUtil.getVendorInfo(fkAssociateId).getAssociateName();
            payoutAndTaxReportSummaryModel=payoutAndTaxesReport.getPayoutAndTaxes(
                fkAssociateId,orderId,orderDateFrom,orderDateTo,orderDeliveryDateFrom,
                orderDeliveryDateTo,startLimit,endLimit,true,vendorName);
        }catch (Exception exception){
            logger.error("Error in getPayoutAndTaxes ",exception);
        }
        return payoutAndTaxReportSummaryModel;
    }
    public VendorDetailsHavingSummaryModel getVendorDetails(int fkAssociateId,int startLimit, int endLimit){
        VendorDetailsHavingSummaryModel vendorDetailsHavingSummaryModel = new VendorDetailsHavingSummaryModel();
        ReportUtil reportUtil = new ReportUtil();
        try{
            vendorDetailsHavingSummaryModel = reportUtil.getVendorDetails(fkAssociateId,startLimit,endLimit);
        }catch (Exception exception){
            logger.error("Error at getVendorDetails in ReportMapper ",exception);
        }
        return vendorDetailsHavingSummaryModel;
    }
    public boolean modifyVendorDetails(int fkAssociateId,String associateName,String contactPerson,String email,
                       String address,String phone,String userId, String password,int status){
        boolean result = false;
        int response;
        ReportUtil reportUtil = new ReportUtil();
        try{
            response = reportUtil.modifyVendorDetails(fkAssociateId,associateName,contactPerson,email,address,phone,userId,password,status);
       if (response==1){
           result=true;
       }
        }catch (Exception exception){
            logger.error("Error at modifyVendorDetails in ReportMapper ",exception);
        }
    return result;
    }
    public boolean addNewVendorMapper(String associateName,String user, String password,
                                      String contactPerson,String email,
                                       String address,String phone,int status){
        boolean result = false;
        int response;
        ReportUtil reportUtil = new ReportUtil();
        try{
            response = reportUtil.addNewVendorUtil(associateName,user,password,contactPerson,email,address,phone,status);
            if (response==1){
                result=true;
            }
        }catch (Exception exception){
            logger.error("Error at modifyVendorDetails in ReportMapper ",exception);
        }
        return result;
    }
    public void fillDataActionPincode(List<Map.Entry<String,List<String>>> tableDataAction){
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Standard Delivery",new ArrayList<String>(
            Arrays.asList("Edit","Enable/Disable"))));
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Fixed Time Delivery",new ArrayList<String>(
            Arrays.asList("Edit","Enable/Disable"))));
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Midnight Delivery",new ArrayList<String>(
            Arrays.asList("Edit","Enable/Disable"))));
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Pincode",new ArrayList<String>(
            Arrays.asList("Enable/Disable"))));
    }
    public void fillDataActionVendor(List<Map.Entry<String,List<String>>> tableDataAction){
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Vendor Id",new ArrayList<String>(
            Arrays.asList("Edit"))));
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Vendor Name",new ArrayList<String>(
            Arrays.asList("Edit"))));
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Contact Person",new ArrayList<String>(
            Arrays.asList("Edit"))));
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Email",new ArrayList<String>(
            Arrays.asList("Edit"))));
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Address",new ArrayList<String>(
            Arrays.asList("Edit"))));
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Phone",new ArrayList<String>(
            Arrays.asList("Edit"))));
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Status",new ArrayList<String>(
            Arrays.asList("Edit"))));
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("User Id",new ArrayList<String>(
            Arrays.asList("Edit"))));
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Password",new ArrayList<String>(
            Arrays.asList("Edit"))));
    }
}
