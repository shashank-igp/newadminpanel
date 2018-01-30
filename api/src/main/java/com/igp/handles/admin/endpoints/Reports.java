package com.igp.handles.admin.endpoints;

import com.igp.handles.admin.mappers.Reports.ReportMapper;
import com.igp.handles.admin.models.Reports.PincodeModelListHavingSummary;
import com.igp.handles.admin.models.Reports.ProductModelListHavingSummary;
import com.igp.handles.vendorpanel.response.HandleServiceResponse;
import com.igp.handles.vendorpanel.response.ReportResponse;
import com.igp.handles.vendorpanel.utils.Reports.SummaryFunctionsUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
/**
 * Created by suditi on 30/1/18.
 */
public class Reports {
    @GET
    @Path("/v1/admin/handels/getPincodeReport")
    public ReportResponse getPincodeReport(@QueryParam("fkAssociateId") @DefaultValue("565") String fkAssociateId,
                                           @QueryParam("startLimit") String startLimit, @QueryParam("endLimit") String endLimit ){
        ReportResponse reportResponse=new ReportResponse();
        List<Map.Entry<String,List<String>>> tableDataAction=new ArrayList<>();
        reportResponse.setTableHeaders(new String[]{"Vendor ID","Vendor Name","Pincode","Standard Delivery","Fixed Time Delivery","Midnight Delivery","Change Required"});
        com.igp.handles.vendorpanel.mappers.Reports.ReportMapper.fillDataActionpincode(tableDataAction);
        reportResponse.setTableDataAction(tableDataAction);
        PincodeModelListHavingSummary pincodeModelListHavingSummary=ReportMapper.getPincodeSummaryDetails(fkAssociateId,startLimit,endLimit);
        reportResponse.setSummary(pincodeModelListHavingSummary.getSummaryModelList());
        List<Object> objectList = new ArrayList<Object>(pincodeModelListHavingSummary.getPincodeTableDataList());
        reportResponse.setTableData(objectList);
        return reportResponse;
    }

    @PUT
    @Path("/v1/admin/handels/handlePincodeChange")
    public HandleServiceResponse updatePincodeDetail(@QueryParam("fkAssociateId") String fkAssociateId,
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
        if(status==1){result=true;}
        handleServiceResponse.setResult(result);
        return handleServiceResponse;
    }

    @GET
    @Path("/v1/admin/handels/getProductReport")
    public ReportResponse getVendorReport(@QueryParam("fkAssociateId") @DefaultValue("565") String fkAssociateId,
                                          @QueryParam("startLimit") String startLimit,
                                          @QueryParam("endLimit") String endLimit ){

        ReportResponse reportResponse=new ReportResponse();
        List<Map.Entry<String,List<String>>> tableDataAction=new ArrayList<>();
        ReportMapper reportMapper = new ReportMapper();
        reportResponse.setTableHeaders(new String[]{"Component_Id_Hide","Component Image","Component_Name","Price","InStock","Required Price"});
        com.igp.handles.vendorpanel.mappers.Reports.ReportMapper.fillDataActionComponent(tableDataAction);
        reportResponse.setTableDataAction(tableDataAction);
        ProductModelListHavingSummary productModelListHavingSummary=null;
        productModelListHavingSummary=reportMapper.getProductSummaryDetails(fkAssociateId,startLimit,endLimit);
        reportResponse.setSummary(productModelListHavingSummary.getSummaryModelList());
        List<Object> objectList = new ArrayList<Object>(productModelListHavingSummary.getProductTableDataList());
        reportResponse.setTableData(objectList);

        return reportResponse;
    }


    @PUT
    @Path("/v1/admin/handels/handleComponentChange")
    public HandleServiceResponse updateComponentDetail(@QueryParam("fkAssociateId") String fkAssociateId,
                                                       @QueryParam("componentId") String componentId,
                                                       @QueryParam("price")String updatePrice,
                                                       @QueryParam("inStock") Boolean inStock){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        Integer updateflag=0;
        String message="",componentName="";
        ReportMapper reportMapper = new ReportMapper();
        componentName= SummaryFunctionsUtil.getComponentName(componentId);
        if (inStock!=null){
            updateflag=1;
            if(inStock==true){
                message="Change status of component "+componentName+" to Instock : ";
            }else {
                message="Change status of component "+componentName+" to Out of stock : ";
            }
        }
        else if (updatePrice!=null){
            updateflag=2;
            message="Change price of component "+componentName+" to "+updatePrice+" : ";
        }
        int result=reportMapper.updateComponentMapper(updateflag,fkAssociateId,componentId,message);
        handleServiceResponse.setResult(result);
        return handleServiceResponse;
    }



}
