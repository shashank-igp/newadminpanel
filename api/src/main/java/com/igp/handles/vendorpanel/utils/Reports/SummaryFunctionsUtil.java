package com.igp.handles.vendorpanel.utils.Reports;

import com.igp.config.instance.Database;
import com.igp.handles.admin.models.Reports.*;
import com.igp.handles.vendorpanel.models.Report.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
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
            sb.append(" and oe.delivery_date >='"+deliveryDateFrom+"'");
        }

        if (deliveryDateTo!=null && !deliveryDateTo.isEmpty()){
            sb.append("  and oe.delivery_date <='"+deliveryDateTo+"' ");
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


    public static ProductModelListHavingSummaryModel getVendorDetailsFunction (String fkAssociateId,String startLimit,String endLimit)
    {

        Connection connection = null;
        ProductModelListHavingSummaryModel productModelListHavingSummaryModel =new ProductModelListHavingSummaryModel();
        List <ProductTableDataModel>  productTableDataModelList= new ArrayList<>();
        String statement;
        List <SummaryModel> summaryModelList=new ArrayList<>();
        ResultSet resultSet = null;
        String queryTotal="";

        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = " select vc.fk_associate_id as fkId,vc.fk_component_id cId,vc.price as price,vc.InStock  as "
                + " inStock,mc.component_code as cCode,mc.component_name as cName,mc.componentImage as cImage,mc.mod_time as cTimestamp, vc.req_price from AA_vendor_to_components as vc inner "
                + " join AA_master_components as mc on vc.fk_component_id=mc.component_id "
                + " where vc.fk_associate_id='"+fkAssociateId+"' limit "+startLimit+","+endLimit+" ";
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query in getVendorDetailsFunction "+preparedStatement);

            queryTotal="select count(*) as totalno from AA_vendor_to_components as vc inner join AA_master_components as mc on vc.fk_component_id=mc.component_id where vc.fk_associate_id='"+fkAssociateId+"'";

            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                ProductTableDataModel productTableDataModel=new ProductTableDataModel();
                productTableDataModel.setAssociateId("fkId");
                productTableDataModel.setComponent_Id_Hide(resultSet.getString("cId"));
                productTableDataModel.setComponentImage(resultSet.getString("cImage")+"?timestamp="+resultSet.getString("cTimestamp"));
                productTableDataModel.setComponentName(resultSet.getString("cName"));

                int price = resultSet.getInt("price");
                String reqPrice = resultSet.getString("vc.req_price");
                Integer inStock=resultSet.getInt("inStock");

                TableDataActionHandels tableDataActionHandels = new TableDataActionHandels();
                tableDataActionHandels.setValue(price+"");
                tableDataActionHandels.setRequestValue("-1");

                TableDataActionHandels tableDataActionHandels1 = new TableDataActionHandels();
                tableDataActionHandels1.setRequestValue("-1");
                if (inStock == 1) {
                    tableDataActionHandels1.setValue("In Stock");
                } else {
                    tableDataActionHandels1.setValue("Out of Stock");
                }
                if(!reqPrice.equals("-1")){
                    if(tableDataActionHandels.getValue().equals(reqPrice) && inStock==0){
                        tableDataActionHandels1.setRequestType("");
                        tableDataActionHandels1.setRequestValue("In Stock");
                    }else if(!tableDataActionHandels.getValue().equals(reqPrice)){
                        tableDataActionHandels.setRequestType("");
                        tableDataActionHandels.setRequestValue(reqPrice);
                    }
                } else if(reqPrice.equals("-1") && inStock==0 ){
                    tableDataActionHandels1.setRequestType("InStock");
                    tableDataActionHandels1.setRequestValue("Out of Stock");
                }

                productTableDataModel.setPrice(tableDataActionHandels);

                productTableDataModel.setInStock(tableDataActionHandels1);

                productTableDataModelList.add(productTableDataModel);
            }
            productModelListHavingSummaryModel.setProductTableDataModelList(productTableDataModelList);
            SummaryModel summaryModelTotalComponents= new SummaryModel();
            summaryModelTotalComponents.setLabel("Total Components");
            summaryModelTotalComponents.setValue(getCount(queryTotal).toString());
            summaryModelList.add(summaryModelTotalComponents);
            productModelListHavingSummaryModel.setSummaryModelList(summaryModelList);
        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return  productModelListHavingSummaryModel;
    }

    public static boolean updateVendorComponent(Integer flag,String fk_associate_id,String  componentId, String price) {
        boolean result = false;
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        try {
            if(flag == 1){
                // bring it in stock.
                statement = "update AA_vendor_to_components set req_price = price where fk_associate_id=" + fk_associate_id + " and fk_component_id=" + componentId + " ";
                // copy the same price in req_price

            }else if(flag == 2) {
                // mark it out of stock.
                statement = "update AA_vendor_to_components set InStock = 0 where fk_associate_id=" + fk_associate_id + " and fk_component_id=" + componentId + " ";
            }
            else {
                // flag = 3 i.e. change the price.
                statement = "update AA_vendor_to_components set req_price = "+price+" where fk_associate_id=" + fk_associate_id + " and fk_component_id=" + componentId + " and InStock = 1 ";
                // copy the requested price in req_price
            }
            connection = Database.INSTANCE.getReadWriteConnection();
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query in updateVendorComponet " + preparedStatement);
            int nums = preparedStatement.executeUpdate();
            if (nums != 0) {
                result = true;
            }

        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;

    }

    public static PincodeModelListHavingSummaryModel getPincodeDetailsFunction (String fkAssociateId, String startLimit, String endLimit)
    {

        Connection connection = null;
        PincodeModelListHavingSummaryModel pincodeModelListHavingSummaryModel =new PincodeModelListHavingSummaryModel();
        String statement;
        String totalQuery="";
        List <SummaryModel> summaryModelList=new ArrayList<>();
        ResultSet resultSet = null;
        Map<Integer,Map<Integer,TableDataActionHandels>> pincodeShipTypeAndShipChargeMap=new HashMap<>();

        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = "select pincode, group_concat(ship_type,',',flag_enabled,',',ship_charge,',',req_price SEPARATOR ':' ) as value from AA_vendor_pincode where vendor_id = "+fkAssociateId+" group by pincode limit "+startLimit+","+endLimit+" ";
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query "+preparedStatement);
            totalQuery = " select count(distinct pincode) as totalno from AA_vendor_pincode where vendor_id="+fkAssociateId+" ";
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                VendorPincodeModel vendorPincodeModel = new VendorPincodeModel();
                List<PincodeTableDataModel> pincodeTableDataModelList = new ArrayList<>();

                vendorPincodeModel.setPincode(resultSet.getInt("pincode"));

                String[] valueArray = resultSet.getString("value").split(":");

                for(int i=0; i<valueArray.length ; i++){
                    String[] values = valueArray[i].split(",");
                    vendorPincodeModel.setShipTypeId(new Integer(values[0]).intValue());
                    vendorPincodeModel.setFlagEnabled(new Integer(values[1]).intValue());
                    vendorPincodeModel.setShipCharge(new Integer(values[2]).intValue());
                    vendorPincodeModel.setReqdPrice(new Integer(values[3]).intValue());

                    TableDataActionHandels tableDataActionHandels = new TableDataActionHandels();
                    tableDataActionHandels.setValue(vendorPincodeModel.getShipCharge()+"");
                    tableDataActionHandels.setRequestValue(vendorPincodeModel.getReqdPrice()+"");

                    if(vendorPincodeModel.getFlagEnabled()==0){
                        // when the ship type at pincode is disabled so it must be not servicable
                        if(vendorPincodeModel.getReqdPrice()!=-1 && tableDataActionHandels.getValue().equals(tableDataActionHandels.getRequestValue())){
                            tableDataActionHandels.setRequestType("");
                            tableDataActionHandels.setRequestValue("Not Serviceable");
                        }else if(vendorPincodeModel.getReqdPrice()==-1){
                            tableDataActionHandels.setRequestType("Enable");
                            tableDataActionHandels.setRequestValue(vendorPincodeModel.getShipCharge()+"");
                        }
                        tableDataActionHandels.setValue("Not Serviceable"); // to mark all the pincodes with flag_enabled =0 as not serviceable
                    } else{
                        if(vendorPincodeModel.getReqdPrice()!=-1 && !tableDataActionHandels.getValue().equals(tableDataActionHandels.getRequestValue())){
                            //  check if price change
                            tableDataActionHandels.setRequestType("");
                        } else {
                            //everthing is good
                            tableDataActionHandels.setRequestValue("-1");
                        }
                    }
                    if (tableDataActionHandels.getValue()==null){
                        tableDataActionHandels.setValue("Not Serviceable");
                    }

                    if(pincodeShipTypeAndShipChargeMap.get(vendorPincodeModel.getPincode())==null){
                        Map<Integer,TableDataActionHandels> shipTypeAndTableMap = new HashMap<>();

                        shipTypeAndTableMap.put(vendorPincodeModel.getShipTypeId(), tableDataActionHandels);

                        pincodeShipTypeAndShipChargeMap.put(vendorPincodeModel.getPincode(),shipTypeAndTableMap);
                    }else {
                        pincodeShipTypeAndShipChargeMap.get(vendorPincodeModel.getPincode()).put(vendorPincodeModel.getShipTypeId(),tableDataActionHandels);
                    }

                }
                for(Map.Entry<Integer,Map<Integer,TableDataActionHandels>> entry:pincodeShipTypeAndShipChargeMap.entrySet()){
                    PincodeTableDataModel pincodeTableDataModel =new PincodeTableDataModel();
                    int pincode = entry.getKey();
                    pincodeTableDataModel.setPincode(pincode+"");
                    Map<Integer,TableDataActionHandels> shipTypeToChargeMap=entry.getValue();
                    for (Map.Entry<Integer,TableDataActionHandels> entry1:shipTypeToChargeMap.entrySet()){
                        int shipType=entry1.getKey();
                        TableDataActionHandels tableDataActionHandels=entry1.getValue();
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
                    pincodeTableDataModel.setVendorName(""); // ignore it
                    pincodeTableDataModelList.add(pincodeTableDataModel);
                }
                pincodeModelListHavingSummaryModel.setPincodeTableDataModelList(pincodeTableDataModelList);
            }

            SummaryModel summaryModelTotalPincodes= new SummaryModel();
            summaryModelTotalPincodes.setLabel("Total Pincodes");
            summaryModelTotalPincodes.setValue(getCount(totalQuery).toString());
            summaryModelList.add(summaryModelTotalPincodes);

            pincodeModelListHavingSummaryModel.setSummaryModelList(summaryModelList);

        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return  pincodeModelListHavingSummaryModel;
    }

    public static boolean updateVendorPincode(Integer flag,String fk_associate_id,String pincode,String shipType,Integer updateStatus,Double price ) {
        Connection connection = null;
        String statement;
        boolean result = false;
        PreparedStatement preparedStatement = null;
        try {

            if(flag == 1){
                // Enable ship type for pincode.
                statement = "update AA_vendor_pincode set req_price = ship_charge where vendor_id="+fk_associate_id+" and pincode="+pincode+" and  ship_type="+shipType+" ";
                // copy the same price in req_price

            }else if(flag == 2) {
                // Disable ship type for pincode.
                statement = "update AA_vendor_pincode set flag_enabled=0 where vendor_id="+fk_associate_id+" and pincode="+pincode+" and  ship_type="+shipType+" ";
            }
            else {
                // flag = 3 i.e. change the price.
                statement = "update AA_vendor_pincode set req_price = "+price+" where vendor_id="+fk_associate_id+" and pincode="+pincode+" and  ship_type="+shipType+" and flag_enabled = 1 ";
                // copy the requested price in req_price
            }
            connection = Database.INSTANCE.getReadWriteConnection();
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query in updateVendorPincode "+preparedStatement);
            int nums = preparedStatement.executeUpdate();
            if (nums!=0){
                result = true;
            }
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
    public int addVendorComponent(String fkAssociateId,String componentCode,int price){
        Connection connection = null;
        ResultSet resultSet =  null;
        String statement;
        PreparedStatement preparedStatement = null;
        int compID=0,status=0;
        try {
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "insert into AA_vendor_to_components (fk_associate_id,fk_component_id,price,InStock,req_price) select  ?,component_id,?,0,? from AA_master_components where component_code = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1,fkAssociateId);
            preparedStatement.setInt(2,price);
            preparedStatement.setInt(3,price);
            preparedStatement.setString(4,componentCode);
            logger.debug("sql statement check: "+preparedStatement);
            status = preparedStatement.executeUpdate();
            if (status != 0) {
                statement = "select component_id from AA_master_components where component_code = ?";
                preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setString(1,componentCode);
                logger.debug("sql query to get componentId "+preparedStatement);
                resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    compID=resultSet.getInt("component_id");
                }
            }

        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return compID;
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
            logger.debug("sql query in getCount "+preparedStatement);
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

}
