package com.igp.handles.admin.mappers.Reports;

import com.igp.handles.admin.models.Reports.PincodeModelListHavingSummary;
import com.igp.handles.admin.models.Reports.ProductModelListHavingSummary;
import com.igp.handles.admin.utils.Reports.ReportUtil;

/**
 * Created by suditi on 30/1/18.
 */
public class ReportMapper {
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
}
