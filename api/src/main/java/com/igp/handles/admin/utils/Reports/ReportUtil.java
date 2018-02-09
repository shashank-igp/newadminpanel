package com.igp.handles.admin.utils.Reports;

import com.igp.config.instance.Database;
import com.igp.handles.admin.models.Reports.*;
import com.igp.handles.admin.models.Vendor.VendorInfoModel;
import com.igp.handles.admin.utils.Vendor.VendorUtil;
import com.igp.handles.vendorpanel.endpoints.Vendor;
import com.igp.handles.vendorpanel.models.Report.OrderReportModel;
import com.igp.handles.vendorpanel.models.Report.ReportOrderWithSummaryModel;
import com.igp.handles.vendorpanel.models.Report.SummaryModel;
import com.igp.handles.vendorpanel.models.Report.orderReportObjectModel;
import com.igp.handles.vendorpanel.utils.Reports.SummaryFunctionsUtil;
import javafx.scene.control.Tab;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suditi on 30/1/18.
 */
public class ReportUtil {
    private static final Logger logger = LoggerFactory.getLogger(ReportUtil.class);

    public ReportOrderWithSummaryModel getOrders(String fkAssociateId, String startDate, String endDate, String startLimit, String endLimit, Integer orderNo, String delhiveryDate, String status, String deliveryDateFrom, String deliveryDateTo){
        Connection connection = null;

        ReportOrderWithSummaryModel reportOrderWithSummaryModel=new ReportOrderWithSummaryModel();
        List <orderReportObjectModel>  orderReportObjectList= new ArrayList<>();
        String statement;
        List <SummaryModel> summaryModelList=new ArrayList<>();
        ResultSet resultSet = null;

        StringBuilder sb=new StringBuilder("");
        if ((delhiveryDate != null && !delhiveryDate.isEmpty())){
            sb.append("and oe.delivery_date='"+delhiveryDate+"'");

        }
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
        String queryCount = "select count(*) as totalNo  from orders_products as op LEFT JOIN vendor_assign_price as  vap "
            + " on op.orders_id=vap.orders_id  and  op.products_id=vap.products_id  inner join order_product_extra_info "
            + " as oe on op.orders_products_id=oe.order_product_id inner  join  orders as o on  vap.orders_id=o.orders_id" +
            " inner join  orders_occasions  as oo  on o.orders_occasionid=oo.occasion_id where " +
            "(op.fk_associate_id=72 OR op.fk_associate_id=vap.fk_associate_id) "+sb.toString();
        int count =SummaryFunctionsUtil.getCount(queryCount);
        String amount = "select sum((vap.vendor_price+vap.shipping)) as totalNo  from orders_products as op LEFT JOIN vendor_assign_price as  vap "
            + " on op.orders_id=vap.orders_id  and  op.products_id=vap.products_id  inner join order_product_extra_info "
            + " as oe on op.orders_products_id=oe.order_product_id inner  join  orders as o on  vap.orders_id=o.orders_id" +
            " inner join  orders_occasions  as oo  on o.orders_occasionid=oo.occasion_id where " +
            "(op.fk_associate_id=72 OR op.fk_associate_id=vap.fk_associate_id) "+sb.toString();
        int amt =SummaryFunctionsUtil.getCount(amount);

        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = " select  vap.fk_associate_id as vendorId,a.associate_name as vendorName,o.date_purchased as datePurchased,o.orders_id as  Order_No,oo.occasion_name as Ocassion , "
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
                orderReportObjectModel.setRecipienName(resultSet.getString("Recipient_Name"));
                orderReportObjectModel.setPrice(resultSet.getDouble("Amount"));
                orderReportObjectModel.setPhoneNumber(resultSet.getString("Phone"));
                orderReportObjectModel.setStatus(resultSet.getInt("status"));
                orderReportObjectModel.setVendorId(resultSet.getString("vendorId"));
                orderReportObjectModel.setVendorName(resultSet.getString("vendorName"));

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
            summaryModelTotalOrders.setValue(count+"");
            summaryModelList.add(summaryModelTotalOrders);
            SummaryModel summaryModelTotalAmount= new SummaryModel();
            summaryModelTotalAmount.setLabel("Total Amount");
            summaryModelTotalAmount.setValue(amt+"");
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
                "a.ship_charge,a.req_price,aso.associate_name from AA_vendor_pincode as a " +
                "JOIN associate_user as au ON a.vendor_id = au.fk_associate_login_id JOIN" +
                " associate as aso on au.fk_associate_login_id=aso.associate_id " +
                "where vendor_id="+fkAssociateId+" limit "+startLimit+","+endLimit+" ";
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query "+preparedStatement);
            totalQuery = " select count(distinct pincode) as totalno from AA_vendor_pincode where vendor_id="+fkAssociateId+"";
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Map<String,TableDataActionHandels> shipTypeAndTableMap = new HashMap<>();
                String pincode=resultSet.getString("a.pincode").trim();
                String shipType=resultSet.getString("a.ship_type").trim();
                String shipCharge=resultSet.getString("a.ship_charge").trim();
                String reqPrice=resultSet.getString("a.req_price").trim();
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
            }
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
                if(pincodeTableDataModel.getStandardDeliveryCharge()==null){
                    pincodeTableDataModel.setStandardDeliveryCharge(tableDataActionHandels);
                }
                if(pincodeTableDataModel.getFixedTimeDeliveryCharge()==null){
                    pincodeTableDataModel.setFixedTimeDeliveryCharge(tableDataActionHandels);
                }
                if(pincodeTableDataModel.getMidnightDeliveryCharge()==null){
                    pincodeTableDataModel.setMidnightDeliveryCharge(tableDataActionHandels);
                }
                pincodeTableDataModel.setVendorId(fkAssociateId);
                pincodeTableDataModel.setVendorName(vendorName);
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
    public boolean addNewVendorPincodeUtil(int fkAssociateId,int pincode,int cityId,int shipType,int shipCharge){
        Connection connection = null;
        String statement;
        boolean result = false;
        PreparedStatement preparedStatement = null;
        int status=0;
        try {
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "INSERT INTO AA_vendor_pincode (vendor_id,city_id,pincode,ship_type," +
                "ship_charge,flag_change,req_price) VALUES (?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,fkAssociateId);
            preparedStatement.setInt(2,cityId);
            preparedStatement.setInt(3,pincode);
            preparedStatement.setInt(4,shipType);
            preparedStatement.setInt(5,shipCharge);
            preparedStatement.setInt(6,0);
            preparedStatement.setInt(7,-1);
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
    public int setVendorGeneralInstruction(int fkAssociateId,int pinOrComp, String value, String message){
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
                productTableData.setPrice(resultSet.getDouble("price"));
                productTableData.setReqPrice(resultSet.getDouble("req_price"));
                Integer inStock=resultSet.getInt("inStock");
                if (inStock==1){productTableData.setInStock("In Stock");}
                else {productTableData.setInStock("Out Of Stock");}

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
    public boolean updateProductComponent(Integer flag,int fk_associate_id,String  componentId,int updatePrice, String inStock ) {
        Connection connection = null;
        String statement;
        int status = 0;
        boolean result = false;
        String column,value;
        PreparedStatement preparedStatement = null;
        try {
            if(flag==1){
                column="InStock=";
                value=inStock;
            }
            else {
                column="price=";
                value=updatePrice+"";
            }
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "update AA_vendor_to_components set "+column+value+" where fk_associate_id="+fk_associate_id+" and fk_component_id="+componentId+" ";
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query in updateVendorComponent "+preparedStatement);
            status = preparedStatement.executeUpdate();
            if(status==1){
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

    public int modifyVendorDetails(int fkAssociateId,String associateName,String contactPerson,String email,
                                   String address,String phone,String userId,String password,int status){
        Connection connection = null;
        int response=0;
        String statement;
        String column="";
        String value="";
        int flag=0;
        PreparedStatement preparedStatement = null;
        try{
            if(!associateName.equals("")){
                column="associate_name";
                value=associateName;
            }
            else if(!contactPerson.equals("")){
                column="associate_contact_person";
                value=contactPerson;
            }
            else if(!email.equals("")){
                column="associate_email";
                value=email;
            }
            else if(!address.equals("")){
                column="associate_address";
                value=address;
            }
            else if(!phone.equals("")){
                column="associate_phone";
                value=phone;
            }
            else if(status!=-1){
                column="associate_status";
                flag=1;
            }
            connection = Database.INSTANCE.getReadWriteConnection();
            if(!column.equals("")) {
                statement = "UPDATE associate SET " + column + "=? WHERE associate_id = ?";
                preparedStatement = connection.prepareStatement(statement);
                if (flag == 0) {
                    preparedStatement.setString(1, value);
                } else {
                    preparedStatement.setInt(1, status);
                }
                preparedStatement.setInt(2, fkAssociateId);
                logger.debug("STATEMENT CHECK: " + preparedStatement);
                response = preparedStatement.executeUpdate();
            }else{
                if(!userId.equals("")){
                    column="associate_user_name";
                    value=userId;
                }
                else if(!password.equals("")){
                    column="associate_user_pass";
                    value=password;
                }
                    String statement1 = "UPDATE associate_user set " +column+"=? WHERE fk_associate_login_id = ?";
                    preparedStatement = connection.prepareStatement(statement1);
                    preparedStatement.setString(1, value);
                    preparedStatement.setInt(2, fkAssociateId);
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
    public int addNewVendorUtil(String associateName,String user, String password,
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
            preparedStatement.setString(2,associateName);
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

}
