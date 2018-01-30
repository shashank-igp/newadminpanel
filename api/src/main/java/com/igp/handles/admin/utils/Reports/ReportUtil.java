package com.igp.handles.admin.utils.Reports;

import com.igp.config.instance.Database;
import com.igp.handles.admin.models.Reports.PincodeModelListHavingSummary;
import com.igp.handles.admin.models.Reports.PincodeTableData;
import com.igp.handles.admin.models.Reports.ProductModelListHavingSummary;
import com.igp.handles.admin.models.Reports.ProductTableData;
import com.igp.handles.vendorpanel.models.Report.SummaryModel;
import com.igp.handles.vendorpanel.models.Vendor.VendorInstruction;
import com.igp.handles.vendorpanel.utils.Reports.SummaryFunctionsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suditi on 30/1/18.
 */
public class ReportUtil {
    private static final Logger logger = LoggerFactory.getLogger(ReportUtil.class);

    public static PincodeModelListHavingSummary getPincodeDetailsFunctionAdmin (String fkAssociateId, String startLimit, String endLimit)
    {
        Connection connection = null;
        PincodeModelListHavingSummary pincodeModelListHavingSummary=new PincodeModelListHavingSummary();
        List<PincodeTableData> pincodeTableDataList= new ArrayList<>();
        String statement;
        String vendorName="";
        String totalQuery="";
        List <SummaryModel> summaryModelList=new ArrayList<>();
        ResultSet resultSet = null;
        Map<String,Map<String,Map<String,String>>> pincodeShipTypeAndShipChargeMap=new HashMap<>();

        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = " select a.vendor_id as vId,a.city_id as cId,a.pincode,a.ship_type," +
                "a.ship_charge,a.req_price,au.associate_user_name from AA_vendor_pincode as a " +
                "JOIN associate_user as au ON a.vendor_id = au.fk_associate_login_id " +
                "where vendor_id="+fkAssociateId+" limit "+startLimit+","+endLimit+" ";
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query "+preparedStatement);
            totalQuery = " select count(distinct pincode) as totalno from AA_vendor_pincode where vendor_id="+fkAssociateId+"";
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String pincode=resultSet.getString("a.pincode").trim();
                String shipType=resultSet.getString("a.ship_type").trim();
                String shipCharge=resultSet.getString("a.ship_charge").trim();
                String reqPrice=resultSet.getString("a.req_price").trim();
                if(pincodeShipTypeAndShipChargeMap.get(pincode)==null){
                    Map<String,Map<String,String>> shipTypeToChargeAndRequiredMap=new HashMap<>();
                    Map<String,String> chargeToChangeChargeMap = new HashMap<>();
                    chargeToChangeChargeMap.put(shipCharge,reqPrice);
                    shipTypeToChargeAndRequiredMap.put(shipType,chargeToChangeChargeMap);
                    pincodeShipTypeAndShipChargeMap.put(pincode,shipTypeToChargeAndRequiredMap);
                }else {
                    Map<String,String> chargeToChangeChargeMap = new HashMap<>();
                    chargeToChangeChargeMap.put(shipCharge,reqPrice);
                    pincodeShipTypeAndShipChargeMap.get(pincode).put(shipType,chargeToChangeChargeMap);
                }
                vendorName  = resultSet.getString("au.associate_user_name");
            }
            for(Map.Entry<String,Map<String,Map<String,String>>> entry:pincodeShipTypeAndShipChargeMap.entrySet()){
                PincodeTableData pincodeTableData=new PincodeTableData();
                String pincode = entry.getKey();
                pincodeTableData.setPincode(pincode);
                Map<String,Map<String,String>> shipTypeToChargeMap=entry.getValue();
                for (Map.Entry<String,Map<String,String>> entry1:shipTypeToChargeMap.entrySet()){
                    String shipType=entry1.getKey();
                    Map<String,String> charges = entry1.getValue();
                    String shipCharge=charges.keySet().toString();
                    if(shipType.equalsIgnoreCase("1")){
                        pincodeTableData.setStandardDeliveryCharge(shipCharge);
                        pincodeTableData.setChangeRequired(shipTypeToChargeMap);
                    }else if(shipType.equalsIgnoreCase("2")){
                        pincodeTableData.setFixedTimeDeliveryCharge(shipCharge);
                        pincodeTableData.setChangeRequired(shipTypeToChargeMap);
                    }else if(shipType.equalsIgnoreCase("3")){
                        pincodeTableData.setMidnightDeliveryCharge(shipCharge);
                        pincodeTableData.setChangeRequired(shipTypeToChargeMap);
                    }
                }
                if(pincodeTableData.getStandardDeliveryCharge()==null){
                    pincodeTableData.setStandardDeliveryCharge("Not Serviceable");
                    pincodeTableData.setChangeRequired(null);
                }
                if(pincodeTableData.getFixedTimeDeliveryCharge()==null){
                    pincodeTableData.setFixedTimeDeliveryCharge("Not Serviceable");
                    pincodeTableData.setChangeRequired(null);
                }
                if(pincodeTableData.getMidnightDeliveryCharge()==null){
                    pincodeTableData.setMidnightDeliveryCharge("Not Serviceable");
                    pincodeTableData.setChangeRequired(null);
                }
                pincodeTableData.setVendorId(fkAssociateId);
                pincodeTableData.setVendorName(vendorName);
                pincodeTableDataList.add(pincodeTableData);
            }
            pincodeModelListHavingSummary.setPincodeTableDataList(pincodeTableDataList);

            SummaryModel summaryModelTotalPincodes= new SummaryModel();
            summaryModelTotalPincodes.setLabel("Total Pincodes");
            summaryModelTotalPincodes.setValue(SummaryFunctionsUtil.getCount(totalQuery).toString());
            summaryModelList.add(summaryModelTotalPincodes);

            pincodeModelListHavingSummary.setSummaryModelList(summaryModelList);

        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return  pincodeModelListHavingSummary;
    }
    public int updateVendorPincode(Integer flag,String fk_associate_id,String pincode,Integer shipType,Integer updateStatus,Integer updatePrice) {
        Connection connection = null;
        String statement;
        String updateClause="";
        int result = 0;
        PreparedStatement preparedStatement = null;
        try {
            if (updateStatus!=null){
                updateClause=",flag_enabled="+updateStatus+"";
            }
            else {
                updateClause=",ship_charge="+updatePrice+"";
            }
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "update AA_vendor_pincode set flag_change="+flag+""+updateClause+" where vendor_id="+fk_associate_id+" and pincode="+pincode+" and  ship_type="+shipType+" ";
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query in updateVendorPincode "+preparedStatement);
            result = preparedStatement.executeUpdate();
        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }

    public int setVendorGeneralInstruction(String fkAssociateId,int pinOrComp, String value, String message){
        Connection connection = null;
        int response=0;
        String statement;
        String column="";
        PreparedStatement preparedStatement = null;
        try{
            if(pinOrComp==0){
                column="pincode";
            }
            else if(pinOrComp==1){
                column="fk_component_id";
            }
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "INSERT INTO vendor_general_instructions (associate_id,"+column+",instruction_msg) VALUES ("+fkAssociateId+","+value+","+"'"+message+"'"+")";
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("STATEMENT CHECK: " + preparedStatement);
            response = preparedStatement.executeUpdate();

        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return response;
    }
    public ProductModelListHavingSummary getProductDetail(String fkAssociateId, String startLimit, String endLimit)
    {
        Connection connection = null;
        ProductModelListHavingSummary productModelListHavingSummary=new ProductModelListHavingSummary();
        List <ProductTableData>  productTableDataList= new ArrayList<>();
        String statement;
        List <SummaryModel> summaryModelList=new ArrayList<>();
        ResultSet resultSet = null;
        String queryTotal="";

        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = " select vc.fk_associate_id as fkId,vc.fk_component_id cId,vc.price as price,vc.InStock  as "
                + " inStock,vc.req_price,mc.component_code as cCode,mc.component_name as cName,mc.componentImage as cImage,mc.mod_time as cTimestamp from AA_vendor_to_components as vc inner "
                + " join AA_master_components as mc on vc.fk_component_id=mc.component_id "
                + " where vc.fk_associate_id='"+fkAssociateId+"' limit "+startLimit+","+endLimit+" ";
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query in getVendorDetailsFunction "+preparedStatement);

            queryTotal="select count(*) as totalno from AA_vendor_to_components as vc inner join AA_master_components as mc on vc.fk_component_id=mc.component_id where vc.fk_associate_id='"+fkAssociateId+"'";

            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                ProductTableData productTableData=new ProductTableData();
                productTableData.setAssociateId("fkId");
                productTableData.setComponent_Id_Hide(resultSet.getString("cId"));
                productTableData.setComponentImage(resultSet.getString("cImage")+"?timestamp="+resultSet.getString("cTimestamp"));
                productTableData.setComponentName(resultSet.getString("cName"));
                productTableData.setPrice(resultSet.getDouble("price"));
                productTableData.setReqPrice(resultSet.getDouble("req_price"));
                Integer inStock=resultSet.getInt("inStock");
                if (inStock==1){productTableData.setInStock("In Stock");}
                else {productTableData.setInStock("Out Of Stock");}

                productTableDataList.add(productTableData);
            }
            productModelListHavingSummary.setProductTableDataList(productTableDataList);
            SummaryModel summaryModelTotalComponents= new SummaryModel();
            summaryModelTotalComponents.setLabel("Total Components");
            summaryModelTotalComponents.setValue(SummaryFunctionsUtil.getCount(queryTotal).toString());
            summaryModelList.add(summaryModelTotalComponents);
            productModelListHavingSummary.setSummaryModelList(summaryModelList);
        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return  productModelListHavingSummary;
    }
    public int updateProductComponent(Integer flag,String fk_associate_id,String  componentId ) {
        Connection connection = null;
        String statement;
        int result = 0;
        PreparedStatement preparedStatement = null;
        try {


            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "update AA_vendor_to_components set flag_change="+flag+" where fk_associate_id="+fk_associate_id+" and fk_component_id="+componentId+" ";
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query in updateVendorComponet "+preparedStatement);
            result = preparedStatement.executeUpdate();

        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;

    }


}
