package com.igp.handles.admin.mappers.Reports;

import com.igp.handles.admin.models.Reports.*;
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

    public  ReportOrderWithSummaryModel getOrderReportMapper(String fkAssociateId, String startDate, String endDate, String startLimit, String endLimit, Integer orderNo, String status, String deliveryDateFrom, String deliveryDateTo){
        ReportUtil reportUtil = new ReportUtil();
        ReportOrderWithSummaryModel reportOrderWithSummaryModel=null;
        reportOrderWithSummaryModel=reportUtil.getOrders(fkAssociateId,startDate,endDate,startLimit,endLimit,orderNo,status,deliveryDateFrom,deliveryDateTo);
        return reportOrderWithSummaryModel;
    }

    public static PincodeModelListHavingSummaryModel getPincodeSummaryDetails(String fkAssociateId, String startLimit, String endLimit){

        PincodeModelListHavingSummaryModel pincodeModelListHavingSummaryModel =null;
        pincodeModelListHavingSummaryModel = ReportUtil.getPincodeDetailsFunctionAdmin(fkAssociateId,startLimit,endLimit);
        return pincodeModelListHavingSummaryModel;
    }

    public boolean updatePincodeMapper(Integer flag,int fk_associate_id,int pincode,int shipType,int updatePrice, String message, String field){
        ReportUtil reportUtil = new ReportUtil();
        boolean response=false;
        int result = reportUtil.updateVendorPincode(flag,fk_associate_id,pincode,shipType,updatePrice,field);
        if(result==1){
            response = reportUtil.setVendorGeneralInstruction(fk_associate_id,0,pincode+"",message);
        }
        return response;
    }

    public boolean addNewVendorPincodeMapper(int fkAssociateId,int pincode,int cityId,int shipType,int shipCharge){
        Map<Integer,String> map= new HashMap<>();
        ReportUtil reportUtil = new ReportUtil();
        if(shipType==4){
            shipType = 1;
        }
        map.put(1,"Standard Delivery");
        map.put(2,"Fixed Time Delivery");
        map.put(3,"Mid Night Delivery");
        boolean result,response=false;
        String message="Added new pincode :- "+pincode+" with shipping type :- "+map.get(shipType)+" and shipping charge :- "+shipCharge+" : ";
        result = reportUtil.addNewVendorPincodeUtil(fkAssociateId,pincode,cityId,shipType,shipCharge,1);
        if(result==true){
            response = reportUtil.setVendorGeneralInstruction(fkAssociateId,0,pincode+"",message);
        }
        return response;
    }

    public boolean addNewComponentMapper(int fkAssociateId,String componentCode,String componentName,int type,int price){
        boolean result,response=false;
        ReportUtil reportUtil =  new ReportUtil();
        String message="Added new Component : Name :- "+componentName+" With Price :- "+price+" : ";
        result = reportUtil.addVendorComponent(String.valueOf(fkAssociateId),componentCode,componentName,type,"dummy.jpg",price);
        if(result==true){
            response = reportUtil.setVendorGeneralInstruction(fkAssociateId,1,componentCode,message);
        }
        return response;
    }
    public ProductModelListHavingSummaryModel getProductSummaryDetails(String fkAssociateId, String startLimit, String endLimit){
        ReportUtil reportUtil = new ReportUtil();
        ProductModelListHavingSummaryModel productModelListHavingSummaryModel = reportUtil.getProductDetail(fkAssociateId,startLimit,endLimit);
        return productModelListHavingSummaryModel;
    }

    public boolean updateComponentMapper(int fkAssociateId,String componentId, String message, int updatePrice,String inStock, String field){
        ReportUtil reportUtil = new ReportUtil();
        boolean response = false;
        boolean result=reportUtil.updateProductComponent(fkAssociateId,componentId,updatePrice,inStock,field);
        if(result==true){
            response = reportUtil.setVendorGeneralInstruction(fkAssociateId,1,componentId,message);
        }
        return response;
    }
    public PayoutAndTaxReportSummaryModel getPayoutAndTaxes(int fkAssociateId,int orderId,String orderDateFrom,String orderDateTo,String orderDeliveryDateFrom,
                                                            String orderDeliveryDateTo,String startLimit,String endLimit){
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
    public boolean modifyVendorDetails(int fkAssociateId,String vendorName,String contactPerson,String email,
                                       String address,String phone,String userId, String password,int status){
        boolean result = false;
        int response;
        ReportUtil reportUtil = new ReportUtil();
        try{
            response = reportUtil.modifyVendorDetails(fkAssociateId,vendorName,contactPerson,email,address,phone,userId,password,status);
            if (response==1){
                result=true;
            }
        }catch (Exception exception){
            logger.error("Error at modifyVendorDetails in ReportMapper ",exception);
        }
        return result;
    }
    public boolean addNewVendorMapper(String vendorName,String user, String password,
                                      String contactPerson,String email,
                                      String address,String phone,int status){
        boolean result = false;
        int response;
        ReportUtil reportUtil = new ReportUtil();
        try{
            response = reportUtil.addNewVendorUtil(vendorName,user,password,contactPerson,email,address,phone,status);
            if (response==1){
                result=true;
            }
        }catch (Exception exception){
            logger.error("Error at modifyVendorDetails in ReportMapper ",exception);
        }
        return result;
    }
    public boolean approveAndRejectMapper(String object, String reportName, String columnName, int fkAssociateId, String approveAndReject){
        ReportUtil reportUtil = new ReportUtil();
        boolean response = false;
        try{
            boolean approveReject = false;
            if(approveAndReject.equals("1")){
                approveReject = true;
            }
            response =  reportUtil.approveAndRejectUtil(object,reportName,columnName,fkAssociateId,approveReject);

        }catch (Exception exception){
            logger.error("Error at modifyVendorDetails in ReportMapper ",exception);
        }
        return response;
    }
    public BarcodeToComponentListHavingSummary getBarcodeToComponentsMapper(String productCode, int startLimit, int endLimit) {
        ReportUtil reportUtil = new ReportUtil();
        BarcodeToComponentListHavingSummary barcodeToComponentListHavingSummary = reportUtil.getBarcodeToComponentsUtil(productCode,startLimit,endLimit);
        return barcodeToComponentListHavingSummary;
    }
    public boolean deleteBarcodeMapper(String productCode){
        ReportUtil reportUtil = new ReportUtil();
        boolean response = false;
        try{
            response =  reportUtil.deleteBarcode(productCode);
        }catch (Exception exception){
            logger.error("Error at deleteBarcodeMapper in ReportMapper ",exception);
        }
        return response;
    }
    public boolean changeBarcodeComponentMapper(String productCode, String componentCode,int quantity){
        ReportUtil reportUtil = new ReportUtil();
        boolean response = false;
        try{
            response =  reportUtil.changeBarcodeComponentUtil(productCode,componentCode,quantity);
        }catch (Exception exception){
            logger.error("Error at changeBarcodeComponentMapper in ReportMapper ",exception);
        }
        return response;
    }
    public BarcodeReportResponseModel getListOfBarcodesMapper(int startLimit, int endLimit) {
        ReportUtil reportUtil = new ReportUtil();
        BarcodeReportResponseModel barcodeReportResponseModel = reportUtil.getListOfBarcodesUtil(startLimit,endLimit);
        return barcodeReportResponseModel;
    }

    public OrderProductUploadFileReportWithSummary getOrderFileUploadReport(String fkAssociateId, String startDate,
        String endDate, String startLimit, String endLimit, Integer orderNo, String deliveryDateFrom,
        String deliveryDateTo){
        ReportUtil reportUtil=new ReportUtil();
        OrderProductUploadFileReportWithSummary orderProductUploadFileReportWithSummary=null;
        try {
            orderProductUploadFileReportWithSummary=reportUtil.uploadPicReport(fkAssociateId,startDate,endDate,startLimit,endLimit,orderNo,deliveryDateFrom,deliveryDateTo);
        }catch (Exception exception){
            logger.error("Error occured when getOrderFileUploadReport ",exception);
        }
        return orderProductUploadFileReportWithSummary;
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
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Vendor_Name",new ArrayList<String>(
            Arrays.asList("Edit"))));
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Contact_Person",new ArrayList<String>(
            Arrays.asList("Edit"))));
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Email",new ArrayList<String>(
            Arrays.asList("Edit"))));
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Address",new ArrayList<String>(
            Arrays.asList("Edit"))));
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Phone",new ArrayList<String>(
            Arrays.asList("Edit"))));
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Status",new ArrayList<String>(
            Arrays.asList("Edit"))));
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("User_Id",new ArrayList<String>(
            Arrays.asList("Edit"))));
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Password",new ArrayList<String>(
            Arrays.asList("Edit"))));
    }
    public void fillDataActionBarcode(List<Map.Entry<String,List<String>>> tableDataAction){
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Product_Code",new ArrayList<String>(
            Arrays.asList("Delete"))));
        tableDataAction.add(new AbstractMap.SimpleEntry<String, List<String>>("Quantity",new ArrayList<String>(
            Arrays.asList("Edit"))));
    }

}
