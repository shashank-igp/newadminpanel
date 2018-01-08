package com.igp.handles.vendorpanel.utils.Reports;

import com.igp.config.instance.Database;
import com.igp.handles.vendorpanel.models.Report.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by shal on 22/9/17.
 */
public class SummaryFunctionsUtil
{
    private static final Logger logger = LoggerFactory.getLogger(SummaryFunctionsUtil.class);

    public static ReportOrderWithSummaryModel    getSummaryDetailsForVendor(String fkAssociateId,String startDate,String endDate,String startLimit,String endLimit,Integer orderNo,String delhiveryDate,String status,String deliveryDateFrom,String deliveryDateTo){
        Connection connection = null;

        ReportOrderWithSummaryModel reportOrderWithSummaryModel=new ReportOrderWithSummaryModel();
        List <orderReportObjectModel>  orderReportObjectList= new ArrayList<>();
        String statement;
        String whereClause="";
        List <SummaryModel> summaryModelList=new ArrayList<>();
        ResultSet resultSet = null;




        StringBuilder sb=new StringBuilder("");
        if ((delhiveryDate != null && !delhiveryDate.isEmpty())){
            sb.append("and oe.delivery_date='"+delhiveryDate+"'");

        }
         if (startDate!=null && !startDate.isEmpty() ){
            sb.append("and vap.assign_time >='"+startDate+"'");
        }

        if (endDate!=null && !endDate.isEmpty()){
            sb.append("and vap.assign_time <='"+endDate+"'");
        }

         if(deliveryDateFrom!=null && !deliveryDateFrom.isEmpty() ){
            sb.append(" and vap.delivery_date >='"+deliveryDateFrom+"'");
        }

        if (deliveryDateTo!=null && !deliveryDateTo.isEmpty()){
             sb.append("  and vap.delivery_date <='"+deliveryDateTo+"' ");
        }

         if (orderNo!=null )
        {
            sb.append("and vap.orders_id="+orderNo+"");
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

        String res=getTotalAndOrder(sb,fkAssociateId);

        String[] parts = res.split("-");

        PreparedStatement preparedStatement = null;
        try{
                connection = Database.INSTANCE.getReadOnlyConnection();
            statement = " select  vap.assign_time as Date,vap.orders_id as  Order_No,oo.occasion_name as Ocassion , "
                + " o.delivery_city as City ,o.delivery_postcode as Pincode ,vap.delivery_date  as Delivery_Date , "
                + " op.orders_product_status as opStatus,op.shipping_type_g as Delivery_Type  , o.delivery_name as "
                + " Recipient_Name , o.delivery_mobile as Phone  , (vap.vendor_price+vap.shipping) as Amount, "
                + " op.delivery_status as status  from vendor_assign_price as  vap  inner join  orders_products as "
                + " op on vap.orders_id=op.orders_id  and  vap.products_id=op.products_id  inner join order_product_extra_info "
                + " as oe on op.orders_products_id=oe.order_product_id inner  join  orders as o on  vap.orders_id=o.orders_id "
                + " inner join  orders_occasions  as oo  on o.orders_occasionid=oo.occasion_id    where "
                + " vap.fk_associate_id="+fkAssociateId+"  "+sb.toString()+" limit "+startLimit+","+endLimit+" ";
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query in getSummaryDetailsForVendor "+preparedStatement);
            resultSet = preparedStatement.executeQuery();
            Double totalPrice=0.00;
            Double value=0.00;
            while(resultSet.next()) {
                orderReportObjectModel orderReportObjectModel = new orderReportObjectModel();
                orderReportObjectModel.setDate(resultSet.getString("Date"));
                orderReportObjectModel.setOrderNo(resultSet.getString("Order_No"));
                orderReportObjectModel.setOccasion(resultSet.getString("Ocassion"));
                orderReportObjectModel.setCity(resultSet.getString("City"));
                orderReportObjectModel.setPincode(resultSet.getInt("Pincode"));
                orderReportObjectModel.setDelivery_Date(resultSet.getString("Delivery_Date"));
                orderReportObjectModel.setDeliveryType(resultSet.getString("Delivery_Type"));
                orderReportObjectModel.setRecipienName(resultSet.getString("Recipient_Name"));
                orderReportObjectModel.setPrice(resultSet.getDouble("Amount"));
                orderReportObjectModel.setPhoneNumber(resultSet.getString("Phone"));
                orderReportObjectModel.setStatus(resultSet.getInt("status"));

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
            summaryModelTotalOrders.setValue(parts[0]);
            summaryModelList.add(summaryModelTotalOrders);
            SummaryModel summaryModelTotalAmount= new SummaryModel();
            summaryModelTotalAmount.setLabel("Total Amount");
            summaryModelTotalAmount.setValue(parts[1]);
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


    public static VendorModelListWithSummary getVendorDetailsFunction (String fkAssociateId,String startLimit,String endLimit)
    {

        Connection connection = null;
        VendorModelListWithSummary vendorModelListWithSummary=new VendorModelListWithSummary();
        List <VendorReportObject>  vendorReportObjectList= new ArrayList<>();
        String statement;
        String whereClause="";
        List <SummaryModel> summaryModelList=new ArrayList<>();
        ResultSet resultSet = null;
        String queryTotal="";

        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = " select vc.fk_associate_id as fkId,vc.fk_component_id cId,vc.price as price,vc.InStock  as "
                + " inStock,mc.component_code as cCode,mc.component_name as cName,mc.componentImage as cImage,mc.mod_time as cTimestamp from AA_vendor_to_components as vc inner "
                + " join AA_master_components as mc on vc.fk_component_id=mc.component_id "
                + " where vc.fk_associate_id='"+fkAssociateId+"' limit "+startLimit+","+endLimit+" ";
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query in getVendorDetailsFunction "+preparedStatement);

            queryTotal="select count(*) as totalno from AA_vendor_to_components as vc inner join AA_master_components as mc on vc.fk_component_id=mc.component_id where vc.fk_associate_id='"+fkAssociateId+"'";

            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                VendorReportObject vendorReportObject=new VendorReportObject();
                vendorReportObject.setAssociateId("fkId");
                vendorReportObject.setComponent_Id_Hide(resultSet.getString("cId"));
                vendorReportObject.setComponentImage(resultSet.getString("cImage")+"?timestamp="+resultSet.getString("cTimestamp"));
                vendorReportObject.setComponentName(resultSet.getString("cName"));
                vendorReportObject.setPrice(resultSet.getDouble("price"));
                Integer inStock=resultSet.getInt("inStock");
                if (inStock==1){vendorReportObject.setInStock("In Stock");}
                else {vendorReportObject.setInStock("Out Of Stock");}

                vendorReportObjectList.add(vendorReportObject);
            }
            vendorModelListWithSummary.setVendorReportObjectList(vendorReportObjectList);
            SummaryModel summaryModelTotalComponents= new SummaryModel();
            summaryModelTotalComponents.setLabel("Total Components");
            summaryModelTotalComponents.setValue(getCount(queryTotal).toString());
            summaryModelList.add(summaryModelTotalComponents);
            vendorModelListWithSummary.setSummaryModelList(summaryModelList);
        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return  vendorModelListWithSummary;


    }

    public static boolean updateVendorComponet(Integer flag,String fk_associate_id,String  componentId ) {
        Connection connection = null;
        String statement;
        boolean result = false;
        PreparedStatement preparedStatement = null;
        try {


            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "update AA_vendor_to_components set flag_change="+flag+" where fk_associate_id="+fk_associate_id+" and fk_component_id="+componentId+" ";
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query in updateVendorComponet "+preparedStatement);
            Integer nums = preparedStatement.executeUpdate();
            if (nums!=null){result = true;}


        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;

    }



    public static PincodeModelListWithSummary getPincodeDetailsFunction (String fkAssociateId,String startLimit,String endLimit)
    {

        Connection connection = null;
        PincodeModelListWithSummary pincodeModelListWithSummary=new PincodeModelListWithSummary();
        List <PincodeReportModel>  pincodeReportModelList= new ArrayList<>();
        String statement;
        String whereClause="";
        String totalQuery="";
        List <SummaryModel> summaryModelList=new ArrayList<>();
        ResultSet resultSet = null;
        Map<String,Map<String,String>> pincodeShipTypeAndShipChargeMap=new HashMap<>();

        PreparedStatement preparedStatement = null;
        try{


            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = " select vendor_id as vId,city_id as cId,pincode,ship_type,ship_charge from AA_vendor_pincode where vendor_id="+fkAssociateId+" limit "+startLimit+","+endLimit+" ";
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query "+preparedStatement);
            totalQuery = " select count(distinct pincode) as totalno from AA_vendor_pincode where vendor_id="+fkAssociateId+"";
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String pincode=resultSet.getString("pincode").trim();
                String shipType=resultSet.getString("ship_type").trim();
                String shipCharge=resultSet.getString("ship_charge").trim();
                if(pincodeShipTypeAndShipChargeMap.get(pincode)==null){
                    Map<String,String> shipTypeToChargeMap=new HashMap<>();
                    shipTypeToChargeMap.put(shipType,shipCharge);
                    pincodeShipTypeAndShipChargeMap.put(pincode,shipTypeToChargeMap);
                }else {
                    pincodeShipTypeAndShipChargeMap.get(pincode).put(shipType,shipCharge);
                }
            }
            for(Map.Entry<String,Map<String,String>> entry:pincodeShipTypeAndShipChargeMap.entrySet()){
                PincodeReportModel pincodeReportModel=new PincodeReportModel();
                String pincode = entry.getKey();
                pincodeReportModel.setPincode(pincode);
                Map<String,String> shipTypeToChargeMap=entry.getValue();
                for (Map.Entry<String,String> entry1:shipTypeToChargeMap.entrySet()){
                    String shipType=entry1.getKey();
                    String shipCharge=entry1.getValue();
                    if(shipType.equalsIgnoreCase("1")){
                        pincodeReportModel.setStandardDeliveryCharge(shipCharge);
                    }else if(shipType.equalsIgnoreCase("2")){
                        pincodeReportModel.setFixedTimeDeliveryCharge(shipCharge);
                    }else if(shipType.equalsIgnoreCase("3")){
                        pincodeReportModel.setMidnightDeliveryCharge(shipCharge);
                    }
                }
                if(pincodeReportModel.getStandardDeliveryCharge()==null){
                    pincodeReportModel.setStandardDeliveryCharge("Not Serviceable");
                }
                if(pincodeReportModel.getFixedTimeDeliveryCharge()==null){
                    pincodeReportModel.setFixedTimeDeliveryCharge("Not Serviceable");
                }
                if(pincodeReportModel.getMidnightDeliveryCharge()==null){
                    pincodeReportModel.setMidnightDeliveryCharge("Not Serviceable");
                }
                pincodeReportModelList.add(pincodeReportModel);
            }
            pincodeModelListWithSummary.setPincodeReportModelList(pincodeReportModelList);

            SummaryModel summaryModelTotalPincodes= new SummaryModel();
            summaryModelTotalPincodes.setLabel("Total Pincodes");
            summaryModelTotalPincodes.setValue(getCount(totalQuery).toString());
            summaryModelList.add(summaryModelTotalPincodes);

            pincodeModelListWithSummary.setSummaryModelList(summaryModelList);


        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return  pincodeModelListWithSummary;


    }

    public static boolean updateVendorPincode(Integer flag,String fk_associate_id,String pincode,Integer shipType,Integer updateStatus,Double updatePrice ) {
        Connection connection = null;
        String statement;
        String updateClause="";
        boolean result = false;
        PreparedStatement preparedStatement = null;
        try {
            if (updateStatus!=null){
                updateClause=",flag_enabled="+updateStatus+"";
            }
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "update AA_vendor_pincode set flag_change="+flag+""+updateClause+" where vendor_id="+fk_associate_id+" and pincode="+pincode+" and  ship_type="+shipType+" ";
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query in updateVendorPincode "+preparedStatement);
            Integer nums = preparedStatement.executeUpdate();
            if (nums!=null){result = true;}
        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }

    public static String getComponentName(String componentId){
        String result="";
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try
        {
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = " SELECT * from AA_master_components where component_id = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1,componentId);
            logger.debug("sql query in getComponentName "+preparedStatement);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                result=resultSet.getString("component_name");
            }
        }catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }
    public static boolean addVendorComponent(String fk_associate_id,String componentCode,String componentName,int type,String componentImagePath,int price){
        Connection connection = null;
        String statement;
        boolean result = false;
        PreparedStatement preparedStatement = null;
        int autoGeneratedID=0,status=0;
        try {
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "INSERT INTO AA_master_components (component_code,component_name,type,componentImage) VALUES (?,?,?,?)";
            preparedStatement = connection.prepareStatement(statement);
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
                preparedStatement.setInt(4,1); //Instock default
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
    public static String getTotalAndOrder(StringBuilder query,String fkAssociateId){
        String result="";
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try
        {
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = "select   count(*) as total,sum((vap.vendor_price+vap.shipping)) as Amount from vendor_assign_price as  vap  inner join  orders_products as op on vap.orders_id=op.orders_id  and  vap.products_id=op.products_id  inner join order_product_extra_info as oe on op.orders_products_id=oe.order_product_id inner  join  orders as o on  vap.orders_id=o.orders_id inner join  orders_occasions  as oo  on o.orders_occasionid=oo.occasion_id  where vap.fk_associate_id="+fkAssociateId+"   "+query.toString()+" "  ;
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query in getTotalAndOrder "+preparedStatement);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                result=""+resultSet.getInt("total")+"-"+resultSet.getInt("Amount")+"";

            }
        }catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }

    public  static String getTimestampString (String date,Integer flag){

        //flag ==0 yyyy/MM/dd to yyyy-MM-dd 00:00:00.0    and flag==1 same but add day+1
        //   and flag==2  yyyy/MM/dd to yyyy-MM-dd   no time stam

        Timestamp timestamp = null;
        if (date ==null || date.isEmpty()){return "";}
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        try {
            timestamp = new Timestamp(((java.util.Date)df.parse(date)).getTime());
            if (flag==1){
                Calendar cal = Calendar.getInstance();
                cal.setTime(timestamp);
                cal.add(Calendar.DAY_OF_WEEK, 1);
                timestamp.setTime(cal.getTime().getTime()); // or
                timestamp = new Timestamp(cal.getTime().getTime());

                return timestamp.toString();
            }
            else if (flag==0){
                return timestamp.toString();
            }else if(flag==2) {
                return simpleDateFormat.format(df.parse(date));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

         return  "";
    }

    public static Integer getCount(String query){

        Connection connection = null;
        String statement;
        Integer  total = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try
        {
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = query;
            preparedStatement = connection.prepareStatement(statement);

            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){

                total=resultSet.getInt("totalNo");
            }
        }catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }


        return total;
    }
//    public static PincodeModelListWithSummary getPincodeDetailsFunction (String fkAssociateId,String startLimit,String endLimit)
//    {
//
//        Connection connection = null;
//        PincodeModelListWithSummary pincodeModelListWithSummary=new PincodeModelListWithSummary();
//        List <PincodeReportModel>  pincodeReportModelList= new ArrayList<>();
//        String statement;
//        String whereClause="";
//        String totalQuery="";
//        List <SummaryModel> summaryModelList=new ArrayList<>();
//        ResultSet resultSet = null;
//        Map<String,String> shippingTypeMap=new HashMap<>();
//        shippingTypeMap.put("1","");
//        shippingTypeMap.put("2","");
//        shippingTypeMap.put("3","");
//        shippingTypeMap.put("4","");
//
//        PreparedStatement preparedStatement = null;
//        try{
//
//
//            connection = Database.INSTANCE.getReadOnlyConnection();
//            statement = " select vendor_id as vId,city_id as cId,pincode,ship_type,ship_charge from AA_vendor_pincode where vendor_id="+fkAssociateId+" limit "+startLimit+","+endLimit+" ";
//            preparedStatement = connection.prepareStatement(statement);
//            totalQuery = " select count(*) as totalno from AA_vendor_pincode where vendor_id="+fkAssociateId+"";
//            resultSet = preparedStatement.executeQuery();
//            while(resultSet.next()) {
//                PincodeReportModel pincodeReportModel=new PincodeReportModel();
//                pincodeReportModel.setCityId(resultSet.getString("cId"));
//                pincodeReportModel.setPincode(resultSet.getString("pincode"));
//                pincodeReportModel.setShipType(resultSet.getString("ship_type"));
//                pincodeReportModel.setShipCharge(resultSet.getDouble("ship_charge"));
//                pincodeReportModelList.add(pincodeReportModel);
//            }
//            pincodeModelListWithSummary.setPincodeReportModelList(pincodeReportModelList);
//
//            SummaryModel summaryModelTotalPincodes= new SummaryModel();
//            summaryModelTotalPincodes.setLabel("Total Pincodes");
//            summaryModelTotalPincodes.setValue(getCount(totalQuery).toString());
//            summaryModelList.add(summaryModelTotalPincodes);
//
//            pincodeModelListWithSummary.setSummaryModelList(summaryModelList);
//
//
//        } catch (Exception exception) {
//            logger.error("Exception in connection", exception);
//        } finally {
//            Database.INSTANCE.closeStatement(preparedStatement);
//            Database.INSTANCE.closeResultSet(resultSet);
//            Database.INSTANCE.closeConnection(connection);
//        }
//        return  pincodeModelListWithSummary;
//
//
//    }

}
