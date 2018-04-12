package com.igp.handles.admin.utils.Reports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.igp.admin.mappers.marketPlace.Constants;
import com.igp.config.Environment;
import com.igp.config.instance.Database;
import com.igp.handles.admin.mappers.Reports.ReportMapper;
import com.igp.handles.admin.mappers.Vendor.VendorMapper;
import com.igp.handles.admin.models.Reports.*;
import com.igp.handles.admin.models.Vendor.VendorInfoModel;
import com.igp.handles.admin.utils.Order.SlaCompliant;
import com.igp.handles.admin.utils.Vendor.VendorUtil;
import com.igp.handles.vendorpanel.models.Report.ReportOrderWithSummaryModel;
import com.igp.handles.vendorpanel.models.Report.SummaryModel;
import com.igp.handles.vendorpanel.models.Report.VendorPincodeModel;
import com.igp.handles.vendorpanel.models.Report.orderReportObjectModel;
import com.igp.handles.vendorpanel.utils.Reports.SummaryFunctionsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * Created by suditi on 30/1/18.
 */
public class ReportUtil {
    private static final Logger logger = LoggerFactory.getLogger(ReportUtil.class);

    public ReportOrderWithSummaryModel getOrders(String fkAssociateId, String startDate, String endDate, String startLimit, String endLimit, Integer orderNo, String status, String deliveryDateFrom, String deliveryDateTo){
        Connection connection = null;

        ReportOrderWithSummaryModel reportOrderWithSummaryModel=new ReportOrderWithSummaryModel();
        List <orderReportObjectModel>  orderReportObjectList= new ArrayList<>();
        String statement;
        List <SummaryModel> summaryModelList=new ArrayList<>();
        ResultSet resultSet = null;
        int count=0,amt=0;
        VendorMapper vendorMapper=new VendorMapper();
        StringBuilder sb=new StringBuilder("");

        if (startDate!=null && !startDate.isEmpty() ){
            sb.append("and o.date_purchased >='"+startDate+"'");
        }

        if (endDate!=null && !endDate.isEmpty()){
            sb.append("and o.date_purchased <='"+endDate+"'");
        }

        if(deliveryDateFrom!=null && !deliveryDateFrom.isEmpty() ){
            sb.append(" and oe.delivery_date >='"+deliveryDateFrom+"'");
        }

        if (deliveryDateTo!=null && !deliveryDateTo.isEmpty()){
            sb.append("  and oe.delivery_date <='"+deliveryDateTo+"' ");
        }

        if (fkAssociateId!=null && !fkAssociateId.isEmpty()){
            sb.append("  and vap.fk_associate_id ='"+fkAssociateId+"' ");
        }

        if (orderNo!=null )
        {
            sb.append("and op.orders_id="+orderNo+"");
        }
        else if (status !=null && !status.isEmpty() ){
            String[] statusArray=status.split(",");
            String statusWhereClause="";
            for(int i=0;i<statusArray.length;i++){
                if(statusArray[i].equals("OutForDelivery")||statusArray[i].equals("Delivered")){
                    statusWhereClause+="'Shipped',";
//                    sb.append("and  op.orders_product_status='Shipped' and op.delivery_status=0 ");
                }
                else
                {
                    statusWhereClause+="'"+statusArray[i]+"',";
//                    sb.append("and op.orders_product_status='"+status+"'");
                }
            }
            statusWhereClause=statusWhereClause.substring(0,statusWhereClause.length()-1);
            if(status.contains("Delivered")){
                sb.append(" and op.orders_product_status in ( "+statusWhereClause+" ) and op.delivery_status = 1 ");
            }else {
                sb.append(" and op.orders_product_status in ( "+statusWhereClause+" ) and op.delivery_status = 0 ");
            }
        }

        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();

            String queryCount = "select count(*) as totalNo ,sum((vap.vendor_price+vap.shipping)) as totalAmt from orders_products as op LEFT JOIN vendor_assign_price as  vap "
                + " on op.orders_id=vap.orders_id  and  op.products_id=vap.products_id  inner join order_product_extra_info "
                + " as oe on op.orders_products_id=oe.order_product_id inner  join  orders as o on  op.orders_id=o.orders_id" +
                " inner join  orders_occasions  as oo  on o.orders_occasionid=oo.occasion_id where " +
                "(op.fk_associate_id=72 OR op.fk_associate_id=vap.fk_associate_id) "+sb.toString();
            preparedStatement = connection.prepareStatement(queryCount);
            logger.debug("sql query in for getting count and total amount for order report in handels panel "+preparedStatement);
            resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                count=resultSet.getInt("totalNo");
                amt=resultSet.getInt("totalAmt");
            }

            statement = " select a.associate_name as vendorName,o.date_purchased as datePurchased,o.orders_id as  Order_No,oo.occasion_name as Ocassion , "
                + " o.delivery_city as City ,o.delivery_postcode as Pincode ,oe.delivery_date  as Delivery_Date , "
                + " op.orders_product_status as opStatus,oe.delivery_type as delivery_type  , o.delivery_name as "
                + " Recipient_Name , o.delivery_mobile as Phone  , (vap.vendor_price+vap.shipping) as Amount, "
                + " op.delivery_status as status,op.orders_products_id as orderProductId  from orders_products as op LEFT JOIN vendor_assign_price as  vap "
                + " on op.orders_id=vap.orders_id  and  op.products_id=vap.products_id  LEFT JOIN associate as a on op.fk_associate_id=a.associate_id" +
                " inner join order_product_extra_info as oe on op.orders_products_id=oe.order_product_id inner  join  orders as o on  op.orders_id=o.orders_id "
                + " inner join  orders_occasions  as oo  on o.orders_occasionid=oo.occasion_id where " +
                "(op.fk_associate_id=72 OR op.fk_associate_id=vap.fk_associate_id) "
                +sb.toString()+" limit "+startLimit+","+endLimit+" ";
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query in getSummaryDetailsForVendor "+preparedStatement);
            resultSet = preparedStatement.executeQuery();
            Double totalPrice=0.00;
            Double value=0.00;
            while(resultSet.next()) {
                orderReportObjectModel orderReportObjectModel = new orderReportObjectModel();
                orderReportObjectModel.setDate(resultSet.getString("datePurchased"));
                orderReportObjectModel.setOrderNo(resultSet.getString("Order_No"));
                orderReportObjectModel.setOccasion(resultSet.getString("Ocassion"));
                orderReportObjectModel.setCity(resultSet.getString("City"));
                orderReportObjectModel.setPincode(resultSet.getInt("Pincode"));
                orderReportObjectModel.setDelivery_Date(resultSet.getString("Delivery_Date"));
                orderReportObjectModel.setDeliveryType(Constants.getDeliveryType(resultSet.getString("delivery_type")));
                orderReportObjectModel.setPrice(resultSet.getDouble("Amount"));
                orderReportObjectModel.setStatus(resultSet.getInt("status"));
                orderReportObjectModel.setVendorInfoModelList(vendorMapper.getVendorList(orderReportObjectModel.getPincode(),resultSet.getInt("delivery_type")));
                orderReportObjectModel.setOrderProductId(resultSet.getString("orderProductId"));
                String vendorName = resultSet.getString("vendorName");
                if(vendorName==null || vendorName.equals("")){
                    orderReportObjectModel.setVendorName("");
                }else {
                    orderReportObjectModel.setVendorName(resultSet.getString("vendorName"));
                }

                if (resultSet.getString("opStatus").equals("Shipped") && resultSet.getInt("status" )==1){

                    orderReportObjectModel.setOrderProductStatus("Delivered");
                }
                else if (resultSet.getString("opStatus").equals("Shipped") && resultSet.getInt("status" )==0 ){

                    orderReportObjectModel.setOrderProductStatus("Out For Delivery");
                }
                else {
                    orderReportObjectModel.setOrderProductStatus(resultSet.getString("opStatus"));
                }
                orderReportObjectList.add(orderReportObjectModel);
                value=resultSet.getDouble("Amount");
                totalPrice=totalPrice+value;
            }
            reportOrderWithSummaryModel.setOrderReportObjectModelList(orderReportObjectList);
            SummaryModel summaryModelTotalOrders= new SummaryModel();
            summaryModelTotalOrders.setLabel("Total orders");
            summaryModelTotalOrders.setValue(String.valueOf(count));
            summaryModelList.add(summaryModelTotalOrders);
            SummaryModel summaryModelTotalAmount= new SummaryModel();
            summaryModelTotalAmount.setLabel("Total Amount");
            summaryModelTotalAmount.setValue(String.valueOf(amt));
            summaryModelList.add(summaryModelTotalAmount);
            reportOrderWithSummaryModel.setSummaryModelList(summaryModelList);
            reportOrderWithSummaryModel.setTotalAmountSummary(totalPrice);
            reportOrderWithSummaryModel.setTotalNumber(orderReportObjectList.size());
        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return  reportOrderWithSummaryModel;
    }


    public static PincodeModelListHavingSummaryModel getPincodeDetailsFunctionAdmin (String fkAssociateId, String startLimit, String endLimit)
    {
        Connection connection = null;
        PincodeModelListHavingSummaryModel pincodeModelListHavingSummaryModel =new PincodeModelListHavingSummaryModel();
        List<PincodeTableDataModel> pincodeTableDataModelList = new ArrayList<>();
        String statement;
        String vendorName="";
        String totalQuery="";
        List <SummaryModel> summaryModelList=new ArrayList<>();
        ResultSet resultSet = null;
        Map<String,Map<Integer,TableDataActionHandels>> pincodeShipTypeAndShipChargeMap=new HashMap<>();

        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = "select aso.associate_name vendor,pincode, " +
                "group_concat(ship_type,',',flag_enabled,',',ship_charge,',',req_price SEPARATOR ':' ) as value " +
                "from AA_vendor_pincode JOIN associate_user as au ON AA_vendor_pincode.vendor_id = au.fk_associate_login_id " +
                "JOIN associate as aso on au.fk_associate_login_id=aso.associate_id where vendor_id = "+fkAssociateId+
                " group by pincode order by pincode limit "+startLimit+","+endLimit+" ";
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query "+preparedStatement);
            totalQuery = " select count(distinct pincode) as totalno from AA_vendor_pincode where vendor_id="+fkAssociateId+"";
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                VendorPincodeModel vendorPincodeModel = new VendorPincodeModel();
                vendorPincodeModel.setPincode(resultSet.getInt("pincode"));

                String[] valueArray = resultSet.getString("value").split(":");

                for (int i = 0; i < valueArray.length; i++) {
                    Map<Integer,TableDataActionHandels> shipTypeAndTableMap = new HashMap<>();

                    String[] values = valueArray[i].split(",");
                    vendorPincodeModel.setShipTypeId(new Integer(values[0]).intValue());
                    vendorPincodeModel.setFlagEnabled(new Integer(values[1]).intValue());
                    vendorPincodeModel.setShipCharge(new Integer(values[2]).intValue());
                    vendorPincodeModel.setReqdPrice(new Integer(values[3]).intValue());

                    TableDataActionHandels tableDataActionHandels = new TableDataActionHandels();
                    tableDataActionHandels.setValue(vendorPincodeModel.getShipCharge()+"");
                    tableDataActionHandels.setRequestValue(vendorPincodeModel.getReqdPrice()+"");
                    if(vendorPincodeModel.getReqdPrice()!=-1){
                        tableDataActionHandels.setRequestType("approve");
                    }else if(vendorPincodeModel.getFlagEnabled()==0 && vendorPincodeModel.getReqdPrice()==-1){
                        tableDataActionHandels.setValue(null);
                        // when the ship type at pincode is disabled so it must be not servicable
                    }
                    shipTypeAndTableMap.put(vendorPincodeModel.getShipTypeId(), tableDataActionHandels);
                    if (pincodeShipTypeAndShipChargeMap.get(vendorPincodeModel.getPincode() + "") == null) {
                        pincodeShipTypeAndShipChargeMap.put(vendorPincodeModel.getPincode() + "", shipTypeAndTableMap);
                    } else {
                        pincodeShipTypeAndShipChargeMap.get(vendorPincodeModel.getPincode() + "").put(vendorPincodeModel.getShipTypeId(), tableDataActionHandels);
                    }
                    vendorName = resultSet.getString("vendor");
                }
            }
            for(Map.Entry<String,Map<Integer,TableDataActionHandels>> entry:pincodeShipTypeAndShipChargeMap.entrySet()){
                PincodeTableDataModel pincodeTableDataModel =new PincodeTableDataModel();
                String pincode = entry.getKey();
                pincodeTableDataModel.setPincode(pincode);
                Map<Integer,TableDataActionHandels> shipTypeAndTableMap = entry.getValue();
                for (Map.Entry<Integer,TableDataActionHandels> entry1: shipTypeAndTableMap.entrySet()){
                    Integer shipType=entry1.getKey();  // this will have shiptype
                    TableDataActionHandels tableDataActionHandels = entry1.getValue();
                    if(shipType==1 && tableDataActionHandels.getValue()!=null){
                        pincodeTableDataModel.setStandardDeliveryCharge(tableDataActionHandels);
                    }else if(shipType==2 && tableDataActionHandels.getValue()!=null){
                        pincodeTableDataModel.setFixedTimeDeliveryCharge(tableDataActionHandels);
                    }else if(shipType==3 && tableDataActionHandels.getValue()!=null){
                        pincodeTableDataModel.setMidnightDeliveryCharge(tableDataActionHandels);
                    }
                }
                TableDataActionHandels tableDataActionHandels = new TableDataActionHandels();
                tableDataActionHandels.setValue("Not Serviceable");
                tableDataActionHandels.setRequestValue("-1");
                if(pincodeTableDataModel.getStandardDeliveryCharge()==null){
                    pincodeTableDataModel.setStandardDeliveryCharge(tableDataActionHandels);
                }
                if(pincodeTableDataModel.getFixedTimeDeliveryCharge()==null){
                    pincodeTableDataModel.setFixedTimeDeliveryCharge(tableDataActionHandels);
                }
                if(pincodeTableDataModel.getMidnightDeliveryCharge()==null){
                    pincodeTableDataModel.setMidnightDeliveryCharge(tableDataActionHandels);
                }
                pincodeTableDataModel.setVendorId(fkAssociateId); //same for all records
                pincodeTableDataModel.setVendorName(vendorName); //same for all records
                pincodeTableDataModelList.add(pincodeTableDataModel);
            }
            pincodeModelListHavingSummaryModel.setPincodeTableDataModelList(pincodeTableDataModelList);

            SummaryModel summaryModelTotalPincodes= new SummaryModel();
            summaryModelTotalPincodes.setLabel("Total Pincodes");
            summaryModelTotalPincodes.setValue(SummaryFunctionsUtil.getCount(totalQuery).toString());
            summaryModelList.add(summaryModelTotalPincodes);

            pincodeModelListHavingSummaryModel.setSummaryModelList(summaryModelList);

        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return pincodeModelListHavingSummaryModel;
    }
    public int updateVendorPincode(Integer flag,int fk_associate_id,int pincode,int shipType,int updatePrice, String field) {
        Connection connection = null;
        String statement;
        String updateClause="";
        int result = 0;
        PreparedStatement preparedStatement = null;
        try {
            connection = Database.INSTANCE.getReadWriteConnection();
            if(field!=null && field.equals("pincode")){
                statement = "update AA_vendor_pincode set flag_enabled="+flag+" where vendor_id=" + fk_associate_id + " and pincode=" + pincode;
                preparedStatement = connection.prepareStatement(statement);
                logger.debug("sql query in updateVendorPincode " + preparedStatement);
                result = preparedStatement.executeUpdate();
            }else {
                if (field!=null && field.equals("reqPrice")){
                    updateClause = "req_price=" + updatePrice;
                } else if (flag!=null && flag == 1) {
                    updateClause = "flag_enabled=" + flag +",req_price = -1 ";
                } else if (flag!=null && flag == 0) {
                    updateClause = "flag_enabled=" + flag +",req_price = -1 ";
                } else {
                    updateClause = "ship_charge=" + updatePrice +",req_price = -1 ";
                }
                statement = "update AA_vendor_pincode set " + updateClause + " where vendor_id=" + fk_associate_id + " and pincode=" + pincode + " and  ship_type=" + shipType + " ";
                preparedStatement = connection.prepareStatement(statement);
                logger.debug("sql query in updateVendorPincode " + preparedStatement);
                result = preparedStatement.executeUpdate();
            }
        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }
    public boolean addNewVendorPincodeUtil(int fkAssociateId,int pincode,int cityId,int shipType,int shipCharge,int adminFlag){
        Connection connection = null;
        String statement;
        boolean result = false;
        PreparedStatement preparedStatement = null;
        int status=0;
        try {
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "INSERT INTO AA_vendor_pincode (vendor_id,city_id,pincode,ship_type," +
                "ship_charge,req_price,flag_enabled) VALUES (?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,fkAssociateId);
            preparedStatement.setInt(2,cityId);
            preparedStatement.setInt(3,pincode);
            preparedStatement.setInt(4,shipType);
            preparedStatement.setInt(5,shipCharge);
            preparedStatement.setInt(6,-1);
            preparedStatement.setInt(7,adminFlag);
            logger.debug("sql statement check: "+preparedStatement);
            status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Pincode could not be added, plz check sql query");
            }
            else {
                result=true;
            }
        } catch (Exception exception) {
            logger.error("Exception in connection at addNewVendorPincodeUtil : ", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }
    public boolean setVendorGeneralInstruction(int fkAssociateId,int pinOrComp, String value, String message){
        Connection connection = null;
        boolean response = false;
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
            int result = preparedStatement.executeUpdate();
            if(result!=0){
                response=true;
            }

        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return response;
    }
    public boolean addVendorComponent(String fk_associate_id,String componentCode,String componentName,int type,String componentImagePath,int price){
        Connection connection = null;
        String statement;
        boolean result = false;
        PreparedStatement preparedStatement = null;
        int autoGeneratedID=0,status=0;
        try {
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "INSERT INTO AA_master_components (component_code,component_name,type,componentImage) VALUES (?,?,?,?)";
            preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,componentCode);
            preparedStatement.setString(2,componentName);
            preparedStatement.setInt(3,type);
            preparedStatement.setString(4,componentImagePath);
            logger.debug("sql statement check: "+preparedStatement);
            status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Component could not be created check sql query");
            }
            else{
                ResultSet tableKeys = preparedStatement.getGeneratedKeys();
                tableKeys.next();
                autoGeneratedID = tableKeys.getInt(1);
                statement = "INSERT INTO AA_vendor_to_components (fk_associate_id,fk_component_id,price,InStock) VALUES (?,?,?,?)";
                preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setInt(1,Integer.parseInt(fk_associate_id));
                preparedStatement.setInt(2,autoGeneratedID);
                preparedStatement.setInt(3,price);
                preparedStatement.setInt(4,1); //Instock default 1 for handel
                logger.debug("sql statement check: "+preparedStatement);
                status = preparedStatement.executeUpdate();
                if (status == 0) {
                    logger.error("Component could not be created check sql query");
                }
                else {
                    result=true;
                }
            }
        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }

    public ProductModelListHavingSummaryModel getProductDetail(String fkAssociateId, String startLimit, String endLimit)
    {
        Connection connection = null;
        ProductModelListHavingSummaryModel productModelListHavingSummaryModel =new ProductModelListHavingSummaryModel();
        List <ProductTableDataModel> productTableDataModelList = new ArrayList<>();
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
                ProductTableDataModel productTableData=new ProductTableDataModel();
                productTableData.setAssociateId("fkId");
                productTableData.setComponent_Id_Hide(resultSet.getString("cId"));
                productTableData.setComponentImage(resultSet.getString("cImage"));
                productTableData.setComponentName(resultSet.getString("cName"));

                String reqPrice = resultSet.getString("vc.req_price");

                TableDataActionHandels tableDataActionHandels = new TableDataActionHandels();
                int price = resultSet.getInt("price");
                tableDataActionHandels.setValue(price+"");
                tableDataActionHandels.setRequestValue("-1");
                if(!reqPrice.equals("-1") && !tableDataActionHandels.getValue().equals(reqPrice)){
                    tableDataActionHandels.setRequestType("approve");
                    tableDataActionHandels.setRequestValue(reqPrice);
                }
                productTableData.setPrice(tableDataActionHandels);

                TableDataActionHandels tableDataActionHandels1 = new TableDataActionHandels();
                Integer inStock=resultSet.getInt("inStock");
                tableDataActionHandels1.setRequestValue("-1");
                if (inStock == 1) {
                    tableDataActionHandels1.setValue("In Stock");
                } else {
                    tableDataActionHandels1.setValue("Out of Stock");
                }
                if(tableDataActionHandels.getValue().equals(reqPrice)) {
                    tableDataActionHandels1.setRequestType("approve");
                    tableDataActionHandels1.setRequestValue("In Stock");
                }
                productTableData.setInStock(tableDataActionHandels1);

                productTableDataModelList.add(productTableData);
            }
            productModelListHavingSummaryModel.setProductTableDataModelList(productTableDataModelList);
            SummaryModel summaryModelTotalComponents= new SummaryModel();
            summaryModelTotalComponents.setLabel("Total Components");
            summaryModelTotalComponents.setValue(SummaryFunctionsUtil.getCount(queryTotal).toString());
            summaryModelList.add(summaryModelTotalComponents);
            productModelListHavingSummaryModel.setSummaryModelList(summaryModelList);
        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return productModelListHavingSummaryModel;
    }
    public boolean updateProductComponent(int fkAssociateId,String  componentId,int updatePrice, String inStock , String field) {
        Connection connection = null;
        String statement;
        int status = 0;
        boolean result = false;
        String column;
        PreparedStatement preparedStatement = null;
        try {
            if(field!=null && field.equals("reqPrice")){
                column="req_price="+updatePrice;
            }
            else if (updatePrice!=-1){
                column="price="+updatePrice+",req_price = -1 ";
            }
            else {
                column="InStock="+inStock+",req_price = -1 ";
            }
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "update AA_vendor_to_components set "+column+" where fk_associate_id="+fkAssociateId+" and fk_component_id="+componentId+" ";
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query in updateVendorComponent "+preparedStatement);
            status = preparedStatement.executeUpdate();
            if(status!=0){
                result=true;
            }
        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }

    public VendorDetailsHavingSummaryModel getVendorDetails(int fkAssociateId,int startLimit, int endLimit){
        VendorDetailsHavingSummaryModel vendorDetailsHavingSummaryModel = new VendorDetailsHavingSummaryModel();
        VendorUtil vendorUtil = new VendorUtil();
        SummaryModel summaryModelForVendor = new SummaryModel();
        List <SummaryModel> summaryModelList=new ArrayList<>();
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        String queryTotal,count="";
        List<VendorInfoModel> vendorInfoModelList=  new ArrayList<VendorInfoModel>();

        if(fkAssociateId!=0){
            VendorInfoModel vendorInfoModel = vendorUtil.getVendorInfo(fkAssociateId);
            count="1";
            vendorInfoModelList.add(vendorInfoModel);
        }
        else {
            try {
                String statement;

                connection = Database.INSTANCE.getReadOnlyConnection();
                queryTotal="select count(*) as totalno from associate as a JOIN associate_user as au ON a.associate_id = au.fk_associate_login_id JOIN vendor_extra_info as v ON au.fk_associate_login_id = v.associate_id where v.type=2 and a.associate_status=1";
                statement = " select * from associate as a JOIN associate_user as au ON " +
                    "a.associate_id = au.fk_associate_login_id JOIN vendor_extra_info as v ON " +
                    "au.fk_associate_login_id = v.associate_id where v.type=2 and a.associate_status=1 limit " + startLimit + "," + endLimit + " ";
                preparedStatement = connection.prepareStatement(statement);
                logger.debug("sql query in getVendorDetails " + preparedStatement);

                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    VendorInfoModel vendorInfoModel = new VendorInfoModel();
                    vendorInfoModel.setVendorId(resultSet.getInt("a.associate_id"));
                    vendorInfoModel.setAssociateName(resultSet.getString("a.associate_name"));
                    vendorInfoModel.setUserId(resultSet.getString("au.associate_user_name"));
                    vendorInfoModel.setPhone(resultSet.getString("a.associate_phone"));
                    vendorInfoModel.setAddress(resultSet.getString("a.associate_address"));
                    vendorInfoModel.setStatus(resultSet.getInt("a.associate_status"));
                    vendorInfoModel.setContactPerson(resultSet.getString("a.associate_contact_person"));
                    vendorInfoModel.setEmail(resultSet.getString("a.associate_email"));
                    vendorInfoModel.setPassword(resultSet.getString("au.associate_user_pass"));

                    vendorInfoModelList.add(vendorInfoModel);
                }
                count = SummaryFunctionsUtil.getCount(queryTotal).toString();

            } catch (Exception exception) {
                logger.error("Error in getPayoutAndTaxes ", exception);

            } finally {
                Database.INSTANCE.closeStatement(preparedStatement);
                Database.INSTANCE.closeConnection(connection);
                Database.INSTANCE.closeResultSet(resultSet);
            }
        }
        summaryModelForVendor.setValue(count);
        summaryModelForVendor.setLabel("Total Vendors");
        summaryModelList.add(summaryModelForVendor);
        vendorDetailsHavingSummaryModel.setVendorDetailsModels(vendorInfoModelList);
        vendorDetailsHavingSummaryModel.setSummaryModelList(summaryModelList);
        return vendorDetailsHavingSummaryModel;
    }

    public int modifyVendorDetails(int fkAssociateId,String vendorName,String contactPerson,String email,
                                   String address,String phone,String userId,String password,int status){
        Connection connection = null;
        int response=0;
        String statement;
        String column="";
        int flag=1;
        PreparedStatement preparedStatement = null;
        try{
            if(!vendorName.equals("")){
                column="associate_name="+vendorName;
            }
            else if(!contactPerson.equals("")){
                column="associate_contact_person="+contactPerson;
            }
            else if(!email.equals("")){
                column="associate_email="+email;
            }
            else if(!address.equals("")){
                column="associate_address="+address;
            }
            else if(!phone.equals("")){
                column="associate_phone="+phone;
            }
            else if(status!=-1){
                column="associate_status="+flag;
            }
            connection = Database.INSTANCE.getReadWriteConnection();
            if(!column.equals("")) {
                statement = "UPDATE associate SET " + column + " WHERE associate_id = ?";
                preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setInt(1, fkAssociateId);
                logger.debug("STATEMENT CHECK: " + preparedStatement);
                response = preparedStatement.executeUpdate();
            }else{
                if(!userId.equals("")){
                    column="associate_user_name="+userId;
                }
                else if(!password.equals("")){
                    column="associate_user_pass="+password;
                }
                String statement1 = "UPDATE associate_user set " +column+" WHERE fk_associate_login_id = ?";
                preparedStatement = connection.prepareStatement(statement1);
                preparedStatement.setInt(1, fkAssociateId);
                logger.debug("sql query in addNewVendorUtil " + preparedStatement);
                response = preparedStatement.executeUpdate();
            }
        } catch (Exception exception) {
            logger.error("Exception in connection : ", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return response;
    }
    public int addNewVendorUtil(String vendorName,String user, String password,
                                String contactPerson,String email,
                                String address,String phone,int status) {
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement=null;
        // PreparedStatement preparedStatement1=null;
        int result = 0;
        int fkAssociateId = 0;
        try {
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "INSERT INTO associate (associate_login_id,associate_name," +
                "associate_contact_person,associate_email,associate_url," +
                "associate_address,associate_phone,associate_description," +
                "fk_associate_type_id, associate_status,discount) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

            preparedStatement = connection.prepareStatement(statement,Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1,user);
            preparedStatement.setString(2,vendorName);
            preparedStatement.setString(3,contactPerson);
            preparedStatement.setString(4,email);
            preparedStatement.setString(5,""); //check associate_url
            preparedStatement.setString(6,address);
            preparedStatement.setString(7,phone);
            preparedStatement.setString(8,""); // check description
            preparedStatement.setInt(9,2); //check
            preparedStatement.setInt(10,status);
            preparedStatement.setInt(11,0); //check

            logger.debug("sql query in addNewVendorUtil "+preparedStatement);
            result = preparedStatement.executeUpdate();
            if (result == 0) {
                logger.error("Failed to create new vendor.");
            } else {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                resultSet.first();
                fkAssociateId = resultSet.getInt(1);
                String statement1 = "INSERT INTO associate_user " +
                    "(fk_associate_login_id,associate_user_name," +
                    "associate_user_status,associate_user_pass," +
                    "associate_user_permission) VALUES (?,?,?,?,?)";
                preparedStatement = connection.prepareStatement(statement1);
                preparedStatement.setInt(1,fkAssociateId);
                preparedStatement.setString(2,user);
                preparedStatement.setInt(3,1);
                preparedStatement.setString(4,password);
                preparedStatement.setString(5,null);

                logger.debug("sql query in addNewVendorUtil "+preparedStatement);
                result = preparedStatement.executeUpdate();
            }
        } catch (Exception exception) {
            logger.error("Exception in connection : ", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }
    public String approveAndRejectUtil(String object, String reportName, String columnName, int fkAssociateId,boolean approveReject){
        ObjectMapper objectMapper = new ObjectMapper();
        TableDataActionHandels actionHandels = new TableDataActionHandels();
        String message = "";
        boolean result = false;
        ReportMapper reportMapper = new ReportMapper();
        try{
            if(reportName.equals("getPincodeReport")){
                int shipType = 0;
                PincodeTableDataModel pincodeTableDataModel = objectMapper.readValue(object,PincodeTableDataModel.class);
                switch (columnName){
                    case "Standard Delivery" :
                        actionHandels = pincodeTableDataModel.getStandardDeliveryCharge();
                        shipType = 1;
                        break;
                    case "Fixed Time Delivery" :
                        actionHandels = pincodeTableDataModel.getFixedTimeDeliveryCharge();
                        shipType = 2;
                        break;
                    case "Midnight Delivery" :
                        actionHandels = pincodeTableDataModel.getMidnightDeliveryCharge();
                        shipType = 3;
                        break;
                }

                if(actionHandels.getValue().equals(actionHandels.getRequestValue())){
                    if(approveReject==true){
                        // request is to enable.
                        message="Enable "+columnName+" for Pincode "+pincodeTableDataModel.getPincode()+" : Request Accepted.";
                        result = reportMapper.updatePincodeMapper(1,fkAssociateId,Integer.parseInt(pincodeTableDataModel.getPincode()),shipType,Integer.parseInt(actionHandels.getRequestValue()),message,"");
                    }
                    else {
                        // request to enable rejected.
                        message="Enable "+columnName+" for Pincode "+pincodeTableDataModel.getPincode()+" : Request Rejected.";
                        result = reportMapper.updatePincodeMapper(1,fkAssociateId,Integer.parseInt(pincodeTableDataModel.getPincode()),shipType,-1,message,"reqPrice");
                    }
                }
                else {
                    if(approveReject==true) {
                        // request is to update the price.
                        message = "Update the price of " + columnName + " for Pincode " + pincodeTableDataModel.getPincode() + " to " + actionHandels.getRequestValue() + " : Request Accepted.";
                        result = reportMapper.updatePincodeMapper(null, fkAssociateId, Integer.parseInt(pincodeTableDataModel.getPincode()), shipType, Integer.parseInt(actionHandels.getRequestValue()), message, "");

                    } else {
                        // request to update the price rejected.
                        message = "Update the price of " + columnName + " for Pincode " + pincodeTableDataModel.getPincode() + " to " + actionHandels.getRequestValue() + " : Request Rejected.";
                        reportMapper.updatePincodeMapper(null,fkAssociateId,Integer.parseInt(pincodeTableDataModel.getPincode()),shipType,-1,message,"reqPrice");
                    }
                }
            }else if(reportName.equals("getVendorReport")){
                ProductTableDataModel productTableDataModel = objectMapper.readValue(object,ProductTableDataModel.class);

                if(columnName.equals("InStock")){
                    actionHandels = productTableDataModel.getInStock();

                    if(actionHandels.getValue().equals("Out of Stock") && actionHandels.getRequestValue().equals("In Stock")){

                        if(approveReject==true){
                            // request is to enable.
                            message="Change status of component "+productTableDataModel.getComponentName()+" to Instock : Approved.";
                            result = reportMapper.updateComponentMapper(fkAssociateId,productTableDataModel.getComponent_Id_Hide(),message,-1,"1","");
                        }
                        else {
                            // request to enable rejected.
                            message="Change status of component "+productTableDataModel.getComponentName()+" to Instock : Rejected.";
                            result = reportMapper.updateComponentMapper(fkAssociateId,productTableDataModel.getComponent_Id_Hide(),message,-1,"0","");
                        }
                    }
                }
                else {
                    actionHandels = productTableDataModel.getPrice();
                    //for update price
                    int value = new Double(actionHandels.getValue()).intValue();
                    int reqValue = new Double(actionHandels.getRequestValue()).intValue();

                    if(value!=reqValue) {

                        //  i.e. price change
                        if (approveReject == true) {
                            // request is to update the price.
                            message = "Change price of component " + productTableDataModel.getComponentName() + " to " + actionHandels.getRequestValue() + " : Accepted.";
                            result = reportMapper.updateComponentMapper(fkAssociateId, productTableDataModel.getComponent_Id_Hide(), message, Integer.parseInt(actionHandels.getRequestValue()), "1", "");

                        } else {
                            // request to update the price rejected.
                            message = "Change price of component " + productTableDataModel.getComponentName() + " to " + actionHandels.getRequestValue() + " : Rejected.";
                            result = reportMapper.updateComponentMapper(fkAssociateId, productTableDataModel.getComponent_Id_Hide(), message, -1, "1", "reqPrice");
                        }
                    }
                }

            }


        }catch (Exception exception){
            logger.error("Error at approveAndRejectUtil in ReportUtil : ",exception);
        }
        if(result==false) message="";

        return message;
    }
    public BarcodeToComponentListHavingSummary getBarcodeToComponentsUtil(String productCode, int startLimit, int endLimit){

        Connection connection = null;
        ResultSet resultSet = null;
        String statement,clause,queryTotal="";
        int count=0;
        PreparedStatement preparedStatement = null;
        SummaryModel summaryModelForBarcodeToComponent =  new SummaryModel();
        List <SummaryModel> summaryModelList=new ArrayList<>();
        BarcodeToComponentListHavingSummary barcodeModelListHavingSummaryModel = new BarcodeToComponentListHavingSummary();
        List<BarcodeToComponentModel> barcodeToComponentModelList = new ArrayList<>();
        //    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //    Calendar calendar=Calendar.getInstance();
        try{
            if(productCode.equals("0")){
                clause = " limit "+startLimit+" , "+endLimit;
                queryTotal="select count(*) as totalno from AA_barcode_to_components bc join AA_master_components mc " +
                    "on mc.component_id=bc.fk_component_id JOIN products as p on bc.barcode=p.products_code ";
                count =SummaryFunctionsUtil.getCount(queryTotal);
            }
            else {
                clause = " where bc.barcode = "+productCode;
                count=1;
            }
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement="select p.products_code,p.products_name,p.products_image_big,mc.component_code,mc.component_name,mc.type,bc.quantity  "
                + " quantity, mc.componentImage componentImage , mc.component_id ,bc.fk_component_id,mc.mod_time from  "
                + " AA_barcode_to_components bc join AA_master_components mc on mc.component_id=bc.fk_component_id LEFT"
                + " JOIN products as p on bc.barcode=p.products_code "+clause;

            preparedStatement = connection.prepareStatement(statement);
            logger.debug("STATEMENT CHECK: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                // calendar.setTime(dateFormat.parse(resultSet.getString("mc.mod_time")));

                BarcodeToComponentModel barcodeToComponentModel= new BarcodeToComponentModel.Builder()
                    .componentCode(resultSet.getString("mc.component_code"))
                    .componentImage(resultSet.getString("componentImage"))
                    .componentName(resultSet.getString("mc.component_name"))
                    .productCode(resultSet.getString("p.products_code"))
                    .productName(resultSet.getString("p.products_name"))
                    .productImage(resultSet.getString("p.products_image_big"))
                    .quantity(resultSet.getInt("bc.quantity")+"")
                    .componentId(resultSet.getInt("bc.fk_component_id"))
                    .build();
                barcodeToComponentModelList.add(barcodeToComponentModel);
            }
            summaryModelForBarcodeToComponent.setLabel("Total Products");
            summaryModelForBarcodeToComponent.setValue(count+"");
            summaryModelList.add(summaryModelForBarcodeToComponent);
            barcodeModelListHavingSummaryModel.setBarcodeToComponentDataModelList(barcodeToComponentModelList);
            barcodeModelListHavingSummaryModel.setSummaryModelList(summaryModelList);
        }
        catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return barcodeModelListHavingSummaryModel;
    }
    public boolean deleteBarcode(String productCode) {
        Connection connection = null;
        String statement;
        int status = 0;
        boolean result = false;
        PreparedStatement preparedStatement = null;
        try {
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "DELETE FROM AA_barcode_to_components where barcode=?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1,productCode);
            logger.debug("sql query in deleteBarcode "+preparedStatement);
            status = preparedStatement.executeUpdate();
            if(status!=0){
                result=true;
            }
        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }
    public boolean changeBarcodeComponentUtil(String productCode, String componentCode,int quantity) {
        Connection connection = null;
        ResultSet resultSet=null;
        String statement;
        int status = 0;
        boolean result = false;
        int componentId=0;
        PreparedStatement preparedStatement = null;
        try {
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "SELECT mc.component_id from AA_master_components mc join AA_barcode_to_components" +
                " bc on mc.component_id=bc.fk_component_id where mc.component_code=? and bc.barcode=?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1,componentCode);
            preparedStatement.setString(2,productCode);
            logger.debug("sql query in changeBarcodeComponentUtil "+preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                componentId=resultSet.getInt("mc.component_id");
                if(componentId!=0){
                    statement = "UPDATE AA_barcode_to_components set quantity = ? where fk_component_id = ? and barcode = ?";
                    preparedStatement = connection.prepareStatement(statement);
                    preparedStatement.setInt(1,quantity);
                    preparedStatement.setInt(2,componentId);
                    preparedStatement.setString(3,productCode);
                    logger.debug("sql query in changeBarcodeComponentUtil "+preparedStatement);
                    status = preparedStatement.executeUpdate();
                    if(status!=0) {
                        result = true;
                    }
                }
            }
        } catch (Exception exception) {
            logger.error("Exception in connection : ", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
            Database.INSTANCE.closeResultSet(resultSet);
        }
        return result;
    }
    public BarcodeReportResponseModel getListOfBarcodesUtil(int startLimit, int endLimit) {
        Connection connection = null;
        ResultSet resultSet=null;
        String statement;
        List<String> productCodeList = new ArrayList<>();
        BarcodeReportResponseModel barcodeReportResponseModel = new BarcodeReportResponseModel();
        PreparedStatement preparedStatement = null;
        try {
            String queryTotal="SELECT count(*) as totalNo from AA_barcode_to_components";
            int count =SummaryFunctionsUtil.getCount(queryTotal);

            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = "SELECT barcode from AA_barcode_to_components limit "+startLimit+","+endLimit;
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query in getListOfBarcodesUtil "+preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String  productCode = resultSet.getString("barcode");
                productCodeList.add(productCode);
            }
            barcodeReportResponseModel.setList(productCodeList);
            barcodeReportResponseModel.setCount(count);
        } catch (Exception exception) {
            logger.error("Exception in connection : ", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
            Database.INSTANCE.closeResultSet(resultSet);
        }
        return barcodeReportResponseModel;
    }

    public OrderProductUploadFileReportWithSummary uploadPicReport(String fkAssociateId, String startDate, String endDate, String startLimit,
        String endLimit, Integer orderNo, String deliveryDateFrom, String deliveryDateTo){
        Connection connection=null;
        ResultSet resultSet=null;
        String statement;
        PreparedStatement preparedStatement=null;
        List<OrderProductUploadFileModel> orderProductUploadFileModelList=new ArrayList<>();
        OrderProductUploadFileReportWithSummary orderProductUploadFileReportWithSummary =new OrderProductUploadFileReportWithSummary();
        List<SummaryModel> summaryModelList=new ArrayList<>();
        String queryForCount, s3BaseUrl="",s3BucketName="";;
        int count=0;
        String pathOutForDelivery=null;
        String pathDelivered=null;
        try {
            s3BaseUrl= Environment.getS3baseUrl();
            s3BucketName=Environment.getVendorPanelS3uploadBucketname();
            StringBuilder sb=new StringBuilder("");

            if (startDate!=null && !startDate.isEmpty() ){
                sb.append("and o.date_purchased >='"+startDate+"'");
            }

            if (endDate!=null && !endDate.isEmpty()){
                sb.append("and o.date_purchased <='"+endDate+"'");
            }

            if(deliveryDateFrom!=null && !deliveryDateFrom.isEmpty() ){
                sb.append(" and opei.delivery_date >='"+deliveryDateFrom+"'");
            }

            if (deliveryDateTo!=null && !deliveryDateTo.isEmpty()){
                sb.append("  and opei.delivery_date <='"+deliveryDateTo+"' ");
            }

            if (fkAssociateId!=null && !fkAssociateId.isEmpty()){
                sb.append("  and vap.fk_associate_id ='"+fkAssociateId+"' ");
            }

            if (orderNo!=null )
            {
                sb.append("and op.orders_id="+orderNo+"");
            }
            queryForCount="select count(distinct op.orders_products_id) as totalNo  from orders o join orders_products op on o.orders_id = op.orders_id  join order_product_extra_info   opei on op.orders_products_id =  opei.order_product_id left  join  vp_file_upload vp on   vp.orders_products_id = op.orders_products_id join associate a on a.associate_id = op.fk_associate_id   join products p on p.products_id=op.products_id and p.fk_associate_id = 72 where 1=1 "+sb.toString();
            count=SummaryFunctionsUtil.getCount(queryForCount);

            connection=Database.INSTANCE.getReadOnlyConnection();
            statement="select o.orders_id,a.associate_name,o.date_purchased,opei.delivery_date, "
                + " group_concat(if(vp.type=0,vp.file_path,null) separator ',') outForDelivery , "
                + " group_concat(if(vp.type=1,vp.file_path,null) separator ',') proofOfDelivery,npei.m_img as product_image_url "
                + " from orders o join orders_products op on o.orders_id = op.orders_id  join order_product_extra_info "
                + " opei on op.orders_products_id =  opei.order_product_id left  join  vp_file_upload vp on "
                + " vp.orders_products_id = op.orders_products_id join associate a on a.associate_id = op.fk_associate_id "
                + " join products p on p.products_id=op.products_id and p.fk_associate_id = 72 join newigp_product_extra_info as  npei on npei.products_id=op.products_id where 1=1 "
                +sb.toString()+" group by op.orders_products_id limit "+startLimit+","+endLimit+" ";
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query in while getting uploaded photos for orders "+preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                OrderProductUploadFileModel orderProductUploadFileModel=new OrderProductUploadFileModel();
                orderProductUploadFileModel.setOrderNo(resultSet.getString("o.orders_id"));
                orderProductUploadFileModel.setVendorName(resultSet.getString("a.associate_name"));
                orderProductUploadFileModel.setDate(resultSet.getString("o.date_purchased"));
                orderProductUploadFileModel.setDeliveryDate(resultSet.getString("opei.delivery_date"));
                orderProductUploadFileModel.setProductActualPhoto(resultSet.getString("product_image_url"));
                pathOutForDelivery=resultSet.getString("outForDelivery");
                pathDelivered=resultSet.getString("proofOfDelivery");
                if(pathOutForDelivery==null){
                    orderProductUploadFileModel.setProductPhotosOutOfDelivery(new ArrayList<>());
                }else if(pathOutForDelivery!=null){
                    String [] tempStringArray=pathOutForDelivery.split(",");
                    for(int i=0;i<tempStringArray.length;i++){
                        tempStringArray[i]=s3BaseUrl+"/"+s3BucketName+"/"+tempStringArray[i];
                    }
                    orderProductUploadFileModel.setProductPhotosOutOfDelivery(Arrays.asList(tempStringArray));
                }
                if(pathDelivered==null) {
                    orderProductUploadFileModel.setProductPhotosDelivered(new ArrayList<>());
                }else if(pathDelivered!=null){
                    String [] tempStringArray=pathDelivered.split(",");
                    for(int i=0;i<tempStringArray.length;i++){
                        tempStringArray[i]=s3BaseUrl+"/"+s3BucketName+"/"+tempStringArray[i];
                    }
                    orderProductUploadFileModel.setProductPhotosDelivered(Arrays.asList(tempStringArray));
                }
                orderProductUploadFileModelList.add(orderProductUploadFileModel);
            }
            orderProductUploadFileReportWithSummary.setOrderProductUploadFileModelList(orderProductUploadFileModelList);
            SummaryModel summaryModelTotalOrders= new SummaryModel();
            summaryModelTotalOrders.setLabel("Total orders");
            summaryModelTotalOrders.setValue(String.valueOf(count));
            summaryModelList.add(summaryModelTotalOrders);
            orderProductUploadFileReportWithSummary.setOrderProductUploadFileModelList(orderProductUploadFileModelList);
            orderProductUploadFileReportWithSummary.setSummaryModelList(summaryModelList);
        }catch (Exception exception){
            logger.error("Exception in connection : ", exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
            Database.INSTANCE.closeResultSet(resultSet);
        }
        return orderProductUploadFileReportWithSummary;
    }
    public SlaReportWithSummary getSlaReport(String fkAssociateId, String assignStartDate, String assignEndDate, String startLimit, String endLimit, Integer orderNo, String deliveryDateFrom, String deliveryDateTo){
        List<SlaReportModel> slaReportModelList=new ArrayList<>();
        SlaReportWithSummary slaReportWithSummary=new SlaReportWithSummary();
        Connection connection = null;
        ResultSet resultSet=null;
        String statement=null,queryForCount=null;
        PreparedStatement preparedStatement = null;
        SlaCompliant slaCompliant=new SlaCompliant();
        List<SummaryModel> summaryModelList=new ArrayList<>();
        int count=0;
        try{
            StringBuilder sb=new StringBuilder("");

            if (assignStartDate!=null && !assignStartDate.isEmpty() ){
                sb.append("and vap.assign_time >= '"+assignStartDate+"' ");
            }

            if (assignEndDate!=null && !assignEndDate.isEmpty()){
                sb.append("and o.assign_time <= '"+assignEndDate+"' ");
            }

            if(deliveryDateFrom!=null && !deliveryDateFrom.isEmpty() ){
                sb.append(" and vap.delivery_date >= '"+deliveryDateFrom+"' ");
            }

            if (deliveryDateTo!=null && !deliveryDateTo.isEmpty()){
                sb.append("  and vap.delivery_date <= '"+deliveryDateTo+"' ");
            }

            if (fkAssociateId!=null && !fkAssociateId.isEmpty()){
                sb.append("  and op.fk_associate_id ='"+fkAssociateId+"' ");
            }

            if (orderNo!=null )
            {
                sb.append("and op.orders_id = "+orderNo+" ");
            }

            queryForCount="select count(*) as totalNo from orders_products op join "
                + " vendor_assign_price vap on op.orders_id = vap.orders_id join trackorders track on "
                + " track.orders_products_id = op.orders_products_id left join associate a on op.fk_associate_id "
                + " = a.associate_id where op.products_id = vap.products_id "+sb.toString();
            count=SummaryFunctionsUtil.getCount(queryForCount);

            connection = Database.INSTANCE.getReadOnlyConnection();
            statement="select op.orders_id,a.associate_name,( CASE WHEN vap.assign_time !='0000-00-00 00:00:00' THEN "
                + " vap.assign_time END ) as assignTime,vap.delivery_date,vap.shipping_type,vap.delivery_time, "
                + " ( CASE WHEN track.releaseDate !='0000-00-00 00:00:00' THEN track.releaseDate END ) as releaseDate, "
                + " ( CASE WHEN track.outForDeliveryDate !='0000-00-00 00:00:00' THEN "
                + " track.outForDeliveryDate END ) as outForDeliveryDate,( CASE WHEN track.deliveredDate "
                + " !='0000-00-00 00:00:00' THEN track.deliveredDate END ) as deliveredDate ,op.sla_code1, "
                + " op.sla_code2,op.sla_code3,track.date_purchased,op.orders_product_status from orders_products op join "
                + " vendor_assign_price vap on op.orders_id = vap.orders_id join trackorders track on "
                + " track.orders_products_id = op.orders_products_id left join associate a on op.fk_associate_id "
                + " = a.associate_id where op.products_id = vap.products_id "+sb.toString()+" limit "+startLimit+","+endLimit+" ";

            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query in while slaReport  "+preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                SlaReportModel slaReportModel=new SlaReportModel();
                String deliveryTime=resultSet.getString("vap.delivery_time");
                String datePurchased=resultSet.getString("track.date_purchased");
                String status=resultSet.getString("op.orders_product_status");
                int sla1=resultSet.getInt("op.sla_code1");
                int sla2=resultSet.getInt("op.sla_code2");
                int sla3=resultSet.getInt("op.sla_code3");
                String releaseDate=resultSet.getString("releaseDate");
                String outForDeliveryDate=resultSet.getString("outForDeliveryDate");
                String deliveredDate=resultSet.getString("deliveredDate");

                slaReportModel.setOrderNo(resultSet.getString("op.orders_id"));
                slaReportModel.setVendorName(resultSet.getString("a.associate_name"));
                slaReportModel.setAssignDate(resultSet.getString("assignTime"));
                slaReportModel.setDeliveryDate(resultSet.getString("vap.delivery_date"));
                slaReportModel.setDeliveryType(resultSet.getString("vap.shipping_type"));
                slaReportModel.setStatus(resultSet.getString("op.orders_product_status"));
                if(releaseDate!=null){
                    slaReportModel.setConfirmTime(releaseDate);
                }else{
                    slaReportModel.setConfirmTime("NA");
                }
                if(outForDeliveryDate!=null){
                    slaReportModel.setOutForDeliveryTime(outForDeliveryDate);
                }else{
                    slaReportModel.setOutForDeliveryTime("NA");
                }
                if(deliveredDate!=null){
                    slaReportModel.setDeliveredTime(deliveredDate);
                }else{
                    slaReportModel.setDeliveredTime("NA");
                }
                if(sla1>0){
                    if(releaseDate!=null){
                        slaReportModel.setSla1(slaCompliant.getSlaTime(slaReportModel.getAssignDate(),sla1,releaseDate,
                            datePurchased,slaReportModel.getDeliveryDate(),deliveryTime));
                    }else{
                        slaReportModel.setSla1("NA");
                    }
                }else{
                    slaReportModel.setSla1("0");
                }
                if(sla2>0){
                    if(outForDeliveryDate!=null){
                        slaReportModel.setSla2(slaCompliant.getSlaTime(slaReportModel.getAssignDate(),sla2,outForDeliveryDate,
                            datePurchased,slaReportModel.getDeliveryDate(),deliveryTime));
                    }else{
                        slaReportModel.setSla2("NA");
                    }
                }else{
                    slaReportModel.setSla2("0");
                }
                if(sla3>0){
                    if(deliveredDate!=null){
                        slaReportModel.setSla3(slaCompliant.getSlaTime(slaReportModel.getAssignDate(),sla3,deliveredDate,
                            datePurchased,slaReportModel.getDeliveryDate(),deliveryTime));
                    }else{
                        slaReportModel.setSla3("NA");
                    }
                }else{
                    slaReportModel.setSla3("0");
                }
                slaReportModelList.add(slaReportModel);
            }
            SummaryModel summaryModelTotalOrders= new SummaryModel();
            summaryModelTotalOrders.setLabel("Total orders");
            summaryModelTotalOrders.setValue(String.valueOf(count));
            summaryModelList.add(summaryModelTotalOrders);
            slaReportWithSummary.setSlaReportModelList(slaReportModelList);
            slaReportWithSummary.setSummaryModelList(summaryModelList);

        }catch (Exception exception) {
            logger.error("Exception in connection : ", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
            Database.INSTANCE.closeResultSet(resultSet);
        }
        return slaReportWithSummary;
    }
}
