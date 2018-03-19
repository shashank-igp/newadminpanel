package com.igp.handles.admin.utils.Reports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.igp.config.Environment;
import com.igp.config.instance.Database;
import com.igp.handles.admin.mappers.Reports.ReportMapper;
import com.igp.handles.admin.models.Reports.*;
import com.igp.handles.admin.models.Vendor.VendorInfoModel;
import com.igp.handles.admin.utils.Vendor.VendorUtil;
import com.igp.handles.vendorpanel.models.Report.ReportOrderWithSummaryModel;
import com.igp.handles.vendorpanel.models.Report.SummaryModel;
import com.igp.handles.vendorpanel.models.Report.orderReportObjectModel;
import com.igp.handles.vendorpanel.utils.Reports.SummaryFunctionsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
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
            if (status.equals("Delivered")){
                sb.append("and op.delivery_status=1");
            }
            else if(status.equals("OutForDelivery")){
                sb.append("and  op.orders_product_status='Shipped' and op.delivery_status=0 ");
            }
            else
            {
                sb.append("and op.orders_product_status='"+status+"'");
            }
        }
//        String queryCount = "select count(*) as totalNo  from orders_products as op LEFT JOIN vendor_assign_price as  vap "
//            + " on op.orders_id=vap.orders_id  and  op.products_id=vap.products_id  inner join order_product_extra_info "
//            + " as oe on op.orders_products_id=oe.order_product_id inner  join  orders as o on  vap.orders_id=o.orders_id" +
//            " inner join  orders_occasions  as oo  on o.orders_occasionid=oo.occasion_id where " +
//            "(op.fk_associate_id=72 OR op.fk_associate_id=vap.fk_associate_id) "+sb.toString();
//        int count =SummaryFunctionsUtil.getCount(queryCount);
//        String amount = "select sum((vap.vendor_price+vap.shipping)) as totalNo  from orders_products as op LEFT JOIN vendor_assign_price as  vap "
//            + " on op.orders_id=vap.orders_id  and  op.products_id=vap.products_id  inner join order_product_extra_info "
//            + " as oe on op.orders_products_id=oe.order_product_id inner  join  orders as o on  vap.orders_id=o.orders_id" +
//            " inner join  orders_occasions  as oo  on o.orders_occasionid=oo.occasion_id where " +
//            "(op.fk_associate_id=72 OR op.fk_associate_id=vap.fk_associate_id) "+sb.toString();
//        int amt =SummaryFunctionsUtil.getCount(amount);

        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();

            String queryCount = "select count(*) as totalNo ,sum((vap.vendor_price+vap.shipping)) as totalAmt from orders_products as op LEFT JOIN vendor_assign_price as  vap "
                + " on op.orders_id=vap.orders_id  and  op.products_id=vap.products_id  inner join order_product_extra_info "
                + " as oe on op.orders_products_id=oe.order_product_id inner  join  orders as o on  vap.orders_id=o.orders_id" +
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
                + " op.orders_product_status as opStatus,op.shipping_type_g as Delivery_Type  , o.delivery_name as "
                + " Recipient_Name , o.delivery_mobile as Phone  , (vap.vendor_price+vap.shipping) as Amount, "
                + " op.delivery_status as status  from orders_products as op LEFT JOIN vendor_assign_price as  vap "
                + " on op.orders_id=vap.orders_id  and  op.products_id=vap.products_id  LEFT JOIN associate as a on vap.fk_associate_id=a.associate_id" +
                " inner join order_product_extra_info as oe on op.orders_products_id=oe.order_product_id inner  join  orders as o on  vap.orders_id=o.orders_id "
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
                orderReportObjectModel.setDeliveryType(resultSet.getString("Delivery_Type"));
                orderReportObjectModel.setPrice(resultSet.getDouble("Amount"));
                orderReportObjectModel.setStatus(resultSet.getInt("status"));
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
        Map<String,Map<String,TableDataActionHandels>> pincodeShipTypeAndShipChargeMap=new HashMap<>();

        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = " select a.vendor_id as vId,a.city_id as cId,a.pincode,a.ship_type," +
                "a.ship_charge,a.req_price,aso.associate_name,a.flag_enabled from AA_vendor_pincode as a " +
                "JOIN associate_user as au ON a.vendor_id = au.fk_associate_login_id JOIN" +
                " associate as aso on au.fk_associate_login_id=aso.associate_id " +
                "where vendor_id="+fkAssociateId+" limit "+startLimit+","+endLimit+" ";
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query "+preparedStatement);
            totalQuery = " select count(distinct pincode) as totalno from AA_vendor_pincode where vendor_id="+fkAssociateId+"";
            resultSet = preparedStatement.executeQuery();
            List flagList = new ArrayList<>();
            while(resultSet.next()) {
                Map<String,TableDataActionHandels> shipTypeAndTableMap = new HashMap<>();
                String pincode=resultSet.getString("a.pincode").trim();
                String shipType=resultSet.getString("a.ship_type").trim();
                String shipCharge=resultSet.getString("a.ship_charge").trim();
                String reqPrice=resultSet.getString("a.req_price").trim();
                int flagEnabled = resultSet.getInt("flag_enabled");
                TableDataActionHandels tableDataActionHandels = new TableDataActionHandels();
                tableDataActionHandels.setValue(shipCharge);
                tableDataActionHandels.setRequestValue(reqPrice);
                if(!reqPrice.equals("-1")){
                    tableDataActionHandels.setRequestType("Approve/Reject");
                }
                shipTypeAndTableMap.put(shipType,tableDataActionHandels);
                if(pincodeShipTypeAndShipChargeMap.get(pincode)==null){
                    pincodeShipTypeAndShipChargeMap.put(pincode,shipTypeAndTableMap);
                }else {
                    pincodeShipTypeAndShipChargeMap.get(pincode).put(shipType,tableDataActionHandels);
                }
                vendorName  = resultSet.getString("aso.associate_name");
                flagList.add(flagEnabled);
            }
            int i = 0;
            for(Map.Entry<String,Map<String,TableDataActionHandels>> entry:pincodeShipTypeAndShipChargeMap.entrySet()){
                PincodeTableDataModel pincodeTableDataModel =new PincodeTableDataModel();
                String pincode = entry.getKey();
                pincodeTableDataModel.setPincode(pincode);
                Map<String,TableDataActionHandels> shipTypeAndTableMap = entry.getValue();
                for (Map.Entry<String,TableDataActionHandels> entry1: shipTypeAndTableMap.entrySet()){
                    String shipType=entry1.getKey();  // this will have shiptype
                    TableDataActionHandels tableDataActionHandels = entry1.getValue();
                    if(shipType.equalsIgnoreCase("1")){
                        pincodeTableDataModel.setStandardDeliveryCharge(tableDataActionHandels);
                    }else if(shipType.equalsIgnoreCase("2")){
                        pincodeTableDataModel.setFixedTimeDeliveryCharge(tableDataActionHandels);
                    }else if(shipType.equalsIgnoreCase("3")){
                        pincodeTableDataModel.setMidnightDeliveryCharge(tableDataActionHandels);
                    }
                }
                TableDataActionHandels tableDataActionHandels = new TableDataActionHandels();
                tableDataActionHandels.setValue("Not Serviceable");
                tableDataActionHandels.setRequestValue("-1");
                if(pincodeTableDataModel.getStandardDeliveryCharge()==null || flagList.get(i).equals(0)){
                    pincodeTableDataModel.setStandardDeliveryCharge(tableDataActionHandels);
                }
                if(pincodeTableDataModel.getFixedTimeDeliveryCharge()==null || flagList.get(i).equals(0)){
                    pincodeTableDataModel.setFixedTimeDeliveryCharge(tableDataActionHandels);
                }
                if(pincodeTableDataModel.getMidnightDeliveryCharge()==null || flagList.get(i).equals(0)){
                    pincodeTableDataModel.setMidnightDeliveryCharge(tableDataActionHandels);
                }
                pincodeTableDataModel.setVendorId(fkAssociateId);
                pincodeTableDataModel.setVendorName(vendorName);
                pincodeTableDataModelList.add(pincodeTableDataModel);
                i++;
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
                }
                else if (flag == 1) {
                    updateClause = "flag_enabled=" + flag+",req_price = -1 ";
                } else {
                    updateClause = "ship_charge=" + updatePrice+",req_price = -1 ";
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
    public boolean addNewVendorPincodeUtil(int fkAssociateId,int pincode,int cityId,int shipType,int shipCharge){
        Connection connection = null;
        String statement;
        boolean result = false;
        PreparedStatement preparedStatement = null;
        int status=0;
        try {
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "INSERT INTO AA_vendor_pincode (vendor_id,city_id,pincode,ship_type," +
                "ship_charge,req_price) VALUES (?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,fkAssociateId);
            preparedStatement.setInt(2,cityId);
            preparedStatement.setInt(3,pincode);
            preparedStatement.setInt(4,shipType);
            preparedStatement.setInt(5,shipCharge);
            preparedStatement.setInt(6,-1);
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
                    tableDataActionHandels.setRequestType("Approve/Reject");
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
                    tableDataActionHandels1.setRequestType("Approve/Reject");
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
    public boolean approveAndRejectUtil(String object, String reportName, String columnName, int fkAssociateId,boolean approveReject){
        ObjectMapper objectMapper = new ObjectMapper();
        TableDataActionHandels actionHandels = new TableDataActionHandels();
        String message;
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
                        actionHandels = pincodeTableDataModel.getFixedTimeDeliveryCharge();
                        shipType = 3;
                        break;
                }

                if(actionHandels.getValue().equals(actionHandels.getRequestValue())){
                    if(approveReject==true){
                        // request is to enable.
                        message="Enable/Disable "+shipType+" for Pincode "+pincodeTableDataModel.getPincode()+" : Request Accepted";
                        result = reportMapper.updatePincodeMapper(1,fkAssociateId,Integer.parseInt(pincodeTableDataModel.getPincode()),shipType,Integer.parseInt(actionHandels.getRequestValue()),message,"");
                    }
                    else {
                        // request to enable rejected.
                        message="Enable/Disable "+shipType+" for Pincode "+pincodeTableDataModel.getPincode()+" : Request Rejected";
                        result = reportMapper.updatePincodeMapper(1,fkAssociateId,Integer.parseInt(pincodeTableDataModel.getPincode()),shipType,-1,message,"reqPrice");
                    }
                }
                else {
                    if(approveReject==true) {
                        // request is to update the price.
                        message = "Update the price of " + shipType + " for Pincode " + pincodeTableDataModel.getPincode() + " to " + actionHandels.getRequestValue() + " : Request Accepted";
                        result = reportMapper.updatePincodeMapper(0, fkAssociateId, Integer.parseInt(pincodeTableDataModel.getPincode()), shipType, Integer.parseInt(actionHandels.getRequestValue()), message, "");

                    } else {
                        // request to update the price rejected.
                        message = "Update the price of " + shipType + " for Pincode " + pincodeTableDataModel.getPincode() + " to " + actionHandels.getRequestValue() + " : Request Rejected";
                        reportMapper.updatePincodeMapper(0,fkAssociateId,Integer.parseInt(pincodeTableDataModel.getPincode()),shipType,-1,message,"reqPrice");
                    }
                }
            }else if(reportName.equals("getVendorReport")){
                ProductTableDataModel productTableDataModel = objectMapper.readValue(object,ProductTableDataModel.class);
                if(columnName.equals("InStock")){
                    actionHandels = productTableDataModel.getInStock();
                    if(actionHandels.getValue().equals(actionHandels.getRequestValue())){
                        if(approveReject==true){
                            // request is to enable.
                            message="Change status of component "+productTableDataModel.getComponentName()+" to Instock : Approved";
                            result = reportMapper.updateComponentMapper(fkAssociateId,productTableDataModel.getComponent_Id_Hide(),message,-1,"1","");
                        }
                        else {
                            // request to enable rejected.
                            message="Change status of component "+productTableDataModel.getComponentName()+" to Instock : Rejected";
                            result = reportMapper.updateComponentMapper(fkAssociateId,productTableDataModel.getComponent_Id_Hide(),message,-1,"0","");
                        }
                    }
                }
                else {
                    actionHandels = productTableDataModel.getPrice();
                    //for update price
                    if(!actionHandels.getValue().equals(actionHandels.getRequestValue())) {

                        if (approveReject == true) {
                            // request is to update the price.
                            message = "Change price of component " + productTableDataModel.getComponentName() + " to " + actionHandels.getRequestValue() + " : Accepted";
                            result = reportMapper.updateComponentMapper(fkAssociateId, productTableDataModel.getComponent_Id_Hide(), message, Integer.parseInt(actionHandels.getRequestValue()), "1", "");

                        } else {
                            // request to update the price rejected.
                            message = "Change price of component " + productTableDataModel.getComponentName() + " to " + actionHandels.getRequestValue() + " : ";
                            result = reportMapper.updateComponentMapper(fkAssociateId, productTableDataModel.getComponent_Id_Hide(), message, -1, "1", "reqPrice");
                        }
                    }
                }

            }


        }catch (Exception exception){
            logger.error("Error at approveAndRejectUtil in ReportUtil : ",exception);
        }
        return result;
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
}
