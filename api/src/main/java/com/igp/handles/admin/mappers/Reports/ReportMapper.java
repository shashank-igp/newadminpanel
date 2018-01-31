package com.igp.handles.admin.mappers.Reports;

import com.igp.handles.admin.models.Reports.PincodeModelListHavingSummary;
import com.igp.handles.admin.models.Reports.ProductModelListHavingSummary;
import com.igp.handles.admin.utils.Reports.ReportUtil;
import com.igp.handles.admin.utils.Vendor.VendorUtil;
import com.igp.handles.vendorpanel.models.Report.PayoutAndTaxReportSummaryModel;
import com.igp.handles.vendorpanel.utils.Reports.PayoutAndTaxesReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by suditi on 30/1/18.
 */
public class ReportMapper {

    private static final Logger logger = LoggerFactory.getLogger(ReportMapper.class);

    public static PincodeModelListHavingSummary getPincodeSummaryDetails(String fkAssociateId, String startLimit, String endLimit){

        PincodeModelListHavingSummary pincodeModelListHavingSummary=null;
        pincodeModelListHavingSummary= ReportUtil.getPincodeDetailsFunctionAdmin(fkAssociateId,startLimit,endLimit);
        return  pincodeModelListHavingSummary;
    }

    public int updatePincodeMapper(Integer flag,String fk_associate_id,String pincode,Integer shipType,Integer updateStatus,Integer updatePrice, String message){
        ReportUtil reportUtil = new ReportUtil();
        int result = reportUtil.updateVendorPincode(flag,fk_associate_id,pincode,shipType,updateStatus,updatePrice);
        if(result==1){
            result = reportUtil.setVendorGeneralInstruction(fk_associate_id,0,pincode,message+"Done");
        }
        return result;
    }
    public ProductModelListHavingSummary getProductSummaryDetails(String fkAssociateId, String startLimit, String endLimit){
        ReportUtil reportUtil = new ReportUtil();
        ProductModelListHavingSummary productModelListHavingSummary=null;
        productModelListHavingSummary=reportUtil.getProductDetail(fkAssociateId,startLimit,endLimit);
        return  productModelListHavingSummary;
    }

    public int updateComponentMapper(Integer flag,String fk_associate_id,String  componentId, String message){
        ReportUtil reportUtil = new ReportUtil();
        int result=reportUtil.updateProductComponent(flag,fk_associate_id,componentId);
        if(result==1){
            result = reportUtil.setVendorGeneralInstruction(fk_associate_id,1,componentId,message+"Done");
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
}
